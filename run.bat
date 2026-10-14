@echo off
title LifeData - Large-Scale Personal Data Analytics Platform
echo ===============================================================================
echo   LifeData: Large-Scale Personal Data Analytics Platform (Advanced DSA)
echo ===============================================================================
echo.

set JAVA_CMD=java
where java >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Users\LENOVO\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\java.exe" (
        set "JAVA_CMD=C:\Users\LENOVO\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\java.exe"
    )
)

if not exist "bin" (
    echo [*] Compiling LifeData Java sources into bin/...
    mkdir bin 2>nul
    set JAVAC_CMD=javac
    where javac >nul 2>nul
    if %errorlevel% neq 0 (
        if exist "C:\Users\LENOVO\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\javac.exe" (
            set "JAVAC_CMD=C:\Users\LENOVO\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\javac.exe"
        )
    )
    "%JAVAC_CMD%" -d bin -sourcepath src src\lifedata\Main.java src\lifedata\LifeDataTestSuite.java
    if %errorlevel% neq 0 (
        echo [!] Compilation failed. Please check JDK installation.
        pause
        exit /b 1
    )
    echo [*] Compilation complete.
    echo.
)

echo [*] Starting LifeData Platform...
"%JAVA_CMD%" -cp bin lifedata.Main
pause
