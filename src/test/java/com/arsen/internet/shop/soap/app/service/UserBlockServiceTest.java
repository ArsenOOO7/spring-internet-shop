package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import com.arsen.internet.shop.soap.app.repository.UserBlockRepository;
import com.arsen.internet.shop.soap.app.service.impl.UserBlockServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
public class UserBlockServiceTest {

    @MockBean
    private UserBlockRepository blockRepository;

    @Autowired
    private UserBlockServiceImpl userBlockService;

    private static UserBlock block;

    @BeforeAll
    public static void beforeAll() {
        block = new UserBlock();
        block.setId(1);
        block.setReason("Some rwason");

        long seconds = System.currentTimeMillis() / 1000;
        block.setStartTime(seconds);
        block.setEndTime(seconds + (60 * 60 * 5));
    }

    @Test
    public void createNullBan(){
        assertThrows(NullEntityReferenceException.class, () -> userBlockService.create(null));
    }


    @Test
    public void createNormalBan(){
        when(blockRepository.save(any(UserBlock.class))).thenAnswer(ans -> ans.getArgument(0));
        assertEquals(block, userBlockService.create(block));
    }


    @Test
    public void updateNullBan(){
        assertThrows(NullEntityReferenceException.class, () -> userBlockService.update(null));
    }

    @Test
    public void updateEmptyBan(){
        when(blockRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userBlockService.update(block));
    }


    @Test
    public void updateNormalBan(){
        when(blockRepository.findById(anyLong())).thenReturn(Optional.of(block));
        when(blockRepository.save(any(UserBlock.class))).thenAnswer(ans -> ans.getArgument(0));

        assertEquals(block, userBlockService.update(block));
    }


    @Test
    public void testReadingBanAllCases(){

        when(blockRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userBlockService.readById(block.getId()));

        when(blockRepository.findById(anyLong())).thenReturn(Optional.of(block));
        assertEquals(block, userBlockService.readById(block.getId()));

    }


    @Test
    public void testReadingBanByUserId(){
        long userId = 10;

        when(blockRepository.findUserBlockByUserId(anyLong())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userBlockService.readByUserId(userId));

        when(blockRepository.findUserBlockByUserId(anyLong())).thenReturn(block);
        assertEquals(block, userBlockService.readByUserId(userId));
    }


    @Test
    public void testDeleteNullBan(){
        when(blockRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userBlockService.delete(1));
    }

    @Test
    public void testDeleteNormalBan(){
        when(blockRepository.findById(anyLong())).thenReturn(Optional.of(block));
        assertDoesNotThrow(() -> userBlockService.delete(1));
    }

}
