package tk.youngdk.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.youngdk.jpashop.domain.Address;
import tk.youngdk.jpashop.domain.Member;
import tk.youngdk.jpashop.service.MemberService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
/*
        엔티티를 외부에 노출시키지 말것
        엔티티를 외부 바인딩 파라미터로 조차 사용하지 말것
        엔티티의 변수명이 달라지면 api 스펙이 변경된다!

        id orders 등 외부에서 받지 말아야 할 값 또한 받아와질 수 있다!
*/
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        // 엔티티가 변경되어도 API 스펙이 일정하게 유지된다
        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(address);

        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
        private String city;
        private String street;
        private String zipcode;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long memberId) {
            this.id = memberId;
        }
    }

}
