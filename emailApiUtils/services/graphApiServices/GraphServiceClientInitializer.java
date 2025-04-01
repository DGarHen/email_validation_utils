package utils.emailApiUtils.services.graphApiServices;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import common.constants.ISerenityConfConstants;
import utils.EncryptorUtils;
import utils.serenity.SerenityConfigReader;

public class GraphServiceClientInitializer {
    private static final String GRAPH_SCOPE = "https://graph.microsoft.com/.default";
    private static final String CLIENT_ID = EncryptorUtils.decrypt(SerenityConfigReader.getProperty(ISerenityConfConstants.GRAPH_API_CLIENT_ID));
    private static final String TENANT_ID = EncryptorUtils.decrypt(SerenityConfigReader.getProperty(ISerenityConfConstants.GRAPH_API_TENANT_ID));
    private static final String CLIENT_SECRET = EncryptorUtils.decrypt(SerenityConfigReader.getProperty(ISerenityConfConstants.GRAPH_API_CLIENT_SECRET));

    public static GraphServiceClient getGraphClientInstance() {
        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(CLIENT_ID)
                .tenantId(TENANT_ID)
                .clientSecret(CLIENT_SECRET)
                .build();
        return new GraphServiceClient(clientSecretCredential, GRAPH_SCOPE);
    }
}
