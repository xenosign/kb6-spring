package org.example.kb6spring.controller.member.v2;

import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.service.member.MemberServiceV1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/member/v2")
public class MemberControllerV2 {
    private final MemberServiceV1 memberService;
    public MemberControllerV2(MemberServiceV1 memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public String memberHome() {
        log.info("=======> '/member', member/index.jsp");
        return "member/index";
    }

    @GetMapping("/list")
    public String memberList(Model model) {
        model.addAttribute("memberList", memberService.getMemberList());
        return "member/list";
    }

    @GetMapping("/add")
    public String addMemberPage() {
        return "member/add";
    }

    @PostMapping("/add")
    public String addMember(@RequestParam("name") String name, @RequestParam("email") String email) {
        log.info("name={}, email={}", name, email);
        memberService.addMember(name, email);

        return "redirect:/member/list";
    }
}
