package utils.emailApiUtils.messageTemplates.entity;

import com.microsoft.graph.models.Message;
import utils.HtmlUtils;
import java.util.List;
import java.util.Map;

public abstract class MessageTemplate {

    private Message message;

    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     *
     * @param expectedData Map<String, String> with the expected information in the email
     * Checks if the expected information is present in the email
     */
    public abstract void checkMessageContent(Map<String, String> expectedData);

    /**
     * Asserts that an email has an expected message contains in its body message
     *
     * @param xpath   xpath to locate the target message into the html body
     * @param messageContent message expected to be found in the body of the email
     * @return true if the email contains the message
     */
    protected boolean hasEmailReceivedBodyMessageWithXpath(String messageContent, String xpath) {
        List<String> fullBodyEmailTexts = getAllTextContentsForHtmlFromAnXpath(xpath);
        String bodyContents = String.join(" ", fullBodyEmailTexts).toLowerCase();
        return bodyContents.toLowerCase().contains(messageContent.toLowerCase());
    }

    /**
     * This method used the same logic as body message, due to our emails don't separate the signature in footer
     *
     * @param expectedSignature message we expect to find in the signature of the email
     * @return true if the signature was found.
     */
    protected boolean hasEmailReceivedSignatureWithXpath(String expectedSignature, String xpath) {
        List<String> fullBodyEmailTexts = getAllTextContentsForHtmlFromAnXpath(xpath);
        String actualSignature = String.join(" ", fullBodyEmailTexts).toLowerCase();
        return actualSignature.toLowerCase().contains(expectedSignature.toLowerCase());
    }

    /**
     * Retrieves all text contents from elements with the specified HTML class within an email's body.
     *
     * @param xpath xpath of the HTML elements to extract text from
     * @return a list of strings, each representing the text content of an element with the specified xpath
     */
    protected List<String> getAllTextContentsForHtmlFromAnXpath(String xpath) {
        String body = getTheEmailBodyContent();
        return HtmlUtils.extractTextByXpath(body, xpath);
    }

    /**
     * Bring the body of the email
     *
     * @return String body of the email not formatted
     */
    protected String getTheEmailBodyContent() {
        return message.getBody().getContent();
    }
}
