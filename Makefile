
compile:
	test -d bin && rm -r bin || true
	mkdir bin
	find . -name "*.java" -print | xargs javac -d ./bin

server: compile
	java --enable-preview -XX:+ShowCodeDetailsInExceptionMessages -cp bin/ App server

serverh: compile
	java --enable-preview -XX:+ShowCodeDetailsInExceptionMessages -cp bin/ App server headless

client:
	java --enable-preview -XX:+ShowCodeDetailsInExceptionMessages -cp bin/ App client