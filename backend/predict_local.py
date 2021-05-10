
'''local model prediction 

load model which is in the directory locally 
collect data for prediction from Firebase Firestore Database
update database with prediction
delete document after to avoid clutter 
'''

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from numpy.lib.stride_tricks import as_strided
from keras.models import load_model
import numpy as np
import math


def search_sequence_numpy(arr):
    """ Find sequence in an array using NumPy only.

    Parameters
    ----------    
    arr    : input 1D array
    seq    : input 1D array

    Output
    ------    
    Output : 1D Array of indices in the input array that satisfy the 
    matching of input sequence in the input array.
    In case of no match, an empty list is returned.
    """
    seq = np.array([1,1,1])
    # Store sizes of input array and sequence
    Na, Nseq = arr.size, seq.size

    # Range of sequence
    r_seq = np.arange(Nseq)

    # Create a 2D array of sliding indices across the entire length of input array.
    # Match up with the input sequence & get the matching starting indices.
    M = (arr[np.arange(Na-Nseq+1)[:,None] + r_seq] == seq).all(1)

    # Get the range of those indices as final output
    if M.any() >0:
        return np.where(np.convolve(M,np.ones((Nseq),dtype=int))>0)[0]
    else:
        return []         

cred = credentials.Certificate(
    '/Users/Ahmed/Desktop/thesis/fitbit-collect/serviceKey.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

docs = db.collection(u'fallData').stream()
model = load_model('/Users/Ahmed/Desktop/thesis/fitbit-collect/model_2704')
falls = []
all = []


for doc in docs:
    uid  =doc.id.split('_')[0]
    
    book = doc.to_dict()
    x = book['data']
    y = x.split(',')
    y = list(map(float, y))
    
    arr = np.array(y)
    h = int(len(y)/6)
    my_data = arr.reshape(h,6)


    windows = 128
    no_windows = math.floor((h-windows)/(windows/2))
    stride = my_data.strides[-1]
    win = as_strided(my_data, shape=(no_windows, windows, 6), strides=(stride*64*6, stride*6, stride))

    y_pred = np.argmax(model.predict(win), axis=-1)

    fell = 0
    inds = search_sequence_numpy(y_pred)
    if len(inds) > 0:
        fell = 1
        falls.append(fell)
    all.append(fell)
    db.collection(u'users').document(uid).update({'fell' : fell})
    doc.reference.delete()
    print(y_pred.shape, y_pred)
print(len(falls),len(all))
