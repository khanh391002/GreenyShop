package vn.fs.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import vn.fs.model.dto.UserDTO;
import vn.fs.model.entities.User;

@Service
public interface UserService extends UserDetailsService{
	public List<UserDTO> findAll();
	
	public UserDTO findById(Long id);
	
	public UserDetails loadUserByUsername(String email);
	
	public UserDTO findByEmail(String email);
	
	public String save(User user, ModelMap model, String password, String opt);
	
	public UserDTO getCurrentUser();
}
