# 315474866
# betyaag

compile: bin
	find src -name "*.java" > sources.txt
	javac -cp biuoop-1.4.jar:src -d bin @sources.txt

jar:
	jar cfm ass7game.jar Mainfest.mf -C bin . -C resources .
	
run:
	java -cp biuoop-1.4.jar:bin:resources Ass7Game

bin:
	mkdir bin