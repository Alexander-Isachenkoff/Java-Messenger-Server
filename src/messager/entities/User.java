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
    private String name;

    @Column(nullable = false)
    private String password;

    @XmlElement
    public String encodedImage;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, String encodedImage) {
        this.name = name;
        this.password = password;
        this.encodedImage = encodedImage;
    }

}
