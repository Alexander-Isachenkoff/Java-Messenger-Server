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
    @Column(nullable = false)
    @XmlAttribute
    private Long id;

    @Column(unique = true, nullable = false)
    @XmlAttribute
    private String name;

    @Column(nullable = false)
    @XmlAttribute
    private String password;

//    @EqualsAndHashCode.Exclude
//    @ManyToMany
//    @XmlElementWrapper
//    @XmlElement
//    private List<Dialog> dialogs = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
