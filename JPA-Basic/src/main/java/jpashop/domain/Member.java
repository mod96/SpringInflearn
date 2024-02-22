package jpashop.domain;

import jakarta.persistence.*;
import jpashop.domain.embedded.Address;
import jpashop.domain.embedded.Period;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long member;
    private String name;
    @Embedded
    private Period workPeriod;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city",
                    column = @Column(name = "WORK_CITY")
            ),
            @AttributeOverride(name = "street",
                    column = @Column(name = "WORK_STREET")
            ),
            @AttributeOverride(name = "zipcode",
                    column = @Column(name = "WORK_ZIPCODE")
            )
    })
    private Address workAddress;

    private String city;
    private String street;
    private String zipcode;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "FAVORITE_FOODS", joinColumns = @JoinColumn(name = "MEMBER_ID")) // 상대 테이블의 정보
    @Column(name = "FOOD_NAME") // 상대 테이블의 정보
    private Set<String> favoriteFoods = new HashSet<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID")) // 상대 테이블의 정보
    private List<Address> addressHistory = new ArrayList<>();
}
