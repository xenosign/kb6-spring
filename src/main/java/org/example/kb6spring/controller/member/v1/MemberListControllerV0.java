package org.example.kb6spring.controller.member.v1;

import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.service.member.MemberServiceV0;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MemberListControllerV0 {
    private final MemberServiceV0 memberService = MemberServiceV0.getInstance();

    @GetMapping("/member/v0/list")
    public String memberList(Model model) {
        model.addAttribute("memberList", memberService.getMemberList());
        return "member/list";
    }
}
