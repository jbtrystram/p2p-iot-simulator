#!/usr/bin/python3

import numpy as np
import matplotlib.pyplot as plt
import argparse

parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
parser.add_argument('coord_file', type=str, help='coord data file path')
#parser.add_argument('out', type=str, help='output file path')

args = parser.parse_args()

#get neighbors file 
neigh_file="graphs/neighbors"+args.coord_file.lstrip("graphs/graph")


coord = np.genfromtxt(args.coord_file, delimiter=';')

#more awesomeness
plt.xkcd()

plt.figure()

#plt.ylabel('X')
#plt.xlabel('Y')
plt.axis('off')  #remove axises
axes = plt.gca() # auto-set ticks

axes.set_xlim([0,1000])
axes.set_ylim([0,1000])
plt.title('Node positions & discovered links')

#plot neigbors relationships

for line in open(neigh_file):
	neighbors = np.fromstring( (line.rstrip('\n')), sep=';', dtype=int)
	for node in range(1, neighbors.size):
		A = neighbors[0]
		B = neighbors[node]
		plt.plot([coord[A,1], coord[B,1]], [coord[A,2], coord[B,2]],  linewidth=0.5, zorder=-1)

# Plot nodes
plt.scatter(coord[:,1], coord[:,2], s=7, zorder=1)

plt.tight_layout()
plt.savefig(args.coord_file+'.pdf')
#plt.savefig(args.coord_file+'.map.png', dpi = (200))
