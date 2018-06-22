import matplotlib.pyplot as plt
import argparse
import os
import pandas
import numpy as np

def plotter(data1, data2):
    
        hauteur = 6
        largeur = 5

        key_index=0
        for haut in  range(0,hauteur):
                for large in range(1,largeur+1):
                        y = data1[keys[key_index]]
                        yy = data2[keys[key_index]]
                        n = len(y)
                        x = np.linspace(0,n,n)
                        plt.subplot(hauteur, largeur, (largeur*haut)+(large))
                        plt.plot(x,y,'b')#, x,yy,'r')

                        # add labels/titles and such here
                        plt.subplots_adjust(hspace = 0.4)
                        plt.title(keys[key_index])
                        key_index+=1
        plt.show()
	#plt.savefig("figs/stats.png", dpi = (400))
        plt.close()

def stat_hoarder(file_seq, path):
        
        current_progress = pandas.read_csv(path+"/progress_dump"+file_seq+".dat", delimiter=';')
	# drop first and last column
	#df.drop(df.columns[len(df.columns)-1], axis=1, inplace=True)

        for item in range(1, len(current_progress.columns)-1):

                # avg progress
                name = current_progress.columns[item]
                avg_progress = current_progress.iloc[:,item].mean()
                
                try:progress[name].append(avg_progress)
                except KeyError: progress[name] = [avg_progress]

                # absolute number of nodes to 100%
                complete = sum(i == 100 for i in current_progress.iloc[:,item])

                try: complete_nodes[name].append(complete)
                except KeyError: complete_nodes[name] = [complete]

def normalize_duration(data1):

        sizes = []
        for key in keys:
                sizes.append(len(data1[key]))

        longest = max(sizes)
        sizes = []
        for key in keys:
                data1[key] = [0] * (longest-len(data1[key])) + data1[key]
                sizes.append(len(data1[key]))

        return data1

def keys_indexer(data1):
        keys = []
        for key in data1.keys():
                keys.append(key)
        return  keys

def duration_collection():

        sizes = pandas.read_csv("dataset_debian/source.csv", usecols=[1,2,3], delimiter=';')
        # number of ticks to complete each package
        for key in keys:
                duration.append(sum(i < 100 for i in progress[key]))
        
        # collect sizes
                for name, size in zip(sizes['name'], sizes['size']):
                        if key.startswith(name):
                                sizes_data.append(size)

def plot_time_to_size(size, time):

        plt.scatter(size,time)

        # add labels/titles and such here
        plt.title("time over file size")
        plt.show()
        plt.close()

if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
    parser.add_argument('path', type=str, help='path to .dat files')
    args = parser.parse_args()
    
    # get list of files
    files = []
    for dat in os.listdir(args.path):
        if dat.startswith("progress") and dat.endswith(".dat"):
                files.append(dat.lstrip("progress_dump").rstrip(".dat")) 


    progress = {}
    complete_nodes = {}
    duration = []
    sizes_data = []
    
    print("Parsing data files")
    for seq in sorted(files):
        stat_hoarder(seq,args.path)

    keys = keys_indexer(progress)    
    duration_collection()

    print("normalizing datasets length")
    progress = normalize_duration(progress)
    complete_nodes = normalize_duration(complete_nodes)

    print("drawing plot")    
    plotter(progress, complete_nodes)

    plot_time_to_size(sizes_data, duration)
