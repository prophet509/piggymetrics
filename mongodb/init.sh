#!/bin/bash
set -e

if test -z "$MONGODB_PASSWORD"; then
    echo "MONGODB_PASSWORD not defined"
    exit 1
fi

auth="-u user -p $MONGODB_PASSWORD"

# MONGODB USER CREATION
(
  echo "setup mongodb auth"
  create_user="if (!db.getUser('user')) { db.createUser({ user: 'user', pwd: '$MONGODB_PASSWORD', roles: [ {role:'readWrite', db:'piggymetrics'} ]}) }"
  until mongosh piggymetrics --eval "$create_user" || mongosh piggymetrics $auth --eval "$create_user"; do sleep 5; done
  pkill mongod || true
  sleep 1
  pkill -9 mongod || true
) &

# INIT DUMP EXECUTION
(
  if test -n "$INIT_DUMP"; then
      echo "execute dump file"
      until mongosh piggymetrics $auth "/dump/$INIT_DUMP"; do sleep 5; done
  fi
) &

echo "start mongodb without auth"
chown -R mongodb /data/db
exec gosu mongodb mongod "$@"
