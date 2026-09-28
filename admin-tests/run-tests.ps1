param([string]$JdkHome = $env:JAVA_HOME)
$ErrorActionPreference = 'Stop'
if (-not $JdkHome) { throw 'Set JAVA_HOME or pass -JdkHome to your JDK 26 folder.' }
$projectDir = Split-Path $PSScriptRoot -Parent
$buildDir = Join-Path $projectDir 'build/admin-tests'
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
$sourceList = Join-Path $buildDir 'sources.txt'
$sourcePaths = @(Get-ChildItem -LiteralPath (Join-Path $projectDir 'src') -Filter '*.java' -Recurse | ForEach-Object { '"' + $_.FullName.Replace('\','/') + '"' })
$sourcePaths += '"' + (Join-Path $PSScriptRoot 'AdminCompatibilityTest.java').Replace('\','/') + '"'
[System.IO.File]::WriteAllLines($sourceList,$sourcePaths,(New-Object System.Text.UTF8Encoding($false)))
$javaHome = $JdkHome.Replace('\','/')
& (Join-Path $JdkHome 'bin/javac.exe') "-J-Djava.home=$javaHome" -encoding UTF-8 -d $buildDir "@$sourceList"
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
$fixtureDir = Join-Path $buildDir ('fixture-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $fixtureDir | Out-Null
Push-Location $fixtureDir
try {
    & (Join-Path $JdkHome 'bin/java.exe') "-Djava.home=$javaHome" '-Djava.awt.headless=true' -cp $buildDir AdminCompatibilityTest
    if ($LASTEXITCODE -ne 0) { throw 'Compatibility test failed.' }
    & (Join-Path $JdkHome 'bin/java.exe') "-Djava.home=$javaHome" '-Djava.awt.headless=true' -cp $buildDir AdminCompatibilityTest --restart
    if ($LASTEXITCODE -ne 0) { throw 'Restart persistence test failed.' }
    Write-Output "Test data and previews: $fixtureDir"
} finally { Pop-Location }
