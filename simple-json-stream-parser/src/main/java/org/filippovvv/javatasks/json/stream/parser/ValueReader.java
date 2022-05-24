package org.filippovvv.javatasks.json.stream.parser;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

public interface ValueReader {
    void readRemainingValue(InputStreamReader inputStreamReader, ByteArrayOutputStream byteArrayOutputStream);
}
