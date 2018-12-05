package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Circle implements Writable {

    private int radius;
    private Point point;


    public Circle(int radius, Point point) {
        this.radius = radius;
        this.point = point;
    }

    public Circle(InputStream inputStream) throws IOException {
        read(inputStream);
    }

    public Circle(OutputStream outputStream) throws IOException {
        write(outputStream);
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "\n" +
                "x: " + point.getX() + "\n" +
                "y: " + point.getY() + "\n" +
                "radius: " + radius;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        byte[] intBytes = new byte[4];
        point.write(outputStream);
        ByteBuffer.wrap(intBytes).putInt(radius);
        outputStream.write(intBytes);
    }

    @Override
    public void read(InputStream inputStream) throws IOException {
        //getting point:
        point = new Point(inputStream);
        this.point = point;
        //getting radius
        byte[] intBytes = new byte[4];
        int actuallyRead;
        actuallyRead = inputStream.read(intBytes);
        if (actuallyRead != 4)
            throw new IOException("error reading radius from stream, did not get 4 bytes");
        radius = ByteBuffer.wrap(intBytes).getInt();
    }
}
