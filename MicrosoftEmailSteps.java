package steps.api.custom_api_steps.emails;

import io.cucumber.java.en.Then;
import org.junit.Assert;
import utils.DateTimeUtils;
import utils.emailApiUtils.EmailFacade;
import utils.emailApiUtils.messageTemplates.InvitationMessage;
import utils.emailApiUtils.messageTemplates.ProjectAndUserAlertMessage;
import utils.emailApiUtils.messageTemplates.PushAlertMessage;
import utils.emailApiUtils.services.entities.EmailServices;
import utils.storage.StorageUtils;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class MicrosoftEmailSteps {

    EmailFacade emailFacade = new EmailFacade(EmailServices.GRAPH_API);

    @Then("verify email was received by user {string} with the follow information")
    public void verify_email_received_by_user_with_the_follow_information(String emailAddress, Map<String, String> information) {
        OffsetDateTime currentUTCDate = DateTimeUtils.getCurrentUtcDateTime().truncatedTo(ChronoUnit.MINUTES);

        InvitationMessage invitationEmail = emailFacade.getEmailReceivedOnTimeWithReTry(emailAddress
                , information.get("subject")
                , currentUTCDate
                , 1
                , 30000l
                , InvitationMessage.class);

        Assert.assertNotNull(String.format("Email with subject: %s and current time wasn't found", information.get("subject"))
                , invitationEmail);

        invitationEmail.checkMessageContent(information);
    }

    @Then("verify email alert was received within {int} minutes by user {string} with the following information")
    public void verify_email_received_by_user_with_the_follow_information(int minutes, String emailAddress, Map<String, String> information) {
        Map<String, String> expectedData = StorageUtils.updateValueFromStorageAndConfFile(information);
        OffsetDateTime currentUTCDate = DateTimeUtils.getCurrentUtcDateTime().truncatedTo(ChronoUnit.MINUTES);

        ProjectAndUserAlertMessage projectAndUserAlertEmail = emailFacade.getEmailReceivedOnTimeWithReTry(emailAddress
                , information.get("subject")
                , currentUTCDate
                , minutes
                , 30000l
                , ProjectAndUserAlertMessage.class);

        Assert.assertNotNull(String.format("Email with subject: %s and current time wasn't found", information.get("subject")), projectAndUserAlertEmail);

        projectAndUserAlertEmail.checkMessageContent(expectedData);
    }

    @Then("verify email was received from push alert by user {string} with the follow information")
    public void verify_email_received_from_push_alert_by_user_with_the_follow_information(String emailAddress, Map<String, String> information) {
        Map<String, String> expectedData = StorageUtils.updateValueFromStorageAndConfFile(information);
        OffsetDateTime currentUTCDate = DateTimeUtils.getCurrentUtcDateTime().truncatedTo(ChronoUnit.MINUTES);

        PushAlertMessage pushAlertEmail = emailFacade.getEmailReceivedOnTimeWithReTry(emailAddress
                , information.get("subject")
                , currentUTCDate
                , 3
                , 40000l
                , PushAlertMessage.class);

        Assert.assertNotNull(String.format("Email with subject: %s and current time wasn't found", information.get("subject")), pushAlertEmail);

        pushAlertEmail.checkMessageContent(expectedData);
    }
}
