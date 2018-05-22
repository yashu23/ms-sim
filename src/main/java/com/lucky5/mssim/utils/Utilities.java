package com.lucky5.mssim.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * author : rawatyashpal
 * lastModifiedDate : 22/05/2018 10:56 AM
 */
public final class Utilities {

    private static final Logger log = LoggerFactory.getLogger(Utilities.class);

    /**
     * Utility method to read file.
     *
     * @param file - File to be read.
     *
     * @return Content of the file.
     */
    public static String read(final String file) {
        StringBuilder data = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(file), StandardCharsets.ISO_8859_1)){
            lines.forEach(line -> data.append(line).append("\n"));
            return data.toString();
        } catch(Exception ex){
            log.error("error occurred while reading file {} {}", file, ex);
            return null;
        }
    }
}
