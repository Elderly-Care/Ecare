import clock from "clock";
import { Accelerometer } from "accelerometer";
import { Gyroscope } from "gyroscope";
import document from "document";
import { preferences } from "user-settings";
import * as util from "../common/utils";
import { display } from "display";
import * as fs from "fs";
import { battery } from "power";
import document from "document";
// import { vibration } from "haptics";
import { outbox } from "file-transfer";
import { HeartRateSensor } from "heart-rate";
// import {f} from "../common/utilsCompanion";




// Update the clock every minute
clock.granularity = "minutes";

const MAIN_CLOCK = document.getElementById("mainClock");
const myBatteryLabel = document.getElementById("myBatteryLabel");
const myHRLabel = document.getElementById("myHRLabel");
const IMG_ECare = document.getElementById("Ecare");
const batteryLogo = document.getElementById("batteryLogo");
const HRLogo = document.getElementById("HR");
const abc = document.getElementById("abc");

// Initialize everything related to the buffer stream for Accel and Gyro sensors
var samplePerSec = 100;
var batchSize = 100;
var recSize = 13;
var buffer = new ArrayBuffer(recSize * batchSize);
var accelX = new Uint16Array( buffer, 0*batchSize, batchSize  ); // batchSize x 2 bytes
var accelY = new Uint16Array( buffer, 2*batchSize, batchSize  ); // batchSize x 2 bytes
var accelZ = new Uint16Array( buffer, 4*batchSize, batchSize  ); // batchSize x 2 bytes
var gyroX  = new Uint16Array( buffer, 6*batchSize, batchSize ); // batchSize x 2 bytes
var gyroY  = new Uint16Array( buffer, 8*batchSize, batchSize ); // batchSize x 2 bytes
var gyroZ  = new Uint16Array( buffer, 10*batchSize, batchSize ); // batchSize x 2 bytes
var actType =  new Uint8Array( buffer, 12*batchSize, batchSize ); // batchSize x 1 byte

// Setup sensor recordings.
var accel = new Accelerometer({ frequency: samplePerSec, batch: batchSize });
var gyro = new Gyroscope({ frequency: samplePerSec, batch: batchSize });

// Get a handle on the <text> element
const myLabel = document.getElementById("myLabel");

// Initialize booleans to check when both sensors have done reading through the batch
var accelReady = false;
var gyroReady = false;

// Initialize main timer count and recording status (0 = Not Recording, 1 = Recording)
var timerCount = 0;

// Initilize fileName that will be used to save the recording
var fileName;
var filesToTransfer = '';



function deleteAllFiles() {
  try {
    let deviceFiles = getDeviceFileNames();
    for (let idx in deviceFiles) {
      let tempFile = deviceFiles[idx];
      fs.unlinkSync(tempFile);
      console.log("File " + tempFile + " deleted.");
    }
  } catch(err) {
    console.log("Error: File " + fileName + " doesn't exists");
  }
}
deleteAllFiles();

// Initialize boolean file that would tell weather to save or delete the previous recording
var saveActivityFile = false;

var enablePlay = true;
var enableStop = false;
var enableSave = false;
var enableSend = false;
function auto_record(){
  // console.log("fell in app: ", f.fell);
  // if (f.fell == 1){
  //   abc.style.visibility = 'visible'
  //   IMG_ECare.style.visibility = 'hidden'
  //   batteryLogo.style.visibility = 'hidden'
  //   HRLogo.style.visibility = 'hidden'
  // }
  // else{
  //   abc.style.visibility = 'hidden'
  // }
    startRecording();
    console.log("Started recording");
    setTimeout(() => {    
    stopRecording();
      console.log("stopped recording");
      saveRecording();
      console.log("saved recording");
      sendRecording();
      console.log("sent recording"); }, 60000);
} 

var myVar = setInterval(auto_record, 60000);

function deleteFile(fileName) {
    try {
      fs.unlinkSync(fileName);
      console.log("File " + fileName + " deleted.")
    } catch(err) {
      console.log("Error: File " + fileName + " doesn't exists");
    }
}

function getFileTimestamp() {
    var d = new Date();
    return d.getFullYear() + util.zeroPad(d.getMonth()+1) + util.zeroPad(d.getDate()) + util.zeroPad(d.getHours()) + util.zeroPad(d.getMinutes()) + util.zeroPad(d.getSeconds()); 
}

function getDeviceFileNames() {
    let deviceFiles = [];
    try {    
      const listDir = fs.listDirSync("/private/data/");
      let fileObj = listDir.next();
      while (!fileObj.done) {
        deviceFiles.push(fileObj.value);
        fileObj = listDir.next();
      }
    } catch(err) {
      console.log("Error: Couldn't get the file: " + err);
    }
    return deviceFiles;
  }

