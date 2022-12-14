package com.clone.soomgo.IntegrationTest;


import com.clone.soomgo.config.security.UserDetailsImpl;
import com.clone.soomgo.config.security.jwt.JwtTokenUtils;
import com.clone.soomgo.layer.ImgUrl.dto.ImgUrlDto;
import com.clone.soomgo.layer.post.dto.PostResponseDto;
import com.clone.soomgo.layer.post.dto.TagDto;
import com.clone.soomgo.layer.post.dto.ViewCountPostResponseDto;
import com.clone.soomgo.layer.post.model.SubjectEnum;
import com.clone.soomgo.layer.post.repository.PostRepository;
import com.clone.soomgo.layer.user.model.User;
import com.clone.soomgo.layer.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;

    private ObjectMapper mapper = new ObjectMapper();


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private String token;

    @BeforeAll
    public void registerUser(){
        User user = new User("username","email", passwordEncoder.encode("password"));
        userRepository.save(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        token = JwtTokenUtils.generateJwtToken(userDetails);
    }

    @BeforeEach
    public void setup() {

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","BEARER"+" "+token);
    }



    @Test
    @Order(1)
    @DisplayName("????????? ??????")
    void test1() throws JsonProcessingException{

        List<TagDto> tagDtoList = new ArrayList<>();

        List<ImgUrlDto> imgUrlDtoList = new ArrayList<>();

        TagDto tagDto = new TagDto("??????");

        ImgUrlDto imgUrlDto = new ImgUrlDto("URL");

        tagDtoList.add(tagDto);
        imgUrlDtoList.add(imgUrlDto);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .content("??????")
                .title("??????")
                .subject(SubjectEnum.QNA)
                .imgUrlList(imgUrlDtoList)
                .tagList(tagDtoList)
                .build();

        String requestBody = mapper.writeValueAsString(postRequestDto);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/posts",
                request,
                String.class
        );
        System.out.println(response);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("??????????????? ?????? ?????????????????????.",response.getBody());

    }

    @Test
    @Order(2)
    @DisplayName("????????? ?????? ????????????")
    void test2(){

        String subject = "ALL";
        int page =0;
        int size =2;
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts?subject="+subject+"&page="+page+"&size="+size,
                String.class

        );

        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    @Test
    @Order(3)
    @DisplayName("????????? ??????????????? ????????????")
    void test3(){
        long postId = 101L;

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<PostResponseDto> response = restTemplate.exchange(
                "/api/posts/"+postId,
                HttpMethod.GET,
                request,
                PostResponseDto.class
        );


        PostResponseDto postResponse = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("??????", Objects.requireNonNull(postResponse).getContent());
        assertEquals("??????",postResponse.getTitle());
        assertEquals(101L,postResponse.getPostId());
        assertEquals(SubjectEnum.QNA,postResponse.getSubject());

    }

    @Test
    @Order(4)
    @DisplayName("???????????? ?????? ????????? ???????????????")
    void test4(){
        long postId = 200L;

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/"+postId,
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("????????? ???????????? ??????")
    void test5() throws JsonProcessingException{
        List<TagDto> tagDtoList = new ArrayList<>();

        List<ImgUrlDto> imgUrlDtoList = new ArrayList<>();

        TagDto tagDto = new TagDto("????????????");

        ImgUrlDto imgUrlDto = new ImgUrlDto("URL??????");

        tagDtoList.add(tagDto);
        imgUrlDtoList.add(imgUrlDto);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .content("????????????")
                .title("????????????")
                .subject(SubjectEnum.QNA)
                .imgUrlList(imgUrlDtoList)
                .tagList(tagDtoList)
                .build();

        String requestBody = mapper.writeValueAsString(postRequestDto);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/101",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    @Test
    @Order(6)
    @DisplayName("????????? ?????????????????? ????????? ????????? ???????????? ??????")
    void test6() throws JsonProcessingException{
        List<TagDto> tagDtoList = new ArrayList<>();

        List<ImgUrlDto> imgUrlDtoList = new ArrayList<>();

        TagDto tagDto = new TagDto("????????????");
        TagDto tagDto2 = new TagDto("????????????");

        ImgUrlDto imgUrlDto = new ImgUrlDto("URL??????");

        tagDtoList.add(tagDto);
        tagDtoList.add(tagDto2);
        imgUrlDtoList.add(imgUrlDto);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .content("????????????")
                .title("????????????")
                .subject(SubjectEnum.QNA)
                .imgUrlList(imgUrlDtoList)
                .tagList(tagDtoList)
                .build();

        String requestBody = mapper.writeValueAsString(postRequestDto);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/101",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("????????? ????????? ???????????????",response.getBody());

    }

    @Test
    @Order(7)
    @DisplayName("????????? ?????????????????? ????????? , ??? ???????????? ??????")
    void test7() throws JsonProcessingException{
        List<TagDto> tagDtoList = new ArrayList<>();

        List<ImgUrlDto> imgUrlDtoList = new ArrayList<>();

        TagDto tagDto = new TagDto("??????,??????");

        ImgUrlDto imgUrlDto = new ImgUrlDto("URL??????");

        tagDtoList.add(tagDto);

        imgUrlDtoList.add(imgUrlDto);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .content("????????????")
                .title("????????????")
                .subject(SubjectEnum.QNA)
                .imgUrlList(imgUrlDtoList)
                .tagList(tagDtoList)
                .build();

        String requestBody = mapper.writeValueAsString(postRequestDto);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/101",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(" ,??? ????????? ????????? ??? ??? ????????????.",response.getBody());

    }

    @Test
    @Order(9)
    @DisplayName("????????? ????????????")
    void test8(){
        long postId = 101L;

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/"+postId,
                HttpMethod.DELETE,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("??????????????? ?????? ???????????????",response.getBody());
    }

    @Test
    @Order(8)
    @DisplayName("????????? ??????????????? ????????????")
    void test9(){


        ResponseEntity<ViewCountPostResponseDto> response = restTemplate.getForEntity(
                "/api/posts/viewcount",
                ViewCountPostResponseDto.class

        );

        ViewCountPostResponseDto result = response.getBody();



        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(4, Objects.requireNonNull(result).getPostList().size());
        assertEquals(101,result.getPostList().get(0).getPostId());

    }











    @Setter
    @Getter
    @Builder
    static class PostRequestDto{

        private SubjectEnum subject;

        private String title;

        private List<TagDto> tagList;

        private String content;

        private List<ImgUrlDto> imgUrlList;

    }





}
