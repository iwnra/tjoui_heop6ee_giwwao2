@echo off
setlocal

:: --- 設定エリア ---
:: 対象のAccessファイルのフルパス
set DB_PATH="C:\Projects\Inventory.accdb"
:: 書き出し先のフォルダ名
set OUT_DIR=".\src"
:: 実行するVBScriptの名前
set VBS_NAME="ExportAccess.vbs"
:: ------------------

echo エクスポートを開始します...
echo 対象: %DB_PATH%

:: VBScriptの実行 (cscriptを使用)
cscript //nologo %VBS_NAME% %DB_PATH% %OUT_DIR%

if %ERRORLEVEL% equ 0 (
    echo.
    echo 完了しました。出力先: %OUT_DIR%
) else (
    echo.
    echo エラーが発生しました。
)

pause