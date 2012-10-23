package pl.itcrowd.seam3.mailman;

import junit.framework.Assert;
import org.jboss.seam.mail.attachments.BaseAttachment;
import org.jboss.seam.mail.attachments.FileAttachment;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import org.junit.Test;
import pl.itcrowd.seam3.mailman.jaxb.EmailMessageMarshaller;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class MailmanTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void test() throws IOException, AddressException, JAXBException
    {
        final EmailMessageMarshaller marshaller = new EmailMessageMarshaller();

        final EmailMessage message = new EmailMessage();
        message.addAttachment(new BaseAttachment("f.txt", "text", ContentDisposition.ATTACHMENT, "Hello".getBytes()));
        message.addAttachment(new FileAttachment(ContentDisposition.INLINE, new File("target/test-classes/sampleAttachment.txt")));
        final String textBody = "Some body text";
        message.setTextBody(textBody);
        final String htmlBody = "<h1>Some body text</h1>";
        message.setHtmlBody(htmlBody);
        final Header header = new Header("Content-type", "text/Blues");
        message.addHeader(header);
        final InternetAddress bccAddress = new InternetAddress("s4237@example.com");
        message.addBccAddress(bccAddress);
        final InternetAddress bccAddress1 = new InternetAddress("bernard@example.com", "Bernard Labno");
        message.addBccAddress(bccAddress1);
        final InternetAddress toAddress = new InternetAddress("tadeusz@example.com");
        message.addToAddress(toAddress);
        final String xml = marshaller.marshal(message);
        final EmailMessage unmarshalledMessage = marshaller.unmarshal(xml);
        Assert.assertEquals(textBody, unmarshalledMessage.getTextBody());
        Assert.assertEquals(htmlBody, unmarshalledMessage.getHtmlBody());
        Assert.assertTrue(unmarshalledMessage.getHeaders().contains(header));
        Assert.assertTrue(unmarshalledMessage.getBccAddresses().contains(bccAddress));
        Assert.assertTrue(unmarshalledMessage.getBccAddresses().contains(bccAddress1));
        Assert.assertTrue(unmarshalledMessage.getToAddresses().contains(toAddress));
    }
}
