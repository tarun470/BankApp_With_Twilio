package com.example;
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Banking {
    static final String DB_URL = "jdbc:oracle:thin:@//192.168.1.37:1521/xe";  
    static final String USER = "system"; 
    static final String PASS = "tarun";
    static final String ACCOUNT_SID = "";
    static final String AUTH_TOKEN = "";
    static final String FROM_PHONE = ""; 
         

    public static void main(String[] args) {
   try{
       
        Class.forName("oracle.jdbc.driver.OracleDriver");
       
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connected to Oracle DB!");
          System.out.println("Login to the account ");
         Scanner scanner=new Scanner(System.in);
         Integer accountId = login(conn, scanner);
        if(accountId!=null){
         Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
          while(true){
          System.out.println("1. Create account\n2. Credit\n3. Debit\n4. Check balance\n5. Logout");
          System.out.print("Enter your choice: ");

          int op=scanner.nextInt();
         LocalDateTime s2= LocalDateTime.now();
         DateTimeFormatter s3=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         String TIME_AND_DATE=s2.format(s3);
         Scanner sc =new Scanner(System.in);
          switch(op){
          case 1:
            System.out.println("Enter name ");
            String name=sc.nextLine();
            System.out.println("Enter balance ");
            int balance=sc.nextInt();
            sc.nextLine();
            System.out.println("Enter phone number:");
            String phone = sc.nextLine();
            createAccount(conn, accountId,name,balance,phone);
            break;
          case 2:
            System.out.println("Enter Amount to be Credited ");
            credit(conn,accountId,sc.nextInt(),TIME_AND_DATE);
            break;
          case 3:
            System.out.println("Enter Amount to be Debited ");
            debit(conn,accountId,sc.nextInt(),TIME_AND_DATE);
            break;
          case 4:
            checkBalance(conn,accountId);
            break;
          case 5:
            System.out.println("Logout");
            return;
          default:
            System.out.println("No operation to performed");
         }
       }
    }
    else{
          System.out.println("No accountId Found");
        }
     }

        } catch (SQLException e) {
            e.printStackTrace();
        }
      
     
      catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    
    }
    
public static void sendSMS(String Phone, String messageBody) {
try{
    Message message = Message.creator(
            new PhoneNumber(Phone),    
            new PhoneNumber(FROM_PHONE),  
            messageBody                  
    ).create();
    System.out.println("SMS Sent: " + message.getSid());
    }
    catch (Exception e) {
        System.out.println("SMS failed: " + e.getMessage());
        e.printStackTrace();
    }

}

     public static Integer login(Connection conn, Scanner scanner) throws SQLException {
    System.out.print("Enter the username: ");
    String username = scanner.nextLine();
    System.out.print("Enter the password: ");
    String password = scanner.nextLine();

    String sql = "SELECT password, accountId FROM users WHERE username = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username.trim());
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    System.out.println("Login successful!");
                    return rs.getInt("accountId");
                } else {
                    System.out.println("Invalid password.");
                }
            } else {
                System.out.println("Username not found.");
            }
        }
    }
    return null;
}
  
    
    public static void createAccount(Connection conn, int accountId , String name, double balance, String phone ) throws SQLException {
    String sql = "INSERT INTO accounts (accountId, name, balance, phone) VALUES (?, ?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, accountId);
        pstmt.setString(2, name);
        pstmt.setDouble(3, balance);
        pstmt.setString(4, phone);
        pstmt.executeUpdate();
        System.out.println("Account created for " + name);
    } catch (SQLException e) {
        if (e.getErrorCode() == 1) {
            System.out.println(accountId + " Id already exists");
        } else {
            throw e;
        }
    }
}

    public static String getPhoneNumber(Connection conn, int accountId) throws SQLException {
    String sql = "SELECT phone FROM accounts WHERE accountId = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, accountId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("phone");
            }
        }
    }
    return null;
}

    public static void credit(Connection conn, int accountId, double amount, String TIME_AND_DATE) throws SQLException {
    String updateSql = "UPDATE accounts SET balance = balance + ? WHERE accountId = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
        pstmt.setDouble(1, amount);
        pstmt.setInt(2, accountId);
        pstmt.executeUpdate();
    }
    String txnSql = "INSERT INTO transactions (accountId, amount, txn_type, txn_time) VALUES (?, ?, 'credit', ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(txnSql)) {
        pstmt.setInt(1, accountId);
        pstmt.setDouble(2, amount);
        pstmt.setTimestamp(3,Timestamp.valueOf(TIME_AND_DATE));
        pstmt.executeUpdate();
        System.out.println("Credited $" + amount);
        String phone = getPhoneNumber(conn, accountId);
        if (phone != null) {
        sendSMS(phone, "From Your account " + accountId + " has been credited with " + amount + " on " + TIME_AND_DATE);
        }
     }
}


    public static void debit(Connection conn, int accountId, double amount, String TIME_AND_DATE) throws SQLException {
    if (getBalance(conn, accountId) < amount) {
        System.out.println("Insufficient balance.");
        return;
    }

    String updateSql = "UPDATE accounts SET balance = balance - ? WHERE accountId = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
        pstmt.setDouble(1, amount);
        pstmt.setInt(2, accountId);
        pstmt.executeUpdate();
    }

    String txnSql = "INSERT INTO transactions (accountId, amount, txn_type, txn_time) VALUES (?, ?, 'debit', ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(txnSql)) {
        pstmt.setInt(1, accountId);
        pstmt.setDouble(2, amount);
        pstmt.setTimestamp(3,Timestamp.valueOf(TIME_AND_DATE));
        pstmt.executeUpdate();
        System.out.println("Debited $" + amount);
        String phone = getPhoneNumber(conn, accountId);
        if (phone != null) {
        sendSMS(phone, " From Your account " + accountId + " has been debited by " + amount + " on " +TIME_AND_DATE);
        }
     }
}


    public static double getBalance(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE accountid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0.0;
    }

       public static void checkBalance(Connection conn, int accountId) throws SQLException {
        double balance = getBalance(conn, accountId);
        System.out.println("Current balance: $" + balance);
    }
}
