javac -cp ".:mysql-connector-java-8.0.18.jar" models/*.java
javac -cp ".:mysql-connector-java-8.0.18.jar" db/*.java
javac -cp ".:mysql-connector-java-8.0.18.jar" connection/*.java
javac -cp ".:mysql-connector-java-8.0.18.jar" paths/*.java
javac -cp ".:mysql-connector-java-8.0.18.jar" views/*.java
javac -cp ".:mysql-connector-java-8.0.18.jar" LoadServer.java
java -cp ".:mysql-connector-java-8.0.18.jar" LoadServer
