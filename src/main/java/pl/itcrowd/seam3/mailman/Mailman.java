package pl.itcrowd.seam3.mailman;

import org.jboss.seam.jms.MessageManager;
import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.solder.core.Requires;
import pl.itcrowd.seam3.mailman.jaxb.EmailMessageMarshaller;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

@Requires({"org.jboss.seam.jms.MessageManager", "org.jboss.seam.mail.api.MailMessage"})
public class Mailman {
// ------------------------------ FIELDS ------------------------------

    private static final String EMAIL_ADDRESS_SEPARATOR = ";";

    @Inject
    private EmailMessageMarshaller emailMessageMarshaller;

    @Inject
    private Instance<MailMessage> mailMessageInstance;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private MessageManager messageManager;

    private String queueName;

// -------------------------- OTHER METHODS --------------------------

    public void receive(Message message) throws JMSException, JAXBException
    {
        final EmailMessage emailMessage = emailMessageMarshaller.unmarshal(((TextMessage) message).getText());
        sendInstantly(emailMessage);
    }

    public void send(EmailMessage emailMessage)
    {
        final String marshal;
        try {
            marshal = emailMessageMarshaller.marshal(emailMessage);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        final TextMessage message = messageManager.createTextMessage(marshal);
        messageManager.sendMessage(message, messageManager.lookupDestination(queueName));
    }

    public void sendInstantly(EmailMessage message)
    {
        final MailMessage mailMessage = mailMessageInstance.get();
        mailMessage.setEmailMessage(message);
        mailMessage.send();
    }
}
