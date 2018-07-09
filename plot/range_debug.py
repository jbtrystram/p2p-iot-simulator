import numpy as np
import matplotlib.pyplot as plt
from matplotlib.patches import Circle
import argparse
import os
import pandas



def plotter(file_seq, path):
    
        coord = pandas.read_csv(path+"/graph_dump"+file_seq+".dat", delimiter=';')
        neigh_file = path+"/neighbors_dump"+file_seq+".dat"
        distance = pandas.read_csv(path+"/range_dump"+file_seq+".dat", delimiter=';')

        w,h = plt.figaspect(1)
        plt.figure(figsize=(w,h))
        ax = plt.gcf().gca()
        axes = plt.gca() # auto-set ticks

        plt.title('Range')

        #plot neigbors relationships
        for line in open(neigh_file):
                neighbors = line.rstrip(';\n').split(';')
                for node in range(1, len(neighbors)):
                        A = neighbors[0]
                        B = neighbors[node]
                        plt.plot([coord.iloc[A,1], coord.iloc[B,1]], [coord.iloc[A,2], coord.iloc[B,2]],  linewidth=0.2, zorder=-1, c='0.5')

        # Plot nodes 
        plt.scatter(coord.iloc[:,1], coord.iloc[:,2], s=20, zorder=1, facecolors='none', edgecolors='b') 

        # data label
        for label, x, y in zip(distance.iloc[:,1], coord.iloc[:,1], coord.iloc[:,2]):

                plt.annotate( label, xy=(x, y), xytext=(-20, 20),
                        textcoords='offset points', ha='right', va='bottom',
                        bbox=dict(boxstyle='round,pad=0.5', fc='yellow', alpha=0.5),
                        arrowprops=dict(arrowstyle = '->', connectionstyle='arc3,rad=0'))

        #plot range
        for i in range(0, distance.shape[1]-1):
                print("hey ! range is "+str(distance.iloc[i,1]))
                circle = Circle((coord.iloc[i,1], coord.iloc[i,2]), radius=distance.iloc[i,1], edgecolor='k', linestyle='--', fill=False)
                ax.add_artist(circle)

        plt.tight_layout()
        #plt.savefig("figs/"+seq+'.pdf')
        plt.savefig("figs/range"+file_seq+'.png', dpi = (200))
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
        print(seq)
        plotter(seq,args.path)
