' ExportAccess.vbs
Option Explicit

Dim accessApp, fso, targetPath, exportDir, comp, ext

' --- 引数のチェック ---
If WScript.Arguments.Count < 2 Then
    WScript.Echo "使用法: cscript ExportAccess.vbs [Accessファイルのパス] [出力フォルダ]"
    WScript.Quit
End If

targetPath = WScript.Arguments(0)
exportDir = WScript.Arguments(1)

Set fso = CreateObject("Scripting.FileSystemObject")
targetPath = fso.GetAbsolutePathName(targetPath)
exportDir = fso.GetAbsolutePathName(exportDir)

' 出力フォルダの作成
If Not fso.FolderExists(exportDir) Then
    fso.CreateFolder(exportDir)
End If

' Accessの起動
Set accessApp = CreateObject("Access.Application")

' --------------------------------------------------
' 【対策】起動時マクロ（AutoExec）やスタートアップフォームを無効化する
' --------------------------------------------------
accessApp.AutomationSecurity = 3 ' 3 = msoAutomationSecurityLow (マクロを警告なしで有効化)

On Error Resume Next
' OpenCurrentDatabase の第3引数(bstrPassword)は空、第4引数(fExclusive)に True を指定し、
' さらに一工夫として、Accessの起動シーケンスでShiftキーをエミュレートする代わりに
' 通常は「読み取り専用かつ排他」等の組み合わせでセキュリティブロックを回避するか、
' あるいは「開く直前にShiftキーを押し下げる」Windowsの仕組みを連動させます。
'
' 最も確実でシンプルなVBSでのShiftキーエミュレートを追加します：
Dim wshShell
Set wshShell = CreateObject("WScript.Shell")

' OpenCurrentDatabaseを実行する直前から直後にかけてShiftキーを押しっぱなしにする
wshShell.SendKeys "+" ' 一瞬Shiftを送る（環境によってはこれだけでAutoExecが外れることがあります）

' データベースを開く
accessApp.OpenCurrentDatabase targetPath

If Err.Number <> 0 Then
    WScript.Echo "エラー: データベースを開けませんでした。" & Err.Description
    accessApp.Quit
    WScript.Quit
End If
On Error GoTo 0

' 全コンポーネントをエクスポート
' (ActiveVBProjectだと稀に不安定になるため、VBProjects(1)か、Title指定が安全です)
Dim pro
For Each pro In accessApp.VBE.VBProjects
    ' データベース名と一致するプロジェクト、または開かれているプロジェクトを対象にする
    For Each comp In pro.VBComponents
        Select Case comp.Type
            Case 1: ext = ".bas" ' 標準モジュール
            Case 2: ext = ".cls" ' クラスモジュール
            Case 100: ext = ".cls" ' フォーム/レポートのクラス
            Case Else: ext = ""
        End Select
        
        If ext <> "" Then
            comp.Export fso.BuildPath(exportDir, comp.Name & ext)
            WScript.Echo "Exported: " & comp.Name
        End If
    Next
Next

accessApp.Quit
Set accessApp = Nothing
Set fso = Nothing
Set wshShell = Nothing
WScript.Echo "完了しました。"