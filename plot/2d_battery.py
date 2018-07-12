import pandas
import matplotlib.pyplot as plt
import argparse
import os

def plotter(file_seq, path):
    
        coord = pandas.read_csv(path+"/graph_dump"+file_seq+".dat", header=None, delimiter=';')
        neighbors = path+"/neighbors_dump"+file_seq+".dat"
        energy = pandas.read_csv(path+"/energy_dump"+file_seq+".dat", header=None, delimiter=';')
        print("using "+"/energy_dump"+file_seq+".dat")

        plt.figure()
        axes = plt.gca() # auto-set ticks
        
        #Values used for the highway dataset
        #plt.xlim(0, 4222530)
        #plt.ylim(0, 759313)
        #plt.xlim(0, 5000)
        #plt.ylim(0, 2000)

        plt.title('Node battery level & neigbors')
        
        indexes =  pandas.Index(coord[0])

        #plot neigbors relationships
        for line in open(neighbors):
             neighbors_list = line.rstrip(';\n').split(';')
             for node in range(1, len(neighbors_list)-1):
                  A = indexes.get_loc(int(neighbors_list[0]))
                  B = indexes.get_loc(int(neighbors_list[node]))
                  plt.plot([coord.iloc[A,1], coord.iloc[B,1]], [coord.iloc[A,2], coord.iloc[B,2]],  linewidth=0.2, zorder=-1, c='0.5')
        # Plot nodes
        plt.scatter(coord.iloc[:,1], coord.iloc[:,2], s=9, zorder=1, c=energy.iloc[:,1], vmin=0, vmax=100)
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
