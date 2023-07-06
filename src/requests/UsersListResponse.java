package requests;

import entities.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class UsersListResponse {
    @XmlElementWrapper(name = "Users")
    @XmlElement(name = "User")
    public List<User> users;

    public UsersListResponse() {
    }

    public UsersListResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
