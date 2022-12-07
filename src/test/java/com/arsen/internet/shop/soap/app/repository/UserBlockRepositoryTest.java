package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import com.arsen.internet.shop.soap.app.service.UserService;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class UserBlockRepositoryTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserBlockRepository blockRepository;


    @Test
    public void testBan(){

        User user = userService.readById(1);
        assertNull(user.getBlock());

        UserBlock userBlock = new UserBlock();
        long millis = System.currentTimeMillis() / 1000;

        userBlock.setUser(user);
        userBlock.setReason("Test");
        userBlock.setStartTime(millis);

        int endTime = 60 * 30 * 5;
        userBlock.setEndTime(millis + (endTime));

        assertTrue(userBlock.isBanned());
        userBlock = blockRepository.save(userBlock);

        assertEquals(1, userBlock.getId());

        user = userService.readById(1);
        assertNotNull(user.getBlock());

        assertEquals(userBlock.getId(), user.getBlock().getId());
        assertTrue(user.getBlock().isBanned());

        assertEquals(1, blockRepository.readAll().size());

        Page<UserBlock> page = blockRepository.findAllBy(PageRequest.of(0, ShopValues.MAX_ROWS));
        assertEquals(1, page.getTotalPages());
        assertEquals(1, page.getContent().size());

    }


}
