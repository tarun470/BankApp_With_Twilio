# 📱 Java + JDBC + Twilio SMS Integration

A simple Java application that demonstrates how to:

- 🔗 Connect to an **Oracle database** using **JDBC**
- 📋 Retrieve user data or transaction details
- 📤 Send SMS notifications using the **Twilio API**

> Perfect for learning how to integrate backend systems with real-time communication.

---

## 🛠️ Technologies Used

- ☕ **Java 8+**
- 🧩 **JDBC** (for database access)
- 📬 **Twilio API** (for sending SMS)
- ⚙️ **Apache Maven** (for build & dependency management)
- 🗄️ **Oracle Database** (XE or Standard Edition)

---

## ✅ Prerequisites

Ensure the following tools and accounts are set up before you begin:

- ✅ Java JDK installed → [Download Java](https://www.oracle.com/java/technologies/javase-downloads.html)  
- ✅ Apache Maven installed → [Download Maven](https://maven.apache.org/download.cgi)  
- ✅ Oracle Database installed and running  
  - Default Connection: `localhost:1521/xe`  
- ✅ Twilio account → [Sign Up Here](https://www.twilio.com/try-twilio)  
  - Required credentials:
    - 🔐 Account SID  
    - 🔐 Auth Token  
    - ☎️ Twilio Phone Number (Trial or Paid)

---

## 📁 Project Structure

```
JavaJDBCTwilio/
├── src/
│   └── main/
│       └── java/
│           └── com/example/Banking.java
├── pom.xml
└── README.md
```

---

## 📦 Add Required Dependencies

Edit your `pom.xml` file to include:

### 🔌 Twilio SDK

```xml
<dependency>
  <groupId>com.twilio.sdk</groupId>
  <artifactId>twilio</artifactId>
  <version>9.10.0</version>
</dependency>
```

### 🗃️ Oracle JDBC Driver (Manual Install)

> Oracle does **not** publish the driver to Maven Central.

Install it manually:

```bash
mvn install:install-file -Dfile="C:\Users\Acer\Downloads\ojdbc8.jar" ^
 -DgroupId=com.oracle.database.jdbc ^
 -DartifactId=ojdbc8 ^
 -Dversion=21.1.0.0 ^
 -Dpackaging=jar
```

### 📢 SLF4J Simple Logger (optional to suppress Twilio warnings)

```xml
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-simple</artifactId>
  <version>1.7.36</version>
</dependency>
```

---

## 🚀 Create and Run the Project

### 🏗️ Generate Maven Project

```bash
mvn archetype:generate -DgroupId=com.example -DartifactId=BankingProject \
 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
cd BankingProject
```

### ⚙️ Build and Run

```bash
# Clean and compile the code
mvn clean compile

# Package the app into a JAR file
mvn package

# Run the application
mvn exec:java -Dexec.mainClass="com.example.Banking"
```

---

## 🔍 How It Works

Once the app is running:

1. 🗄️ Connects to your Oracle database
2. 📤 Fetches a transaction or user message
3. 🧾 Formats the data into a readable SMS
4. 📲 Sends the SMS using the Twilio API

---

## ✅ Example Output

```
Connected to database...
Transaction: ₹500 debited from A/C 123456789
Sending SMS to +91XXXXXXXXXX...
Message sent successfully!
```

---

## 📌 Notes

- Ensure the Oracle listener is running on your system.
- Use test credentials during development to avoid charges on Twilio.
- Use valid phone numbers approved in your Twilio dashboard (especially in trial mode).

---

## 📄 License

This project is licensed under the **MIT License**. Feel free to use and modify it for your learning or personal projects!
