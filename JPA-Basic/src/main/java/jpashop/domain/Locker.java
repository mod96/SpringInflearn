package jpashop.domain;

import jakarta.persistence.*;

@Entity
public class Locker {
    @Id @GeneratedValue @Column(name = "LOCKER_ID")
    private Long id;
    private String name;
    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)
    private Member member;
}
