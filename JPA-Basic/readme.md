# Section 2: JPA 시작하기

기본 type 과 setting
```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
// open transaction
EntityTransaction tx = em.getTransaction();
tx.begin();
try {
    Member member = new Member();
    member.setId(2L);
    member.setName("asd");
    em.persist(member);

    tx.commit();
} catch (Exception e) {
    tx.rollback();
} finally {
    em.close();
}
emf.close();
```

변경 저장
```java
Member m = em.find(Member.class, 2L);
m.setName("changedName"); // doesn't have to em.persist (persistence context)

tx.commit();
```

JPQL
```java
List<Member> res = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();
res.forEach(m -> System.out.println(m.getName()));
```

# Section 3: 영속성 관리

<img width="70%" src="imgs/em_persist.png" />

- 비영속(new/transient): 영속성 컨텍스트와 관련 없음
- 영속(managed): 영속성 컨텍스트에 관리되는 상태 (`em.persist(member);`)
- 준영속(detached): 영속성 컨텍스트에 저장되었다가 분리된 상태 (`em.detach(member);`) => db 반영 안하고 객체만 변경하기 위함
- 삭제(removed): 삭제된 상태 (`em.remove(member);`)

조회 쿼리는 1차 캐시에서 받거나 없으면 db에서 받아온다. 업데이트 쿼리는 commit 하고 나서 한번에 날라간다.

```java
em.persist(memberA);
```

<img width="70%" src="imgs/em_persist_func.png" />

```java
tx.commit(); // this makes em.flush
```

<img width="70%" src="imgs/tx_commit_func.png" />

```java
em.flush();
```

<img width="70%" src="imgs/em_flush_func.png" />

이에 따라 새로 객체를 만들지 않는 이상(즉, persistence context에서 관리되는 객체라면) `em.persist`를 할 필요가 없다. 쿼리는 다음 3가지 경우에 날라간다.
- `em.flush();` <= 영속성 컨텍스트를 비우지 않는다.
- `tx.commit();`
- JPQL 쿼리 실행


영속성 컨텍스트를 사용함으로서 다음의 장점이 있다.
- 1차 캐시
- 동일성(identity) 보장 (쿼리가 달라도, 같은 record에 대해 결과값 메모리 값이 같다.)
- 트랜잭션을 지원하는 쓰기 지연 (transactional write-behind)
- 변경 감지(dirty checking)
- 지연 로딩(lazy loading)

### 동일성 보장에 대해

쿼리 달라도 됨. 레코드가 같으면 메모리 같음.

```java
List<Member> res = em.createQuery("select m from Member as m where m.name like concat(:k1, '%')", Member.class)
        .setParameter("k1", "test")
        .getResultList();
List<Member> res2 = em.createQuery("select m from Member as m where m.id = 1", Member.class)
        .getResultList();
res.forEach(m -> {
    System.out.println(m.getName());
    if (m.getId() == 1) {
        System.out.println("Are the same = " + (m == res2.get(0))); // true
    }
});
```

# Section 4: 엔티티 매핑

- `@Entity`

JPA가 관리하는 객체이다. 기본 생성자가 필요하다. 
`name` parameter는 JPA에서 사용되는 엔티티 이름이다. 기본값은 클래스 이름과 같다.

- `@Table`

엔티티와 매핑할 테이블을 지정한다.
`name` parameter는 매핑할 테이블 이름이다. 기본값은 엔티티 이름이다. 
`catalog` parameter는 데이터베이스 catalog를 매핑한다.
`schema` parameter는 데이터베이스 schema를 매핑한다.
`uniqueConstraints` parameter는 DDL 생성 시에 유니크 제약 조건을 생성한다.

- `@Column`

필드와 컬럼을 매핑한다. 제약조건을 추가할 수 있다. 단, 제약조건은 ddl 생성 시에만 적용되며 application 로직에는 반영되지 않는다.  

- `hibernate.hbm2ddl.auto`

create(drop->create) / create-drop(drop->create->drop) / update(필드 삭제는 안먹음) / validate(확인기능) / none

ddl: 데이터베이스 구조 정의에 사용하는 언어. 생성된 ddl은 운영에서 사용하지 않거나 다듬어 사용해야 한다.

**주의** : 개발 초기에 create/update, 테스트 서버에서 update/validate, 스테이징과 운영은 validate/none

### 필드 매핑 annotation 들

```java
@Id
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
```
위 상태에서 ddl create시:
```sql
create table Member (
    age integer,
    testLocalDate date,
    createDate timestamp(6),
    id bigint not null,
    lastModifiedDate timestamp(6),
    testLocalDateTime timestamp(6),
    name varchar(255),
    roleType varchar(255) check (roleType in ('NORMAL','VIP')),
    description clob,
    primary key (id)
)
```

#### @Column

왠만하면 `name` 직접 적어놓자. spring boot 관례랑 회사 관례랑 다를 수도 있다.

<img width="70%" src="imgs/column.png" />

#### @Enumerated

<img width="70%" src="imgs/enumerated.png" />

#### @Temporal

<img width="70%" src="imgs/temporal.png" />

#### @Lob

지정할 수 있는 속성이 없다. 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB 매핑.

#### @Transient

데이터베이스에 저장 및 조회 안됨. 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용한다.



### 기본 키 매핑

직접 할당하려면 `@Id` 만 사용한다. 주로 자동 생성하기에 `@GeneratedValue`를 함께 사용한다. 여기서 `strategy` parameter를 지정한다. 아래의 4가지가 있다.

