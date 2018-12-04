package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ClientThread extends Thread {

    public static final int ADD_CIRCLE = 1;
    private static final int CHECK_CIRCLE = 2;
    public static final int CONTAINS_NAME = 100;
    public static final int DOES_NOT_CONTAIN_NAME = 101;
    public static final int DELETE_CIRCLE = 1;
    public static final int DELTE_SUCCESSFULLY = 51;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;


    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            System.out.println("stam0");
            int actuallyRead = inputStream.read();
            System.out.println("stam1");
            if (actuallyRead == -1)
                return;
            int choice = actuallyRead;
            switch (choice) {
                case ADD_CIRCLE:
                    System.out.println("case 1");
                    if (addCircle()) return;
                    break;

                case CHECK_CIRCLE:
                    System.out.println("case 2");
                    int stringLength = inputStream.read();
                    String name = null;
                    if (stringLength == -1)
                        throw new IOException("invalid string length check server run method");
                    byte[] bytes = new byte[stringLength];
                    actuallyRead = inputStream.read(bytes);
                    if (actuallyRead != stringLength)
                        throw new IOException("invalid string length check server run method");
                    name = new String(bytes);
                    //if there is that name in the map
                    if (Main.hashMap.containsKey(name)) {
                        //sending answer:
                        outputStream.write(CONTAINS_NAME);
                        System.out.println("checked name: " + name + " and result was true");
                        //getting choice again for editing hashmap:
                        byte[] editBytes = new byte[4];
                        int editChoice = inputStream.read(editBytes);
                        if (editChoice == DELETE_CIRCLE){
                            //deleting on hashmap:
                            Main.hashMap.remove(name);
                            System.out.println("removed key named: " + name + " by request from user");
                            //sending result back:
                            outputStream.write(DELTE_SUCCESSFULLY);
                        }
                    } else {
                        outputStream.write(DOES_NOT_CONTAIN_NAME);
                        System.out.println("checked name: " + name + " and result was false");
                    }
                    break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean addCircle() throws IOException {
        int actuallyRead;
        byte[] buffer;
        //for name:
        String name = null;
        int stringLength = inputStream.read();
        if (stringLength == -1)
            throw new IOException("invalid string length check server run method");
        byte[] bytes = new byte[stringLength];
        actuallyRead = inputStream.read(bytes);
        if (actuallyRead != stringLength)
            throw new IOException("invalid string length check server run method");
        name = new String(bytes);

        //for object:
        Circle circle = new Circle(inputStream);
        Main.hashMap.put(name, circle);
        System.out.println("circle created: " + name + " " + circle);
        outputStream.write(name.length());
        outputStream.write(name.getBytes());
        circle.write(outputStream);
        return false;
    }
}
