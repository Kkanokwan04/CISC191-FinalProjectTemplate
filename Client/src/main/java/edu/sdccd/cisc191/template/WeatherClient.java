package edu.sdccd.cisc191.template;
/**
 * This program opens a connection to a computer specified
 * as the first command-line argument.  If no command-line
 * argument is given, it prompts the user for a computer
 * to connect to.  The connection is made to
 * the port specified by LISTENING_PORT.  The program reads one
 * line of text from the connection and then closes the
 * connection.  It displays the text that it read on
 * standard output.  This program is meant to be used with
 * the server program, DateServer, which sends the current
 * date and time on the computer where the server is running.
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WeatherClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Scanner scanner = new Scanner(System.in);
            boolean validCity = false;


            //add a loop to continuously ask the user to input a valid city if their input is not recognized.
            while (!validCity) {
                // Ask user to enter a city name
                System.out.println("Enter the name of the city (San Diego or New York): ");
                String city = scanner.nextLine();

                // Send the city name to the server
                out.writeObject(city);

                // Receive weather reports from the server
                WeatherReport[] reports = (WeatherReport[]) in.readObject();

                if (reports[0].getReportDetails().equals("City not found")) {
                    System.out.println("City not found. Please try again.");
                } else {
                    validCity = true;
                    // Print received weather reports
                    System.out.println("Weather Reports for " + city + ":");
                    for (WeatherReport report : reports) {
                        System.out.println(report);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