기본 키는 non-null, unique, not be changed 조건을 갖는다. 비즈니스와 연관된 키를 사용하면 변할 확률이 높다. Long + 대체키 + 생성(uuid 등) 전략을 사용하자.

#### IDENTITY
데이터베이스에 위임 (MYSQL, POSTGRESQL, SQL SERVER, DB2)

데이터베이스에 insert 문을 넣고난 후에야 id를 알 수 있다. 이 때문에 `em.persist(instance);` 하는 시점에 insert 쿼리가 날라간다. (안 그러면 영속성 컨텍스트에서 어떻게 관리하겠는가?) db driver 안에 insert 후 id를 리턴하도록 되어있기 때문에 select가 날라가진 않는다.  

#### SEQUENCE
데이터베이스 시퀀스 사용 (ORACLE, POSTGRESQL, DB2, H2) - `@SequenceGenerator` 필요

```java
@Entity
@SequenceGenerator(
    name = "MEMBER_SEQ_GENERATOR",
    sequenceName = "MEMBER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
    initialValue = 1, allocationSize = 1)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id; 
}
```

`@SequenceGenerator` 의 속성들

<img width="70%" src="imgs/sequence_generator.png" />

만약 `allocationSize=1`인 상태로 여러 새로운 객체들을 `em.persist` 하면 계속 db와 통신하며 sequence값을 받아야 한다. 하지만 `allocationSize=50` 으로 하면 미리 받아와서 쓴다. 50~100 정도가 적절하다.

한 번 할당된 sequence 범위는 메모리 어딘가 저장되는 듯. 트랜잭션 닫고 다시 해도 순서 유지됨. 단, ddl 끄고 어플리케이션을 아예 다시 시작하면 52번부터 시작함.

```java
public static void main(String[] args) {
    emf = Persistence.createEntityManagerFactory("hello");

    makeDataDDL();
    makeDataDDL();
    makeDataDDL();

    emf.close();
}
```
```
Hibernate: 
    select
        next value for MEMBER_SEQ
Id: 52
Id: 53
```


#### TABLE
키 생성용 테이블 사용 (모든 db) - `@TableGenerator` 필요

```java
@Entity
@TableGenerator(
    name = "MEMBER_SEQ_GENERATOR",
    table = "MY_SEQUENCES",
    pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id; 
}
```

```sql
create table MY_SEQUENCES (
    sequence_name varchar(255) not null,
    next_val bigint,
    primary key ( sequence_name )
)
```

위와 같이 그냥 테이블을 사용하므로 모든 데이터베이스에 적용 가능하나, 성능 단점이 있다.

`@TableGenerator` 속성들

<img width="70%" src="imgs/table_generator.png" />

#### (기본값)AUTO
방언에 따라 자동 지정


# Section 5: 연관관계 매핑 기초

- 방향(Direction): 단방향, 양방향
- 다중성(Multiplicity): ManyToOne, OneToMany, OneToOne, ManyToMany
- 연관관계 주인(Owner)

테이블은 외래 키 조인을 통해 연관을 처리하므로 모든 것이 양방향이다. 
하지만 객체는 참조를 사용해서 연관을 처리하므로 단방향 매핑만 존재하며 이를 양쪽에서 사용하면 양방향 매핑이 된다. 이 때문에 관계의 주인이 생기게 되며 양방향 매핑은 고려할 게 많기 때문에 지양해야 한다. 

## 단방향 매핑

외래 키를 가진 애가 연관관계의 주인을 갖게 해야 한다. 비즈니스 로직으로 선택하면 안된다.

```java
public class Order {
    //    @Column(name = "MEMBER_ID")
    //    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // column을 써준다.
    private Member member;
}
```

## 양방향 매핑

지양하자. 처음에 설계할 때는 단방향만 하고 JPQL이 복잡해진다면 그 때 추가하자. (양방향은 추가해도 테이블에 영향을 안준다.)

```java
public class Member {
    @Id @GeneratedValue @Column(name = "MEMBER_ID")
    private Long member;
    @OneToMany(mappedBy = "member") // 상대편의 field를 써준다.
    private List<Order> orders = new ArrayList<>(); // null ptr exception 방지용
}
```

`mappedBy` 를 사용하면 반대쪽에서 관계의 주인이라는 뜻이다. 
관계의 주인을 제외하고는 읽기만 된다. 단, 객체지향 관점에서 다음과 같은 일이 벌어지지 않도록 하려면:

```java
Member member1 = new Member();
em.persist(member1);

Order order1 = new Order();
order1.setMember(member1);
em.persist(order1);

System.out.println(member1.getOrders().size()); // 0
```

`member1` 를 가진 `order1` 을 만들었지만 `member1` 객체의 `orders` 에는 반영되지 않았다. (`setter`는 java 기본 규칙이 적용되는 method 이므로) `change` 라는 편의 method를 정의하자:

```java
public class Order {
    public void changeMember(Member member) {
        // 있던 것 삭제해주는 로직도 필요
        this.member = member;
        member.getOrders().add(this);
    }
}
```

편의 메소드 생성 시 무한 루프를 조심하자. (e.g. toString(), lombok, JSON 생성 라이브러리)
또한 위에서는 관계의 주인인 `order` 에 편의 메소드를 넣었지만, `member` 에 넣을 수도 있다.
단, 편의 메소드는 한 쪽에만 넣어야 나중에 헷갈리지 않는다. 설계 시 정하고 들어가자.

* 엔티티가 바뀔 것을 대비하여 controller에서는 절대 엔티티를 반환하지 말자. dto 사용하자.





























