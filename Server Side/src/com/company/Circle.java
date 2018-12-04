package com.company;

import java.io.*;
import java.nio.ByteBuffer;

public class Circle implements Writable {

    private int x,y,radius;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;

    }

    public Circle(InputStream inputStream) throws IOException {
        read(inputStream);
    }

    public Circle(OutputStream outputStream) throws IOException {
        write(outputStream);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "(x: " + x + ", y: " + y + ", radius: " + radius + ")";
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        byte[] intBytes = new byte[4];
        ByteBuffer.wrap(intBytes).putInt(x);
        outputStream.write(intBytes);
        ByteBuffer.wrap(intBytes).putInt(y);
        outputStream.write(intBytes);
        ByteBuffer.wrap(intBytes).putInt(radius);
        outputStream.write(intBytes);
    }

    @Override
    public void read(InputStream inputStream) throws IOException {
        byte[] intBytes = new byte[4];
        int actuallyRead;
        //getting x:
        actuallyRead = inputStream.read(intBytes);
        if (actuallyRead != 4)
            throw new IOException("error reading x from stream, did not get 4 bytes");
        x = ByteBuffer.wrap(intBytes).getInt();
        //getting y:
        actuallyRead = inputStream.read(intBytes);
        if (actuallyRead != 4)
            throw new IOException("error reading y from stream, did not get 4 bytes");
        y = ByteBuffer.wrap(intBytes).getInt();
        //getting radius
        actuallyRead = inputStream.read(intBytes);
        if (actuallyRead != 4)
            throw new IOException("error reading radius from stream, did not get 4 bytes");
        radius = ByteBuffer.wrap(intBytes).getInt();
    }
}
