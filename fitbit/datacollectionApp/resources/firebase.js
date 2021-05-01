"use strict";
const { exec } = require('child_process');
exec('npm install firebase-admin --save', (err, stdout, stderr) => {
  if (err) {
    // node couldn't execute the command
    return;
  }

  // the *entire* stdout and stderr (buffered)
  console.log(`stdout: ${stdout}`);
  console.log(`stderr: ${stderr}`);
});
const FirebaseAdmin = require("firebase-admin");

const FirebaseKey = require("/resources/serviceKey.json");

let FirebaseApp = FirebaseAdmin.initializeApp({
    credential: FirebaseAdmin.credential.cert(FirebaseKey)
});

// Exporting an object that contains two other objects
module.exports = {
    FirebaseApp
};