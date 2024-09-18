package vn.fs.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import vn.fs.model.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
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
		return userRepository.findAllByStatusIsTrue();
	}

	@Override
	@ReadOnlyProperty
	public List<User> getAllUserIsAdmin() {
		return userRepository.findAllUserIsAdmin();
	}

	@Override
	public void update(Long id, User user, Model model) {
		// TODO Auto-generated method stub
		
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

}
