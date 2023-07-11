package messager.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import messager.entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@XmlRootElement
public class DialogsListRequest {
    @XmlElement
    private User user;
}
