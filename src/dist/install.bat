@echo off

rem This script installs EnvManager on Windows.
rem Execute in a terminal with administrator permissions.

SETLOCAL

rem Program path
SET ENVM_PATH=%ProgramFiles%\EnvManager

rem Set destination directories
SET BIN_PATH=%ENVM_PATH%\bin
SET LIB_PATH=%ENVM_PATH%\lib

rem Create destination directories
mkdir "%BIN_PATH%"
mkdir "%LIB_PATH%"

rem Copy program files
xcopy /E /I ".\bin" "%BIN_PATH%"
xcopy /E /I ".\lib" "%LIB_PATH%"

rem Add to PATH
SETX ENVM_PATH "%ENVM_PATH%"
SETX PATH "%PATH%;%ENVM_PATH%"

rem Copy terminal executable
copy ".\envm.bat" "%ENVM_PATH%"

echo ✓ Installed
ENDLOCAL
