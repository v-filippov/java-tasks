package org.filippovvv.javatasks.json.stream.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

public class JsonValueReaderImpl implements ValueReader {
    @Override
    public void readRemainingValue(InputStreamReader inputStreamReader, ByteArrayOutputStream byteArrayOutputStream) {
        int ch = -1;
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        try {
            while ((ch = inputStreamReader.read()) != -1 && !stack.isEmpty()) {
                if ('{' == (char) ch) {
                    stack.push(1);
                }
                if ('}' == (char) ch) {
                    stack.pop();
                }
                byteArrayOutputStream.write(ch);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
