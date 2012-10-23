package pl.itcrowd.seam3.mailman.jaxb;

import com.sun.xml.bind.api.JAXBRIContext;
import org.jboss.jaxb.intros.IntroductionsAnnotationReader;
import org.jboss.jaxb.intros.IntroductionsConfigParser;
import org.jboss.jaxb.intros.configmodel.JaxbIntros;
import org.jboss.seam.mail.core.EmailMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;

@ApplicationScoped
public class EmailMessageMarshaller {
// ------------------------------ FIELDS ------------------------------

    private final Marshaller marshaller;

    private final Unmarshaller unmarshaller;

// --------------------------- CONSTRUCTORS ---------------------------

    public EmailMessageMarshaller() throws JAXBException
    {
        JaxbIntros config = IntroductionsConfigParser.parseConfig(getClass().getResourceAsStream("/jaxb-intros.xml"));
        IntroductionsAnnotationReader reader = new IntroductionsAnnotationReader(config);

        JAXBContext jaxbContext = JAXBContext.newInstance(new Class[]{EmailMessage.class}, Collections.singletonMap(JAXBRIContext.ANNOTATION_READER, reader));
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

// -------------------------- OTHER METHODS --------------------------

    public String marshal(EmailMessage message) throws JAXBException
    {
        final StringWriter writer = new StringWriter();
        marshaller.marshal(message, writer);
        return writer.toString();
    }

    public EmailMessage unmarshal(String string) throws JAXBException
    {
        return (EmailMessage) unmarshaller.unmarshal(new StringReader(string));
    }
}
