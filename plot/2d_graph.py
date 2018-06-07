import numpy as np
import matplotlib.pyplot as plt
import argparse
import os

def plotter(file_seq, path):
    
        coord = np.genfromtxt(path+"/graph_dump"+file_seq+".dat", delimiter=';')
        neigh_file = path+"/neighbors_dump"+file_seq+".dat"
        print("using "+"/data_pieces_dump"+file_seq+".dat")
        progress = np.genfromtxt(path+"/progress_dump"+file_seq+".dat", delimiter=';', skip_header=1)
        names = np.genfromtxt(path+"/progress_dump"+file_seq+".dat", delimiter=';', names=True)

        plt.figure()
        for item in range(1, progress.shape[1]-1):

                plt.subplot(1, progress.shape[1]-2, item, aspect='equal')
                plt.title(names.dtype.names[item])
                plt.axis('square')

                #plot neigbors relationships
                for line in open(neigh_file):
                        neighbors = np.fromstring( (line.rstrip('\n')), sep=';', dtype=int)
                        for node in range(1, neighbors.size):
                                A = neighbors[0]
                                B = neighbors[node]
                                plt.plot([coord[A,1], coord[B,1]], [coord[A,2], coord[B,2]],  linewidth=0.2, zorder=-1, c='0.5')

                # Plot nodes
                plt.scatter(coord[:,1], coord[:,2], s=9, zorder=1, c=progress[:,item], vmin=0, vmax=100)
                plt.colorbar()
                plt.xticks([], [])
                plt.yticks([], [])
                plt.xlim(0,1000)
                plt.ylim(0,1000)

        #plt.subplots_adjust() 
        #plt.tight_layout()
        #plt.savefig("figs/"+seq+'.pdf')
        plt.savefig("figs/progress"+seq+'.png', dpi = (200))
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
    for seq in files:
        plotter(seq,args.path)
