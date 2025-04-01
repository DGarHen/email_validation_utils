package utils.emailApiUtils.messageTemplates;

import org.assertj.core.api.SoftAssertions;
import utils.emailApiUtils.messageTemplates.entity.MessageTemplate;
import java.util.Map;
import java.util.function.BiFunction;

public class InvitationMessage extends MessageTemplate {
    private final String messageXpath = "//*[@id='email-message']//*[@class='email-text']";
    private final String signatureXpath = "//*[@id='email-signature']//*[@class='email-text']";
    private Map<String, BiFunction<String, String, Boolean>> validators = Map.of(
            "message", this::hasEmailReceivedBodyMessageWithXpath,
            "signature", this::hasEmailReceivedSignatureWithXpath
    );
    private Map<String, String> xpaths = Map.of(
            "message", messageXpath,
            "signature", signatureXpath
    );

    public InvitationMessage() {
        super();
    }

    @Override
    public void checkMessageContent(Map<String, String> expectedData) {
        SoftAssertions softAssertions = new SoftAssertions();
        expectedData.forEach((key, value) -> {
            if (value != null && validators.containsKey(key)) {
                boolean result = validators.get(key).apply(value, xpaths.get(key));
                softAssertions.assertThat(result)
                        .as(String.format("Expected email does not contain the %s: %s", key, value))
                        .isTrue();
            }
        });

        softAssertions.assertAll();
    }
}
