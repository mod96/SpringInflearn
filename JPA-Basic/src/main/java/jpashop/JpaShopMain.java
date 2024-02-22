package jpashop;

import jakarta.persistence.*;
import jpashop.domain.Member;
import jpashop.domain.Order;
import jpashop.domain.cascade.Child;
import jpashop.domain.cascade.Parent;
import org.hibernate.Hibernate;

public class JpaShopMain {
    static EntityManagerFactory emf;
    static void test() {
        EntityManager em = emf.createEntityManager();
        // open transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Parent parent = new Parent();
            parent.setName("a");
            em.persist(parent);

            Child child1 = new Child();
            child1.setName("c1");
            Child child2 = new Child();
            child2.setName("c2");

            parent.addChild(child1);
            parent.addChild(child2);

            em.flush();
            em.clear();

            Parent parent1 = em.find(Parent.class, parent.getId());
            for (Child child : parent1.getChildList()) {
                System.out.println(child.getName());
            }

            parent1.getChildList().remove(0);

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

