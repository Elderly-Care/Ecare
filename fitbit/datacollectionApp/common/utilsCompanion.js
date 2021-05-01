function pad(n) {
  return n<10 ? '0'+n : n;
}

// Function used to upload the raw data into a webservice using 'fetch'
export function uploadDataToServer(theData, activityType) {
  var activity_type = ['Walking', 'D01','D02','D03','D04','D05','D06','D07','D08','D09','D10', 'D11', 'D12', 'D13', 'D14', 'D15', 'D16', 'D17', 'D18', 'D19'];
  var d = new Date();
  console.log("YOU ARE IN UPLOAD")
  let path = 'https://firestore.googleapis.com/v1/projects/ecare-a10dd/databases/(default)/documents/Mohamed?key=AIzaSyA7Ctxqzk__vXitpx5k8yuFT13nso1EJiM&documentId=' + activity_type[activityType] + "_" + d.getFullYear() + pad(d.getMonth()+1) + pad(d.getDate()) + pad(d.getHours()) + pad(d.getMinutes()) + pad(d.getSeconds())
  
  fetch(path, {
    method: 'POST',
    body:  "{\"fields\" : {\"data\" : { \"stringValue\":\" "+ theData +" \"}}}"
  })
  .catch(error => console.log('Error:', error));
  
  console.log("YOU ARE LEAVING UPLOAD")
  console.log("{\"fields\" : {\"data\" : { \"stringValue\": "+ theData +"}}}")
}






























// Convert a Uint16Array (bits) into float
export function uInt16ToFloat(h) {
  var s = (h & 0x8000) >> 15;
  var e = (h & 0x7C00) >> 10;
  var f = h & 0x03FF;
  if(e == 0) {
      return (s?-1:1) * Math.pow(2,-14) * (f/Math.pow(2, 10));
  } else if (e == 0x1F) {
      return f?NaN:((s?-1:1)*Infinity);
  }
  return (s?-1:1) * Math.pow(2, e-15) * (1+(f/Math.pow(2, 10)));
}