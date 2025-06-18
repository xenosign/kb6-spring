package org.example.kb6spring.controller.post.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.service.post.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post/v1")
public class PostController {
    private final PostService postService;

    @GetMapping("/list")
    public String list(Model model) {
        log.info("=========> 게시글 목록 페이지 호출", "/post/v1/list");

        model.addAttribute("postList", postService.findAll());
        return "post/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        log.info("=========> 게시글 삭제 기능 호출", "/post/v1/delete");

        int affectedRows = postService.delete(id);

        if (affectedRows == 0) {
            log.error("삭제 실패");
        }

        return "redirect:/post/v1/list";
    }

    @GetMapping("/search")
    public String postSearch(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             Model model) {
        log.info("==============> 게시글 검색 기능 호출, /post/v1/search");

        model.addAttribute("postList", postService.findByCond(title, content));

        return "post/list";
    }

    @GetMapping("/compare")
    public String compare(Model model) {
        log.info("==================> DB 비교 기능 호출, /post/v1/compare");

        int count = 1000;

        postService.resetAndGeneratePosts(count);

        model.addAttribute("count", count);
        model.addAttribute("mysqlTime", postService.testMysqlReadTime(count));
        model.addAttribute("redisTime", postService.testRedisReadTime(count));

        return "post/compare";
    }

    @GetMapping("/error")
    public void error(Model model) {
        throw new RuntimeException("의도적으로 발생 시킨 예외");
    }
}
