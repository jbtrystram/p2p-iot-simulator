VER=1.0.5

all:
	javac -classpath src:jep-2.3.0.jar:djep-1.0.0.jar:univocity-parsers-2.5.9.jar:maven-artifact-3.5.3.jar `find src/main/ -name "*.java"`
clean:
	rm -f `find src/main -name "*.class"`
run:
	java -cp jep-2.3.0.jar:djep-1.0.0.jar:univocity-parsers-2.5.9.jar:maven-artifact-3.5.3.jar:src peersim.Simulator $(CONFIG).txt

order:
	rm -rf graphs/
	mkdir -p graphs
	mv *.dat graphs/

graph:
	rm -rf figs
	mkdir -p figs
	python plot/2d_graph.py raw_dat


gif: graph
	convert -delay 20 -loop 1 figs/*.png map.gif
	convert map.gif -fuzz 10% -layers Optimize map.gif


