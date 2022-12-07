package com.arsen.internet.shop.soap.app.model;

import com.arsen.internet.shop.soap.app.utils.ImageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ImageTest {

    @Test
    public void imageTest() throws IOException {

        Path path = Path.of("src", "test", "resources", "images", "image.jpg");
        byte[] bytes = Files.readAllBytes(path);

        Image image = Image.builder()
                .data(ImageUtil.compress(bytes))
                .uuid("My image")
                .contentType( "image/jpeg")
                .build();

        assertArrayEquals(bytes, ImageUtil.decompress(image.getData()));

    }

}
