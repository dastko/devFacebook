package helpers;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import play.Play;

/**
 * Created by dastko on 8/26/15.
 */
public class SendEmail {

    public static void send(String email, String message) {

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setSubject("devFacebook Welcome");
            mail.setFrom("devFacebook <devfacebook@gmail.com>");
            mail.addTo("Contact <devfacebook@gmail.com>");
            mail.addTo(email);
            mail.setMsg(message);
            mail.setHtmlMsg(String
                    .format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>",
                            "Thanks for signing up to devFacebook!",
                            "We wish you a pleasant chat time :).", message));
            mail.setHostName("smtp.gmail.com");
            mail.setStartTLSEnabled(true);
            mail.setSSLOnConnect(true);
            mail.setAuthenticator(new DefaultAuthenticator(
                    Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
                    Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
            ));
            mail.send();
        } catch (EmailException e) {

        }
    }
}