package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.fs.model.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.UserService;

@Controller
public class UserController{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@GetMapping(value = "/admin/users")
	public String customer(Model model, Principal principal) {
		
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);
		
		List<User> users = userService.getAll();
		model.addAttribute("users", users);
		
		return "/admin/users";
	}
}
