VER=1.0.5

.PHONY: all clean doc release

all:
	javac -classpath src:jep-2.3.0.jar:djep-1.0.0.jar:univocity-parsers-2.5.9.jar:maven-artifact-3.5.3.jar `find src -name "*.java"`
clean:
	rm -f `find src -name "*.class"`
doc:
	rm -rf doc/*
	javadoc -overview overview.html -classpath src:jep-2.3.0.jar:djep-1.0.0.jar -d doc \
                -group "Peersim" "peersim*" \
                -group "Examples" "example.*" \
		peersim \
		peersim.cdsim \
		peersim.config \
		peersim.core \
		peersim.dynamics \
		peersim.edsim \
		peersim.graph \
		peersim.rangesim \
		peersim.reports \
		peersim.transport \
		peersim.util \
		peersim.vector \
		example.aggregation \
		example.loadbalance \
		example.edaggregation \
		example.hot \
		example.newscast 

docnew:
	rm -rf doc/*
	javadoc -overview overview.html -docletpath peersim-doclet.jar -doclet peersim.tools.doclets.standard.Standard -classpath src:jep-2.3.0.jar:djep-1.0.0.jar -d doc \
                -group "Peersim" "peersim*" \
                -group "Examples" "example.*" \
		peersim \
		peersim.cdsim \
		peersim.config \
		peersim.core \
		peersim.dynamics \
		peersim.edsim \
		peersim.graph \
		peersim.rangesim \
		peersim.reports \
		peersim.transport \
		peersim.util \
		peersim.vector \
		example.aggregation \
		example.loadbalance \
		example.hot \
		example.edaggregation \
		example.newscast 


release: clean all docnew
	rm -fr peersim-$(VER)
	mkdir peersim-$(VER)
	cp -r doc peersim-$(VER)
	cp Makefile overview.html README CHANGELOG RELEASE-NOTES build.xml peersim-doclet.jar peersim-$(VER)
	mkdir peersim-$(VER)/example
	cp example/*.txt peersim-$(VER)/example
	mkdir peersim-$(VER)/src
	cp --parents `find src/peersim src/example -name "*.java"` peersim-$(VER)
	cd src ; jar cf ../peersim-$(VER).jar `find peersim example -name "*.class"`
	mv peersim-$(VER).jar peersim-$(VER)
	cp jep-2.3.0.jar peersim-$(VER)
	cp djep-1.0.0.jar peersim-$(VER)

run:	
	java -cp jep-2.3.0.jar:djep-1.0.0.jar:univocity-parsers-2.5.9.jar:src peersim.Simulator test.txt

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


