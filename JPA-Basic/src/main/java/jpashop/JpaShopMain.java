package jpashop;

import jakarta.persistence.*;
import jpashop.domain.Member;
import jpashop.domain.Order;
import jpashop.domain.Team;
import jpashop.domain.cascade.Child;
import jpashop.domain.cascade.Parent;
import jpashop.domain.embedded.Address;
import org.hibernate.Hibernate;

import java.util.List;

public class JpaShopMain {
    static EntityManagerFactory emf;

    static void createTeamMembers(EntityManager em) {
        Team teamA = new Team();
        teamA.setName("팀A");
        em.persist(teamA);

        Team teamB = new Team();
        teamB.setName("팀B");
        em.persist(teamB);

        Member member1 = new Member();
        member1.setName("회원1");
        member1.setTeam(teamA);
        em.persist(member1);

        Member member2 = new Member();
        member2.setName("회원2");
        member2.setTeam(teamA);
        em.persist(member2);

        Member member3 = new Member();
        member3.setName("회원3");
        member3.setTeam(teamB);
        em.persist(member3);

        Member member4 = new Member();
        member4.setName("회원4");
        member4.setTeam(teamB);
        em.persist(member4);
    }

    static void fetchJoin(EntityManager em) {
        createTeamMembers(em);
        em.flush();
        em.clear();

        String jpql = "select t from Team t join fetch t.members where t.name = '팀A'";
        List<Team> teams = em.createQuery(jpql, Team.class).getResultList();
        for(Team team : teams) {
            System.out.println("teamname = " + team.getName() + ", team = " + team);
            for (Member member : team.getMembers()) {
                //페치 조인으로 팀과 회원을 함께 조회해서 지연 로딩 발생 안함
                System.out.println("-> username = " + member.getName()+ ", member = " + member);
            }
        }
    }

    static void treatTest(EntityManager em) {
        em.createQuery("select b from Book b where b.author = 'kim'").getResultList();
    }

    static void bulkTest(EntityManager em) {
        createTeamMembers(em);
        em.createQuery("update Member m set m.name = 'bulkTest' where m.name like '회원%'").executeUpdate();
        for (Member member : em.createQuery("select m from Member m", Member.class).getResultList()) {
            System.out.println(member.getName());
        }
        em.clear();
        for (Member member : em.createQuery("select m from Member m", Member.class).getResultList()) {
            System.out.println(member.getName());
        }
    }
    static void test() {
        EntityManager em = emf.createEntityManager();
        // open transaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            bulkTest(em);

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

