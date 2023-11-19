import com.google.gson.Gson;
import io.restassured.RestAssured;
import model.CreateFirstIssue;
import org.BaseTest;
import org.testng.annotations.Test;

import java.util.UUID;

public class PerformanceTestForCreateIssue  {

    private CreateFirstIssue createIssue;
    private final String title1 = "create issue first" + UUID.randomUUID();

    @Test
    public void createIssue() {
        RestAssured.given()
                .log()
                .all()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer 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" )
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body("{\n" +
                        "  \"title\": \"Performance Test\",\n" +
                        "  \"body\": \"I'm having a problem with this.\",\n" +
                        "  \"assignees\": [\n" +
                        "    \"mzadap\"\n" +
                        "  ],\n" +
                        "  \"labels\": [\n" +
                        "    \"bug\"\n" +
                        "  ]\n" +
                        "}")
                .when()
                .post("/repos/mzadap/Demo-repo-testing/issues")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201);
    }
}
