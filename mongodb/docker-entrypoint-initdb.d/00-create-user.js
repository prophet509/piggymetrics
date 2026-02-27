const pwd = process.env.MONGODB_PASSWORD;
if (!pwd) {
  print('MONGODB_PASSWORD not defined');
  quit(1);
}

const dbName = 'piggymetrics';
const dbRef = db.getSiblingDB(dbName);

if (!dbRef.getUser('user')) {
  dbRef.createUser({
    user: 'user',
    pwd: pwd,
    roles: [ { role: 'readWrite', db: dbName } ]
  });
  print('created user in ' + dbName);
} else {
  print('user already exists in ' + dbName);
}
