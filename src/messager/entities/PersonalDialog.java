package messager.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class PersonalDialog extends Dialog {

    @XmlElement
    @ManyToOne
    private User user1;

    @XmlElement
    @ManyToOne
    private User user2;

}
