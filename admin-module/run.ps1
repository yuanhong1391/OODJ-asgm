param([switch]$Demo)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    if (-not (Test-Path 'dist/HMSAdmin.jar')) { & ./build.ps1 }
    $runtimeInfo = & java -XshowSettings:properties -version 2>&1 | Out-String
    if ($runtimeInfo -notmatch 'java.home\s*=\s*([^\r\n]+)') { throw 'Cannot locate the installed JDK.' }
    $jdkDirectory = $Matches[1].Trim()
    if ($jdkDirectory -match '^[A-Za-z]:$') { $jdkDirectory += '\' }
    $javaPath = Join-Path $jdkDirectory 'bin/java.exe'
    if ($Demo) {
        if (-not (Test-Path 'demo-data/users.txt')) {
            & $javaPath "-Djava.home=$jdkDirectory" -cp dist/HMSAdmin.jar hms.DemoData
            if ($LASTEXITCODE -ne 0) { throw 'Demo data creation failed' }
        }
        & $javaPath "-Djava.home=$jdkDirectory" '-Dhms.data=demo-data' -jar dist/HMSAdmin.jar
    } else {
        & $javaPath "-Djava.home=$jdkDirectory" -jar dist/HMSAdmin.jar
    }
} finally { Pop-Location }
