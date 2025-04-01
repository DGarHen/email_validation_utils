package utils.emailApiUtils.messageTemplates;

import org.assertj.core.api.SoftAssertions;
import utils.emailApiUtils.messageTemplates.entity.MessageTemplate;

import java.util.Map;

public class ProjectAndUserAlertMessage extends MessageTemplate {
    private final String titleXpath = "//div[@class='email-body']//h3";
    private final String headersXpath = "//table[@class='dfin_table']//th";
    private final String alertNameXpath = "//table[@class='dfin_table']//td[@class='dfin_table_cell'][1]";
    private final String fileNameXpath = "//table[@class='dfin_table']//td[@class='dfin_table_cell'][4]";
    private final String fileChangeXpath = "//table[@class='dfin_table']//td[@class='dfin_table_cell'][5]";

    public ProjectAndUserAlertMessage() {
        super();
    }

    @Override
    public void checkMessageContent(Map<String, String> expectedData) {
        SoftAssertions softAssertions = new SoftAssertions();
        if (expectedData.get("title") != null) {
            softAssertions.assertThat(
                            hasEmailReceivedBodyMessageWithXpath(expectedData.get("title"), titleXpath))
                    .as(String.format("Expected email does not contain the Message %s", expectedData.get("title")))
                    .isTrue();
        }
        if (expectedData.get("table headers") != null) {
            softAssertions.assertThat(
                            hasEmailReceivedBodyMessageWithXpath(expectedData.get("table headers"), headersXpath))
                    .as("Expected email does not contain correct table headers")
                    .isTrue();
        }
        if (expectedData.get("alert name") != null) {
            softAssertions.assertThat(
                            hasEmailReceivedBodyMessageWithXpath(expectedData.get("alert name"), alertNameXpath))
                    .as("Expected email does not contain correct alert name")
                    .isTrue();
        }
        if (expectedData.get("file name") != null) {
            softAssertions.assertThat(
                            hasEmailReceivedBodyMessageWithXpath(expectedData.get("file name"), fileNameXpath))
                    .as("Expected email does not contain correct file name")
                    .isTrue();
        }
        if (expectedData.get("file change") != null) {
            softAssertions.assertThat(
                           hasEmailReceivedBodyMessageWithXpath(expectedData.get("file change"), fileChangeXpath))
                    .as("Expected email does not contain correct file change")
                    .isTrue();
        }
        softAssertions.assertAll();
    }
}
