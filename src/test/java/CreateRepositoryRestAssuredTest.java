import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import model.CreateRepository;
import org.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateRepositoryRestAssuredTest extends BaseTest {
    private CreateRepository createRepository;
    private CreateRepository repository;
    private final String name = "create-repository" + UUID.randomUUID();

    @BeforeClass
    public void setUp() {
        createRepository = getObjectFromJson("createrepository.json", CreateRepository.class);
        repository = getObjectFromJson("longnamecreaterespository.json", CreateRepository.class);
        createRepository.setName(name);
    }
    @Test
    public void shouldCreateNewRespository() {
        String response = createRepository(createRepository, 201);
        JsonPath jsonPath = new JsonPath(response);
        String actualName = jsonPath.get("name");
        Assert.assertEquals(actualName, name);
    }

    @Test(priority = 1)
    public void shouldNotCreateRespositoryIfTokenIsInvalid() {
        createRepository(createRepository, 401, "nan");
    }

    @Test(priority = 2)
    public void shouldNotCreateRepositoryIfInvalidBodyIsProvided() {
        createRepository("abc", 400, "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");
    }

    @Test(priority = 3)
    public void shouldNotCreateRespositoryIfInvalidEndpointIsProvided() {
        createRepository1(createRepository, 404, "/abc");
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
                .body(createRepository)
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
                .body(createRepository)
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
        createRepository(repository, 422);
    }
}
