
                                  Java + JDBC + Twilio SMS Integration
This project is a simple Java application that shows how to:
  Connect to an Oracle database using JDBC
  Retrieve user data or transaction details
  Send SMS notifications using the Twilio API
Perfect for learning how to integrate backend systems with real-time communication.


                                     Technologies Used
Java 8+
JDBC (for database access)
Twilio API (for sending SMS)
Apache Maven (for build and dependency management)
Oracle Database (XE or Standard edition)

                                     ✅ Prerequisites

✅ Java JDK installed 
✅ Apache Maven installed 
✅ Oracle Database up and running
Default URL used: localhost:1521/xe
✅ A Twilio account → Sign up here
You’ll need:
Account SID
Auth Token
Twilio phone number (trial or paid)
 
                                       Project Structure
JavaJDBCTwilio/
├── src/
│   └── main/
│       └── java/
│           └── com/example/Banking.java
├── pom.xml
└── README.md


                                      Add Required Dependencies
Ensure your pom.xml contains:
  Twilio SDK Dependency
  Oracle JDBC driver  Dependency
  SLF4J Simple Logger to fix warning  Dependency  

                                       To  complie and run the application
 *** This creates a folder named BankingProject with basic structure.
mvn archetype:generate -DgroupId=com.example -DartifactId=BankingProject -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
now navigate to your folder 
i.e   cd BankingProject 

#Addition of Oracle Driver
Oracle JDBC driver isn’t available via Maven Central. You must manually install it:
  mvn install:install-file -Dfile="C:\Users\Acer\Downloads\ojdbc8.jar" ^
  -DgroupId=com.oracle.database.jdbc ^
  -DartifactId=ojdbc8 ^
  -Dversion=21.1.0.0 ^
  -Dpackaging=jar
                          
# Clean and compile the code
  mvn clean compile
# Package the app into a JAR file
  mvn package
# Run the application
  mvn exec:java -Dexec.mainClass="com.example.Banking"


                                              How it Works
Once running, the app:
Connects to your Oracle DB
Fetches sample transaction data
Builds a formatted message
Sends that message via SMS to the provided mobile number using Twilio
