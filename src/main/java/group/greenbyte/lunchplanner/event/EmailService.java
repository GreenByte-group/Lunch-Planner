package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.SessionManager;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailService {

    @Autowired
    private EmailProperties emailProps;

    @Autowired
    private UserLogic user;

    public EmailProperties getEmailProps() {
        return emailProps;
    }

    public void setEmailProps(EmailProperties emailProps) {
        this.emailProps = emailProps;
    }

    public void send(String emailTo, String subject, String body) throws HttpRequestException, Exception {

        System.out.println(emailProps.getHost());
        System.out.println("alle varianlen für Email: "+emailTo+", "+subject+", "+body);

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", emailProps.getHost());

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(emailProps.getFrom()));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }
//

}
