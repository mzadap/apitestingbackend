import io.restassured.path.json.JsonPath;
import model.CreateFirstIssue;
import model.UpdateIssue;
import org.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class CreateIssueForRespositoryRestAssuredTest extends BaseTest {
    private CreateFirstIssue createIssue;
    private UpdateIssue updateIssue;
    private final String title1 = "create issue first" + UUID.randomUUID();
    private final String title2 = "create issue second" + UUID.randomUUID();
    int firstIssueNumber, secondIssueNumber;

    @Test
    public void createFirstIssue() {
        createIssue = getObjectFromJson("createfirstissue.json", CreateFirstIssue.class);
        createIssue.setTitle(title1);
        String resp = createIssue(201, createIssue);
        JsonPath jsonPath = new JsonPath(resp);
        firstIssueNumber = jsonPath.get("number");
        System.out.println(firstIssueNumber);
    }

    @Test(dependsOnMethods = "createFirstIssue")
    public void getFirstIssue() {
        String res = getIssue(String.valueOf(firstIssueNumber), 200);
        JsonPath jsonPath = new JsonPath(res);
        int expectedNumber = jsonPath.get("number");
        Assert.assertEquals(firstIssueNumber, expectedNumber);
        String title = jsonPath.get("title");
        Assert.assertEquals(title, title1);
    }

    @Test(dependsOnMethods = "getFirstIssue")
    public void closeFirstIssue() {
        updateIssue = getObjectFromJson("updateissue.json", UpdateIssue.class);
        updateIssue.setState("closed");
        updateIssue(200, updateIssue, firstIssueNumber);
    }

    @Test(dependsOnMethods = "closeFirstIssue")
    public void getFirstIssueAfterStateIsClosed() {
        String res = getIssue(String.valueOf(firstIssueNumber), 200);
        JsonPath jsonPath = new JsonPath(res);
        String actualState = jsonPath.get("state");
        Assert.assertEquals(actualState, "closed");
    }

    @Test(dependsOnMethods = "getFirstIssueAfterStateIsClosed")
    public void createSecondIssue() {
        createIssue = getObjectFromJson("createsecondissue.json", CreateFirstIssue.class);
        createIssue.setTitle(title2);
        String resp = createIssue(201, createIssue);
        JsonPath jsonPath = new JsonPath(resp);
        secondIssueNumber = jsonPath.get("number");
    }

    @Test(dependsOnMethods = "createSecondIssue")
    public void getSecondIssue() {
        String res = getIssue(String.valueOf(secondIssueNumber), 200);
        JsonPath jsonPath = new JsonPath(res);
        int actualNumber = jsonPath.get("number");
        Assert.assertEquals(actualNumber, secondIssueNumber);
        String title = jsonPath.get("title");
        Assert.assertEquals(title, title2);
    }

    @Test(dependsOnMethods = "getSecondIssue")
    public void closeSecondIssue() {
        updateIssue = getObjectFromJson("updateissue.json", UpdateIssue.class);
        updateIssue.setState("closed");
        updateIssue(200, updateIssue, secondIssueNumber);
    }

    @Test(dependsOnMethods = "closeSecondIssue")
    public void getSecondIssueAfterStateIsClosed() {
        String res = getIssue(String.valueOf(secondIssueNumber), 200);
        JsonPath jsonPath = new JsonPath(res);
        String actualState = jsonPath.get("state");
        Assert.assertEquals(actualState, "closed");
    }
}
