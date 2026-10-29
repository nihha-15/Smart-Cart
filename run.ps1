# SmartCart Execution Script for PowerShell in VS Code
$ErrorActionPreference = "Stop"

Write-Host "===================================================================" -ForegroundColor Cyan
Write-Host "    SmartCart — Intelligent E-Commerce Cart System (Java)" -ForegroundColor Green
Write-Host "===================================================================" -ForegroundColor Cyan

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectDir

# Locate Java compiler
$javac = "javac"
$java = "java"
if (Test-Path "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe") {
    $javac = "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe"
    $java = "C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
}

# Create output folder
if (!(Test-Path "target\classes")) {
    New-Item -ItemType Directory -Path "target\classes" -Force | Out-Null
}

Write-Host "[1/3] Compiling Java Servlets, Models, DAOs, and Decision Engine..." -ForegroundColor Yellow
$jar = "lib\jakarta.servlet-api-6.1.0.jar"
$srcFiles = Get-ChildItem -Path "src\main\java" -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName

& $javac -encoding UTF-8 -d "target\classes" -cp "$jar;src\main\java" $srcFiles

if (Test-Path "src\main\resources") {
    Copy-Item -Path "src\main\resources\*" -Destination "target\classes" -Recurse -Force -ErrorAction SilentlyContinue
}

Write-Host "[2/3] Build completed successfully." -ForegroundColor Green
Write-Host "[3/3] Starting SmartCart HTTP Server on http://localhost:8080 ..." -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor White
Write-Host "  Access in Browser: http://localhost:8080" -ForegroundColor Green
Write-Host "  Press Ctrl+C to terminate the server." -ForegroundColor Gray
Write-Host "===================================================================" -ForegroundColor White

& $java -cp "target\classes;$jar" com.smartcart.SmartCartServer 8080
