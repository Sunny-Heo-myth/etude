package org.alan.etude.entity.post;

import org.alan.etude.exception.UnsupportedImageFormatException;
import org.alan.etude.factory.entity.ImageFactory;
import org.alan.etude.factory.entity.PostFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageTest {

    @Test
    void createImageTest() {
        // given
        String validExtension = "JPEG";

        // when, then
        ImageFactory.createImageWithOriginName("image." + validExtension);
    }

    @Test
    void createImageExceptionByUnsupportedFormatTest() {
        // given
        String invalidExtension = "invalid";

        // when, then
        assertThatThrownBy(() -> ImageFactory.createImageWithOriginName("image." + invalidExtension))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void createImageExceptionByNoneExtensionTest() {
        // given
        String originName = "image";

        // when, then
        assertThatThrownBy(() -> ImageFactory.createImageWithOriginName(originName))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void initPostTest() {
        // given
        Image image = ImageFactory.createImage();

        // when
        Post post = PostFactory.createPost();
        image.initPost(post);

        // then
        assertThat(image.getPost()).isSameAs(post);
    }

    @Test
    void initPostNotChangedTest() {
        // given
        Image image = ImageFactory.createImage();
        image.initPost(PostFactory.createPost());

        // when
        Post post = PostFactory.createPost();
        image.initPost(PostFactory.createPost());

        // then
        assertThat(image.getPost()).isNotSameAs(post);
    }
}
