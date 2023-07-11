package messager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class TextMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    @XmlAttribute
    private Long id;

    @XmlElement
    @ManyToOne
    private User userFrom;

    @XmlAttribute
    private String message;

    @XmlAttribute
    private String dateTime;

    @ManyToOne
    @XmlElement
    private Dialog dialog;

    public TextMessage(User userFrom, String message, String dateTime, Dialog dialog) {
        this.userFrom = userFrom;
        this.message = message;
        this.dateTime = dateTime;
        this.dialog = dialog;
    }

}