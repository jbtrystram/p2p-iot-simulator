import pandas

#needed columns
cols_to_get = ['Vehicle_ID', 'Total_Frames', 'Global_Time', 'Global_X', 'Global_Y', 'v_Vel']
df = pandas.read_csv("highway_101.csv", usecols=cols_to_get, na_filter=False)

max_vID = df['Vehicle_ID'].max()

uniques = df.drop_duplicates(subset=['Vehicle_ID', 'Total_Frames'])

uniques[uniques['Vehicle_ID'].duplicated()]
