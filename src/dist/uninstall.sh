#!/usr/bin/env bash

# Local paths
LOCAL_BIN_PATH="/usr/local/bin"
LOCAL_LIB_PATH="/usr/local/lib"

sudo rm -rf "$LOCAL_LIB_PATH/com.neo.envmanager"
sudo rm -f "$LOCAL_BIN_PATH/envm"

echo "✔ Uninstalled"