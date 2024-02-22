package jpashop.domain.cascade;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Child {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Parent parent;
}
