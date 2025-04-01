package utils.emailApiUtils.emailActions;

import com.microsoft.graph.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import utils.emailApiUtils.services.entities.EmailServices;
import utils.emailApiUtils.services.EmailServiceFactory;
import utils.emailApiUtils.services.entities.IEmailService;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class EmailActions {

    private final IEmailService emailService;

    public EmailActions(EmailServices serviceType) {
         this.emailService = EmailServiceFactory.getEmailService(serviceType);
    }

    /**
     * Retrieves the top N emails for a user.
     *
     * @param emailAddress User's email address.
     * @return List of email messages.
     */
    public List fetchTopEmails(String emailAddress) {
        return emailService.getTopTenUserMessages(emailAddress);
    }

    /**
     * Asserts that email was received at certain date/time within a range of minutes.
     *
     * @param email           email that was received.
     * @param expectedDate    time that you expect to find in the email.
     * @param minusMinutes    amount of minutes for the lower bound of the timeframe
     * @param plusMinutes     amount of minutes for the upper bound of the timeframe
     */
    public boolean assertEmailWasReceivedAtExpectedTimeWithMinuteRange(Message email, OffsetDateTime expectedDate, int minusMinutes, int plusMinutes) {
        OffsetDateTime receivedTime = email.getReceivedDateTime().truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime lowerBound = expectedDate.minusMinutes(minusMinutes);
        OffsetDateTime upperBound = expectedDate.plusMinutes(plusMinutes);

        return !receivedTime.isBefore(lowerBound) && !receivedTime.isAfter(upperBound);
    }

    /**
     * Finds and returns the first email with the specified subject from the provided list.
     *
     * @param emails  The list of email messages to search within.
     * @param subject The subject to search for in the email messages.
     * @return The first email that matches the subject, or null if no match is found.
     */
    public Message findEmailBySubject(List<Message> emails, String subject) {
        return emails.stream()
                .filter(email -> StringUtils.containsIgnoreCase(email.getSubject(), subject))
                .findFirst()
                .orElse(null);
    }

}
