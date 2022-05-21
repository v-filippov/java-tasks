package org.filippovvv.javatasks.json.stream.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser {
    final InputStream in;
    public Parser(final InputStream inputStream) {
        this.in = inputStream;
    }
    public String findByKey(final String key) {
        System.out.println("key: " + key);
        InputStreamReader reader = new InputStreamReader(in);
        int ch = -1;
        while (true) {
            try {
                if (!((ch = reader.read()) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Char: " + ch);
        }
        return key;
    }
}
