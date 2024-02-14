package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "MemberTest")
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 50)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(nullable = false)
    private Long id;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING) // enum
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP) // 날짜
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP) // 날짜
    private Date lastModifiedDate;
    @Lob // BLOB, CLOB
    private String description;
    @Transient // 매핑 무시
    private int notField;
    private LocalDate testLocalDate; // Temporal 안써도 됨
    private LocalDateTime testLocalDateTime; // Temporal 안써도 됨

    public Member() {}

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNotField() {
        return notField;
    }

    public void setNotField(int notField) {
        this.notField = notField;
    }
}
