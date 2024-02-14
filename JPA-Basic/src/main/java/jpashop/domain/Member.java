package jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Member {
    @Id @GeneratedValue @Column(name = "MEMBER_ID")
    private Long member;
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
