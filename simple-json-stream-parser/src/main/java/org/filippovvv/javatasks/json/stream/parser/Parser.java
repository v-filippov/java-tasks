package org.filippovvv.javatasks.json.stream.parser;

import java.io.*;
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
                //System.out.println("Char: " + (char)ch);
                if (DOUBLE_QUOTE == ch) {
                    System.out.println("The first quote found. Checking whether it's the given key");
                    if (checkStringValueBetweenQutas(reader, key)) {
                        System.out.println("Key " + key + " found, extracting its value");
                        final String value = extractKeyValue(reader);
                        System.out.println("Key value: " + value);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return key;
    }

    private String extractKeyValue(InputStreamReader reader) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ValueType valueType = determineValueType(reader, byteArrayOutputStream);
            System.out.println("Value type: " + valueType);
            if (valueType == ValueType.JSON) {
                new JsonValueReaderImpl().readRemainingValue(reader, byteArrayOutputStream);
            } else if (valueType == ValueType.STRING) {
                new StringValueReaderImpl().readRemainingValue(reader, byteArrayOutputStream);
            }
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException("Failed to extract key value: ", e);
        }

    }

    private ValueType determineValueType(InputStreamReader reader, ByteArrayOutputStream byteArrayOutputStream) {
        try {
            int firstChar = reader.read();
            byteArrayOutputStream.write(firstChar);
            char ch = (char) firstChar;
            return switch (ch) {
                case '"' -> ValueType.STRING;
                case '1', '2', '3', '4', '5', '6', '7', '8', '9'  -> ValueType.NUMBER;
                case '[' -> ValueType.ARRAY;
                case '{' -> ValueType.JSON;
                default -> throw new IllegalStateException("Failed to examine the first character or key's value. Unexpected character: " + ch);
            };
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException("Failed to examine the first character or key's value", e);
        }
    }

    private boolean checkStringValueBetweenQutas(InputStreamReader reader, String key) {
        System.out.println("Capturing string between quotas");
        int ch = -1;
        try {
            int i = 0;
            int keyLength = key.length();

            while (i < keyLength && ((ch = reader.read()) != -1) && key.charAt(i) == (char) ch && (char) ch != DOUBLE_QUOTE) {
                System.out.println("  ch: " + (char) ch + ", i: " + i);
                i++;
            }
            if (i == keyLength && (char) reader.read() == DOUBLE_QUOTE) {
                System.out.println("String between quotas matches the given key");
                while ((ch = reader.read()) != -1 && isWhitespaceOrTab((char) ch)) {
                    continue;
                }
                System.out.println("Char after the second quote: " + (char) ch);
                if (COLON == (char) ch) {
                    System.out.println("After the second quota follows a colon thus it's a key name, not a string value");
                    return true;
                }
                System.out.println("After the second quota follows " + (char) ch + " thus it's not a key name");
            } else {
                System.out.println("String between quotas doesn't match the given key");
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
