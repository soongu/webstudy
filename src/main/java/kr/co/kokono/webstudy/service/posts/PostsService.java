package kr.co.kokono.webstudy.service.posts;

import kr.co.kokono.webstudy.domain.posts.Posts;
import kr.co.kokono.webstudy.domain.posts.PostsRepository;
import kr.co.kokono.webstudy.web.dto.PostsResponseDto;
import kr.co.kokono.webstudy.web.dto.PostsSaveRequestDto;
import kr.co.kokono.webstudy.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "해당 게시글이 없습니다. id=" + id
                        ));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
                        );

        return new PostsResponseDto(entity);
    }

}