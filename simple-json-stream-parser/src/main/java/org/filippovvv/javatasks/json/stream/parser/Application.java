package org.filippovvv.javatasks.json.stream.parser;

import org.filippovvv.javatasks.json.stream.parser.model.Result;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Application {
    public static void main(String[] args) {
        try (InputStream inputStream = new FileInputStream("src/main/resources/test.json")) {
            final Parser parser = new Parser(inputStream);
            final String key = "key";
            final Optional<Result> value = parser.findByKey(key);
            System.out.println("Value found by key \"" + key + "\" is " + (value.isPresent() ? value.get() : "not found"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
