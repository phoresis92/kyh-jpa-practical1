package tk.youngdk.jpashop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.domain.Member;
import tk.youngdk.jpashop.repository.MemberRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원가입")
    public void join() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(member.getId()));
    }

    @Test
    @DisplayName("중복 회원 예외")
    public void duplicateUserJoin() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setName("kim");

        Member memberB = new Member();
        memberB.setName("kim");

        //when
        memberService.join(memberA);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(memberB);
        });
    }
}