package org.filippovvv.javatasks.json.stream.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Application {
    public static void main(String[] args) {
        final Parser parser;
        try (InputStream inputStream = new FileInputStream("src/main/resources/test.json")) {
            parser = new Parser(inputStream);
            parser.findByKey("key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
