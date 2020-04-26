# ruuvijavabackend

This is a backend for a service wich fetches data from Amazon DynamoDB and provides it through REST-interface. The data used is measurements from Ruuvi-tags. Users are authenticated with JWT. Interface also has support for Google Dialogflow. 

AWS DynamoDB requires the following ENV variables:
- S3_KEY
- S3_KEY

Also provide:
- JWT_SECRET
- PORT

Compile the code with maven:

mvn packaged

Run the service:

java -jar target/javabackend-1.0-SNAPSHOT.jar

Use the provided Dockerfile to run the service in container. 
