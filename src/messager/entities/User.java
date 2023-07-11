package messager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@Entity
@Getter
@NoArgsConstructor
@XmlRootElement
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @XmlAttribute
    private Long id;

    @Column(unique = true)
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
