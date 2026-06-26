# Docker run notes

## What this project does

This project is a simple Java-based website monitor. It can fetch the content of a website URL and print the result to the terminal. This is useful for testing, validating that a URL is reachable, and checking what content the program retrieves.

## What you need before starting

1. Install Docker on your machine.
   - On Windows or macOS, install Docker Desktop.
   - On Linux, install Docker Engine and ensure the Docker daemon is running.
2. Open a terminal in the project folder.
3. Make sure you are in the repository directory:

```bash
cd /workspaces/webMonitor/webMonitor
```

## How to install Docker

If Docker is not installed yet:

- Windows/macOS: install Docker Desktop from the official Docker website.
- Linux: install Docker using your distribution package manager, then start the service.

After installation, verify it works:

```bash
docker --version
```

## How to build a Docker image

From the project folder, build the container image with:

```bash
docker build -t webmonitor:latest .
```

This command reads the Dockerfile and creates an image named `webmonitor` with the tag `latest`.

## How to run the program inside Docker

Run the container and pass a website URL as a command-line argument:

```bash
docker run --rm webmonitor:latest https://example.com
```

You can also print the fetched content for easier testing:

```bash
docker run --rm webmonitor:latest --show https://example.com
```

## How to update the container

If you change the Java source code, rebuild the image:

```bash
docker build -t webmonitor:latest .
```

Then run the updated container again with the same command:

```bash
docker run --rm webmonitor:latest https://example.com
```

## Example output

```text
Website Monitor CLI
Target URL: https://example.com
Fetched 559 characters from https://example.com
Initial content received successfully.
```

## How to share this with others

To make it easy for someone else to use:

1. Share the project folder, including the Dockerfile.
2. Tell them to run:

```bash
docker build -t webmonitor:latest .
docker run --rm webmonitor:latest https://example.com
```

3. If you want to share it more easily, you can also push the image to Docker Hub:

```bash
docker login
docker tag webmonitor:latest your-dockerhub-username/webmonitor:latest
docker push your-dockerhub-username/webmonitor:latest
```

Then other people can run:

```bash
docker run --rm your-dockerhub-username/webmonitor:latest https://example.com
```
