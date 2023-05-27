package org.filippovvv.javatasks.json.stream.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Application {
    public static void main(String[] args) {
        try (InputStream inputStream = new FileInputStream("src/main/resources/test.json")) {
            final Parser parser = new Parser(inputStream);
            final String key = "key";
            final Optional<String> value = parser.findByKey(key);
            System.out.println("Value found by key \"" + key + "\" is " + value.orElse("Not found"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
