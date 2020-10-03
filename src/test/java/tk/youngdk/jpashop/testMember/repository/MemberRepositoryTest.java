package tk.youngdk.jpashop.testMember.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.testMember.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 저장")
    @Transactional
    @Rollback(false)
    public void save() throws Exception {
        //given
        Member member = new Member();
        member.setUserName("memberA");
        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());

        assertThat(findMember).isSameAs(member);

        System.out.println("findMember = " + findMember);
        System.out.println("member = " + member);

    }



    @Test
    @DisplayName("멤버 찾기")
    void find() {
    }
}