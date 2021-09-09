package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class LoginAttemptTracker {

     private static final String FILE_NAME = "C195LoginAttempts.txt";

     public LoginAttemptTracker() {}

     public static void logAttempt(String username, boolean loggedIn, String message) {
         try (FileWriter fileWriter = new FileWriter(FILE_NAME, true);
              BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
              PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
             printWriter.println("Username: " + username + " was a" + (loggedIn ? " success" : " failure") + " " + message + " " + Instant.now().toString());
         } catch (IOException e) {
             System.out.println("Log In Error: " + e.getMessage());
         }
     }

}
