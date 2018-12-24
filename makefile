all:
	@if [ ! -f bin ]; then rm -rf bin; fi
	@mkdir bin
	@find src -name "*.java" > sources
	@javac -d bin @sources
	@rm sources
