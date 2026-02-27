#!/bin/sh
set -e

if [ -n "$INIT_DUMP" ]; then
  echo "execute dump file: $INIT_DUMP"
  mongosh piggymetrics "/dump/$INIT_DUMP"
fi
