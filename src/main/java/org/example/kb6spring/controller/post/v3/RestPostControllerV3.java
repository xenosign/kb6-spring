package org.example.kb6spring.controller.post.v3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.dto.post.PostDto;
import org.example.kb6spring.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/post/v3")
public class RestPostControllerV3 {
    private final PostService postService;

    @GetMapping("/list")
    public ResponseEntity<List<PostDto>> list(HttpServletRequest request) {
        log.info("=========> 게시글 목록 페이지 호출, {}", request.getRequestURI());

        List<PostDto> list = postService.findAll();

        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/test", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") int id) {
        log.info("=========> 게시글 삭제 기능 호출", "/post/v1/delete");

        try {
            int affectedRows = postService.delete(id);
            
            if (affectedRows == 0) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 대상을 찾을 수 없습니다");
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
        }
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

    @PostMapping("/new")
    public ResponseEntity<String> addPost(@RequestBody PostDto postDto) {
        log.info("=========> 게시글 추가 기능 호출, {}", postDto);
        
        return ResponseEntity.ok("게시글 추가 요청");
    }
}
