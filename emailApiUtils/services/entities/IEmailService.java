package utils.emailApiUtils.services.entities;

import java.util.List;

public interface IEmailService<T> {
    /**
     *
     * @param emailAddress String with a valid email
     * @return List of T class that should be the OBJECT related with an email, of list of email that this method recollects
     */
    List<T> getTopTenUserMessages(String emailAddress);
}
