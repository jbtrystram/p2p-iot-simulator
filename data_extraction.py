import pandas
import argparse
import os
from textwrap import dedent
from texttable import Texttable


#Sets the interval for the data we want to look at (1-400):
interval = 40



f= open("log.txt","w+")

def goodProgress(file_seq,path, id, packageNr):
	  


	fileNumber = int(file_seq) - interval
	fileNumerAsString = str(fileNumber).zfill(8)
	

	if fileNumber>0:
		progressOld = pandas.read_csv(path+"/progress_dump"+fileNumerAsString+".dat", delimiter=';')

		#print(progressOld)
	else:
		return


	progressNew = pandas.read_csv(path+"/progress_dump"+file_seq+".dat", delimiter=';')



	progress = int(progressNew.iloc[id, packageNr]) - int(progressOld.iloc[id, packageNr])
	

	return progress



def makePretty(coord, progress, storage, file_seq, path):
	
	print("--------------------------------------------------------")
	print("Looking @ "+file_seq)
	print("--------------------------------------------------------")

	f.write("\n Looking @ "+file_seq+ "\n")
	t = Texttable()
	t.add_rows([['Node ID', 'X', 'Y', 'Storage', 'Progress','+%(1)', '+%(2)', 'Neighbors']])	
	A = 0
	# for x in range(0,9):
	# 	t.add_row([coord.iloc[A,0],coord.iloc[A,1],coord.iloc[A,2], storage.iloc[A-1, 1], progress.iloc[A,:]])
	# 	A+=1

	neighbors = path+"/neighbors_dump"+file_seq+".dat"

	for line in open(neighbors):
		#print(getProgress(seq,args.path).iloc[A,2])

		neighbors_list = line.rstrip(';\n').split(';')
		t.add_row([coord.iloc[A,0],coord.iloc[A,1],coord.iloc[A,2], storage.iloc[A, 1], progress.iloc[A,:], goodProgress(file_seq, path, A,1), goodProgress(file_seq, path, A, 2), neighbors_list])
		A+=1

		

	print(t.draw())
	f.write(t.draw())

	
	t.reset()


    #return

def getCoords(file_seq, path):
    
        coord = pandas.read_csv(path+"/graph_dump"+file_seq+".dat", header=None, delimiter=';')
      


        #print(coord)

        return coord


def getProgress(file_seq, path):

    progress = pandas.read_csv(path+"/progress_dump"+file_seq+".dat", delimiter=';')

    #print(progress)


    return progress

def getStorage(file_seq, path):

    storage = pandas.read_csv(path+"/storage_dump"+file_seq+".dat", delimiter=';')

    #print(storage)

    return(storage)

        



if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='turn datafile into 2d map of nodes')
    parser.add_argument('path', type=str, help='path to .dat Files')
    args = parser.parse_args()
    
    Files = []
    for dat in os.listdir(args.path):
        if dat.startswith("graph") and dat.endswith(".dat"):
                Files.append(dat.lstrip("graph_dump").rstrip(".dat"))               

    
    for seq in sorted(Files):
    	

        if int(seq)%interval == 0:
        	
        	makePretty(getCoords(seq,args.path),  getProgress(seq,args.path), getStorage(seq,args.path), seq, args.path)
    f.close() 