package vn.fs.controller.admin;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.model.entities.Blog;
import vn.fs.model.entities.CategoryBlog;
import vn.fs.model.entities.User;
import vn.fs.repository.BlogRepository;
import vn.fs.repository.CategoryBlogRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.BlogService;
import vn.fs.service.CategoryBlogService;

@Controller
@RequestMapping("/admin")
public class BlogController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	BlogRepository blogRepository;

	@Autowired
	BlogService blogService;

	@Autowired
	CategoryBlogRepository categoryBlogRepository;

	@Autowired
	CategoryBlogService categoryBlogService;

	@Autowired
	UserRepository userRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	public BlogController(CategoryBlogRepository categoryBlogRepository, BlogService blogService,
			BlogRepository blogRepository) {
		this.blogRepository = blogRepository;
		this.blogService = blogService;
		this.categoryBlogRepository = categoryBlogRepository;
	}

	// show list blog - table list
	@ModelAttribute("blogs")
	public List<Blog> showBlog(Model model) {
		List<Blog> blogs = blogService.getAll();
		model.addAttribute("blogs", blogs);

		return blogs;
	}

	@GetMapping(value = "/blogs")
	public String blogs(Model model, Principal principal) {
		Blog blog = new Blog();
		model.addAttribute("blog", blog);

		return "admin/blogs";
	}

	@GetMapping(value = "/addBlog")
	public String showPopUpAddBlog(Model model) {
		Blog blog = new Blog();
		model.addAttribute("blog", blog);
		return "admin/createBlog";
	}

	// add blog
	@PostMapping(value = "/addBlog")
	public String addBlog(@ModelAttribute("blog") Blog blog, Model model, @RequestParam("file") MultipartFile file,
			BindingResult result, HttpServletRequest httpServletRequest) {
		blogService.add(blog, file, model);
		return "redirect:/admin/blogs";
	}

	// show select option ở add blog
	@ModelAttribute("categoryBlogList")
	public List<CategoryBlog> showCategoryBlog(Model model) {
		List<CategoryBlog> categoryBlogList = categoryBlogService.getAll();
		model.addAttribute("categoryBlogList", categoryBlogList);

		return categoryBlogList;
	}

	// get Edit brand
	@GetMapping(value = "/editBlog/{id}")
	public String showEditPage(@PathVariable("id") Long id, Model model) {
		Blog blog = blogService.getById(id, model);
		if (ObjectUtils.isEmpty(blog)) {
			return "redirect:/admin/blogs";
		}
		model.addAttribute("blog", blog);
		return "admin/editBlog";
	}

	// add blog
	@PostMapping(value = "/editBlog/{id}")
	public String editBlog(@PathVariable("id") Long id, @ModelAttribute("blog") Blog blog, Model model,
			@RequestParam("file") MultipartFile file, BindingResult result, HttpServletRequest httpServletRequest) {
		blogService.update(id, blog, file, model);
		return "redirect:/admin/blogs";
	}

	// delete category
	@GetMapping("/deleteBlog/{id}")
	public String delBlog(@PathVariable("id") Long id, Model model) {
		blogService.delete(id, model);
		return "redirect:/admin/blogs";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
}
