package com.clone.soomgo.layer.post.repository;

import com.clone.soomgo.layer.post.model.Post;
import com.clone.soomgo.layer.post.model.SubjectEnum;
import com.clone.soomgo.layer.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from  Post  p where p.subject NOT IN :subject ORDER BY p.id DESC ")
    Slice<Post> findAllByOrderByIdDesc(SubjectEnum subject,Pageable pageable); //subject가 KNOWHOW인 포스트를 제외한 모든 포스트를 보여주는 페이지네이션 처음 부분
    @Query("select p from  Post  p where p.subject NOT IN :subject AND p.id<:id ORDER BY p.id DESC ")
    Slice<Post> findByIdLessThanOrderByIdDesc(Long id,SubjectEnum subject, Pageable pageable); //subject가 KNOWHOW인 포스트를 제외한 모든 포스트를 보여주는 페이지네이션 후 부분

    Slice<Post> findAllBySubjectOrderByIdDesc(SubjectEnum subject, Pageable pageable); // 카테고리가 subject 포스트를 보여주는 페이지네이션 처음 부분

    Slice<Post> findByIdLessThanAndSubjectOrderByIdDesc(Long id,SubjectEnum subject, Pageable pageable); // 카테고리가 subject 포스트를 보여주는 페이지네이션 후 부분


    @Query("select p from Post p where  p.subject NOT IN :subject AND (p.title LIKE %:title% Or p.tags LIKE %:tags% Or p.content LIKE %:content%) ORDER BY p.id DESC ")
    Slice<Post> findAllBySearchKeyword(String title,String content,String tags,SubjectEnum subject ,Pageable pageable); // 제목,내용,태그에 하나라도 keyword가 들어간 포스트를 보여주는 페이지네이션 첫 부분


    @Query("select p from Post p where p.id<:id AND p.subject NOT IN :subject AND (p.title LIKE %:title% Or p.tags LIKE %:tags% Or p.content LIKE %:content%) ORDER BY p.id DESC ")
    Slice<Post> findByIdLessThanSearchKeyword(Long id,String title,String tags,String content,SubjectEnum subject ,Pageable pageable);// 제목,내용,태그에 하나라도 keyword가 들어간 포스트를 보여주는 페이지네이션 후 부분


    List<Post> findByUser(User user);


    @Query("select p from Post p  WHERE p.subject NOT IN ?1 ORDER BY p.viewUserList.size DESC,p.id DESC ")
    List<Post> findByOrderByViewUserListSize(SubjectEnum subject,Pageable pageable);





}
