#!/usr/bin/env bash

set -e

HOST_PORT="$1"
shift
CMD="$@"

# Estrae host e porta (es. mysql:3306 → mysql + 3306)
HOST=$(echo "$HOST_PORT" | cut -d':' -f1)
PORT=$(echo "$HOST_PORT" | cut -d':' -f2)

echo "Waiting for $HOST:$PORT..."

# Tenta connessione fino a quando MySQL è pronto
until nc -z "$HOST" "$PORT"; do
  echo "Waiting for $HOST:$PORT..."
  sleep 1
done

echo "MySQL is up — executing command"
exec $CMD
