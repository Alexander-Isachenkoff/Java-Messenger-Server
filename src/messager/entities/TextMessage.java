package messager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class TextMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    @XmlAttribute
    private long id;

    @XmlElement
    @ManyToOne
    private User userFrom;

    @XmlAttribute
    private String message;

    @XmlAttribute
    private String dateTime;

    @ManyToOne
    private Dialog dialog;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> readBy;

    public TextMessage(User userFrom, String message, String dateTime, Dialog dialog) {
        this.userFrom = userFrom;
        this.message = message;
        this.dateTime = dateTime;
        this.dialog = dialog;
    }

}