import numpy as np
import matplotlib.pyplot as plt
import argparse
import os

def plotter(data):
    
        fig, ax = plt.subplots(len(data), 1, figsize=(7,5))
        for a,key in zip(ax,data.keys() ):
                y = data[key]
                n = len(y)
                x = np.linspace(1,n,n)
                a.plot(x,y)
                
                # add labels/titles and such here       
                a.title(key)
        plt.show()

def stat_hoarder(file_seq, path):
        
        current_progress = np.genfromtxt(path+"/progress_dump"+file_seq+".dat", delimiter=';', skip_header=1)
        current_names = np.genfromtxt(path+"/progress_dump"+file_seq+".dat", delimiter=';', names=True)
        
        for item in range(1, current_progress.shape[1]-1):

                name = current_names.dtype.names[item]
                avg_progress = np.average(current_progress[:,item])
                
                try: 
                        #if (progress[name][-1] != 100.0):
                                progress[name].append(avg_progress)
                except KeyError: progress[name] = [avg_progress]


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
    
    # gather the progress 
    for seq in sorted(files):
        stat_hoarder(seq,args.path)
    
    plotter(progress)
