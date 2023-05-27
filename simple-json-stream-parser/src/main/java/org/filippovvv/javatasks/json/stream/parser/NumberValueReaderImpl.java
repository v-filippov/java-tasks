package org.filippovvv.javatasks.json.stream.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PushbackReader;

public class NumberValueReaderImpl implements ValueReader {
    @Override
    public void readRemainingValue(PushbackReader pushbackReader, ByteArrayOutputStream byteArrayOutputStream) {
        int ch = -1;
        try {
            while ((ch = pushbackReader.read()) != -1 && Character.isDigit(ch)) {
                byteArrayOutputStream.write(ch);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
