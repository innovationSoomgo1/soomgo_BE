package com.clone.soomgo.layer.post.dto;

import com.clone.soomgo.layer.post.model.SubjectEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class PostsResponseDto {

    private Long postId;

    private SubjectEnum subject;

    private String title;

    private String content;

    private int likeCount;

    private int commentCount;

    private List<String> tagList;

    private List<String> imgUrlList;

    private Long createdAt;


}
