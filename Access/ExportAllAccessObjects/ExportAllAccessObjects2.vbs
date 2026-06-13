' ExportAllAccessObjects.vbs
Option Explicit

Dim accessApp, fso, wshShell, targetPath, exportDir, obj, i

' --- Object Type Constants ---
Const acForm = 2
Const acReport = 3
Const acModule = 5
Const acMacro = 4
Const acQuery = 1

' --- Arguments ---
If WScript.Arguments.Count < 2 Then
    WScript.Echo "Usage: cscript ExportAllAccessObjects.vbs [AccessFilePath] [ExportDirectory]"
    WScript.Quit
End If

targetPath = WScript.Arguments(0)
exportDir = WScript.Arguments(1)

Set fso = CreateObject("Scripting.FileSystemObject")
targetPath = fso.GetAbsolutePathName(targetPath)
exportDir = fso.GetAbsolutePathName(exportDir)

If Not fso.FolderExists(exportDir) Then fso.CreateFolder(exportDir)

' Access Instance
Set accessApp = CreateObject("Access.Application")
Set wshShell = CreateObject("WScript.Shell")

' --------------------------------------------------
' 【対策1】マクロのセキュリティダイアログを抑制
' --------------------------------------------------
accessApp.AutomationSecurity = 3 ' msoAutomationSecurityLow

' --------------------------------------------------
' 【対策2】Shiftキーを押しながら開く状態を再現（AutoExecバイパス）
' --------------------------------------------------
wshShell.SendKeys "+" ' 事前にShiftキー（+）の入力をキューに送る

On Error Resume Next
accessApp.OpenCurrentDatabase targetPath
If Err.Number <> 0 Then
    WScript.Echo "Error: Could not open database. " & Err.Description
    accessApp.Quit
    WScript.Quit
End If
On Error GoTo 0

WScript.Echo "Exporting objects from: " & targetPath

' --- Export Forms ---
For Each obj In accessApp.CurrentProject.AllForms
    WScript.Echo "Exporting Form: " & obj.Name
    accessApp.SaveAsText acForm, obj.Name, fso.BuildPath(exportDir, "Form_" & obj.Name & ".txt")
Next

' --- Export Reports ---
For Each obj In accessApp.CurrentProject.AllReports
    WScript.Echo "Exporting Report: " & obj.Name
    accessApp.SaveAsText acReport, obj.Name, fso.BuildPath(exportDir, "Report_" & obj.Name & ".txt")
Next

' --- Export Modules ---
For Each obj In accessApp.CurrentProject.AllModules
    WScript.Echo "Exporting Module: " & obj.Name
    accessApp.SaveAsText acModule, obj.Name, fso.BuildPath(exportDir, "Module_" & obj.Name & ".txt")
Next

' --- Export Macros ---
For Each obj In accessApp.CurrentProject.AllMacros
    WScript.Echo "Exporting Macro: " & obj.Name
    accessApp.SaveAsText acMacro, obj.Name, fso.BuildPath(exportDir, "Macro_" & obj.Name & ".txt")
Next

accessApp.Quit
Set accessApp = Nothing
Set fso = Nothing
Set wshShell = Nothing
WScript.Echo "Done."