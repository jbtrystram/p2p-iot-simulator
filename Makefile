space :=
space +=
JARS=$(wildcard lib/*.jar)
LIBS:=$(subst $(space),:,$(JARS))

print:
	@echo $(JARS)
	@echo $(LIBS)

all: clean
	javac -source 1.8 -target 1.8 -classpath $(LIBS) `find src/ -name "*.java"`
clean:
	rm -f `find src/ -name "*.class"`
run: order
	java -cp $(LIBS):src peersim.Simulator $(CONFIG)
	find raw_dat -type f -empty -delete

order:
	rm -rf raw_dat/ figs/
	mkdir -p raw_dat figs

graph:
	rm -rf figs
	mkdir -p figs
	python3 plot/2d_graph.py raw_dat


gif: graph
	convert -delay 20 -loop 1 figs/*.png map.gif
	convert map.gif -fuzz 10% -layers Optimize map.gif

stats:
	python3 plot/statistics.py raw_dat

extract:
	python3 data_extraction.py raw_dat
