import pandas


def percentage(total, progress):
        percent = progress / total
        if (percent.is_integer()):
                print(str(percent)+"%")
                
#needed columns
cols_to_get = ['Vehicle_ID', 'Total_Frames', 'Global_Time', 'Global_X', 'Global_Y', 'v_Vel']
data_in = "highway_101.csv"
print("importing file : "+data_in)
df = pandas.read_csv(data_in, usecols=cols_to_get, na_filter=False)

#max_vID = df['Vehicle_ID'].max()
#max_vID +=1

print("Processing duplicates")
uniques = df.drop_duplicates(subset=['Vehicle_ID', 'Total_Frames'])

print("Rewrite index values")
for i in range(0, df.shape[0]):
        index = uniques.index[(uniques['Vehicle_ID'] == df.iloc[i,0]) & (uniques['Total_Frames'] == df.iloc[i,1])]
        df.iloc[i,0] = index[0]
        percentage(df.shape[0] ,i)

data_out = "highway_101_fixed.csv"
print("Saving new dataset : "+data_out)
df.to_csv(data_out, sep=';')
