seam3-mailman
=============

Integration of seam3 JMS and mail.

If you want to assure your mail get's delivered despite temporary mail server problems use JMS. This library helps to integrate seam3 JMS and Mail modules.

Just include the seam3-mailman.jar into your classpath and configure destinations for JMSMailListener MDB ans Mailman:
>ejb-jar.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <ejb-jar version="3.0" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd">
        <enterprise-beans>
            <message-driven>
                <ejb-name>JMSMailListener</ejb-name>
                <ejb-class>pl.itcrowd.seam3.mailman.JMSMailListener</ejb-class>
                <activation-config>
                    <activation-config-property>
                        <activation-config-property-name>destination</activation-config-property-name>
                        <activation-config-property-value>queue/exampleAppMail</activation-config-property-value>
                    </activation-config-property>
                </activation-config>
            </message-driven>
        </enterprise-beans>    
    </ejb-jar>
>beans.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://java.sun.com/xml/ns/javaee" xmlns:s="urn:java:ee" xmlns:mailman="urn:java:pl.com.it_crowd.seam3.mailman"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://docs.jboss.org/cdi/beans_1_0.xsd">
        <mailman:Mailman queueName="queue/exampleAppMail">
            <s:modifies/>
        </mailman:Mailman>    
    </beans>

Suggested API usage:

    import org.jboss.seam.mail.api.MailMessage;
    import org.jboss.seam.mail.core.EmailAttachment;
    import org.jboss.seam.mail.core.EmailContact;
    import org.jboss.seam.mail.templating.freemarker.FreeMarkerTemplate;
    import org.jboss.solder.resourceLoader.ResourceProvider;
    import pl.itcrowd.seam3.mailman.Mailman;

    public class YourMailman {

        @Inject
        private ResourceProvider resourceProvider;
        @Inject
        private Mailman mailman;
        @Inject
        private Instance<MailMessage> mailMessage;

        public void send(String template, String subject, String fromEmail, Map<String, Object> context,
                                  Collection<EmailAttachment> attachements, String... toEmail)
        {
            final MailMessage message = mailMessage.get()
                .from(fromEmail)
                .to(toEmail)
                .subject(subject)
                .bodyHtml(new FreeMarkerTemplate(resourceProvider.loadResourceStream(template)))
                .put(context)
                .addAttachments(attachements);
            message.mergeTemplates();
            mailman.send(message.getEmailMessage());
        }
    }

**Rember that if you set subject, body text or html via templates you must invoke MailMessage.mergeTemplates() before sending it to mailman!**