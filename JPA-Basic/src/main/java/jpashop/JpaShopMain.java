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
            em.createQuery("SELECT m FROM Member m where m.username=?1", Member.class).setParameter(1, "어쩌고");

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

