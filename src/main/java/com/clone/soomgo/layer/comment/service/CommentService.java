package com.clone.soomgo.layer.comment.service;


import com.clone.soomgo.layer.comment.dto.request.CommentRequestDto;
import com.clone.soomgo.layer.comment.dto.response.CommentResponseDto;
import com.clone.soomgo.layer.comment.model.Comment;
import com.clone.soomgo.layer.comment.repository.CommentRepository;
import com.clone.soomgo.layer.post.model.Post;
import com.clone.soomgo.layer.post.repository.PostRepository;
import com.clone.soomgo.layer.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    public ResponseEntity<?> showPostComment(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new NullPointerException("글이 없습니다."));

        List<Comment> comments = commentRepository.findAllByPost(post);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for(Comment comment : comments) {
            commentResponseDtoList.add(new CommentResponseDto(comment.getId(),comment.getUser().getUsername(),comment.getContent(),comment.getCreatedAt()));
        }

        return new ResponseEntity<>(commentResponseDtoList, HttpStatus.valueOf(200));
    }

    public ResponseEntity<?> registerComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Post post = postRepository.findById(postId).get();

        Comment comment = new Comment(post,user,commentRequestDto.getContent());

        commentRepository.save(comment);

        return new ResponseEntity<>("성공적으로 댓글이 작성되었습니다.",HttpStatus.valueOf(200));
    }

    public ResponseEntity<?> deleteComment(Long commentId,User user) {
        Comment comment = commentRepository.findById(commentId).get();

        if(comment.getUser().getId().equals(user.getId()))
            commentRepository.deleteById(commentId);
        else
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");

        return new ResponseEntity<>("성공적으로 댓글이 삭제되었습니다.",HttpStatus.valueOf(200));
    }
}