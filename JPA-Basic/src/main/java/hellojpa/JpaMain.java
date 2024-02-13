package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {
    static EntityManagerFactory emf;

    static void makeData() {
        EntityManager em = emf.createEntityManager();
        // open transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(new Member(1L, "test1"));
            em.persist(new Member(2L, "test2"));
            em.persist(new Member(3L, "test3"));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    static void testIdentityComplex() {
        EntityManager em = emf.createEntityManager();
        // open transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
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

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory("hello");

        makeData();
        testIdentityComplex();

        emf.close();
    }
}
