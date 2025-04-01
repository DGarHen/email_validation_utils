package utils.emailApiUtils.messageTemplates;

import org.assertj.core.api.SoftAssertions;
import utils.emailApiUtils.messageTemplates.entity.MessageTemplate;

import java.util.Map;

public class PushAlertMessage extends MessageTemplate {
    private final String titleXpath = "//div[@class='email-body']//h3";
    private final String messageXpath = "//div[@class='email-body']/div[1]";

    public PushAlertMessage() {
        super();
    }

    @Override
    public void checkMessageContent(Map<String, String> expectedData) {
        SoftAssertions softAssertions = new SoftAssertions();
        if (expectedData.get("title") != null) {
            softAssertions.assertThat(
                            hasEmailReceivedBodyMessageWithXpath(expectedData.get("title"), titleXpath))
                    .as(String.format("Expected email does not contain the Title: %s", expectedData.get("title")))
                    .isTrue();
        }
        if (expectedData.get("message") != null) {
            softAssertions.assertThat(
                            hasEmailReceivedBodyMessageWithXpath(expectedData.get("title") + expectedData.get("message"), messageXpath))
                    .as(String.format("Expected email does not contain the Message: %s", expectedData.get("title") + expectedData.get("message")))
                    .isTrue();
        }
        softAssertions.assertAll();
    }
}
