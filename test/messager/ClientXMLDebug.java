package messager;

import messager.client.Client;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class ClientXMLDebug extends Client {

    public void post(Object object) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(object.getClass());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(object, System.out);
            System.out.println();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
