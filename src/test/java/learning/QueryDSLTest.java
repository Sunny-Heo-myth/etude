package learning;

import com.chatboard.etude.config.QuerydslConfig;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.entity.post.QPost;
import com.chatboard.etude.repository.post.CustomPostRepository;
import com.chatboard.etude.repository.post.PostRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Does not work
@DataJpaTest
@Import(QuerydslConfig.class)
public class QueryDSLTest {

    PostRepository postRepository;

    @Test
    public void testPredicate() {
        String type = "t";
        String keyword = "17";

        BooleanBuilder builder = new BooleanBuilder();

        QPost qPost = QPost.post;

        if (type.equals("t")) {
            builder.and(qPost.title.like("%" + keyword + "%"));
        }

        // id > 0
        builder.and(qPost.id.gt(0));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> postPage = postRepository.findAll(pageable);

        System.out.println(
                "Page size : " + postPage.getSize() +
                        "\nTotal pages : " + postPage.getTotalPages() +
                        "\nTotal count : " + postPage.getTotalElements() +
                        "\nNext : " + postPage.nextPageable()
        );

        List<Post> list = postPage.getContent();

        list.forEach(System.out::println);
    }
}
