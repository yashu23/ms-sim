package com.lucky5.mssim.controller;

import com.lucky5.mssim.service.MsSimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * author : rawatyashpal
 * lastModifiedDate : 26/04/2018 1:53 PM
 *
 * This simulator expects serviceId is present in the request payload and can be used as primary identifier.
 */
@Controller
public class MSGenericSimController {

    private MsSimService msSimService;
    private static final Logger logger = LoggerFactory.getLogger(MSGenericSimController.class);

    public MSGenericSimController(MsSimService msSimService) {
        this.msSimService = msSimService;
    }

    /**
     * Post request from the consumer will follow pattern with two notifications i.e.
     * accepted followed by a final response. Api name will be retrieved from path and key identifier will
     * be fetched from request body.
     *
     *
     *
     * @param requestMap - Map containing request json as properties.
     * @param apiName - Name of the api for which request is made.
     *
     * @return Api response.
     */
    @RequestMapping(value = "/ms-sim/{apiName}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSimulatorRequest(@RequestBody Map<String, String> requestMap,
                                                 @PathVariable String apiName) {

        logger.info("Post request for api = {}", apiName);
        final String serviceId = requestMap.get("serviceId");
        logger.info("serviceId {}", serviceId);

        try {
            String response;
            // Send Default Response Configured for API
            if (StringUtils.isEmpty(serviceId)) {
                response = msSimService.getApiResponse(apiName, "");
            } else {
                response = msSimService.getApiResponse(apiName, serviceId);
            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception ex) {
            return new ResponseEntity<>("{\"error\":\"" + ex.getMessage() + "\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method can be used for simulating a synchronous get rest call. Currently the implementation
     * assumes apiName and unique key will be provided in the input. If not present then default response
     * will be returned.
     *
     * @param tokenId - Unique key to identify response file for the api.
     * @param apiName - api name.
     *
     * @return Api response
     */
    @RequestMapping(value = "/ms-sim/{apiName}/{tokenId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSimulatorResponse(@PathVariable String tokenId,
                                                 @PathVariable String apiName) {
        logger.info("Get request for api = {}", apiName);
        try {
            return new ResponseEntity<>(msSimService.getApiResponse(apiName, tokenId),
                    HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}