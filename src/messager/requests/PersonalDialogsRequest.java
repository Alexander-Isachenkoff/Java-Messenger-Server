package messager.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@XmlRootElement
public class PersonalDialogsRequest implements Request {
    @XmlElement
    private long userId;
}
