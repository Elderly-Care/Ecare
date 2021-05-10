import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import datetime
import requests

cred = credentials.Certificate(
    '/Users/Ahmed/Desktop/thesis/fitbit-collect/serviceKey.json')
firebase_admin.initialize_app(cred)

db = firestore.client()


def ips_collection():
    resp = requests.get('https://ecare-a10dd-default-rtdb.firebaseio.com/.json?print=pretty').json()
    
    for user in resp:
        locts = resp[user]
        for location in locts:
            data = locts[location]
            if location != 'Location':
                for date in data:
                    if data[date] != 0:
                        length = int(((len(data[date])-2)/2))
                        total_time = []
                        
                        for i in range(1,length+1):
                            entry = datetime.datetime.strptime(data[date]["Entry"+str(i)], '%H:%M:%S')
                            exit = datetime.datetime.strptime(data[date]["Exit"+str(i)], '%H:%M:%S')
                            total_time.append(exit - entry)
                        summed_total_time = sum(total_time,datetime.timedelta()).seconds
                        data[date]['Num_Entries'] = length
                        data[date]['Total_Time'] = summed_total_time
                        data[date].pop('Value')
                        db.collection(u'history').document(user).update({location : data})
                        print(data)       


if __name__ == '__main__':
    ips_collection()
    
