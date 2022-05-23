package org.filippovvv.javatasks.json.stream.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Parser {
    final InputStream in;
    final char DOUBLE_QUOTE = '"';
    final char COLON = ':';

    public Parser(final InputStream inputStream) {
        this.in = inputStream;
    }

    public String findByKey(final String key) {
        System.out.println("key: " + key);
        int ch = -1;
        int[] buffer = new int[key.length()];
        try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            while ((ch = reader.read()) != -1) {
                System.out.println("Char: " + (char)ch);
                if (DOUBLE_QUOTE == ch) {
                    System.out.println("quote found");
                    if (captureString(reader, key)) {
                        System.out.println("Key " + key + " found, extracting its value");
                        extractKeyValue(reader);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return key;
    }

    private void extractKeyValue(InputStreamReader reader) {

    }

    private boolean captureString(InputStreamReader reader, String key) {
        System.out.println("Capturing string");
        int ch = -1;
        try {
            int i = 0;
            int keyLength = key.length();

            while ( i < keyLength && ((ch = reader.read()) != -1) && key.charAt(i) == (char)ch && (char)ch != DOUBLE_QUOTE) {
                System.out.println("  ch: " + (char)ch + ", i: " + i);
                i++;
            }
            if (i == keyLength && (char)reader.read() == DOUBLE_QUOTE) {
                System.out.println("match");
                while ((ch = reader.read()) != -1 && isWhitespaceOrTab((char)ch)) {
                    continue;
                }
                System.out.println("Char after the second quote: " + (char)ch);
                if (COLON == (char) ch) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    private boolean isWhitespaceOrTab(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }
}
