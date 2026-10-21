param(
    [ValidateSet('junit', 'selenium', 'all', 'smoke')]
    [string]$Suite = 'all',
    [switch]$ShowBrowser
)

$ErrorActionPreference = 'Stop'
$PSNativeCommandUseErrorActionPreference = $false
$projectRoot = Split-Path $PSScriptRoot -Parent
$runName = Get-Date -Format 'yyyyMMdd-HHmmss'
$runDirectory = Join-Path $projectRoot "target/test-runs/$runName"
New-Item -ItemType Directory -Force -Path $runDirectory | Out-Null
$reportDirectories = @()
$runFailed = $false

Push-Location $projectRoot
try {
    $selectedSuites = if ($Suite -eq 'all') { @('junit', 'selenium') } else { @($Suite) }
    foreach ($selected in $selectedSuites) {
        $reportPath = "target/test-runs/$runName/$selected"
        $mavenArguments = @('-B', '-ntp', "-Dtest.reportDirectory=$reportPath", "-Dextent.report.directory=$runDirectory/extent/$selected")
        if ($selected -eq 'selenium') {
            $mavenArguments += @('-Pui', '-Dgroups=ui')
            $headlessChoice = (-not $ShowBrowser.IsPresent).ToString().ToLowerInvariant()
            $mavenArguments += "-Dheadless=$headlessChoice"
        }
        if ($selected -eq 'smoke') { $mavenArguments += '-Dgroups=smoke' }
        $mavenArguments += 'test'
        & .\mvnw.cmd @mavenArguments 2>&1 | Tee-Object -FilePath (Join-Path $runDirectory "$selected.log")
        if ($LASTEXITCODE -ne 0) { $runFailed = $true }
        $reportDirectories += $reportPath
    }

    $pythonCommand = Get-Command python -ErrorAction SilentlyContinue
    if ($pythonCommand) {
        $pythonPath = $pythonCommand.Source
    } else {
        $pythonPath = Join-Path $env:USERPROFILE '.cache/codex-runtimes/codex-primary-runtime/dependencies/python/python.exe'
    }
    if (-not (Test-Path -LiteralPath $pythonPath)) {
        throw 'Python 3 is needed to update the test-case report. Maven XML reports remain in target/test-runs.'
    }
    & $pythonPath -X utf8 scripts/update_test_results.py --reports @reportDirectories
    if ($LASTEXITCODE -ne 0) { $runFailed = $true }
    Write-Host "Execution logs: $runDirectory"
} finally {
    Pop-Location
}
if ($runFailed) { exit 1 }
