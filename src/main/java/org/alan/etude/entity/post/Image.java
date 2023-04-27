package org.alan.etude.entity.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.alan.etude.exception.UnsupportedImageFormatException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uniqueName;  // unique name

    @Column(nullable = false)
    private String originName;  // original image name

    private final static String[] supportedExtension = {"jpg", "jpeg", "gif", "bmp", "png"};

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    public Image(String originName) {
        this.uniqueName = generateUniqueName(extractExtension(originName)); // build a unique image name
        this.originName = originName;
    }

    public void initPost(Post post) {
        if (this.post == null) {
            this.post = post;
        }
    }

    // extract extension name
    private String extractExtension(String originName) {
        try {
            String extension = originName.substring(originName.lastIndexOf(".") + 1);
            if (isSupportedFormat(extension)) {
                return extension;
            }
        }
        catch (StringIndexOutOfBoundsException ignored) {}
        throw new UnsupportedImageFormatException();
    }

    // generate unique name
    private String generateUniqueName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    // check if it is supported format
    private boolean isSupportedFormat(String extension) {
        return Arrays.stream(supportedExtension)
                .anyMatch(supported -> supported.equalsIgnoreCase(extension));
    }

}
