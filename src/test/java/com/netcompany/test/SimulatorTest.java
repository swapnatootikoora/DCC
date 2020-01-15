package com.netcompany.test;

import com.netcompany.utility.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.netcompany.utility.CommonUtility.generateStringFromResource;
import static io.restassured.RestAssured.given;

public class SimulatorTest {
    public static ReadProperties properties;
    public static String Gaining_Supplier_URL;
    public static String Losing_Supplier_URL;
    public static String correlationID;

    private static Logger log = LogManager.getLogger(SimulatorTest.class.getName());

    public SimulatorTest() {
        properties = new ReadProperties();
    }


    @BeforeTest
    public void getData() throws IOException {
        Gaining_Supplier_URL = ReadConfig.readConfigProperty().getProperty("SIT_SIMULATOR_GAINING_SUPPLIER");
        Losing_Supplier_URL = ReadConfig.readConfigProperty().getProperty("SIT_SIMULATOR_LOSING_SUPPLIER");
    }


    @Test
    public void initiateSwitchRequestForGas() throws IOException {
        RestAssured.baseURI = Gaining_Supplier_URL;
        Response res = given().contentType(ContentType.JSON).
                body(generateStringFromResource("switchRequestGas")).
                when().
                post(properties.switchRequestEndpoint()).
                then().
                assertThat().
                statusCode(202).
                extract().response();

        JsonPath js = CommonUtility.rawToJson(res);
        correlationID = js.get("correlationId");
        String eventID = js.get("eventId");
        log.info("correlationId = " + correlationID);
        log.info("eventId = " + eventID);
    }

    @Test
    public void verifySwitchRequestForGasSubmittedAndExecutedSucessfully() throws IOException {
        RestAssured.baseURI = Losing_Supplier_URL;
        String invitationToInterventionPayload = generateStringFromResource("invitationIntervention");
        JSONObject obj = new JSONObject(invitationToInterventionPayload);
        obj.put("correlationId", correlationID);
        invitationToInterventionPayload = obj.toString();
                given().contentType(ContentType
                        .JSON).
                body(invitationToInterventionPayload).
                when().
                post(properties.invitationToInterventionEndpoint()).
                then().
                assertThat().
                statusCode(202).
                extract().response();
    }
}
