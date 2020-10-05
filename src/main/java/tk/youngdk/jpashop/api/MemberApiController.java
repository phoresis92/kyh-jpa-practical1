package tk.youngdk.jpashop.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/api/v1/members/{id}")
    public UpdateMemberResponse updateMemberV1(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());

//        Member updateMember = memberService.update(id, request.getName(), address);
        /*
        커맨드랑 쿼리를 철저히 분리한다
        위에서 반환 받은 유저는 영속성이 끊긴다
        유저를 찾을거면 find로 다시 찾아오자
        */
        memberService.update(id, request.getName(), address);
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
        private String city;
        private String street;
        private String zipcode;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
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
