#!/usr/bin/python3

import random
import numpy as np
from math import sqrt

import matplotlib
import matplotlib.pyplot as plt

## This script simply populate a csv file with coordinates
#
# The map is populated with predefined GROUPS, evenly distributed


MAPSIZE = 1000
GROUP_NUMBER = 7
NODES_PER_GROUP = 5
GROUP_OFFSET = 40

OUTPUT_FILENAME = "nodes_position.csv"

## Group layout
# *     *
# *  *  *
# *     *

# Define counter for how many actually get placed.
numPoints = 0
# Define fail safe, how many iterations you want to keep trying before quitting.
maxIterations = 1000 * GROUP_NUMBER
loopCounter = 1
# Define how close points can come to other points before being rejected.
minClosestDistance = MAPSIZE/(sqrt(GROUP_NUMBER));


# Declare arrays to hold the x and y coordinate values.
x = np.empty(GROUP_NUMBER*NODES_PER_GROUP, dtype=int)
y = np.empty(GROUP_NUMBER*NODES_PER_GROUP, dtype=int)
distances = np.empty(GROUP_NUMBER, dtype=float)

while (numPoints < GROUP_NUMBER and loopCounter < maxIterations):
    # Get a random point.
    xPossible = random.randint(0, MAPSIZE)
    yPossible = random.randint(0, MAPSIZE)
    if numPoints == 0 :
        # First point automatically is valid.
        x[numPoints] = xPossible
        y[numPoints] = yPossible
        numPoints += 1
        continue
    # Find distances between this point and all others.
    for i in range(GROUP_NUMBER):
        distances[i] = pow(sqrt(abs(x[i]-xPossible)), 2) + pow(sqrt(abs(y[i] - yPossible)), 2)
    if np.amin(distances) >= minClosestDistance :
        # It's far enough away from all the other points, or it's the first point.
        # Include it in our list of acceptable points.
        x[numPoints] = xPossible
        y[numPoints] = yPossible
        numPoints += 1
        
    # Increment the loop counter.
    loopCounter = loopCounter + 1


  
### Now create the groups around found coordinates
# Design manually for the moment.
j = 0
for i in range(GROUP_NUMBER, GROUP_NUMBER*NODES_PER_GROUP, NODES_PER_GROUP-1):
    
    np.put(x, [i, i+1, i+2, i+3], [x[j]+GROUP_OFFSET, x[j]+GROUP_OFFSET, x[j]-GROUP_OFFSET, x[j]-GROUP_OFFSET] )
    np.put(y, [i, i+1, i+2, i+3], [y[j]+GROUP_OFFSET, y[j]-GROUP_OFFSET, y[j]+GROUP_OFFSET, y[j]-GROUP_OFFSET] )

    j+=1


# correct Out of bonds values
x = np.clip(x, 0, MAPSIZE)
y = np.clip(y, 0, MAPSIZE)

#reshape arrays
x = np.reshape(x, (x.size,1))
y = np.reshape(y, (y.size,1))
    
## concat the two arrays
data = np.concatenate((x,y), axis=1)
    
## CSV !!     
np.savetxt(OUTPUT_FILENAME, data, fmt='%u', delimiter=",")    
    
fig, ax = plt.subplots()
ax.plot(y, x, 'x')
ax.set_title('Nodes')
plt.show()