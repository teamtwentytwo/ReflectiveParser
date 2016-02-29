main = ReflectiveParser.jar
sources := $(shell find -type f -name "*.java")

.SUFFIXES: .java .class

.PHONY: all
all: $(main)

.PHONY: run
run: $(main)
	java -jar $(main)

.PHONY: clean
clean:
	-@rm -f $(main)
	-@find -name '*.class' -delete

$(main): $(sources) manifest
	javac $(sources)
	find -type f -name '*.class' -print0 | xargs -0 jar cfm $@ manifest
	-@find -name '*.class' -delete
