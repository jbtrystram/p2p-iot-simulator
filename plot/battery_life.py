import numpy as np
import matplotlib.pyplot as plt
import argparse
import os
import pandas

def plotter(data1):
        
        n = len(data1)
        x = np.linspace(0,n,n)
        plt.plot(x,data1,'b')
        plt.title("Battery")
        plt.show()        
	#plt.savefig("figs/stats.png", dpi = (400))
        plt.close()

def stat_hoarder(file_seq, path):
        
        current_energy = pandas.read_csv(path+"/energy_dump"+file_seq+".dat", delimiter=';')
        
        energy.append(current_energy.loc[current_energy.iloc[:,1] <101].iloc[:,1].mean())

if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
    parser.add_argument('path', type=str, help='path to .dat files')
    args = parser.parse_args()
    
    # get list of files
    files = []
    for dat in os.listdir(args.path):
        if dat.startswith("energy") and dat.endswith(".dat"):
                files.append(dat.lstrip("energy_dump").rstrip(".dat")) 

    energy = []
    
    print("Parsing data files")
    for seq in sorted(files):
        stat_hoarder(seq,args.path)

    print("drawing plot")    
    plotter(energy)
