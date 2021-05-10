'''plot fitbit data

plot data collected in fitbit_data.json
which was written in run_collect.py
'''
import json
from datetime import datetime

import matplotlib.pyplot as plt
import numpy as np


def plot_steps(data):
    steps = data['activities-log-steps']

    x = [datetime.strptime(d['dateTime'], '%Y-%M-%d').strftime("%A") for d in steps]
    y = [float(d['value']) for d in steps]

    plt.bar(x,y)
    plt.title('Steps last 7 days')
    plt.show()


def plot_sleep(data):
    sleep = data['sleep']
    x = [datetime.strptime(d['dateOfSleep'], '%Y-%M-%d').strftime("%A") for d in sleep][::-1]

    deep = [float(d['levels']['summary']['deep']['minutes'])/60.0 for d in sleep][::-1]
    light = [float(d['levels']['summary']['light']['minutes'])/60.0 for d in sleep][::-1]
    rem = [float(d['levels']['summary']['rem']['minutes'])/60.0 for d in sleep][::-1]
    awake = [float(d['levels']['summary']['wake']['minutes'])/60.0 for d in sleep][::-1]

    barWidth = 0.15

    r1 = np.arange(len(deep))
    r2 = [x + barWidth for x in r1]
    r3 = [x + barWidth for x in r2]
    r4 = [x + barWidth for x in r3]

    # Make the plot
    plt.bar(r1, awake, color='#ffa600', width=barWidth, edgecolor='white', label='Awake')
    plt.bar(r2, rem, color='#ff6361', width=barWidth, edgecolor='white', label='REM')
    plt.bar(r3, light, color='#bc5090', width=barWidth, edgecolor='white', label='Light')
    plt.bar(r4, deep, color='#003f5c', width=barWidth, edgecolor='white', label='Deep')

    # Add xticks on the middle of the group bars
    plt.xlabel('Day', fontweight='bold')
    plt.ylabel('Hours', fontweight='bold')
    plt.xticks([r + barWidth for r in range(len(deep))], x)
 
    # Create legend & Show graphic
    plt.legend()
    plt.show()

if __name__ == '__main__':
    with open('fitbit_data.json') as f:
        data = json.load(f)

    plot_steps(data)
    # plot_sleep(data)



