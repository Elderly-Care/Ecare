# Fall detection
## Importance of Fall detection
Falling is one of the main causes of serious injury to elderly people and the world's second leading cause of accidental deaths. Here, elderly people refer to a person aged 65 years or older. Different effects can be caused by falls in the elderly, such as major or minor wrist or hip fractures, broken ribs or, in some cases, significant damage to cause death or permanent disability.

## Hardware used
* Fitbit Sense smartwatch
* Smartphone (Android/IOS)

## Design
* E-Care system uses a smartwatch which will make the designed algorithm fall under the category of Wearable based Systems (WS).
* WS is proven to be more practical, cheaper and provide better security of the patient’s personal data.
* Using Machine Learnin in developing such algorithms tends to produce better results than threshold-based algorithms; that is why in E-Care system we decided to implement the algorithm using deep machine learning.
* To train this model we used a blend of an online dataset along with creating our own dataset directly from the smartwatch to enhance the model’s accuracy.

### Dataset collection
* The SisFall dataset was the most complete dataset and the most commonly used in research as well.
<img src="https://github.com/Elderly-Care/Ecare/blob/main/media/falldata_comparison.png" width="800" height="250">

* The SisFall Dataset is the most complete and simulates the widest range of fall and ADLs (Activity of Daily Living). But we had two problems with this dataset, first, it placed the sensors on the user’s waist while our sensors are placed on the wrist (embedded in the watch). The second issue was the frequency as the SisFall sampled the data with frequency of 200Hz and the maximum frequency we could acquire from the watch was only 100Hz.
* This situation was the main motive for us to create our own dataset mimicking the SisFall one in terms of falls and ADLs performed. We created a clock face using Fitbit Studio to record the accelerometer and gyroscope data with a frequency of 100Hz after the user choses which activity he wishes to simulate. After the user is done, he stops the timer and send this data to our database.

<img src="https://github.com/Elderly-Care/Ecare/blob/main/media/data_collection_app.png" width="200" height="250"><img src="https://github.com/Elderly-Care/Ecare/blob/main/media/falldetection_data.png" width="500" height="250">

## Implementation
* A machine learning model was created from the dataset collected by the E-Care data collection application
* The model used was based on RNN (Recurrent Neural Network) architecture with LSTM (Long Short-Term Memory) blocks. It was designed in this fashion specifically to be light and not consume much power or time while executing in real time.
* In the preprocessing process the data was divided into windows of 128 readings which account for 1.28 seconds.
* The final model reached accuracy of detecting falls of 85% and false positive results of only 10%. This is a very good accuracy to start with and we plan to work on increasing these percentages in the future after expanding our dataset and through other techniques which will be discussed in the final future work section.
* Finally, all what was needed from the smartwatch was to send the accelerometer and gyroscope readings periodically once every minute with the desired frequency to our database, and there will be a google cloud function that shall fetch this data, feed it to the ML model and uses the output to update the UI.
* The fall detection feature works in real-time through E-Care's clock face as shown below.
<img src="https://github.com/Elderly-Care/Ecare/blob/main/media/clock_face.jpeg" width="200" height="250">

* Battery note: The application running at all times causes the battery lifetime to decrease from 16-18 hours to 12-14 hours.
  * This is an issue we wish to solve as our goal is to have the battery life boosted up to 16 hours as this is the average amount of time a human is awake in a day.

