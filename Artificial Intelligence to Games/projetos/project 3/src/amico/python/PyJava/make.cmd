@echo off

:: This script should be runned from Microsoft Visual Studio Command Prompt

SET build_dir=..\..\..\..\bin\AmiCoBuild\PyJava
SET src_dir=src

if "%1" == "" goto :build
if "%1" == "clean" goto :clean
goto :eof

:build
if not exist "%~dp0\%build_dir%" mkdir "%~dp0\%build_dir%"
echo.
echo -------------------
echo Compiling......
echo The result will be stored in "bin\AmiCoBuild\PyJava" directory
echo -------------------
echo.
cl /EHsc ".\%src_dir%\PythonCallsJava.cc" /IC:\Python26\include /I"C:\Program Files\Java\jdk1.6.0_16\include" /I"C:\Program Files\Java\jdk1.6.0_16\include\win32" "C:\Python26\libs\python26.lib" "C:\Program Files\Java\jdk1.6.0_16\lib\jvm.lib" /LD /Fe"%build_dir%\AmiCoPyJava.dll" /Fo"%build_dir%\AmiCoPyJava.obj"  /DEF:".\"%src_dir%"\AmiCoPyJava.def"
goto :eof

:clean
del /F /Q "%build_dir%"
goto :eof

:missing
echo C/C++ compiler not found in the system path. Try to run this script from Microsoft Visual Studio Command Prompt.
goto :eof