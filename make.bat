@echo off
set ARGC=0
for %%x in (%*) do set /A ARGC+=1
if %ARGC% equ 1 (
	set JAVAC_PATH=%1
) else (
	set JAVAC_PATH="C:\Program Files\Java\jdk1.8.0_172\bin"
)
echo %PATH% | find /C /I %JAVAC_PATH% > nul
if %errorlevel% equ 0 goto main
if not exist %JAVAC_PATH% goto end_error
set PATH=%PATH%;%JAVAC_PATH%

:main
dir /s /b src\*.java > sources
dir /s /b res\dependencies\*.jar > dependencies
if exist bin rmdir /S /Q bin
mkdir bin
javac -cp @dependencies -d bin @sources
del dependencies
del sources
goto end

:end_error
echo "Invalid file path! Please provide a valid one"

:end