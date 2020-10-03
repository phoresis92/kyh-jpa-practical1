package tk.youngdk.jpashop.testMember.repository;

import org.springframework.stereotype.Repository;
import tk.youngdk.jpashop.testMember.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }


}
