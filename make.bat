@echo off
dir /s /b src\*.java > sources
echo %PATH% | find /C /I "Java\jdk1.8" > nul
if %errorlevel% equ 1 set PATH=%PATH%;"C:\Program Files\Java\jdk1.8.0_172\bin"
if exist bin rmdir /S /Q bin
mkdir bin
javac -d bin @sources
del sources