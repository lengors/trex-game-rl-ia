all:
	@if [ ! -f bin ]; then rm -rf bin; fi
	@mkdir bin
	@find src -name "*.java" > sources
	@find res/dependencies -name "*.jar" | paste -sd ":" - > dependencies
	@javac -cp @dependencies -d bin @sources
	@rm dependencies
	@rm sources
