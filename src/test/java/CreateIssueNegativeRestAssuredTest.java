import com.google.gson.Gson;
import io.restassured.RestAssured;
import model.CreateFirstIssue;
import org.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.UUID;

public class CreateIssueNegativeRestAssuredTest extends BaseTest {
    private CreateFirstIssue createIssue;
    private final String title1 = "create issue first" + UUID.randomUUID();

    @BeforeClass
    public void setUp() {
        createIssue = getObjectFromJson("createfirstissue.json", CreateFirstIssue.class);
        createIssue.setTitle(title1);
    }

    @Test
    public void shouldNotCreateIssueIfTokenIsInvalid() {
        createIssue(401, createIssue, "bac");
    }

    @Test
    public void shouldNotCreateIssueIfInvalidBodyIsProvided() {
        Object abc = null;
        createIssue(422, abc);
    }

    @Test
    public void shouldNotCreateIssueIfInvalidHttpMethodIsProvided() {
        RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body(createIssue)
                .when()
                .delete("/repos/mzadap/Demo-repo-testing/issues")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void shouldNotCreateIssueIfInvalidHeaderIsSet() {
        RestAssured.baseURI = "https://api.github.com";
        RestAssured.given()
                .log()
                .all()
                .header("Authorizationsssss", "oathhh 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")
                .body(createIssue)
                .when()
                .post("/repos/mzadap/Demo-repo-testing/issues")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void shouldNotCreateIssueIfTitleIsTooLong() {
        createIssue.setTitle("nullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnullnull");
        createIssue(422, createIssue);
    }

    @Test
    public void shouldNotCreateIssueIfInvalidAssigneeIsProvided() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("openbugs");
        createIssue.setAssignees(arrayList);
        createIssue(422, createIssue);
    }

    @Test
    public void shouldNotCreateIssueIfReservedKeywordsInFieldName() {
        RestAssured.given()
                .log()
                .all()
                .spec(settingBaseUrl())
                .body("{\n" +
                        "    \"class\": \"not working\",\n" +
                        "    \"body\": \"I'm having a problem with this.\",\n" +
                        "    \"assignees\": [\n" +
                        "        \"mzadap\"\n" +
                        "    ],\n" +
                        "    //\"milestone\": 1,\n" +
                        "    \"labels\": [\n" +
                        "        \"bug\"\n" +
                        "    ]\n" +
                        "}")
                .when()
                .post("/repos/mzadap/Demo-repo-testing/issues")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(422);
    }

    @Test
    public void shouldNotCreateIssueIfInvalidEndpointsIsProvided() {
        createIssue1(404, createIssue, "/abbbc");
    }

}
