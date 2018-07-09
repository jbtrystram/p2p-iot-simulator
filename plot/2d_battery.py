import numpy as np
import matplotlib.pyplot as plt
import argparse
import os

def plotter(file_seq, path):
    
        coord = np.genfromtxt(path+"/graph_dump"+file_seq+".dat", delimiter=';')
        neigh_file = path+"/neighbors_dump"+file_seq+".dat"
        energy = np.genfromtxt(path+"/energy_dump"+file_seq+".dat", delimiter=';')
        print("using "+"/energy_dump"+file_seq+".dat")

        plt.figure()
        axes = plt.gca() # auto-set ticks
        
        #Values used for the highway dataset
        #plt.xlim(0, 4222530)
        #plt.ylim(0, 759313)
        #plt.xlim(0, 5000)
        #plt.ylim(0, 2000)

        plt.title('Node battery level & neigbors')

        #plot neigbors relationships
        for line in open(neigh_file):
        	neighbors = np.fromstring( (line.rstrip('\n')), sep=';', dtype=int)
        	for node in range(1, neighbors.size):
        		A = neighbors[0]
        		B = neighbors[node]
        		plt.plot([coord[A,1], coord[B,1]], [coord[A,2], coord[B,2]],  linewidth=0.2, zorder=-1, c='0.5')

        # Plot nodes
        plt.scatter(coord[:,1], coord[:,2], s=9, zorder=1, c=energy[:,1], vmin=0, vmax=100)
        plt.colorbar()

        plt.tight_layout()
        plt.savefig("figs/energy"+seq+'.png', dpi = (200))
        plt.close()



if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
    parser.add_argument('path', type=str, help='path to .dat files')
    args = parser.parse_args()
    
    # get list of files
    files = []
    for dat in os.listdir(args.path):
        if dat.startswith("graph") and dat.endswith(".dat"):
                files.append(dat.lstrip("graph_dump").rstrip(".dat"))               

    # plot each file
    for seq in sorted(files):
        plotter(seq,args.path)
