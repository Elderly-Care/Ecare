"""
Script to retrieve Fitbit data for the given user
"""
import json

from fitbit import Fitbit
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from dictor import dictor
import datetime
from datetime import time
import requests

# Use a service account
cred = credentials.Certificate(
    '/Users/Ahmed/Desktop/thesis/fitbit-collect/serviceKey.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

FITBIT_API = 'https://api.fitbit.com'

CLIENT_DETAILS_FILE = 'client_details.json'  # configuration for for the client
USER_DETAILS_FILE = 'user_details.json'  # user details file

RESULT_FILE = 'fitbit_data_heart.json'  # The place where we will place the results
RESULT_FILE2 = 'fitbit_data_sleep.json'
RESULT_FILE3 = 'fitbit_data_cal.json'
RESULT_FILE4 = 'fitbit_data_steps.json'

IPS_FILE = 'ips_data.json'
weekdays = ["Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"]


def refresh_callback(token):
    """
    This method is only called when the authenticate token is out of date
    and a new token has been issued which needs to be stored somewhere for
    the next run
    param (token): A dictionary with the new details
    """
    print('CALLBACK: The token has been updated since last run')
    with open(USER_DETAILS_FILE, 'w') as f:
        json.dump(token, f)
    print('Successfully written update refresh token')


def _get_user_details():
    """
    The specific user that you want to retrieve data for.
    """
    with open(USER_DETAILS_FILE) as f:
        fitbit_user = json.load(f)
        access_token = fitbit_user['access_token']
        refresh_token = fitbit_user['refresh_token']
        expires_at = fitbit_user['expires_at']

    return access_token, refresh_token, expires_at


def _get_client_details():
    """The client is the application which requires access"""
    with open(CLIENT_DETAILS_FILE) as f:
        client_details = json.load(f)
        client_id = client_details['client_id']
        client_secret = client_details['client_secret']

    return client_id, client_secret


def _write_results(json_response,file):
    with open(file, 'w') as f:
        json.dump(json_response, f)
    print(f'Successfully written result data to file {file}')


def _get_api_call(no):
    """
    Date Api in this format:
    GET /<api-version>/user/<user-id>/<resource-path>/date/<base-date>/<end-date>.<response-format>
    <user-id> can be left as - to get the current user
    date format is '%Y-%m-%d' for example 2020-04-01
    Some examples below
    """

    # Steps in the last 7 days
    steps_last_seven_days = '/1/user/-/activities/log/steps/date/2021-03-02/7d.json'
    # Steps between two dates
    steps_dates = '/1/user/-/activities/log/steps/date/2021-02-22/2021-03-04.json'
    # calories last 7 days
    calories_last_seven_days = '/1/user/-/activities/log/calories/date/today/7d.json'
    # profile info
    profile_info = '/1/user/-/profile.json'
    # Sleep between two dates
    sleep_dates = '/1.2/user/-/sleep/date/2020-04-13/2020-04-17.json'
    # activity between two dates
    activity = '/1/user/-/activities/steps/date/2021-02-27/1d/1min.json'

    heart = '/1/user/-/activities/heart/date/2021-03-02/1d/1hr.json'

    heart_intra = '/1/user/-/activities/heart/date/today/1d/1min.json'

    sleep = '/1.2/user/-/sleep/date/2021-02-22/2021-03-02.json'

    if no == 1:
        # heart rate intraday
        url = '/1/user/-/activities/heart/date/today/1d/1min.json'
    elif no == 2:
        # Sleep between two dates
        url = '/1.2/user/-/sleep/date/2021-02-22/2021-03-02.json'
    elif no == 3:
        # calories last 7 days
        url =  '/1/user/-/activities/log/calories/date/today/7d.json'
    elif no == 4:
        # Steps in the last 7 days
        url = '/1/user/-/activities/log/steps/date/today/7d.json'
    return url


def run():
    client_id, client_secret = _get_client_details()
    access_token, refresh_token, expires_at = _get_user_details()

    # print(f'Running Fitbit request with details: {client_id} {client_secret}'
    #      f' {access_token} {refresh_token} {expires_at}')
    auth2_client = Fitbit(client_id, client_secret, oauth2=True,
                          access_token=access_token,
                          refresh_token=refresh_token, expires_at=expires_at,
                          refresh_cb=refresh_callback)

    fitbit_url = FITBIT_API + _get_api_call(1)
    json_response = auth2_client.make_request(fitbit_url)
    _write_results(json_response,RESULT_FILE)

    length = len(json_response["activities-heart-intraday"]['dataset'])
    heart_min_data = json_response["activities-heart-intraday"]['dataset']    
    HR_hour_data = {} 
    HR_min_data = {} 
    for i in range(0,length):
        if(i%60 == 0):
            if heart_min_data[i]["time"].split(":")[0][0] == '0':
                HR_min_data[heart_min_data[i]["time"].split(":")[0][1]] = [heart_min_data[i]["value"]]
            else:
                HR_min_data[heart_min_data[i]["time"].split(":")[0]] = [heart_min_data[i]["value"]]
        else:
            if heart_min_data[i]["time"].split(":")[0][0] == '0':
                HR_min_data[heart_min_data[i]["time"].split(":")[0][1]].append(heart_min_data[i]["value"])
            else:
                HR_min_data[heart_min_data[i]["time"].split(":")[0]].append(heart_min_data[i]["value"])

    for i,j in HR_min_data.items():
        HR_hour_data[i] = int(sum(j)/len(j))
        
    print(HR_hour_data)
    
    fitbit_url = FITBIT_API + _get_api_call(2)
    json_response = auth2_client.make_request(fitbit_url)
    _write_results(json_response,RESULT_FILE2)

    sleep_data = {}
    for i in json_response["sleep"]:
        sleep_data[i['dateOfSleep']]= i['minutesAsleep']

    print(sleep_data)

    fitbit_url = FITBIT_API + _get_api_call(3)
    json_response = auth2_client.make_request(fitbit_url)
    _write_results(json_response,RESULT_FILE3)

    calories = {}
    for date in json_response["activities-log-calories"]:
        calories[date['dateTime']]= int(date['value'])
    print(calories)
    print(json_response)


    fitbit_url = FITBIT_API + _get_api_call(4)
    json_response = auth2_client.make_request(fitbit_url)
    _write_results(json_response,RESULT_FILE4)
    
    steps_date = {}
    weekly_steps = {}
    print(json_response["activities-log-steps"])
    for date in json_response["activities-log-steps"]:
        steps_date[date['dateTime']]= int(date['value'])
    print(steps_date)

    for i in steps_date:
            if i != 0:
                date = i
                weekday = datetime.datetime.strptime(date, '%Y-%m-%d').weekday()
                weekly_steps[weekdays[weekday]] = steps_date[i]
    print(weekly_steps)

    # weekly_HR = {}
    # weekday_HR = {}
    # for i in range(7):
    #     restingHR = dictor(json_response, "activities-heart." +
    #                        str(i)+".value.restingHeartRate")
    #     if restingHR != None:
    #         date = dictor(json_response, "activities-heart." +
    #                       str(i)+".dateTime")
    #         weekly_HR[date] = restingHR
    #         weekday = datetime.datetime.strptime(date, '%Y-%m-%d').weekday()
    #         weekday_HR[weekdays[weekday]] = restingHR

    


    # print(weekly_HR)

    


    # usersRef = db.collection(u'users')
    # users = usersRef.where(u'name', u'==', u'Nouran Flaifel').where(
    #     u'dateOfBirth', u'==', u'1998-08-23').get()
    # for doc in users:
    #     uid = (f'{doc.id}')

    # db.collection(u'users').document(uid).update({'hourlyHR' : HR_hour_data})
    # db.collection(u'history').document(uid).update({'sleep': sleep_data})
    # db.collection(u'history').document(uid).update({'calories': calories})
    # db.collection(u'history').document(uid).update({'heartRate': weekly_HR})
    # db.collection(u'history').document(uid).update({'steps': steps_date})
    # db.collection(u'users').document(uid).update({'weekSteps' : weekly_steps})
    # db.collection(u'users').document(uid).update({'weekDayHR' : weekday_HR})
   


if __name__ == '__main__':
    run()
