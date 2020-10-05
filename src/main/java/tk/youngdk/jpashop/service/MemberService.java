package tk.youngdk.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.domain.Address;
import tk.youngdk.jpashop.domain.Member;
import tk.youngdk.jpashop.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    @Transactional
    public Long join(Member member){

        validateDuplicateMember(member);

        memberRepository.save(member);

        return member.getId();

    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByName(member.getName());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name, Address address) {
        Member findMember = memberRepository.findOne(id);
        findMember.update(name, address);
    }
}
