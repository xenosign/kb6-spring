package org.example.kb6spring.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.kb6spring.dto.post.PostDto;

import java.util.List;

@Mapper
public interface PostMapper {
    List<PostDto> findAll();

    int delete(@Param("id") int id);

    List<PostDto> findByCond(@Param("title") String title, @Param("content") String content);

    void deleteAll();
    int saveForTest(PostDto post);
    PostDto findById(@Param("id") int id);
}
