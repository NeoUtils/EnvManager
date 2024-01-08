#!/usr/bin/env bash

# Local paths
LOCAL_BIN_PATH="/usr/local/bin"
LOCAL_LIB_PATH="/usr/local/lib"

# Destination paths
DEST="$LOCAL_LIB_PATH/Properties"

sudo rm -rf "$DEST"
sudo rm -f "$LOCAL_BIN_PATH/properties"

echo "✔ Uninstalled"