package kr.co.kokono.webstudy.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kokono.webstudy.domain.posts.Posts;
import kr.co.kokono.webstudy.domain.posts.PostsRepository;
import kr.co.kokono.webstudy.web.dto.PostsSaveRequestDto;
import kr.co.kokono.webstudy.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testPostPosts() throws Exception {

        //given
        String title = "꾸꾸까까";
        String content = "대즐미대즐미~~베이베~";

        PostsSaveRequestDto requestDto
                = PostsSaveRequestDto.builder().title(title)
                .content(content).author("위키미키").build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        /*ResponseEntity<Long> responseEntity
                = restTemplate.postForEntity(url, requestDto, Long.class);

         */
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        //I expect Test have to give 200 Response code.
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //I expect Test's return value is greater than 0.
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();

        //I expect the requested title and actual saved title to be same.
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testPostsUpdate() throws Exception {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                    .title("바보바보").content("쿠쿠쿠쿳쿠쿠쿠쿠....")
                    .author("임창정").build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "바보바보2";
        String expectedContent = "내용없다고오오";

        PostsUpdateRequestDto requestDto
                = PostsUpdateRequestDto.builder()
                    .title(expectedTitle).content(expectedContent).build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity
                = new HttpEntity<>(requestDto);

        //when
//        ResponseEntity<Long> responseEntity
//                = restTemplate.exchange(url, HttpMethod.PUT,
//                                            requestEntity, Long.class);
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }

}







