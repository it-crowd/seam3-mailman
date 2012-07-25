package pl.com.it_crowd.seam3.mailman;

import org.jboss.solder.core.Requires;
import org.jboss.solder.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@Requires({"pl.com.it_crowd.seam3.mailman.Mailman", "org.jboss.seam.jms.MessageManager", "org.jboss.seam.mail.api.MailMessage"})
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
