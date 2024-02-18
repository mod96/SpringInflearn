package jpashop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue @Column(name = "ORDER_ITEM_ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Long order;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Long item;
    private int orderPrice;
    private int count;
}
