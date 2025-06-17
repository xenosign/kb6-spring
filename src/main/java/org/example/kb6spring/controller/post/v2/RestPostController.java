package org.example.kb6spring.controller.post.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.dto.post.PostDto;
import org.example.kb6spring.service.post.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post/v2")
public class RestPostController {
    private final PostService postService;

    @GetMapping("/list")
    public List<PostDto> list(HttpServletRequest request) {
        log.info("=========> 게시글 목록 페이지 호출, {}", request.getRequestURI());

        return postService.findAll();
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
    public List<PostDto> postSearch(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             Model model) {
        log.info("==============> 게시글 검색 기능 호출, /post/v1/search");

        return postService.findByCond(title, content);
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








}
