@echo off
rem Runs the Maven build + tests with JDK 21 and saves the output to test-output.log
cd /d "%~dp0"
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Running tests, please wait...
call mvnw.cmd -B test > test-output.log 2>&1
echo EXIT CODE: %ERRORLEVEL% >> test-output.log
echo Done. See test-output.log
