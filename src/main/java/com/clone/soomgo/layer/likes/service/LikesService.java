package com.clone.soomgo.layer.likes.service;

import com.clone.soomgo.layer.likes.model.Likes;
import com.clone.soomgo.layer.likes.repository.LikesRepository;

import com.clone.soomgo.layer.post.model.Post;
import com.clone.soomgo.layer.post.repository.PostRepository;
import com.clone.soomgo.layer.user.model.User;
import com.clone.soomgo.layer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> postLikes(Long userId, Long postId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("포스트가 없습니다"));
        Optional<Likes> Likefound = likesRepository.findByUserAndPost(user,post);
        if(Likefound.isPresent()){
            return new ResponseEntity<>("좋아요를 이미 하신 게시글입니다",HttpStatus.valueOf(400));
        }
        Likes like = new Likes(user, post);
        likesRepository.save(like);

        return new ResponseEntity<>("좋아요 등록 성공", HttpStatus.valueOf(200));

    }

    public ResponseEntity<?> deleteLikes(Long userId, Long postId) {
        List<Likes> likes = likesRepository.findByuserId(userId);
        for (Likes like : likes) {
            if (like.getPost().getId().equals(postId)) {
                likesRepository.deleteById(like.getId());
            }
        }
        return new ResponseEntity<>("좋아요 삭제 성공", HttpStatus.valueOf(200));

    }

}
