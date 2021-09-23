package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class LoginAttemptTracker {

    /** File name that is file*/
    private static final String FILE_NAME = "C195LoginAttempts.txt";
    /** Basic constructor*/
    public LoginAttemptTracker() {}
    /** Log attempt that tracks all login attempts
     CREDIT: A user on Stack Overflow helped me develop this snippet. It is my work with a few suggestions.*/
    public static void logAttempt(String username, boolean loggedIn, String message) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println("Username " + username + " was a" + (loggedIn ? " success!" : " failure.") + " " + message + " " + Instant.now().toString());
        } catch (IOException e) {
            System.out.println("Log In Error: " + e.getMessage());
        }
    }
}
