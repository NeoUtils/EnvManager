#!/usr/bin/env bash

# Identify system
if [ -n "$PREFIX" ] && [ -d "$PREFIX/bin" ]; then
  echo "Installing for Termux on Android"
  INSTALLATION_PATH="$PREFIX" # For Termux on Android
else
  echo "Installing for GNU/Linux"

  # Check if the script is run as root
  if [ "$(id -u)" != "0" ]; then
     echo "Root privileges required to install"
  fi

  INSTALLATION_PATH="/usr/local" # GNU/Linux
fi

# Paths

BIN_PATH="$INSTALLATION_PATH/bin"
LIB_PATH="$INSTALLATION_PATH/lib"

# Copy program files

PACKAGE="com.neo.envmanager"

mkdir -p "$LIB_PATH/$PACKAGE/bin"
mkdir -p "$LIB_PATH/$PACKAGE/lib"

cp -r "./bin" "$LIB_PATH/$PACKAGE"
cp -r "./lib" "$LIB_PATH/$PACKAGE"

# Copy executable
cp "./envm" "$BIN_PATH"

echo "✔ Installed"