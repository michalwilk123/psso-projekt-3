
compile:
	rm -r ./bin
	mkdir bin
	find . -name "*.java" -print | xargs javac -d ./bin

server: compile
	java --enable-preview -XX:+ShowCodeDetailsInExceptionMessages -cp bin/ App server

client:
	java --enable-preview -XX:+ShowCodeDetailsInExceptionMessages -cp bin/ App client