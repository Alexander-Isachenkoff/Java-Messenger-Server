package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@EqualsAndHashCode
@Entity
@Getter
@NoArgsConstructor
@XmlRootElement
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    @XmlAttribute
    private Long id;

    @XmlAttribute
    private String name;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    private List<TextMessage> messages;

    public Dialog(List<User> users) {
        this.users = users;
    }

    public Dialog(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }

}
