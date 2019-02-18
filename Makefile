.DEFAULT_GOAL := all

all: qclient qserver permissions

qclient:
	javac Client.java
	echo  'java -XX:ParallelGCThreads=2 Client $$1 $$2' > qclient

qserver:
	javac -cp "/.:./json-simple-1.1.1.jar" Server.java
	echo 'java -cp json-simple-1.1.1.jar:. -XX:ParallelGCThreads=2 Server' > qserver

permissions:
	chmod 777 qserver
	chmod 777 qclient

clean:
	rm qclient
	rm qserver
