package pl.com.it_crowd.seam3.mailman.jaxb;

import org.jboss.seam.mail.core.Header;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class HeaderAdapter extends XmlAdapter<AdaptedHeader, Header> {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public AdaptedHeader marshal(Header header) throws Exception
    {
        AdaptedHeader adaptedHeader = new AdaptedHeader();
        adaptedHeader.setName(header.getName());
        adaptedHeader.setValue(header.getValue());
        return adaptedHeader;
    }

    @Override
    public Header unmarshal(AdaptedHeader adaptedHeader) throws Exception
    {
        return new Header(adaptedHeader.getName(), adaptedHeader.getValue());
    }
}
