package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor
@XmlRootElement
public class CommandDialog extends Dialog {

    @XmlElement
    private String name;

    @XmlElement
    private String encodedImage;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private final List<User> users = new ArrayList<>();

    public boolean hasUser(long userId) {
        return getUsers().stream().map(User::getId).anyMatch(id -> id == userId);
    }

}
