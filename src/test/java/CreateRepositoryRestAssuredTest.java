import io.restassured.RestAssured;
import org.BaseTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateRepositoryRestAssuredTest extends BaseTest {

    File file = new File("C:\\Users\\nzadap\\Desktop\\DemoTesting\\api testing\\src\\main\\resources\\createrepository.json");
    File file1 = new File("C:\\Users\\nzadap\\Desktop\\DemoTesting\\api testing\\src\\main\\resources\\duplicatekeycreaterespository.json");
    File file2 = new File("C:\\Users\\nzadap\\Desktop\\DemoTesting\\api testing\\src\\main\\resources\\longnamecreaterespository.json");

    @Test
    public void shouldCreateNewRespository() {
        String repoName = "new api repo " + UUID.randomUUID();
        //String body = String.valueOf(file);
        //System.out.println(body);
        createRepository(file, 201);
    }

    @Test(priority = 1)
    public void shouldNotCreateRespositoryIfTokenIsInvalid() {
        createRepository("file", 401, "nan");
    }

    @Test(priority = 2)
    public void shouldNotCreateRepositoryIfInvalidBodyIsProvided() {
        createRepository("abc", 400, "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");
    }

    @Test(priority = 3)
    public void shouldNotCreateRespositoryIfInvalidEndpointIsProvided() {
        createRepository1(file, 404, "/abc");
    }

    @Test(priority = 4)
    public void shouldNotCreateRepositoryIfPayloadIsNotProvided() {
        RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                    .assertThat()
                .statusCode(400);
    }

    @Test(priority = 5)
    public void shouldNotCreateRepositoryIfNameIsRemovedFromPayload() {
        Map<String, String> map = new HashMap<>();
        map.put("description", "This repo is used for create repo using api's");
        map.put("homepage", "https://github.com");
        RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(map)
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(422);
    }

    @Test(priority = 6)
    public void shouldNotCreateRespositoryIfInvalidFieldIsProvided() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Testing ");
        map.put("description", "This repo is used for create repo using api's");
        map.put("homepage", "https://github.com");
        map.put("private", "false");
        RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(map)
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(422);
    }

    @Test(priority = 7)
    public void shouldNotCreateRespositoryIfInvalidHeadersIsSet() {
        RestAssured.baseURI = "https://api.github.com";
        RestAssured.given()
                .log()
                .all()
                .header("Authorizationsssss", "oathhh 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")
                .body(file)
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(401);
    }

    @Test(priority = 8)
    public void shouldNotCreateRespositoryIfWrongDataTypeIsSent() {
        RestAssured.baseURI = "https://api.github.com";
        RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(file1)
                .when()
                .post("/user/repos")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(422);
    }

    @Test(priority = 9)
    public void shouldNotCreateRespositoryIfNameIsTooLong() {
        createRepository(file2, 422);
    }

}
