package org.filippovvv.javatasks.json.stream.parser;

import java.io.ByteArrayOutputStream;
import java.io.PushbackReader;

public interface ValueReader {
    void readRemainingValue(PushbackReader pushbackReader, ByteArrayOutputStream byteArrayOutputStream);
}
