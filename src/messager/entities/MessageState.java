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
public class MessageState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    @XmlAttribute
    private long id;

    @ManyToOne
    private TextMessage textMessage;

    @ManyToOne
    private User user;

    public MessageState(TextMessage textMessage, User user) {
        this.textMessage = textMessage;
        this.user = user;
    }
}
