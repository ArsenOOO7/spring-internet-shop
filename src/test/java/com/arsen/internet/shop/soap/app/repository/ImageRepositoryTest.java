package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Image;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.service.UserService;
import com.arsen.internet.shop.soap.app.utils.ImageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ImageRepositoryTest {


    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageRepository;


    @Test
    public void imageWritingTest() throws IOException {

        Path path = Path.of("src", "test", "resources", "images", "image.jpg");
        byte[] bytes = Files.readAllBytes(path);

        Image image = Image.builder()
                .data(ImageUtil.compress(bytes))
                .uuid("My image")
                .contentType("image/jpeg")
                .build();

        image = imageRepository.save(image);

        assertEquals(1, image.getId());

        Image read = imageRepository.findById(1L).orElse(null);
        assertNotNull(read);

    }


    @Test
    public void testImageOnUser() throws IOException {

        User user = userService.readById(1);

        Path path = Path.of("src", "test", "resources", "images", "image.jpg");
        byte[] bytes = Files.readAllBytes(path);

        Image image = Image.builder()
                .data(ImageUtil.compress(bytes))
                .uuid("My image")
                .contentType("image/jpeg")
                .build();

        image = imageRepository.save(image);

        user.setImage(image);
        userService.update(user);

        User readUser = userService.readById(1);
        assertNotNull(readUser.getImage());
        assertEquals(image.getId(), readUser.getImage().getId());

    }

}
