package com.lucky5.mssim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * author : rawatyashpal
 * lastModifiedDate : 21/05/2018 5:38 PM
 */
@Service
public class MsSimService {

    private Environment env;
    private static final Logger logger = LoggerFactory.getLogger(MsSimService.class);

    public MsSimService(Environment env) {
        this.env = env;
    }

    /**
     * Send the response file present in base location.
     *
     *
     * @param apiName - Api name
     * @param key - key identifier
     *
     * @return - content of response file.
     *
     * @throws IOException - Exception if file response or default response is not present.
     */
    public String getApiResponse(final String apiName,
                                                  final String key) throws IOException {

        final String baseLocation = env.getProperty("response.location","");

        // Construct complete path to response file
        Path path = Paths.get(baseLocation, apiName, key + ".json");

        // if response file is not configured then check if default api response configured.
        if (!path.toFile().exists()) {
            logger.info("unable to find the response file at {} ", path);
            path  = Paths.get(baseLocation, apiName, "default.json");
        }

        // Check if there is valid response file
        if (!path.toFile().exists()) {
            logger.info("unable to find the default file at {} ", path);
            throw new IllegalArgumentException("no response configured");
        }

        logger.debug("complete path to output file => {}", path);
        return read(path);
    }


    /**
     * Method assumes Path is available on the system.
     *
     * @param path - {@link Path} Location to the file.
     *
     * @return - Content of the file.
     *
     * @throws java.io.IOException - If exception occurs while reading response from the file.
     */
    private String read(final Path path) throws IOException {
        logger.debug("complete path to output file => {}", path);
        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();
        logger.debug("response => {}", data);
        return data.toString();
    }
}
