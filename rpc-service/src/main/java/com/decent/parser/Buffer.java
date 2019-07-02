package com.decent.parser;

import java.io.DataInputStream;

public class Buffer {
    private String line = "";
    private int column = 0;
    private int lineNo = 0;
    private DataInputStream in;

    public Buffer(DataInputStream in) {
        this.in = in;
    } // Buffer

    public char get() {
        column++;
        if (column >= line.length()) {
            try {
                line = in.readLine();
            } catch (Exception e) {
                Scanner.logger.error("The error is {}", e);
            } // try
            column = 0;
            lineNo++;
            line = line + "\n";
        } // if column
        return line.charAt(column);
    } // get

}