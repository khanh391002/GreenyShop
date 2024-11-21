package vn.fs.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.model.entities.Role;
import vn.fs.model.entities.User;
import vn.fs.model.response.UserResponse;
import vn.fs.repository.RoleRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.UserService;

@Controller
public class UserController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping(value = "/admin/users")
	public String customer(Model model, Principal principal) {

		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);

		List<User> users = userService.getAll();
		model.addAttribute("users", users);

		return "/admin/users";
	}

	// Hiển thị trang chỉnh sửa thông tin người dùng
	@GetMapping("/admin/users/edit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model, Principal principal) {
		UserResponse userResponse = userService.getByIdUserResponse(id);
		List<Role> roles = roleRepository.findAllNotDuplicate();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userDto", userResponse);
		model.addAttribute("roles", roles);
		return "admin/editUser"; // Trả về tên view để chỉnh sửa
	}

	// Xử lý thông tin chỉnh sửa
	@PostMapping("/admin/users/edit/{id}")
	public String updateUser(@PathVariable("id") Long id, UserResponse userDto, Model model,
			@RequestParam("file") MultipartFile file, Principal principal) {
		String avatar = null;
		if (file.getSize() > 0) {
			avatar = file.getOriginalFilename();
			try {
				if (file.getSize() > 0) {
					avatar = file.getOriginalFilename();
				} else {
					avatar = userDto.getAvatar();
				}
				File convFile = new File(pathUploadImage + "/" + (avatar));
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		} else {
			avatar = userDto.getAvatar();
		}
		if (avatar != null) {
			userDto.setAvatar(avatar);
		}
		userService.update(id, userDto, model);
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);
		return "redirect:/admin/users"; // Quay lại danh sách người dùng sau khi chỉnh sửa
	}

}
