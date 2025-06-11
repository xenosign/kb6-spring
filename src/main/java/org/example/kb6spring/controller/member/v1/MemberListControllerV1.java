package org.example.kb6spring.controller.member.v1;

import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.service.member.MemberServiceV1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MemberListControllerV1 {
    private final MemberServiceV1 memberService;

    public MemberListControllerV1(MemberServiceV1 memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/list")
    public String memberList(Model model) {
        model.addAttribute("memberList", memberService.getMemberList());
        return "member/list";
    }
}
