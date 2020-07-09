package kr.co.kokono.webstudy.web;

import kr.co.kokono.webstudy.domain.posts.Posts;
import kr.co.kokono.webstudy.domain.posts.PostsRepository;
import kr.co.kokono.webstudy.web.dto.PostsSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void testPostPosts() throws Exception {

        //given
        String title = "꾸꾸까까";
        String content = "대즐미대즐미~~베이베~";

        PostsSaveRequestDto requestDto
                = PostsSaveRequestDto.builder().title(title)
                .content(content).author("위키미키").build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity
                = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        //I expect Test have to give 200 Response code.
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //I expect Test's return value is greater than 0.
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();

        //I expect the requested title and actual saved title to be same.
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }
}







