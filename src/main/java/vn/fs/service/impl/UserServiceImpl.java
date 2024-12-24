package vn.fs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import vn.fs.model.entities.Role;
import vn.fs.model.entities.User;
import vn.fs.model.response.UserResponse;
import vn.fs.repository.RoleRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	@ReadOnlyProperty
	public User findByEmail(String email) {
		Optional<User> userOptional = userRepository.findByEmailAndStatusIsTrue(email);
		if (!userOptional.isPresent()) {
			return null;
		}
		return userOptional.get();
	}

	@Override
	public void add(User user, Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@ReadOnlyProperty
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	@ReadOnlyProperty
	public List<User> getAllUserIsAdmin() {
		return userRepository.findAllUserIsAdmin();
	}

	@Override
	public void update(Long id, UserResponse userRequest, Model model) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setName(userRequest.getName() == null ? user.getName() : userRequest.getName());
			user.setEmail(userRequest.getEmail() == null ? user.getEmail() : userRequest.getEmail());
			user.setAvatar(userRequest.getAvatar() == null ? user.getAvatar() : userRequest.getAvatar());
			user.setPhone(userRequest.getPhone() == null ? user.getPhone() : userRequest.getPhone());
			user.setAddress(userRequest.getAddress() == null ? user.getAddress() : userRequest.getAddress());
			user.setStatus(userRequest.getStatus() == null ? user.getStatus() : userRequest.getStatus());
			user.setDistrict(userRequest.getDistrict() == null ? user.getDistrict() : userRequest.getDistrict());
			user.setCity(userRequest.getCity() == null ? user.getCity() : userRequest.getCity());
			user.setRoles(userRequest.getRoles().isEmpty() ? user.getRoles() : userRequest.getRoles());
			userRepository.save(user);
			model.addAttribute("userDto", user);
		}
	}
	
	@Override
	public void updateProfile(String email, UserResponse userRequest, Model model) {
		User user = findByEmail(email);
		if (!ObjectUtils.isEmpty(user)) {
			user.setName(userRequest.getName() == null ? user.getName() : userRequest.getName());
			user.setAvatar(userRequest.getAvatar() == null ? user.getAvatar() : userRequest.getAvatar());
			user.setPhone(userRequest.getPhone() == null ? user.getPhone() : userRequest.getPhone());
			user.setAddress(userRequest.getAddress() == null ? user.getAddress() : userRequest.getAddress());
			user.setDistrict(userRequest.getDistrict() == null ? user.getDistrict() : userRequest.getDistrict());
			user.setCity(userRequest.getCity() == null ? user.getCity() : userRequest.getCity());
			userRepository.save(user);
			model.addAttribute("user", user);
		}
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			return null;
		}
		return userOptional.get();
	}

	@Override
	public UserResponse getByIdUserResponse(Long id) {
		User user = getById(id);
		UserResponse userResponse = new UserResponse();
		if (!Objects.isNull(user)) {
			buildUserResponse(user, userResponse);
		}
		return userResponse;
	}

	private void buildUserResponse(User user, UserResponse userResponse) {
		userResponse.setId(user.getUserId());
		userResponse.setName(user.getName());
		userResponse.setEmail(user.getEmail());
		userResponse.setAvatar(user.getAvatar());
		userResponse.setPassword(user.getPassword());
		userResponse.setRegisterDate(user.getRegisterDate());
		userResponse.setPhone(user.getPhone());
		userResponse.setAddress(user.getAddress());
		userResponse.setCity(user.getCity());
		userResponse.setDistrict(user.getDistrict());
		userResponse.setStatus(user.getStatus());
//		List<String> roles = user.getRoles().stream().map(Role::getName).distinct().collect(Collectors.toList());
		userResponse.setRoles(new ArrayList<Role>(user.getRoles()));
	}

}
