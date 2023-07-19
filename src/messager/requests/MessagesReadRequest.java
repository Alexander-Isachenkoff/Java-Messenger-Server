package messager.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class MessagesReadRequest implements Request {
    @XmlElement
    private long userId;

    @XmlElementWrapper(name = "Messages")
    @XmlElement(name = "MessageId")
    private List<Long> readMessagesId;
}
