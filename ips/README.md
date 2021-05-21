# Indoor Positioning System (IPS)
## IPS features

* The system tracks the patient’s well-being during his/her day-to-day activities. 
* The system allows the caregiver to know the location of the patient within the house at all times, which will help ease the stress if the patient was not responding, they will simply open the application and find that they are in the bathroom for example. 
* The system will also provide summaries of how many visits did the patient do to each room and the total time spent there too. 
* The system can detect any irregularities in the daily routine based on the patient’s average in the past weeks. 
* This feature could detect health issues such as visiting the bathroom more times than usual or possibility in psychological disorders caused by not leaving the bedroom or not visiting the kitchen to grab their meals.

## Hardware
* Fitbit Sense smartwatch
* ESP32 
* Smartphone (Android/IOS)

## Implementation
* Arduino IDE was used to program the ESP32 which are the microcontrollers
to be put in each room.
* The ESP32s act as the BLE client searching for the smartwatch
at all times.
*  Whenever the smartwatch MAC address is found within a certain range
from the microcontroller, it will send to the database a flag showing that the user is
currently in that specific room, it will also send a timestamp of the entry time along with the RSSI (Received Signal Strength Indicator).
* The UI will be updated based on the flags received from each room, if two flags were true at the same time, the RSSI will be the deciding factor in the positioning decision.

<img src="https://github.com/Elderly-Care/Ecare/blob/main/media/real_time_DB.png" width="600" height="250">

* The total number of entries and the time spent in each room will be calculated and saved in the user’s history. 
* If any irregularity in this data was detected compared to the patient’s normal entries in the past weeks, a notification will be sent to him and his caregivers.
