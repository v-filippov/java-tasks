package org.filippovvv.javatasks.json.stream.parser;

import org.filippovvv.javatasks.json.stream.parser.model.Result;
import org.filippovvv.javatasks.json.stream.parser.model.ValueType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static org.filippovvv.javatasks.json.stream.parser.model.ValueType.*;

public class Parser {
    private static final char WHITESPACE = ' ';
    private static final char TABULATION = '\t';
    private static final char LINEBREAK = '\n';
    private final InputStream in;
    private final char DOUBLE_QUOTE = '"';
    private final char COLON = ':';

    private Map<ValueType, ValueReader> readerMap;

    public Parser(final InputStream inputStream) {
        this.in = inputStream;
        initReaders();
    }

    private void initReaders() {
        readerMap = Map.of(ValueType.NUMBER, new NumberValueReaderImpl(),
                ValueType.STRING, new StringValueReaderImpl(),
                JSON, new JsonValueReaderImpl(),
                ValueType.ARRAY, new ArrayValueReaderImpl());
    }

    /**
     * We read the given input stream until we find a double quote
     * Once the first double quota found we check a string between it
     * and the next double quota whether it matches the given key.
     * If the string matches the key and a colon follows the second quote
     * we conclude it was a key (not a string value) and it's the same as the provided key.
     * In this case we continue reading the stream and grab the key's value.
     *
     * @param key - key to find in JSON documents
     * @return Optional that contains key's value if the key is found.
     */
    public Optional<Result> findByKey(final String key) {
        System.out.println("key: " + key);
        int ch = -1;
        try (PushbackReader reader = new PushbackReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            while ((ch = reader.read()) != -1) {
                if (DOUBLE_QUOTE == ch) {
                    System.out.println("The first quote found. Checking whether it's the given key");
                    if (checkStringValueBetweenQuotas(reader, key)) {
                        System.out.println("Key " + key + " found, extracting its value");
                        final Result value = extractKeyValue(reader);
                        System.out.println("Key value: " + value);
                        return Optional.of(value);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }

    private Result extractKeyValue(PushbackReader reader) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            trim(reader);
            ValueType valueType = determineValueType(reader, byteArrayOutputStream);
            System.out.println("Value type: " + valueType);
            readerMap.get(valueType).readRemainingValue(reader, byteArrayOutputStream);
            return new Result(valueType, byteArrayOutputStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException("Failed to extract key value: ", e);
        }

    }

    private void trim(PushbackReader pushbackReader) {
        int ch = -1;
        try {
            while ((ch = pushbackReader.read()) != -1 && isWhitespaceOrTab((char) ch)) ;
            pushbackReader.unread(ch);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException("Failed to extract key value: ", e);
        }
    }

    private ValueType determineValueType(PushbackReader reader, ByteArrayOutputStream byteArrayOutputStream) {
        try {
            int firstChar = reader.read();
            byteArrayOutputStream.write(firstChar);
            char ch = (char) firstChar;
            return switch (ch) {
                case '"' -> ValueType.STRING;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> ValueType.NUMBER;
                case '[' -> ValueType.ARRAY;
                case '{' -> JSON;
                default -> throw new IllegalStateException("Failed to examine the first character or key's value. Unexpected character: " + ch);
            };
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException("Failed to examine the first character or key's value", e);
        }
    }

    /**
     * Checks whether a string between quotas is a key name or it's a string value
     * @param reader inputs stream reader with JSON data
     * @param key key we are looking for
     * @return <code>true</code> if a string is a key name, otherwise returns <code>false</code>
     * Example.
     */
    private boolean checkStringValueBetweenQuotas(PushbackReader reader, String key) {
        System.out.println("Capturing string between quotas");
        int ch = -1;
        try {
            int i = 0;
            int keyLength = key.length();
            // loop until string between quotas is equal to the given key and until we find a double quota.
            while (i < keyLength && ((ch = reader.read()) != -1) && key.charAt(i) == (char) ch && (char) ch != DOUBLE_QUOTE) {
                System.out.println("  ch: " + (char) ch + ", i: " + i);
                i++;
            }
            // if the string matched the given key and the next character is a double quota, check the next symbol.
            // e.g. key="name", string="name", if the next symbol is ':', this means we found a key "name": , not a string value
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

    private boolean isWhitespaceOrTab(final char ch) {
        return ch == WHITESPACE || ch == TABULATION || ch == LINEBREAK;
    }
}
