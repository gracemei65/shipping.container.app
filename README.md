# shipping.container.app
This is demo shipping container management application 
1. Container Owner Perform container CRUD functions agaist DB (Embedded H2 database) via REST APIs
2. Handle container status via JMS message queue as

Read a container status message from STATUS.INBOUND.QUEUE via JMS Queues (Embedded ActiveMQ).
If the timestamp of the message is after the most recent in the table for the containerId, then add the record to the table and 
send a message to the STATUS.OUTBOUND.QUEUE in XML format.
If the timestamp is before that most recent table for the container id or the message does not meet the validations for the JSON, 
place the record on the dead-letter queue STATUS.INBOUND.QUEUE.DLQ

*********************************************************************************************************************************************

Run APP and Test in Docker container
1. docker pull gracemei65/shipping-container-app
2. docker run -p 9090:9090 gracemei65/shipping-container-app
3. view log : "docker exec -it <DOCKER_CONTAINER_ID> /bin/bash" and cd /var/log/shipping-container-app.log
4. Refer Swagger document: http://<YOUR_IP>:9090/swagger-ui.html e.g http://192.168.99.100:9090/swagger-ui.html
5. view H2 db data : http://<YOUR_IP>:9090/h2-console and type password

Run APP and Test locally
github: https://github.com/gracemei65/shipping.container.app
1. git clone https://github.com/gracemei65/shipping.container.app.git
2. cd shipping.container.app 
3. mvn clean install 
4. mvn spring-boot:run


