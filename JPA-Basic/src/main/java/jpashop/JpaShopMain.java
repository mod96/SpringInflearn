package jpashop;

import jakarta.persistence.*;
import jpashop.domain.Member;
import jpashop.domain.Order;
import jpashop.domain.cascade.Child;
import jpashop.domain.cascade.Parent;
import jpashop.domain.embedded.Address;
import org.hibernate.Hibernate;

public class JpaShopMain {
    static EntityManagerFactory emf;
    static void test() {
        EntityManager em = emf.createEntityManager();
        // open transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member1 = new Member();
            member1.getAddressHistory().add(new Address("city", "street", "zipcode"));
            member1.getAddressHistory().add(new Address("city", "street", "zipcode"));

            Member member2 = new Member();
            member2.getAddressHistory().add(new Address("city", "street", "zipcode"));
            member2.getAddressHistory().add(new Address("city", "street", "zipcode"));

            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            em.find(Member.class, member1.getMember()).getAddressHistory().remove(0);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory("hello");

        test();

        emf.close();
    }
}

