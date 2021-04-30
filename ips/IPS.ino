#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEScan.h>
#include <BLEAdvertisedDevice.h>
#include <WiFi.h>
#include <FirebaseESP32.h>
#include "time.h"
#include <string>



#define LED_BUILTIN 2
#define FIREBASE_HOST "https://ecare-a10dd-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "wirj34WnZReiv47wCSEAAsotrxjQGYiHgHpM7KOW"

//Define FirebaseESP32 data object
FirebaseData firebaseData;
FirebaseJson json;

//const char* WIFI_SSID = "WE7C8D4E";
//const char* WIFI_PASSWORD = "kb215733";
//const char* WIFI_SSID = "Naguib2";
//const char* WIFI_PASSWORD = "amnz19931993";
const char* WIFI_SSID       = "WE56733F";
const char* WIFI_PASSWORD   = "l2036592";
//const char* WIFI_SSID = "iPhone";
//const char* WIFI_PASSWORD = "12345678";

//Data Needed for timestamps calculations
const char* ntpServer = "pool.ntp.org";
const long  gmtOffset_sec = 7200;
const int   daylightOffset_sec = 3600;
struct tm timeinfo;

//Galaxy: "ff:ff:cc:c0:49:a5"
//ITag: "ff:ff:cc:c0:49:a5"
//Fitbit: "f3:20:da:df:5c:fd"
String knownBLEAddresses = "f3:20:da:df:5c:fd";
int RSSI_THRESHOLD = -75;
int scanTime = 5; //In seconds
int LocationFlag = 0;
int num_of_entries = 0;
int Test = 11;
BLEScan* pBLEScan;

class MyAdvertisedDeviceCallbacks: public BLEAdvertisedDeviceCallbacks {
    void onResult(BLEAdvertisedDevice advertisedDevice) {
     Serial.printf("Advertised Device: %s \n", advertisedDevice.toString().c_str());
    }
};

void printLocalTime()
{
  if(!getLocalTime(&timeinfo)){
    Serial.println("Failed to obtain time");
    return;
  }
  Serial.println(&timeinfo, "%A, %B %d %Y %H:%M:%S");
}

String getTime(bool dateFlag)
{
          getLocalTime(&timeinfo);
          int current_hour = timeinfo.tm_hour;
          int current_min = timeinfo.tm_min;
          int current_sec  = timeinfo.tm_sec;

          int current_day = timeinfo.tm_mday;
          int current_month = timeinfo.tm_mon + 1;
          int current_year = timeinfo.tm_year +1900;
          //Serial.printf("%d/%d/%d, %d:%d:%d", current_day, current_month, current_year, current_hour, current_min, current_sec);
          String TimeString = "", DateString = "";
          char temp[10], totalDay[100]= "", totalTime[100] = "", temp2[10];
          itoa (current_day,temp,10);
          strcpy(totalDay,temp);
          strcat(totalDay,"-");
          itoa (current_month,temp,10);
          strcat(totalDay, temp);
          strcat(totalDay,"-");
          itoa (current_year,temp,10);
          strcat(totalDay, temp);
          DateString = String(totalDay);
          

          itoa (current_hour,temp2,10);
          strcat(totalTime, temp2);
          strcat(totalTime,":");
          itoa (current_min,temp2,10);
          strcat(totalTime, temp2);
          strcat(totalTime,":");
          itoa (current_sec,temp2,10);
          strcat(totalTime, temp2);

          TimeString = String(totalTime);

          if(dateFlag == true)
          {
            return DateString;
          }
          else
          {
          return TimeString;
          }
}

void setup() {
  Serial.begin(115200); //Enable UART on ESP32

  
  /* ----- WiFi Steup -----*/
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* ----- Firebase Steup -----*/
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
 
  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  //tiny, small, medium, large and unlimited.
  //Size and its write timeout e.g. tiny (1s), small (10s), medium (30s) and large (60s).
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
    /*
  This option allows get and delete functions (PUT and DELETE HTTP requests) works for device connected behind the
  Firewall that allows only GET and POST requests.
  
  Firebase.enableClassicRequest(firebaseData, true);
  */
 
  //String path = "/data";
  Serial.println("------------------------------------");
  Serial.println("Connected...");

  /* ------- BLE Setup ------------- */
  Serial.println("Scanning..."); // Print Scanning
  pinMode(LED_BUILTIN, OUTPUT); //make BUILTIN_LED pin as output
  BLEDevice::init("");
  pBLEScan = BLEDevice::getScan(); //create new scan
  pBLEScan->setAdvertisedDeviceCallbacks(new MyAdvertisedDeviceCallbacks()); //Init Callback Function
  pBLEScan->setActiveScan(true); //active scan uses more power, but get results faster
  pBLEScan->setInterval(100); // set Scan interval
  pBLEScan->setWindow(99);  // less or equal setInterval value

  //init the time
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
  printLocalTime();
}
void loop() {
  Serial.print("Number of entries: ");
  Serial.println(num_of_entries);
  String Date, DayPath;
  Date = getTime(1);
  DayPath = "TzV04aBQlhTn8CN7w07Ub1p9k7G2/Kitchen/"+Date;
  int DeviceFound = 0;
  // put your main code here, to run repeatedly:
  BLEScanResults foundDevices = pBLEScan->start(scanTime, false);
  for (int i = 0; i < foundDevices.getCount(); i++)
  {
    BLEAdvertisedDevice device = foundDevices.getDevice(i);
    int rssi = device.getRSSI();
    Serial.print("RSSI: ");
    Serial.println(rssi);
    if (rssi > RSSI_THRESHOLD && strcmp(device.getAddress().toString().c_str(), knownBLEAddresses.c_str()) == 0)
      { 
        if(LocationFlag == 0) //Just enetered the room
        {
          num_of_entries++;
          
          String TimeString, PathString;
          TimeString = getTime(0);
          Serial.println(TimeString);
          

          char temp[10], Path[100];
          itoa (num_of_entries,temp,10);
          strcpy(Path,"/Entry");
          strcat(Path,temp);
          PathString = String(Path); // /Entry1, /Exit2 ....

          json.set(PathString, TimeString);
          Firebase.updateNode(firebaseData,DayPath,json);

        }
        DeviceFound = 1;
        digitalWrite(LED_BUILTIN, HIGH);
        LocationFlag = 1;
        break;
      }
  }
  if (DeviceFound == 0)
  {
    if(LocationFlag == 1) //Just left the room
    {
      digitalWrite(LED_BUILTIN, LOW);
      LocationFlag = 0;
      
      String TimeString, PathString;
      TimeString = getTime(0);
      Serial.println(TimeString);

      
      char temp[10], Path[100];
      itoa (num_of_entries,temp,10);
      strcpy(Path,"/Exit");
      strcat(Path,temp);
      PathString = String(Path);

      json.set(PathString, TimeString);
      Firebase.updateNode(firebaseData, DayPath,json);

    }
  }
  
  json.set("/Value", LocationFlag);
  Firebase.updateNode(firebaseData,"/TzV04aBQlhTn8CN7w07Ub1p9k7G2/Location/Kitchen",json);

  
  pBLEScan->clearResults();   // delete results fromBLEScan buffer to release memory
  Serial.println(LocationFlag); 

  json.set("/Num_Entries", num_of_entries);
  Firebase.updateNode(firebaseData, DayPath,json);

  
}
