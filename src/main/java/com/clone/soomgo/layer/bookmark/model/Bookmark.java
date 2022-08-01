package com.clone.soomgo.layer.bookmark.model;

import com.clone.soomgo.layer.post.model.Post;
import com.clone.soomgo.layer.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Bookmark {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "BOOKMARK_ID", nullable = false)
        private Long id;


        @ManyToOne
        @JoinColumn(name = "POST_ID", nullable = false)
        private Post post;

        @ManyToOne
        @JoinColumn(name = "USER_ID", nullable = false)
        private User user;

        public Bookmark(User user, Post post) {
            this.user = user;
            this.post = post;
        }
    }