// Start Recording
function startRecording() {
    enablePlay = enableSave = enableSend = false;
    enableStop = true;
  
    if (!saveActivityFile && fileName!== undefined) {
      deleteFile(fileName);
    }
    saveActivityFile = false;
    fileName = 'Reading_' + getFileTimestamp() + '.bin';
  
    accel.start();
    gyro.start();
    // vibration.start("bump");
}

// Stop Recording
function stopRecording() {
    enablePlay = enableSave = enableSend = true;
    enableStop = false;
    
    accel.stop();
    gyro.stop();
    // vibration.start("bump");
}

// Save Recording
function saveRecording() {
    enablePlay = enableSend = true;
    enableStop = enableSave = false;
    
    saveActivityFile = true;
    // vibration.start("confirmation-max");
}
  
// Send Recording
function sendRecording() {
    enablePlay = enableStop = enableSave = enableSend = false;
    
    if (!saveActivityFile) {
      deleteFile(fileName);
    }
  
    // vibration.start("nudge");
    filesToTransfer = getDeviceFileNames();
    sendRawData();
}

// Write accel data each time an accel reading happens
accel.onreading = function() {
    for (let index = 0; index < accel.readings.timestamp.length; index++) {
      accelX[index] = util.floatToUint16(accel.readings.x[index]);
      accelY[index] = util.floatToUint16(accel.readings.y[index]);
      accelZ[index] = util.floatToUint16(accel.readings.z[index]);
    }
    accelReady = true;
    if (accelReady && gyroReady) {
      accelReady = false;
      gyroReady = false;
      let file = fs.openSync(fileName, "a+");
      fs.writeSync(file, buffer); // write the record
      fs.closeSync(file); // and commit changes -- important if you are about to read from the file
    }
}

// Write gyro data each time an gyro reading happens
gyro.onreading = function() {
    for (let index = 0; index < gyro.readings.timestamp.length; index++) {
      gyroX[index] = util.floatToUint16(gyro.readings.x[index]);
      gyroY[index] = util.floatToUint16(gyro.readings.y[index]);
      gyroZ[index] = util.floatToUint16(gyro.readings.z[index]);
    }
    gyroReady = true;
    if (accelReady && gyroReady) {
      accelReady = false;
      gyroReady = false;
      let file = fs.openSync(fileName, "a+");
      fs.writeSync(file, buffer); // write the record
      fs.closeSync(file); // and commit changes -- important if you are about to read from the file
    }
}

// Function to send the raw data to the companion
function sendRawData() {
    let filesPending = filesToTransfer.length;
  
    if (filesPending > 0) {
      let tempQueueFile = filesToTransfer.shift();
      console.log("Temp Queue: " + tempQueueFile);
  
      outbox.enqueueFile(tempQueueFile).then((ft) => {
        console.log('Transfer of ' + ft.name + ' successfully queued.');
        ft.onchange = () => {
          console.log('File Transfer State: ' + ft.readyState);
          if (ft.readyState === 'transferred') {
            // Keep sending raw data if there are files available
            deleteFile(ft.name);
            sendRawData();
          }
        }
      })
      .catch((error) => {console.log('Failed to schedule transfer: ' + error);})
  
    } else {
      enablePlay = true;
      enableStop = enableSave = enableSend = false;
      // vibration.start("nudge-max");
    }
  }
// Update the <text> element every tick with the current time
clock.ontick = (evt) => {
  let today = evt.date;
  let hours = today.getHours();
  if (preferences.clockDisplay === "12h") {
    // 12h format
    hours = hours % 12 || 12;
  } else {
    // 24h format
    hours = util.zeroPad(hours);
  }
  let mins = util.zeroPad(today.getMinutes());
  myLabel.text = `${hours}:${mins}`;
}

myBatteryLabel.text = `${battery.chargeLevel} %`;
battery.onchange = (evt) => {
  myBatteryLabel.text = `${battery.chargeLevel} %`;
}

if (HeartRateSensor) {
   
   const hrm = new HeartRateSensor();
   hrm.addEventListener("reading", () => {
     myHRLabel.text = "HR: "+`${hrm.heartRate}`;
   });
   hrm.start();
} else {
   console.log("This device does NOT have a HeartRateSensor!");
}
//console.log(f.fell);
IMG_ECare.style.visibility = 'visible'
batteryLogo.style.visibility = 'visible'
HRLogo.style.visibility = 'visible'

