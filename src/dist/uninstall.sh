#!/usr/bin/env bash

# Identify system
if [ -n "$PREFIX" ] && [ -d "$PREFIX/bin" ]; then
  echo "Uninstalling from Termux on Android"
  INSTALLATION_PATH="$PREFIX" # For Termux on Android
else
  echo "Uninstalling from GNU/Linux"
  INSTALLATION_PATH="/usr/local" # GNU/Linux
fi

# Paths
BIN_PATH="$INSTALLATION_PATH/bin"
LIB_PATH="$INSTALLATION_PATH/lib"

# Remove program files
sudo rm -rf "$LIB_PATH/com.neo.envmanager"

# Remove executable
sudo rm -f "$BIN_PATH/envm"

echo "✔ Uninstalled"