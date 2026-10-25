@rem Luna Visual Gradle wrapper
@echo off
setlocal
set DIR=%~dp0
if "%DIR%"=="" set DIR=.
set CLASSPATH=%DIR%\gradle\wrapper\gradle-wrapper.jar
java -Dorg.gradle.appname=gradlew -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
