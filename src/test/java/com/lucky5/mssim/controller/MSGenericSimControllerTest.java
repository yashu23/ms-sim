package com.lucky5.mssim.controller;

import com.lucky5.mssim.service.MsSimService;
import com.lucky5.mssim.utils.Utilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Intent of the tests is to verify correct http codes and jsons are being returned, provided service layer is working
 * as expected.
 *
 *
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class MSGenericSimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MsSimService msSimService;

    public void setup() {

    }

    @Test
    public void postSimulatorRequest_NoServiceIdPresentInRequestAndNoDefaultResponseShouldReturnHttp500Error()
            throws Exception {
        Mockito.when(msSimService.getApiResponse(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("no response configured"));

        mockMvc.perform(post("/ms-sim/ncdUnidStatusTest")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    public void postSimulatorRequest_ServiceIdPresentAndApiNotPresentAndNoDefaultResponseShouldReturnHttp500Error()
            throws Exception {
        final String requestJson = "{\"serviceId\": \"my-service-id\" }";

        Mockito.when(msSimService.getApiResponse(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("no response configured"));

        mockMvc.perform(post("/ms-sim/ncdUnidStatusTest1")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    public void postSimulatorRequest_ServiceIdPresentApiResponsePresentOrDefaultResponsePresentShouldReturnSuccess()
            throws Exception {
        final String requestJson = "{\"serviceId\": \"my-service-id\" }";

        // Assumed that service layer finds default file configured for the api and returns the content.
        Mockito.when(msSimService.getApiResponse(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Utilities
                        .read("d:/development/ms-sim/src/main/resources/ncdUnidStatusTest/my-service-id.json"));

        mockMvc.perform(post("/ms-sim/ncdUnidStatusTest")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void getSimulatorRequest_NoApiResponseAndNoDefaultResponseShouldReturnHttp500Error()
            throws Exception {
        Mockito.when(msSimService.getApiResponse(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("no response configured"));

        mockMvc.perform(get("/ms-sim/ncdUnidStatusTest/asdfasdf"))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    public void getSimulatorRequest_ApiResponsePresentOrDefaultResponsePresentShouldReturnSuccess()
            throws Exception {
        // Assumed that service layer finds default file configured for the api and returns the content.
        Mockito.when(msSimService.getApiResponse(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Utilities
                        .read("d:/development/ms-sim/src/main/resources/ncdUnidStatusTest/my-service-id.json"));

        mockMvc.perform(get("/ms-sim/ncdUnidStatusTest/my-service-id"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}