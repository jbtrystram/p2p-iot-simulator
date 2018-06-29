import pandas

# example to work
#df = pandas.DataFrame(data={'Vehicle_ID': [1,2,2,2,3,3,3], 'Total_Frames': [11,20,22,22,33,33,35]} )

#df.loc[(df['Vehicle_ID'] == uniques.iloc[2,0]) & (df['Total_Frames'] == uniques.iloc[2,1]), ['Vehicle_ID']] = 1000

def percentage(total, progress):
        percent = (progress / total )*100
	#if (percent.is_integer()):
        print(str(percent)+"%")
                
#needed columns
cols_to_get = ['Vehicle_ID', 'Total_Frames', 'Global_Time', 'Global_X', 'Global_Y', 'v_Vel']
data_in = "highway_101.csv"
print("importing file : "+data_in)
df = pandas.read_csv(data_in, usecols=cols_to_get, na_filter=False)

print("Processing duplicates")
uniques = df.drop_duplicates(subset=['Vehicle_ID', 'Total_Frames'])

print("Rewrite index values")
for i in range(0, uniques.shape[0]):
        df.loc[(df['Vehicle_ID'] == uniques.iloc[i,0]) & (df['Total_Frames'] == uniques.iloc[i,1]), ['Vehicle_ID']] = i
        percentage(uniques.shape[0] ,i)

data_out = "highway_101_fixed.csv"
print("Saving new dataset : "+data_out)
df.to_csv(data_out, sep=';')
