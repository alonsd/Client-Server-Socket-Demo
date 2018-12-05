package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Point implements Writable {

    private int x,y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(InputStream inputStream) throws IOException {
        read(inputStream);
    }

    public Point(OutputStream outputStream) throws  IOException {
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

    @Override
    public void write(OutputStream outputStream) throws IOException {
        byte[] intBytes = new byte[4];
        ByteBuffer.wrap(intBytes).putInt(x);
        outputStream.write(intBytes);
        ByteBuffer.wrap(intBytes).putInt(y);
        outputStream.write(intBytes);
    }

    @Override
    public String toString() {
        return "x: " + x +
                " y: " + y;
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
    }
}
