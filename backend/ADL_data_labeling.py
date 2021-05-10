'''ADL data labeling script

stream data from Firebase Firestore Database collection which is in json format 
convert data from json to python dict then get data from dict and put into an array
then create a csv file from the array where every 6 readings are 3 accelometer readings
and 3 gyroscope readings and the 7th being the label for whether a fall has occured or not 
which is zero for all these files
'''

import csv
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore


cred = credentials.Certificate(
    '/Users/Ahmed/Desktop/thesis/fitbit-collect/serviceKey.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

docs = db.collection(u'Marwan').stream()

for doc in docs:
    # print(f'{doc.id}')
    act_type = doc.id.split('_')[0]
    book = doc.to_dict()
    x = book['data']
    y = x.split(',')
    print(len(y))
    print(act_type[0],act_type[1:3])
    if act_type[0] == 'D':
        if act_type[1:3] == '01' or act_type[1:3] == '02' or act_type[1:3] == '03' or act_type[1:3] == '04' or act_type[1:3] == '09' or act_type[1:3] == '10':
            no = 30000
        elif act_type[1:3] == '05' or act_type[1:3] == '06':
            no = 15000
        elif act_type[1:3] == '07' or act_type[1:3] == '08':
            no = 9000
        else:
            no = 6000

        with open(f'Fall_Detection_data/Marwan/{doc.id}.csv', 'w', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(["X", "Y", "Z",'gX','gY','gZ','fall'])
            for i in range(0,no,6):
                writer.writerow([y[i],y[i+1],y[i+2],y[i+3],y[i+4],y[i+5],0])

