package pl.com.it_crowd.seam3.mailman;

import org.jboss.solder.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(name = "JMSMailListener", activationConfig = {@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Session-transacted")})
public class JMSMailListener implements MessageListener {
// ------------------------------ FIELDS ------------------------------

    @SuppressWarnings("EjbEnvironmentInspection")
    @Resource
    private MessageDrivenContext context;

    @Inject
    private Logger logger;

    @Inject
    private Mailman mailman;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface MessageListener ---------------------

    public void onMessage(Message message)
    {
        try {
            mailman.receive(message);
        } catch (Exception e) {
            context.setRollbackOnly();
            logger.error("Problem reading JMS message", e);
        }
    }
}
