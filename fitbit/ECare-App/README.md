# Fitbit ECare clock face

<img src= />

ECare's clock face app is always sending accelerometer and gyroscope readings to firebase in order to run the fall detection ML model.

The Fitbit watch sends the readings every 1 minute firebase, meaning that when a fall happens the notification will be sent after 1 minute.

The Fitbit's battery life running the app is between 12 and 14 hours

# Future work

We are planning on increasing the battery life by Processing the fall detection data on the smartwatch to detect irregularities before sending
it to the cloud functions to analyze it and find if it’s a fall, this would consume less power from the smartwatch as it would decrease the number 
of data sent to the server.
