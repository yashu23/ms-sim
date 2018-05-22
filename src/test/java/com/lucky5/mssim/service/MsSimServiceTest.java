package com.lucky5.mssim.service;

import com.lucky5.mssim.utils.Utilities;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.env.MockEnvironment;

import java.io.IOException;

@RunWith(JUnit4.class)
public class MsSimServiceTest {

    private MsSimService msSimService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        final MockEnvironment env = new MockEnvironment();
        env.setProperty("response.location","d:/development/ms-sim/src/main/resources");
        msSimService = new MsSimService(env);
    }

    /**
     * Test Case: No Response configured in the simulator and no default response configured for the api.
     *
     * Expected Result: Illegal Argument Exception should be thrown
     *
     * @throws IOException throws io exception for file path issues.
     */
    @Test
    public void testGetApiResponse_NoResponsePresentAndNoApiDefaultResponsePresentShouldThrowException()
            throws IOException{
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("no response configured");
        msSimService.getApiResponse("adfafds","");
    }

    /**
     * Test Case: No Response configured in the simulator but a default response configured for api.
     *
     * Expected Result: Response json should be returned from default response file.
     *
     * @throws Exception throws io exception for file path issues.
     */
    @Test
    public void testGetApiResponse_NoResponsePresentAndApiDefaultResponsePresentShouldPass() throws Exception {
        final String actualJson = msSimService
                .getApiResponse("myApi", "adsfafsadf");
        final String expectedJson =
                Utilities.read("d:/development/ms-sim/src/main/resources/myApi/default.json");

        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    /**
     * Test Case: Response configured in the simulator.
     *
     * Expected Result: Response json should be returned.
     *
     * @throws Exception exception reading file.
     */
    @Test
    public void testGetApiResponse_ResponsePresentShouldPass() throws Exception {
        final String actualJson = msSimService
                .getApiResponse("myApi", "my-service-id");
        final String expectedJson =
                Utilities.read("d:/development/ms-sim/src/main/resources/myApi/my-service-id.json");

        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }



}