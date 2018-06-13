import pandas
import matplotlib.pyplot as plt
import argparse
import os
import math

def plotter(file_seq, path):
    
        coord = pandas.read_csv(path+"/graph_dump"+file_seq+".dat", header=None, delimiter=';')
        neighbors = path+"/neighbors_dump"+file_seq+".dat"
        print("Processing "+"/progress_dump"+file_seq+".dat")
        progress = pandas.read_csv(path+"/progress_dump"+file_seq+".dat", delimiter=';')

        #define subplot graph size
        hauteur = math.floor(math.sqrt(len(progress.columns)))
        largeur = math.ceil(math.sqrt(len(progress.columns)))

        key_index=1
        for haut in  range(0,hauteur):
                for large in range(1,largeur+1):
                        if (key_index < len(progress.columns)-1):
                                plt.subplot(hauteur, largeur, (largeur*haut)+(large), aspect='equal')
                                plt.title(progress.columns[key_index])
                                plt.axis('square')

                                #plot neigbors relationships
                                for line in open(neighbors):
                                        neighbors_list = line.rstrip(';\n').split(';')
                                        for node in range(1, len(neighbors_list)):
                                                A = int(neighbors_list[0])
                                                B = int(neighbors_list[node])
                                                plt.plot([coord.iloc[A,1], coord.iloc[B,1]], [coord.iloc[A,2], coord.iloc[B,2]],  linewidth=0.2, zorder=-1, c='0.5')

                                # Plot nodes
                                plt.scatter(coord.iloc[:,1], coord.iloc[:,2], s=9, zorder=1, c=progress.iloc[:,key_index], vmin=0, vmax=100)
                                plt.xticks([], [])
                                plt.yticks([], [])
                                plt.xlim(0,1000)
                                plt.ylim(0,1000)

                                key_index+=1
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
    
    for seq in sorted(files):
        plotter(seq,args.path)
