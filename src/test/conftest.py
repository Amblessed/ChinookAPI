import subprocess
import time
import pytest
import os
import socket

# --- Configuration ---
SPRING_BOOT_CMD = [r"C:\maven\apache-maven-3.9.9-bin\apache-maven-3.9.9\bin\mvn.cmd", "spring-boot:run"] # Update Maven path
MAVEN_CMD = r"C:\apache-maven-3.9.5\bin\mvn.cmd"
BASE_URL = "http://localhost:8080/api/v1/customer/"  # Endpoint your test uses
SERVER_PORT = 8080
STARTUP_WAIT = 60  # seconds
POLL_INTERVAL = 1  # seconds
LOG_FILE = "springboot.log"
BACKEND_PATH=r"C:\Users\okeyb\Documents\Java\ChinookAPI"

def is_port_open(port):
    """Check if port is already in use"""
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex(('localhost', port)) == 0

@pytest.fixture(scope="session", autouse=True)
def spring_boot_server():

    """
    Start Spring Boot only if running locally.
    On CI (GitHub Actions), the server is started by the workflow.
    """
    if os.getenv("CI", "false").lower() == "true":
        print("CI environment detected — assuming Spring Boot is already running.")
        yield
        return

    # Check if port is already in use
    if is_port_open(SERVER_PORT):
        raise RuntimeError(f"Port {SERVER_PORT} is already in use. Stop any process using it first.")

    # Start Spring Boot
    print("\nStarting Spring Boot server...")
    with open(LOG_FILE, "w") as log:
        process = subprocess.Popen(SPRING_BOOT_CMD,
            cwd=BACKEND_PATH, stdout=log, stderr=log)

    # Wait until server is up
    print(f"Waiting {STARTUP_WAIT}s for server to start...")
    time.sleep(STARTUP_WAIT)

    # Run tests
    yield

    # Teardown: stop Spring Boot
    print("\nStopping Spring Boot server...")
    if os.name == 'nt':  # Windows
        # process.send_signal(signal.CTRL_BREAK_EVENT)
        subprocess.run(["taskkill", "/F", "/T", "/PID", str(process.pid)])
    else:  # Unix/Linux/Mac
        process.terminate()
    process.wait()