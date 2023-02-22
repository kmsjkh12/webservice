package book.springboot.service.posts;

import book.springboot.domain.posts.Posts;
import book.springboot.domain.posts.PostsRepository;
import book.springboot.web.dto.PostListResponseDto;
import book.springboot.web.dto.PostsResponseDto;
import book.springboot.web.dto.PostsSaveRequestDto;
import book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    //영속석 컨텍스트 데이터베이스에 쿼리를 날리지 않음
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException(
                "해당게시글이 없습니다. id="+id
        ));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException(
                "해당게시글이 없습니다 id=" +id
        ));

        return new PostsResponseDto(entity);
    }

    @Transactional
    public List<PostListResponseDto> findByDesc(){
        return postsRepository.findAllDesc().stream().map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당게시글이없습니다 id="+ id));
        postsRepository.delete(posts);
    }
}
