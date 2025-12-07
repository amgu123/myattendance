# make_requests.ps1
$ErrorActionPreference = "Stop"
$successCount = 0
$failureCount = 0

# Create log file
$logFile = "api_requests_log_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"
"Starting API requests at $(Get-Date)" | Out-File $logFile

1..1000 | ForEach-Object {
    $requestNumber = $_
    
    # Create payload
    $payload = @{
        data = "test dat$requestNumber"
        actualDataTime = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss.fffZ")
    }
    
    # Convert to JSON
    $jsonPayload = $payload | ConvertTo-Json
    
    Write-Host "Sending request $requestNumber..." -ForegroundColor Yellow
    
    try {
        # Make API call
        $response = Invoke-RestMethod `
            -Uri "http://localhost:8080/api/requests" `
            -Method Post `
            -Body $jsonPayload `
            -ContentType "application/json"
        
        $successCount++
        $message = "Request $requestNumber successful"
        Write-Host $message -ForegroundColor Green
        $message | Out-File $logFile -Append
    }
    catch {
        $failureCount++
        $message = "Request $requestNumber failed: $($_.Exception.Message)"
        Write-Host $message -ForegroundColor Red
        $message | Out-File $logFile -Append
    }
    
    # Add small delay between requests
    Start-Sleep -Milliseconds 100
}

# Print summary
$summary = @"

Summary:
--------
Total Requests: 1000
Successful: $successCount
Failed: $failureCount
Log File: $logFile
"@

Write-Host $summary -ForegroundColor Cyan
$summary | Out-File $logFile -Append
