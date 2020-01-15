package com.netcompany.utility;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonUtility {

    public static String absolutePath = rootPath();

    public static JsonPath rawToJson(Response r) {
        String response = r.asString();
        JsonPath path = new JsonPath(response);
        return path;
    }

    public static String generateStringFromResource(String filename) throws IOException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String path = s + "/src/test/resources/testData/postData/"+filename+".json";
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static final String rootPath() {
        Path currentPath = Paths.get("");
        return currentPath.toAbsolutePath().toString();
    }
}
