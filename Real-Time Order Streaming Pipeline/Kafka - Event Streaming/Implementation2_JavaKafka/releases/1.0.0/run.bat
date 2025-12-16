@echo off
echo Kafka Producer - Starting...

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo Java not found. Please install Java 11+
    pause
    exit /b 1
)

REM Check JAR exists
if not exist "RetailProducerApp.jar" (
    echo RetailProducerApp.jar not found
    pause
    exit /b 1
)

for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do set "JAVA_VER=%%i"
echo Java: %JAVA_VER:"=%

for %%F in ("RetailProducerApp.jar") do set "SIZE=%%~zF"
set /a "SIZE_MB=SIZE/1024/1024"
echo JAR Size: %SIZE_MB% MB
echo.

echo Running: java -jar RetailProducerApp.jar %*
echo.

java -jar RetailProducerApp.jar %*
pause