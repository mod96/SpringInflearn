package jpashop;

import jakarta.persistence.*;
import jpashop.domain.Member;
import jpashop.domain.Order;

public class JpaShopMain {
    static EntityManagerFactory emf;
    static void test() {
        EntityManager em = emf.createEntityManager();
        // open transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member1 = new Member();
            em.persist(member1);

            Order order1 = new Order();
            order1.changeMember(member1);
            em.persist(order1);

            System.out.println(member1.getOrders().size()); // 1

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
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

