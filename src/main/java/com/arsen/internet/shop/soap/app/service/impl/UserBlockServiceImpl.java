package com.arsen.internet.shop.soap.app.service.impl;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import com.arsen.internet.shop.soap.app.repository.UserBlockRepository;
import com.arsen.internet.shop.soap.app.service.UserBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserBlockServiceImpl implements UserBlockService {

    private final UserBlockRepository userBlockRepository;

    @Autowired
    public UserBlockServiceImpl(UserBlockRepository userBlockRepository) {
        this.userBlockRepository = userBlockRepository;
    }

    @Override
    public UserBlock create(UserBlock block) {
        if(block == null){
            throw new NullEntityReferenceException("Block cannot be null!");
        }

        return userBlockRepository.save(block);
    }

    @Override
    public UserBlock update(UserBlock block) {
        if(block == null){
            throw new NullEntityReferenceException("Block cannot be null!");
        }

        readById(block.getId());
        return userBlockRepository.save(block);
    }

    @Override
    public UserBlock readById(long id) {
        return userBlockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Block with id " + id + " is not found!"));
    }

    @Override
    public UserBlock readByUserId(long userId) {

        UserBlock block = userBlockRepository.findUserBlockByUserId(userId);
        if (block == null) {
            throw new EntityNotFoundException("User with  " + userId + " (was) is not banned!");
        }
        return block;
    }


    @Override
    public void delete(long id) {
        userBlockRepository.delete(readById(id));
    }
}
