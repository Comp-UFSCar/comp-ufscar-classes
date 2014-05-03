@ECHO OFF
:: AmiCoJavaPy example run script.
:: If you have a problem while compiling, please contact us to solve your problem.
::
:: All usage options will be described in documentation soon.
::
:: Author: Nikolay Sohryakov, nikolay.sohryakov@gmail.com

set MARIO_DIR=..\..\..\..\bin\MarioAI
set CMD_LINE_OPTIONS=
set AGENT=forwardjumpingagent.ForwardJumpingAgent.py
set COMPILE_LIBRARY="true"

set B_DIR=..\..\..\..\bin\AmiCoBuild\JavaPy

:Loop
if "%1"=="" goto :Continue

goto :%~1

:-nc
set COMPILE_LIBRARY="false"
goto :next

:-ch
shift
set MARIO_DIR=%1
goto :next

:-o
shift
set CMD_LINE_OPTIONS=%1
goto :next
	
:next
shift
goto Loop
:Continue

if %COMPILE_LIBRARY% == "true" (
    if not exist "%~dp0make.cmd" (
        echo.
        echo Compile script not found. Exiting...
        echo.
        goto :eof
    )
    ::call "%~dp0make.cmd"
    nmake /F Makefile.Win
)

if not exist "%~dp0%B_DIR%" (
    echo.
    echo Executable file of the library not found in the path %~dp0%B_DIR%. Terminating...
    echo.
    goto :eof
)

echo.
echo Copying agents...
echo.

copy /Y "..\agents\*.py" "%~dp0%B_DIR%"

echo.
echo Copying MarioAI Benchmark files...
echo.

::xcopy /E /Y /I "%~dp0%MARIO_DIR%\ch" "%~dp0%B_DIR%\ch"

cd "%~dp0%B_DIR%"

set CMD_LINE_OPTIONS=%CMD_LINE_OPTIONS:~1,-1%
if "%CMD_LINE_OPTIONS%"=="~1,-1" set CMD_LINE_OPTIONS=

if "%CMD_LINE_OPTIONS%"=="" (
    java ch.idsia.scenarios.Main -ag %AGENT%
) else (
    echo %CMD_LINE_OPTIONS%
    java ch.idsia.scenarios.Main %CMD_LINE_OPTIONS%
)

cd %~dp0
