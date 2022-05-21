package org.filippovvv.javatasks.json.stream.parser;

import java.io.InputStream;

public class Application {
    public static void main(String[] args) {
        final InputStream inputStream = System.in;
        final Parser parser = new Parser(inputStream);
        parser.findByKey("key");

    }
}
