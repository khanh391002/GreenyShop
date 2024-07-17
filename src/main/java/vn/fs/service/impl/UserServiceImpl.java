package vn.fs.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import lombok.RequiredArgsConstructor;
import vn.fs.model.dto.UserDTO;
import vn.fs.model.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends DefaultOAuth2UserService implements UserService {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<UserDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(User user, ModelMap model, String password, String opt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO getCurrentUser() {
		Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        if (!authentication.isPresent()) 
        	return null;
        Object principalObj = authentication.get().getPrincipal();
        if (principalObj != null) {
        	Principal principal = mapper.map(principalObj, Principal.class);
        	UserDTO userDTO = mapper.map(userRepository.findByEmail(principal.getName()), UserDTO.class);
        	return userDTO;
		}
		return null;
	}

}
