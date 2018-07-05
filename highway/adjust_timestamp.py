import pandas

# example to work
# ;Vehicle_ID;Total_Frames;Global_Time;Global_X;Global_Y;v_Vel
#0;0;437;1118846980200;6451137.641;1873344.9619999998;40.0

def percentage(total, progress):
        percent = (progress / total )*100
	#if (percent.is_integer()):
        print(str(percent)+"%")
                
#needed columns
cols_to_get = ['Vehicle_ID', 'Total_Frames', 'Frame_ID', 'Global_X', 'Global_Y']
#data_in = "highway_101.csv"
data_in = "shortway.csv"
print("importing file : "+data_in)
df = pandas.read_csv(data_in, usecols=cols_to_get, na_filter=False)

line = 0
total_lines = df.shape[0]
newId = 0
result="time;id;X;Y\n"
while (line < total_lines):
	aild = df.loc[line, 'Vehicle_ID']
	nbframes = df.loc[line, 'Total_Frames']
	stopline = line + nbframes
	while((line < stopline)):
		if(not (aild == df.loc[line, 'Vehicle_ID'])):
			print(line)
			exit(0)
		result += str(df.loc[line, 'Frame_ID']) + ";" + str(newId) + ";" + str(df.loc[line, 'Global_X']) + ";" + str(df.loc[line, 'Global_Y']) + "\n"
		line+=1
	newId +=1
	#percentage(total_lines, line)
	

file = open("yoyo.csv", 'w')
file.write(result)
#data_out = "highway_101_yofixed.csv"
#print("Saving new dataset : "+data_out)
#df.to_csv(data_out, sep=';')
