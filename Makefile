space :=
space +=
JARS=$(wildcard lib/*.jar)
LIBS:=$(subst $(space),:,$(JARS))

print:
	@echo $(JARS)
	@echo $(LIBS)

all:
	javac -source 1.8 -target 1.8 -classpath $(LIBS) `find src/main/ -name "*.java"`
clean:
	rm -f `find src/main -name "*.class"`
run:
	java -cp $(LIBS):src peersim.Simulator $(CONFIG).txt

order:
	rm -rf raw_dat/ figs/
	mkdir -p raw_dat figs

graph:
	rm -rf figs
	mkdir -p figs
	python plot/2d_graph.py raw_dat


gif: graph
	convert -delay 20 -loop 1 figs/*.png map.gif
	convert map.gif -fuzz 10% -layers Optimize map.gif


