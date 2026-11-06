[CmdletBinding(PositionalBinding=$false)]
param(
    [Parameter(Mandatory)][string]$Repo,
    [string]$Python = 'python',
    [string]$GitBinDirectory,
    [string]$AuthorName,
    [string]$AuthorEmail,
    [string]$CredentialFile,
    [string]$GhExecutable = 'gh',
    [Parameter(ValueFromRemainingArguments=$true)][string[]]$BoardArgs
)
$ErrorActionPreference = 'Stop'
if ($GitBinDirectory) { $env:PATH = $GitBinDirectory + [IO.Path]::PathSeparator + $env:PATH }
if (($AuthorName -and -not $AuthorEmail) -or ($AuthorEmail -and -not $AuthorName)) {
    throw 'Pass both AuthorName and AuthorEmail, or neither to use Git configuration.'
}
if ($AuthorName) {
    $env:GIT_AUTHOR_NAME = $AuthorName
    $env:GIT_AUTHOR_EMAIL = $AuthorEmail
    $env:GIT_COMMITTER_NAME = $AuthorName
    $env:GIT_COMMITTER_EMAIL = $AuthorEmail
}
if ($CredentialFile) {
    $credentialText = [IO.File]::ReadAllText((Resolve-Path -LiteralPath $CredentialFile).Path).Trim()
    if ($credentialText -match '(?m)^\s*GH_TOKEN\s*=\s*(.+?)\s*$') {
        $env:GH_TOKEN = $Matches[1].Trim().Trim('"').Trim("'")
    } else {
        $env:GH_TOKEN = $credentialText
    }
    if (-not $env:GH_TOKEN) { throw 'Credential file is empty.' }
    & $GhExecutable auth setup-git
    if ($LASTEXITCODE -ne 0) { throw 'GitHub credential setup failed.' }
}
& $Python (Join-Path $PSScriptRoot 'board.py') --repo $Repo @BoardArgs
exit $LASTEXITCODE
