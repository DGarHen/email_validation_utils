package utils.emailApiUtils.services;

import utils.emailApiUtils.services.entities.EmailServices;
import utils.emailApiUtils.services.entities.IEmailService;
import utils.emailApiUtils.services.graphApiServices.GraphAPIService;
import utils.emailApiUtils.services.graphApiServices.GraphServiceClientInitializer;

public class EmailServiceFactory {

    public static IEmailService getEmailService(EmailServices serviceType) {
        switch (serviceType) {
            case GRAPH_API:
                return new GraphAPIService(GraphServiceClientInitializer.getGraphClientInstance());
            default:
                throw new IllegalArgumentException("Unsupported email service type: " + serviceType);
        }
    }
}
