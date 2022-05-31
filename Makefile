.PHONY: test
test: clean
	mvn test

.PHONY: run
run: clean
	mvn quarkus:dev -Ddebug=false

.PHONY: debug
debug: clean
	mvn quarkus:dev -Ddebug=8000 -Dsuspend=false

.PHONY: clean
clean:
	mvn -q clean
