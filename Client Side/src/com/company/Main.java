package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 3000;
    private static final int CREATE_CIRCLE = 1;
    private static final int SEARCH_CIRCLE = 2;
    private static final int EXIT = 3;
    public static final int ADD_CIRLCE = 1;
    public static final int CONTAINS_NAME = 100;
    public static final int DOES_NOT_CONTAIN_NAME = 101;
    public static final int DELETE_CIRCLE = 1;
    public static final int DELETE_SUCCESSFULLY = 51;
    public static final int Y_STARTING_POSITION = 15;
    public static final int X_STARTING_POSITION = 15;

    public static void main(String[] args) {
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        boolean isConnected = true;
        int choice;
        Scanner scanner = new Scanner(System.in);
        while (isConnected) {
            try {
                choice = printMenu();
                socket = new Socket(HOST, PORT);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                byte[] buffer = new byte[4];
                switch (choice) {
                    case CREATE_CIRCLE:
                        outputStream.write(ADD_CIRLCE);
                        //create circle and add to list:
                        createCircleOnServer(inputStream, outputStream, scanner);
                        break;
                    case SEARCH_CIRCLE:
                        outputStream.write(SEARCH_CIRCLE);
                        System.out.println("enter a circle name to edit: ");
                        //sending data to server:
                        String name = scanner.nextLine();
                        outputStream.write(name.length());
                        outputStream.write(name.getBytes());
                        //receiving data from server:
                        int read;
                        if ((read = inputStream.read()) == -1)
                            throw new IOException("illegal boolean from server");
                        //int intBoolean = inputStream.read(intBytes);
                        if (read == CONTAINS_NAME) {
                            System.out.println("found circle: " + name);
                            int editChoice = printEditCircleMenu();
                            switch (editChoice) {
                                case 1:
                                    //sending delete request:
                                    outputStream.write(DELETE_CIRCLE);
                                    //System.out.println("successfully deleted circle " + name);
                                    //getting back result:
                                    int result = inputStream.read();
                                    if (result == DELETE_SUCCESSFULLY) {
                                        System.out.println("-------------\n" +
                                                "successfully deleted circle " + name + "\n" +
                                                "-------------");
                                        printMenu();
                                    }
                                    break;
                                case 2:
                                    printMenu();
                                    break;
                            }
                        } else if (read == DOES_NOT_CONTAIN_NAME)
                            System.out.println("did not find circle: " + name);
                        break;
                    case EXIT:
                        System.out.println("bye bye");
                        isConnected = false;
                        break;
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void createCircleOnServer(InputStream inputStream, OutputStream outputStream, Scanner scanner) throws IOException {
        //sending to server:
        System.out.println("enter key name for the new circle:");
        String name = new String(scanner.nextLine());
        //int xParam = scanner.nextInt();
        int xParam = readIntegerFromConsole("please enter x parameter for circle.\n" +
                "(recommended value is  between 1 and 15) - ");
        //checkParams(xParam);
        int yParam = readIntegerFromConsole("please enter y parameter for circle.\n" +
                "(recommended value is  between 1 and 15) - ");
        //checkParams(yParam);
        int radiusParam = readIntegerFromConsole("please enter radius parameter for circle.\n" +
                "(recommended value is  between 1 and 15) - ");
        //checkParams(radiusParam);
        //scanner.nextLine();
        int length = name.length();
        //writing string to server:
        outputStream.write(length);
        outputStream.write(name.getBytes());
        //writing object to server:
        Circle c = new Circle(new Point(xParam, yParam), radiusParam);
        c.write(outputStream);
        //outputStream.write(buffer);
        //receiving from server:
        //receiving the string name:
        int stringlength = inputStream.read();
        String circleName = null;
        if (stringlength == -1)
            throw new IOException("string length exception");
        byte[] bytes = new byte[stringlength];
        int actuallyRead = inputStream.read(bytes);
        if (actuallyRead != stringlength)
            throw new IOException("invalid string check client row 90");
        circleName = new String(bytes);
        //receiving the circle object from server:
        Circle circle = new Circle(inputStream);
        System.out.println("-----------------\n" +
                "successfully created a circle on server named " + circleName +
                " with: " + circle.toString() + "\n" +
                "a scheme of your circle will be drawn based on radius: \n");
        if (radiusParam < 0 || radiusParam > 15 ||
                xParam > 15 || xParam < 0 ||
                yParam > 15 || yParam < 0) {
            System.out.println("------------------\n" +
                    "ERROR: Can't print this circle because of console limitation.\n" +
                    "All circle parameters must be between 0 and 15 excluding 0 in order to be printed.\n" +
                    "Still, circle has been fully received and saved by the server.\n" +
                    "------------------");
        } else {
            circleDrawer(xParam, yParam, radiusParam);
        }
        System.out.println("\n" +
                "-----------------");
    }

    private static void checkParams(String x) {
        try{
            int xParam = Integer.valueOf(x);
        } catch (NumberFormatException e){
            System.out.println("----------\n" +
                    "Only numbers accepted.\n" +
                    "----------");
        }
    }

    public static int printMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("welcome! please select:");
        System.out.println("1.add a new Circle");
        System.out.println("2.search for a circle");
        System.out.println("3.exit");
        int choice = readIntegerFromConsole("your choice: ");
        if (choice < 1 || choice > 3) {
            System.out.println("invalid choice!");
            return printMenu();
        }
        return choice;
    }

    public static int printEditCircleMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select an option:");
        System.out.println("1. delete this circle");
        System.out.println("2. do nothing and return back");
        int choice = readIntegerFromConsole("your choice: ");
        if (choice < 1 || choice > 2) {
            System.out.println("invalid choice!");
            return printEditCircleMenu();
        }
        return choice;
    }

    private static int readIntegerFromConsole(String instruction) {
        System.out.print(instruction);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int x = -1;
        try {
            x = Integer.valueOf(input);
        } catch (Exception ex) {
            System.out.println("this is not a valid character");
            return readIntegerFromConsole(instruction);
        }
        return x;
    }

    public static void circleDrawer(int xPos, int yPos, int radius) {
        xPos += X_STARTING_POSITION;
        yPos += Y_STARTING_POSITION;
        for (int i = 0; i <= xPos + radius; i++) {
            for (int j = 1; j <= yPos + radius; j++) {
                int xSquared = (i - xPos) * (i - xPos);
                int ySquared = (j - yPos) * (j - yPos);
                if (Math.abs(xSquared + ySquared - radius * radius) < radius) {
                    try {
                        System.out.print("#");
                        Thread.sleep((long) (5 / Math.sqrt(radius)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.print("-");
                        Thread.sleep((long) (10 / Math.sqrt(radius)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println();
        }
    }
}
