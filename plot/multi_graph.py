#!/usr/bin/python3

import numpy as np
import matplotlib.pyplot as plt
import argparse
from multiprocessing import Process
import os


# Initialisation ===============
def plotter(file_seq, path):
    
    coord = np.genfromtxt(path+"/graph_dump"+file_seq+".dat", delimiter=';')
    neigh_file = path+"/neighbors_dump"+file_seq+".dat"
    energy = np.genfromtxt(path+"/energy_dump"+file_seq+".dat", delimiter=';')
    
    plt.figure()
    plt.axis('off')  #remove axises
    plt.title('Node positions & discovered links')

    #plot neigbors relationships
    for line in open(neigh_file):
    	neighbors = np.fromstring( (line.rstrip('\n')), sep=';', dtype=int)
    	for node in range(1, neighbors.size):
    		A = neighbors[0]
    		B = neighbors[node]
    		plt.plot([coord[A,1], coord[B,1]], [coord[A,2], coord[B,2]],  linewidth=0.5, zorder=-1)

    # Plot nodes
    plt.scatter(coord[:,1], coord[:,2], s=7, zorder=1, c=energy[:,1])

    plt.tight_layout()
    plt.show()
    #plt.savefig(path+"/"+file_seq+'.pdf')
    #plt.savefig('.png', dpi = (200))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
    parser.add_argument('path', type=str, help='path to .dat files')
    args = parser.parse_args()

    # get list of files
    files = []
    for dat in os.listdir(args.path):
        if dat.startswith("graph") and dat.endswith(".dat"):
                files.append(dat.lstrip("graph_dump").rstrip(".dat"))               

    # create a thread for each file
    thread_list = []
    for seq in files:
        p = Process(target=plotter, args=(seq, args.path))
        thread_list.append(p)
        p.start()


    for t in thread_list:
        t.join()