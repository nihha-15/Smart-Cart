@echo off
setlocal enabledelayedexpansion
title SmartCart - Intelligent E-Commerce Cart System

echo ===================================================================
echo     SmartCart — Intelligent E-Commerce Cart System (Java)
echo ===================================================================

cd /d "%~dp0"

:: 1. Locate Java Compiler & Runtime
set "JAVA_BIN=java"
set "JAVAC_BIN=javac"

if exist "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe" (
    set "JAVAC_BIN=C:\Program Files\Java\jdk-26.0.1\bin\javac.exe"
    set "JAVA_BIN=C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
)

:: 2. Ensure target directories
if not exist "target\classes" mkdir "target\classes"

echo [1/3] Compiling Java Servlets, Models, DAOs, and Decision Engine...
"%JAVAC_BIN%" -encoding UTF-8 -d target\classes -cp "lib\jakarta.servlet-api-6.1.0.jar;src\main\java" src\main\java\com\smartcart\model\*.java src\main\java\com\smartcart\dao\*.java src\main\java\com\smartcart\service\*.java src\main\java\com\smartcart\controller\*.java src\main\java\com\smartcart\util\*.java src\main\java\com\smartcart\SmartCartServer.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed. Please check the compiler errors above.
    pause
    exit /b %ERRORLEVEL%
)

:: 3. Copy resources if present
if exist "src\main\resources" (
    xcopy /s /y /q "src\main\resources\*" "target\classes\" >nul 2>&1
)

echo [2/3] Build completed successfully.
echo [3/3] Starting SmartCart HTTP Server on http://localhost:8080 ...
echo ===================================================================
echo   Open your browser at: http://localhost:8080
echo   Press Ctrl+C to shut down the server.
echo ===================================================================

"%JAVA_BIN%" -cp "target\classes;lib\jakarta.servlet-api-6.1.0.jar" com.smartcart.SmartCartServer 8080
