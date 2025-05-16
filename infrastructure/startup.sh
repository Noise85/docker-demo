#!/bin/bash

ENV_FILE=".env"

# Default values
POSTGRES_USER_DEFAULT="postgres"
POSTGRES_PASSWORD_DEFAULT="postgres"
POSTGRES_DB_DEFAULT="todo"
DB_HOST_DEFAULT="postgresql"
DB_PORT_DEFAULT="5432"
SPRING_PROFILES_ACTIVE_DEFAULT="prod"
CERTBOT_EMAIL_DEFAULT="your-email@example.com"
CERTBOT_DOMAIN_DEFAULT="localhost"
CORS_ALLOWED_ORIGINS_DEFAULT="localhost:443"
API_BASE_URL_DEFAULT="http://localhost:8080/api"

print_help() {
  echo "[STARTUP] Usage: ./start.sh [OPTIONS]"
  echo "[STARTUP] "
  echo "[STARTUP] Optional CLI overrides:"
  echo "[STARTUP]   --POSTGRES_USER=..."
  echo "[STARTUP]   --POSTGRES_PASSWORD=..."
  echo "[STARTUP]   --POSTGRES_DB=..."
  echo "[STARTUP]   --DB_HOST=..."
  echo "[STARTUP]   --DB_PORT=..."
  echo "[STARTUP]   --SPRING_PROFILES_ACTIVE=..."
  echo "[STARTUP]   --CERTBOT_EMAIL=..."
  echo "[STARTUP]   --CERTBOT_DOMAIN=..."
  echo "[STARTUP]   --CORS_ALLOWED_ORIGINS=..."
  echo "[STARTUP]   --API_BASE_URL=..."
  echo "[STARTUP]   --help    Show this help and exit"
}

generate_certs() {
  echo "[STARTUP] Generating SSL certificates..."
  mkdir -p ./apache2/conf/ssl/
  openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout ./apache2/conf/ssl/server.key \
    -out ./apache2/conf/ssl/server.crt \
    -subj "/C=US/ST=State/L=City/O=Organization/OU=Unit/CN=$CORS_ALLOWED_ORIGINS"
  echo "[STARTUP] Certificates generated for domain $CORS_ALLOWED_ORIGINS"
}

# Parse CLI args
for arg in "$@"; do
  case $arg in
    --POSTGRES_USER=*) POSTGRES_USER="${arg#*=}" ;;
    --POSTGRES_PASSWORD=*) POSTGRES_PASSWORD="${arg#*=}" ;;
    --POSTGRES_DB=*) POSTGRES_DB="${arg#*=}" ;;
    --DB_HOST=*) DB_HOST="${arg#*=}" ;;
    --DB_PORT=*) DB_PORT="${arg#*=}" ;;
    --SPRING_PROFILES_ACTIVE=*) SPRING_PROFILES_ACTIVE="${arg#*=}" ;;
    --CERTBOT_EMAIL=*) CERTBOT_EMAIL="${arg#*=}" ;;
    --CERTBOT_DOMAIN=*) CERTBOT_DOMAIN="${arg#*=}" ;;
    --CORS_ALLOWED_ORIGINS=*) CORS_ALLOWED_ORIGINS="${arg#*=}" ;;
    --API_BASE_URL=*) API_BASE_URL="${arg#*=}" ;;
    --help) print_help; exit 0 ;;
    *) echo "[STARTUP] Unknown option: $arg"; print_help; exit 1 ;;
  esac
done

# Validate existing .env
if [ -f "$ENV_FILE" ]; then
  echo "[STARTUP] Found existing .env file. Validating..."

  REQUIRED_VARS=(
    POSTGRES_USER
    POSTGRES_PASSWORD
    POSTGRES_DB
    DB_HOST
    DB_PORT
    SPRING_PROFILES_ACTIVE
    CERTBOT_EMAIL
    CERTBOT_DOMAIN
    CORS_ALLOWED_ORIGINS
    API_BASE_URL
  )

  MISSING_VARS=0
  for VAR in "${REQUIRED_VARS[@]}"; do
    VALUE=$(grep "^$VAR=" "$ENV_FILE" | cut -d '=' -f2-)
    if [ -z "$VALUE" ]; then
      echo "[STARTUP] Missing variable: $VAR"
      MISSING_VARS=1
    fi
  done

  if [ "$MISSING_VARS" -eq 0 ]; then
    echo "[STARTUP] .env is complete. Skipping regeneration."
  else
    echo "[STARTUP] .env is incomplete. Regenerating with prompts..."
    rm -f "$ENV_FILE"
  fi
fi

# Prompt for missing values if no .env exists
if [ ! -f "$ENV_FILE" ]; then
  #mkdir -p ./infrastructure

  prompt_var() {
    VAR_NAME=$1
    DEFAULT=$2
    VAR_VALUE="${!VAR_NAME}"

    if [ -z "$VAR_VALUE" ]; then
      read -rp "$VAR_NAME [$DEFAULT]: " INPUT
      VAR_VALUE="${INPUT:-$DEFAULT}"
    fi

    export "$VAR_NAME"="$VAR_VALUE"
  }

  echo "[STARTUP] Configure environment values (press Enter to accept default):"
  prompt_var POSTGRES_USER "$POSTGRES_USER_DEFAULT"
  prompt_var POSTGRES_PASSWORD "$POSTGRES_PASSWORD_DEFAULT"
  prompt_var POSTGRES_DB "$POSTGRES_DB_DEFAULT"
  prompt_var DB_HOST "$DB_HOST_DEFAULT"
  prompt_var DB_PORT "$DB_PORT_DEFAULT"
  prompt_var SPRING_PROFILES_ACTIVE "$SPRING_PROFILES_ACTIVE_DEFAULT"
  prompt_var CERTBOT_EMAIL "$CERTBOT_EMAIL_DEFAULT"
  prompt_var CERTBOT_DOMAIN "$CERTBOT_DOMAIN_DEFAULT"
  prompt_var CORS_ALLOWED_ORIGINS "$CORS_ALLOWED_ORIGINS_DEFAULT"
  prompt_var API_BASE_URL "$API_BASE_URL_DEFAULT"

  # Validate inputs
  if ! [[ "$DB_PORT" =~ ^[0-9]+$ ]]; then
    echo "[STARTUP] Error: DB_PORT must be numeric."
    exit 1
  fi

  if [[ ! "$API_BASE_URL" =~ ^https?:// ]]; then
    echo "[STARTUP] Error: API_BASE_URL must start with http:// or https://"
    exit 1
  fi

  if [[ ! "$CERTBOT_EMAIL" =~ ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$ ]]; then
    echo "[STARTUP] Error: Invalid CERTBOT_EMAIL format."
    exit 1
  fi

  # Write .env
  cat > "$ENV_FILE" <<EOF
POSTGRES_USER=$POSTGRES_USER
POSTGRES_PASSWORD=$POSTGRES_PASSWORD
POSTGRES_DB=$POSTGRES_DB
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
CERTBOT_EMAIL=$CERTBOT_EMAIL
CERTBOT_DOMAIN=$CERTBOT_DOMAIN
CORS_ALLOWED_ORIGINS=$CORS_ALLOWED_ORIGINS
API_BASE_URL=$API_BASE_URL
COMPOSE_BAKE=true
EOF

  chmod 600 "$ENV_FILE"
  echo "[STARTUP] .env created at $ENV_FILE with restricted permissions (600)."
fi

generate_certs;

# Run Docker Compose
echo "[STARTUP] Starting Docker Compose..."
docker compose --env-file "$ENV_FILE" up -d --build --force-recreate
