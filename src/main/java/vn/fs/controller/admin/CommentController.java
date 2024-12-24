package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.Comment;
import vn.fs.model.entities.User;
import vn.fs.repository.CategoryRepository;
import vn.fs.repository.CommentRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class CommentController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CommentRepository commentRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	public CommentController(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	// show list product - table list
	@ModelAttribute("comments")
	public List<Comment> showComment(Model model) {
		List<Comment> comments = commentRepository.findAllByOrderByIdDesc();
		model.addAttribute("comments", comments);

		return comments;
	}

	@GetMapping(value = "/comments")
	public String comments(Model model, Principal principal) {
		Comment comment = new Comment();
		model.addAttribute("comment", comment);

		return "admin/comments";
	}

	// delete category
	@GetMapping("/deleteComment/{id}")
	public String deleteComment(@PathVariable("id") Long id, Model model) {
		commentRepository.deleteById(id);
		model.addAttribute("message", "Xoá bình luận thành công!");

		return "redirect:/admin/comments";
	}
}
