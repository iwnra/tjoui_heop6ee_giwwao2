
' ExportAccess.vbs
Option Explicit

Dim accessApp, fso, targetPath, exportDir, objProject, comp, ext

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

On Error Resume Next
accessApp.OpenCurrentDatabase targetPath
If Err.Number <> 0 Then
    WScript.Echo "エラー: データベースを開けませんでした。" & Err.Description
    accessApp.Quit
    WScript.Quit
End If
On Error GoTo 0

' 全コンポーネントをエクスポート
For Each comp In accessApp.VBE.ActiveVBProject.VBComponents
    Select Case comp.Type
        Case 1: ext = ".bas" ' 標準モジュール
        Case 2: ext = ".cls" ' クラスモジュール
        Case 100: ext = ".cls" ' フォーム/レポートのクラス
        Case Else: ext = ".txt"
    End Select
    
    comp.Export fso.BuildPath(exportDir, comp.Name & ext)
    WScript.Echo "Exported: " & comp.Name
Next

accessApp.Quit
WScript.Echo "完了しました。"