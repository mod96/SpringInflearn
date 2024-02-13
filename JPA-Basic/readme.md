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








