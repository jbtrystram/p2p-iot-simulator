# Discovery-based P2P data distribution 

Using the peersim engine to simulate distribution of data with no infrastructure. 
The node connections are set up by discovery. 

## How to run it

### Requirements

* java 1.8 or superior
* python3 with pandas and matplotlib installed
* Coffee and patience.

### Let's dive into it !
#### Run an experiment
Clone the repo on your machine then compile the code. A makefile is provided to ease things.

`make all` will compile the code.

To run a simulation you'll need to provide a configuration to the simulation engine.
This is done through a text file. 
`make run CONFIG=example.txt` will feed the engine with `example.txt`.

Now, try with `make run CONFIG=myfile.txt`. 
_I see numbers_ ! Yes, these are the simulation time, it gives you an idea of how fast the simulation goes.

#### But what happened ?
A lot of data has been created in a folder called "raw_dat". These are text file with simultaion data.
You could try to graph them ! 
`make graph` will create a folder called `figs` in which figures are created. Have a look ! 
You'll see how the nodes are mooving and their noighbors relationships.
Try to fiddle a bit with the config file and experiment ! 
