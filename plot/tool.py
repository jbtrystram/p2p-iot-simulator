import os
import pandas
import matplotlib.pyplot as plt
import argparse
import math


# get list of files
def fileList(prefix, path):
    files = []
    for dat in os.listdir(path):
        if dat.startswith(prefix) and dat.endswith(".dat"):
                files.append(dat.lstrip(prefix+"_dump").rstrip(".dat"))
    return sorted(files)
   
    
def plotNeighbors():   
    indexes =  pandas.Index(coord[0])
    #plot neigbors relationships
    for line in open(neighbors):
         neighbors_list = line.rstrip(';\n').split(';')
         for node in range(1, len(neighbors_list)-1):
              A = indexes.get_loc(int(neighbors_list[0]))
              B = indexes.get_loc(int(neighbors_list[node]))
              plt.plot([coord.iloc[A,1], coord.iloc[B,1]], [coord.iloc[A,2], coord.iloc[B,2]],  linewidth=0.2, zorder=-1, c='0.5')
   
#special subplots for progress and stats             
def progress_custom_subplots():
        
        hauteur = math.floor(math.sqrt(len(progress.columns)))
        largeur = math.ceil(math.sqrt(len(progress.columns)))

        global key_index
        key_index=1
        for haut in  range(0,hauteur):
                for large in range(1,largeur+1):
                        if (key_index < len(progress.columns)-1):
                                plt.subplot(hauteur, largeur, (largeur*haut)+(large), aspect='equal')
                                plt.title(progress.columns[key_index])
                                plt.axis('square')
                                
                                plotNeighbors()
                                scatter_plot_nodes("progress")
                                #plt.xticks([], [])
                                #plt.yticks([], [])
                                plt.xlim(0,1000)
                                plt.ylim(0,1000)
                                key_index+=1
                                

def scatter_plot_nodes(data):
        if (data is "progress"):
                plt.scatter(coord.iloc[:,1], coord.iloc[:,2], s=9, zorder=1, c=progress.iloc[:,key_index], vmin=0, vmax=100)
        elif (data is "battery"):       
                plt.scatter(coord.iloc[:,1], coord.iloc[:,2], s=9, zorder=1, c=energy.iloc[:,1], vmin=0, vmax=100)
        else:
                plt.scatter(coord.iloc[:,1], coord.iloc[:,2], s=9, zorder=1)




    
if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('path', type=str, help='path to .dat files')
    parser.add_argument("--progress", help="Plotting the progress", action="store_true")
    parser.add_argument('--battery', help="Will plot the battery level on each node", action="store_true")
    args = parser.parse_args()
    
    # get list of files
    files = fileList("graph", args.path)             

    # matplotlib initialisation
    
    
    for seq in sorted(files):
        print("Drawing "+seq)
        
        coord = pandas.read_csv(args.path+"/graph_dump"+seq+".dat", header=None, delimiter=';')
        neighbors = args.path+"/neighbors_dump"+seq+".dat"
        
        if (args.progress):
             draw_type = "progress"
             progress = pandas.read_csv(args.path+"/progress_dump"+seq+".dat", delimiter=';')
             progress_custom_subplots()   
        
        elif(args.battery):
                draw_type = "battery"
                energy = pandas.read_csv(args.path+"/energy_dump"+seq+".dat", header=None, delimiter=';')
                plt.title('Node battery level & neigbors')
                plotNeighbors()
                scatter_plot_nodes("battery")
                plt.colorbar()
        
        # save and close plot        
        plt.tight_layout()
        plt.savefig("figs/"+draw_type+seq+'.png', dpi = (200))
        plt.close()
