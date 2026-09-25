param([switch]$Test)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $runtimeInfo = & java -XshowSettings:properties -version 2>&1 | Out-String
    if ($runtimeInfo -notmatch 'java.home\s*=\s*([^\r\n]+)') { throw 'Cannot locate the installed JDK.' }
    $jdkDirectory = $Matches[1].Trim()
    if ($jdkDirectory -match '^[A-Za-z]:$') { $jdkDirectory += '\' }
    $compilerPath = Join-Path $jdkDirectory 'bin/javac.exe'
    $jarPath = Join-Path $jdkDirectory 'bin/jar.exe'
    $javaPath = Join-Path $jdkDirectory 'bin/java.exe'
    New-Item -ItemType Directory -Force build/classes,dist | Out-Null
    $sources = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
    & $compilerPath "-J-Djava.home=$jdkDirectory" --release 17 -encoding UTF-8 -d build/classes $sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
    & $jarPath "-J-Djava.home=$jdkDirectory" --create --file dist/HMSAdmin.jar --main-class hms.App -C build/classes .
    if ($LASTEXITCODE -ne 0) { throw 'Packaging failed' }
    if ($Test) {
        New-Item -ItemType Directory -Force build/test-classes | Out-Null
        & $compilerPath "-J-Djava.home=$jdkDirectory" --release 17 -encoding UTF-8 -cp build/classes -d build/test-classes test/hms/ServiceTest.java
        if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed' }
        & $javaPath "-Djava.home=$jdkDirectory" -cp 'build/classes;build/test-classes' hms.ServiceTest
        if ($LASTEXITCODE -ne 0) { throw 'Tests failed' }
    }
} finally { Pop-Location }
