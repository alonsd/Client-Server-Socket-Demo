package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Writable {
    void write(OutputStream outputStream) throws IOException;
    void read(InputStream inputStream) throws IOException;
}
