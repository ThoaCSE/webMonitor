# Docker run notes

## Build command

```bash
docker build -t webmonitor:latest .
```

## Run command

```bash
docker run --rm webmonitor:latest https://example.com
```

## Program output

```text
Website Monitor CLI
Target URL: https://example.com
Fetched 559 characters from https://example.com
Initial content received successfully.
```
