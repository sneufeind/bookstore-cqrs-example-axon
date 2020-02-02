# Example: Bookstore implemented with the CQRS pattern based on the Axon-Framework
Simple example of a bookstore implemented with the axon framework using CQRS pattern.




## Axon-Server
see: https://docs.axoniq.io/reference-guide/axon-server
* docker pull axoniq/axonserver:4.2.4-jdk11
* docker run --rm -it -p 8024:8024 -p 8124:8124 --name axon-server axoniq/axonserver:4.2.4-jdk11 
* docker stop axon-server

or download Axon Server as jar from here https://axoniq.io/download an run it:

`java -jar ./axonserver.jar`