package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@Entity
@Getter
@NoArgsConstructor
@XmlRootElement
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    @XmlElement
    private long id;

    @Column(unique = true, nullable = false)
    @XmlElement
    private String login;

    @Column(unique = true, nullable = false)
    @XmlElement
    private String name;

    @Column(nullable = false)
    private int passwordHash;

    @XmlElement
    public String encodedImage;

    public User(String login, String name, String password) {
        this(login, name, password, null);
    }

    public User(String login, String name, String password, String encodedImage) {
        this.login = login;
        this.name = name;
        this.passwordHash = password.hashCode();
        this.encodedImage = encodedImage;
    }

}
