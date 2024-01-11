#!/usr/bin/env bash

# Identify system
if [ -n "$PREFIX" ] && [ -d "$PREFIX/bin" ]; then
  echo "Installing for Termux on Android"
  INSTALLATION_PATH="$PREFIX" # For Termux on Android
else
  echo "Installing for GNU/Linux"
  INSTALLATION_PATH="/usr/local" # GNU/Linux
fi

# Paths
BIN_PATH="$INSTALLATION_PATH/bin"
LIB_PATH="$INSTALLATION_PATH/lib"

# Copy program files

PACKAGE="com.neo.envmanager"

sudo mkdir -p "$LIB_PATH/$PACKAGE/bin"
sudo mkdir -p "$LIB_PATH/$PACKAGE/lib"

sudo cp -r "./bin" "$LIB_PATH/$PACKAGE"
sudo cp -r "./lib" "$LIB_PATH/$PACKAGE"

# Copy executable
sudo cp "./envm" "$BIN_PATH"

echo "✔ Installed"