# E2E Java HTTP One Vulnerability

Small fixture for ScanMatrix E2E. The HTTP endpoint `/run?cmd=...` passes a reachable query parameter to `Runtime.exec`, intentionally creating one command injection issue.
