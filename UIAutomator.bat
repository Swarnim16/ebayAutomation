@echo off
echo "Welcome to ebay Search Test Automation"
echo "*******"
echo "Cleaning previous build and compiling"
call mvn clean compile
echo "Running UI Automation"
echo "*******"
call mvn test
echo "*******"
echo "UI Automation completed"
pause