package kr.co.kokono.webstudy.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After //Used to specify the method to be executed whenever the unit test ends.
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void insertAndReadArticle() {
        //given
        String title = "테스트 게시물";
        String content = "테스트 내용~~ 아아아아";

        postsRepository.save(Posts.builder()
                                  .title(title)
                                  .content(content)
                                  .author("hsg9984@gmail.com")
                                  .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void postBaseTimeEntity() {

        //given
        LocalDateTime now = LocalDateTime.of(2020, 7, 9, 0, 0, 0);
        postsRepository.save(Posts.builder()
                                .title("title").author("author").content("content")
                                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        System.out.println(">>>>>>>> createDate=" + posts.getCreatedDate()
                            + ", modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}







