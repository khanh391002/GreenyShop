package vn.fs.service;

import java.util.List;

import org.springframework.ui.Model;

import vn.fs.model.entities.User;

public interface UserService {
	User findByEmail(String email);

    void add(User user, Model model);

    List<User> getAll();
    
    List<User> getAllUserIsAdmin();

    void update(Long id, User user, Model model);

    void delete(Long id);

    User getById(Long idd);
}