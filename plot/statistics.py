import numpy as np
import matplotlib.pyplot as plt
import argparse
import os

def plotter(data1, data2):
    
        hauteur = 6
        largeur = 5

        keys = []
        for key in data1.keys():
                keys.append(key)
        key_index=0
        for haut in  range(0,hauteur):
                for large in range(1,largeur+1):
                        y = data1[keys[key_index]]
                        yy = data2[keys[key_index]]
                        n = len(y)
                        x = np.linspace(0,n,n)
                        plt.subplot(hauteur, largeur, (largeur*haut)+(large))
                        plt.plot(x,y,'b', x,yy,'r')

                        # add labels/titles and such here
                        plt.subplots_adjust(hspace = 0.4)
                        plt.title(keys[key_index])
                        key_index+=1
        plt.savefig("figs/stats.png", dpi = (400))
        plt.close()

def stat_hoarder(file_seq, path):
        
        current_progress = np.genfromtxt(path+"/progress_dump"+file_seq+".dat", delimiter=';', skip_header=1)
        current_names = np.genfromtxt(path+"/progress_dump"+file_seq+".dat", delimiter=';', names=True)

        for item in range(1, current_progress.shape[1]-1):

                # avg progress
                name = current_names.dtype.names[item]
                avg_progress = np.average(current_progress[:,item])
                
                try:progress[name].append(avg_progress)
                except KeyError: progress[name] = [avg_progress]

                # absolute number of nodes to 100%
                complete = sum(i == 100 for i in current_progress[:,item])

                try: complete_nodes[name].append(complete)
                except KeyError: complete_nodes[name] = [complete]

def normalize_duration(data1):

        keys = []
        sizes = []
        for key in data1.keys():
                keys.append(key)
                sizes.append(len(data1[key]))

        longest = max(sizes)
        sizes = []
        for key in keys:
                data1[key] = [0] * (longest-len(data1[key])) + data1[key]
                sizes.append(len(data1[key]))

        return data1

if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
    parser.add_argument('path', type=str, help='path to .dat files')
    args = parser.parse_args()
    
    # get list of files
    files = []
    for dat in os.listdir(args.path):
        if dat.startswith("graph") and dat.endswith(".dat"):
                files.append(dat.lstrip("graph_dump").rstrip(".dat")) 


    progress = {}
    complete_nodes = {}
    
    print("Parsing data files")
    for seq in sorted(files):
        stat_hoarder(seq,args.path)
    
    print("normalizing datasets length")
    progress = normalize_duration(progress)
    complete_nodes = normalize_duration(complete_nodes)

    plotter(progress, complete_nodes)
