import numpy as np
import matplotlib.pyplot as plt
import argparse
import os



def plotter(file_seq, path):
    
        coord = np.genfromtxt(path+"/graph_dump"+file_seq+".dat", delimiter=';')
        neigh_file = path+"/neighbors_dump"+file_seq+".dat"
        energy = np.genfromtxt(path+"/energy_dump"+file_seq+".dat", delimiter=';')

        #more awesomeness
        #plt.xkcd()

        plt.figure()
        #plt.ylabel('X')
        #plt.xlabel('Y')
        #plt.axis('off')  #remove axises
        axes = plt.gca() # auto-set ticks

        #axes.set_xlim([0,1000])
        #axes.set_ylim([0,1000])
        plt.title('battery level')

        #plot neigbors relationships
        for line in open(neigh_file):
        	neighbors = np.fromstring( (line.rstrip('\n')), sep=';', dtype=int)
        	for node in range(1, neighbors.size):
        		A = neighbors[0]
        		B = neighbors[node]
        		plt.plot([coord[A,1], coord[B,1]], [coord[A,2], coord[B,2]],  linewidth=0.2, zorder=-1, c='0.5')

        # Plot nodes
        plt.scatter(coord[:,1], coord[:,2], s=9, zorder=1)

        for label, x, y in zip(energy[:,1], coord[:,1], coord[:,2]):

                plt.annotate( label, xy=(x, y), xytext=(-20, 20),
                        textcoords='offset points', ha='right', va='bottom',
                        bbox=dict(boxstyle='round,pad=0.5', fc='yellow', alpha=0.5),
                        arrowprops=dict(arrowstyle = '->', connectionstyle='arc3,rad=0'))
        

        plt.tight_layout()
        #plt.savefig("figs/"+seq+'.pdf')
        plt.savefig("figs/"+seq+'.png', dpi = (200))
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
