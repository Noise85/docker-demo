#!/bin/bash
# Clean up on shutdown
docker compose --env-file "$ENV_FILE" down --rmi local --remove-orphans
echo "[SHUTDOWN] Docker Compose stopped. Cleaning up..."