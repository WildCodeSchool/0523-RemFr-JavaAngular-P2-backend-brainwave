package com.templateproject.api.repository;

import com.templateproject.api.entity.Role;
import com.templateproject.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

import java.util.Optional;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

List<User>findUserByFirstnameIsContainingOrLastnameIsContainingIgnoreCase(String searchFirstnameTerm,String searchLastNameTerm);


    User findByEmail(String username);

    Optional<Object> findByRole(Role role);

}
