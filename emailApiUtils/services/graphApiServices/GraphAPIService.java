package utils.emailApiUtils.services.graphApiServices;

import com.microsoft.graph.models.Message;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;
import utils.emailApiUtils.services.entities.IEmailService;

import java.util.List;
import java.util.Objects;

@Slf4j
public class GraphAPIService implements IEmailService<Message> {

    private final GraphServiceClient graphClient;

    public GraphAPIService(GraphServiceClient client) {
        this.graphClient = client;
    }

    /**
     * This Method is able to bring the most recent 10 emails of an authorized user
     *
     * @param email : should be an authorized user, if is used a not existing or not authorized the method throws a not handle exception
     * @return list of Message which contains the top ten email of the account inbox
     */
    @Override
    public List<Message> getTopTenUserMessages(String email) {
        log.info("Fetching top ten messages for user {}", email);
        try {
            List<Message> emails = Objects.requireNonNull(
                            graphClient.users()
                                    .byUserId(email)
                                    .messages()
                                    .get())
                    .getValue();
            logMessagesDetails(emails);
            return emails;
        } catch (Exception e) {
            log.error("Error fetching messages for user {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to fetch messages", e);
        }
    }

    /**
     * Logs details of the messages retrieved.
     *
     * @param messages List of messages to log.
     */
    public void logMessagesDetails(List<Message> messages) {
        if (messages != null && !messages.isEmpty()) {
            log.info("Our Total Messages Is: {}", messages.size());
            messages.forEach(message -> {
                log.info("Subject: {}", message.getSubject());
                log.info("User: {}", message.getSender().getEmailAddress().getName());
                log.info("Date: {}", message.getReceivedDateTime());
                log.info("Contains: {}", message.getBody().getContent());
                log.info("-------------------------------------------");
            });
        } else {
            log.warn("No messages to display.");
        }
    }
}
