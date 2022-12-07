package com.arsen.internet.shop.soap.app.service.impl;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import com.arsen.internet.shop.soap.app.repository.UserBlockRepository;
import com.arsen.internet.shop.soap.app.repository.UserRepository;
import com.arsen.internet.shop.soap.app.service.RoleService;
import com.arsen.internet.shop.soap.app.service.UserService;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final UserBlockRepository blockRepository;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserBlockRepository blockRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.blockRepository = blockRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        if(user == null){
            throw new NullEntityReferenceException("User cannot be null!");
        }
        user.setRole(roleService.readByName("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if(user == null){
            throw new NullEntityReferenceException("User cannot be null!");
        }

        readById(user.getId());
        return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " is not found!"));
    }

    @Override
    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new EntityNotFoundException("User with email " + email + " is not found!");
        }

        return user;
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(readById(userId));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAll(int page) {
        return userRepository.findAll(PageRequest.of(--page, ShopValues.MAX_ROWS));
    }

    @Override
    public Page<UserBlock> getAllBanned(int page) {
        return blockRepository.findAllBy(PageRequest.of(--page, ShopValues.MAX_ROWS));
    }
}
