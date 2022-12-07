package com.arsen.internet.shop.soap.app.service.impl;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Role;
import com.arsen.internet.shop.soap.app.repository.RoleRepository;
import com.arsen.internet.shop.soap.app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        if(role == null){
            throw new NullEntityReferenceException("Role cannot be null!");
        }

        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        if(role == null){
            throw new NullEntityReferenceException("Role cannot be null!");
        }

        readById(role.getId());
        return roleRepository.save(role);

    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " is not found!"));
    }

    @Override
    public Role readByName(String name) {
        Role role = roleRepository.readRoleByName(name);
        if(role == null){
            throw new EntityNotFoundException("Role with name " + name + " is not found!");
        }
        return role;
    }

    @Override
    public void delete(long id) {
        roleRepository.delete(readById(id));
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
