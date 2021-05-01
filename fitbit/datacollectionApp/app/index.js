import { Accelerometer } from "accelerometer";
import { Gyroscope } from "gyroscope";
import { display } from "display";
import document from "document";
import * as util from "../common/utilsDevice";
import * as fs from "fs";
import { battery } from "power";
import document from "document";
import { vibration } from "haptics";
import { outbox } from "file-transfer";


// Initialize all constant screen variables
const MAIN_CLOCK = document.getElementById("mainClock");
const BATTERY = document.getElementById("clockBattery");
const ACT_TYPE = document.getElementById("activityType");
const FILE_COUNT = document.getElementById("fileCount");
const BACK_RECT = document.getElementById("background");
const IMG_PLAY = document.getElementById("play");
const IMG_STOP = document.getElementById("stop");
const IMG_SAVE = document.getElementById("save");
const IMG_SEND = document.getElementById("send");

// Initialize main timer count and recording status (0 = Not Recording, 1 = Recording)
var timerCount = 0;

// Initialize an array with all different posible activities as well as the activity counter to display the correct activity
var activity_type = ['Walking', 'D01','D02','D03','D04','D05','D06','D07','D08','D09','D10', 'D11', 'D12', 'D13', 'D14', 'D15', 'D16', 'D17', 'D18', 'D19'];
var activity_count = 0;

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

// Initialize booleans to check when both sensors have done reading through the batch
var accelReady = false;
var gyroReady = false;

// Initilize fileName that will be used to save the recording
var fileName;
var filesToTransfer = '';
util.deleteAllFiles();

// Initialize boolean file that would tell weather to save or delete the previous recording
var saveActivityFile = false;

var enablePlay = true;
var enableStop = false;
var enableSave = false;
var enableSend = false;

// function auto_record(){
//     startRecording();
//     console.log("Started recording");
//     setTimeout(() => {    
//     stopRecording();
//       console.log("stopped recording");
//       saveRecording();
//       console.log("saved recording");
//       sendRecording();
//       console.log("sent recording"); }, 60000);
// } 

// var myVar = setInterval(auto_record, 60000);

BACK_RECT.onmouseup = (evt)  => {
  let screenX = evt.screenX;
  let screenY = evt.screenY;

  // Click on Bottom Right corner (Start/Stop recording)
  if ((enablePlay || enableStop) && screenX >= 160 && screenY >= 220) {
    if (enablePlay) {
      // auto_record();
      startRecording();
    } else if (enableStop) {
      stopRecording();
    }
  }
  // Click on Mid Right corner (Save recording)
  if (enableSave && screenX >= 200 && (screenY >= 110 && screenY <= 200)) {
    saveRecording();
  }
  // Click on Top Right corner (Send recording)
  if (enableSend && screenX >= 160 && screenY <= 70) {
    sendRecording();
  }
  // Click on Bottom Left corner (Change Activity Type)
  if (screenX <= 140 && screenY >= 220) {
    changeActivity();
  }
}

function changeButtonDisplay(play, stop, save, send) {
  IMG_PLAY.style.visibility = (play) ? 'visible' : 'hidden';
  IMG_STOP.style.visibility = (stop) ? 'visible' : 'hidden';
  IMG_SAVE.style.visibility = (save) ? 'visible' : 'hidden';
  IMG_SEND.style.visibility = (send) ? 'visible' : 'hidden';
}

// Start Recording
function startRecording() {
  enablePlay = enableSave = enableSend = false;
  enableStop = true;
  changeButtonDisplay(enablePlay, enableStop, enableSave, enableSend);

  if (!saveActivityFile && fileName!== undefined) {
    util.deleteFile(fileName);
  }
  saveActivityFile = false;
  fileName = activity_type[activity_count] + '_' + util.getFileTimestamp() + '.bin';

  timerCount = 0;

  accel.start();
  gyro.start();
  vibration.start("bump");
}

// Stop Recording
function stopRecording() {
  enablePlay = enableSave = enableSend = true;
  enableStop = false;
  changeButtonDisplay(enablePlay, enableStop, enableSave, enableSend);
  
  accel.stop();
  gyro.stop();
  vibration.start("bump");
}

// Save Recording
function saveRecording() {
  enablePlay = enableSend = true;
  enableStop = enableSave = false;
  changeButtonDisplay(enablePlay, enableStop, enableSave, enableSend);
  
  saveActivityFile = true;
  FILE_COUNT.text = util.getDeviceFileNames().length;
  vibration.start("confirmation-max");
}

// Send Recording
function sendRecording() {
  enablePlay = enableStop = enableSave = enableSend = false;
  changeButtonDisplay(enablePlay, enableStop, enableSave, enableSend);

  if (!saveActivityFile) {
    util.deleteFile(fileName);
  }

  vibration.start("nudge");
  filesToTransfer = util.getDeviceFileNames();
  sendRawData();
}

// Change Activity Type
function changeActivity() {
  activity_count = (activity_count < activity_type.length-1) ? activity_count+1 : 0;
  ACT_TYPE.text = activity_type[activity_count];
}


// Write accel data each time an accel reading happens
accel.onreading = function() {
    for (let index = 0; index < accel.readings.timestamp.length; index++) {
      accelX[index] = util.floatToUint16(accel.readings.x[index]);
      accelY[index] = util.floatToUint16(accel.readings.y[index]);
      accelZ[index] = util.floatToUint16(accel.readings.z[index]);
      actType[index] = activity_count;
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
    FILE_COUNT.text = filesPending+ "*";
    let tempQueueFile = filesToTransfer.shift();
    console.log("Temp Queue: " + tempQueueFile);

    outbox.enqueueFile(tempQueueFile).then((ft) => {
      console.log('Transfer of ' + ft.name + ' successfully queued.');
      FILE_COUNT.text = filesPending+ "*";
      ft.onchange = () => {
        console.log('File Transfer State: ' + ft.readyState);
        if (ft.readyState === 'transferred') {
          // Keep sending raw data if there are files available
          util.deleteFile(ft.name);
          sendRawData();
        }
      }
    })
    .catch((error) => {console.log('Failed to schedule transfer: ' + error);})

  } else {
    enablePlay = true;
    enableStop = enableSave = enableSend = false;
    changeButtonDisplay(enablePlay, enableStop, enableSave, enableSend);
    
    FILE_COUNT.text = filesPending;
    vibration.start("nudge-max");
  }
}


// Set an interval event of 1 second that adds one to the timeCount every second. If the timer reaches 60 seconds, it stops
setInterval(clockTick, 1000);
function clockTick() {
  let timerLimit = 60
  if (enableStop) {
    timerCount += 1;
    if (timerCount >= timerLimit) {
      stopRecording();
    }
  }
  MAIN_CLOCK.text = `${timerCount}`;
}

// Listen to Battery onchange event to refresh display
BATTERY.text = `${battery.chargeLevel} %`;
battery.onchange = (evt) => {
  BATTERY.text = `${battery.chargeLevel} %`;
}

