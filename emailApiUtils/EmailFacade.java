package utils.emailApiUtils;

import com.microsoft.graph.models.Message;
import utils.emailApiUtils.emailActions.EmailActions;
import utils.emailApiUtils.messageTemplates.entity.MessageTemplate;
import utils.emailApiUtils.services.entities.EmailServices;

import java.time.OffsetDateTime;
import java.util.List;

public class EmailFacade {

    private final EmailServices serviceType;
    private EmailActions emailActions;

    public EmailFacade(EmailServices serviceType) {
        this.serviceType = serviceType;
        emailActions = new EmailActions(this.serviceType);
    }

    /**
     * Method search and return the email that fulfill the expected subject and date received time
     *
     * @param emails         List of Message in which we are going to search
     * @param subject        target subject
     * @param currentUTCDate target received time
     * @param minusMinutes   amount of minutes for the lower bound of the timeframe from currentUTCDate
     * @param plusMinutes    amount of minutes for the upper bound of the timeframe from currentUTCDate
     * @return the Message that fulfill the conditions
     */
    public <T extends MessageTemplate> MessageTemplate getEmailReceivedOnTime(List emails, String subject, OffsetDateTime currentUTCDate, int minusMinutes, int plusMinutes, Class<T> templateClass) {
        Message email = emailActions.findEmailBySubject(emails, subject);

        if (email != null && wasEmailReceivedWithTimeValidation(email, currentUTCDate, minusMinutes, plusMinutes)) {
            try {
                T templateInstance = templateClass.getDeclaredConstructor().newInstance();
                templateInstance.setMessage(email);
                return templateInstance;
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate template class: " + templateClass.getSimpleName(), e);
            }
        } else return null;
    }

    /**
     * Asserts that an email with the specified subject exists in the provided list and
     * was received at the expected UTC time (within the current minute).
     *
     * @param email          target email that required the validation
     * @param currentUTCDate date in min that is the expected date
     * @param minusMinutes   amount of minutes for the lower bound of the timeframe
     * @param plusMinutes    amount of minutes for the upper bound of the timeframe
     * @return true if the email exists and was received at the expected time with a timeframe of minusMinutes and PlusMinutes, otherwise throws an AssertionError.
     */
    private boolean wasEmailReceivedWithTimeValidation(Message email, OffsetDateTime currentUTCDate, int minusMinutes, int plusMinutes) {
        return emailActions.assertEmailWasReceivedAtExpectedTimeWithMinuteRange(email, currentUTCDate, minusMinutes, plusMinutes);
    }

    /**
     * Bring the list of the top ten emails from the inbox of the email address
     *
     * @param emailAddress target email address from where I want to get the emails
     * @return List of emails recollected from email address' inbox.
     */
    public List fetchTopTenEmailsFrom(String emailAddress) {
        return emailActions.fetchTopEmails(emailAddress);
    }

    /**
     * Method to retry email list fetch until a time waiting to find an email subject
     */
    public <T extends MessageTemplate> T getEmailReceivedOnTimeWithReTry(String emailAddress, String subject, OffsetDateTime currentUTCDate, int limitTimeInMinutes, Long sleepTime, Class<T> templateClass) {
        long maxMilliseconds = (long) limitTimeInMinutes * 60 * 1000;
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (elapsedTime < maxMilliseconds) {
            List emails = fetchTopTenEmailsFrom(emailAddress);
            T email = (T) getEmailReceivedOnTime(emails, subject, currentUTCDate, 1, limitTimeInMinutes, templateClass);
            if (email != null) {
                return email;
            } else {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= maxMilliseconds) {
                    return null;
                }
            }
        }
        return null;
    }
}
