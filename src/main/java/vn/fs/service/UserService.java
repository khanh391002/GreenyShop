package vn.fs.service;

import java.util.List;

import org.springframework.ui.Model;

import vn.fs.model.entities.User;
import vn.fs.model.response.UserResponse;

public interface UserService {
	User findByEmail(String email);

    void add(User user, Model model);

    List<User> getAll();
    
    List<User> getAllUserIsAdmin();

    void update(Long id, UserResponse user, Model model);
    
    void updateProfile(String email, UserResponse user, Model model);

    void delete(Long id);

    User getById(Long id);
    
    UserResponse getByIdUserResponse(Long id);
}
