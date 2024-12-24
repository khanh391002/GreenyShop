package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.CategoryBlog;
import vn.fs.model.entities.User;
import vn.fs.repository.CategoryBlogRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.CategoryBlogService;

@Controller
@RequestMapping("/admin")
public class CategoryBlogController {

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

	// show list categoryBlogs - table list
	@ModelAttribute("category-blogs")
	public List<CategoryBlog> showCategory(Model model) {
		List<CategoryBlog> categoryBlogs = categoryBlogService.getAll();
		model.addAttribute("categoryBlogs", categoryBlogs);

		return categoryBlogs;
	}

	@GetMapping(value = "/category-blogs")
	public String categoryBlogs(Model model, Principal principal) {
		CategoryBlog categoryBlog = new CategoryBlog();
		model.addAttribute("categoryBlog", categoryBlog);

		return "admin/categoryBlogs";
	}

	// add category Blog
	@PostMapping(value = "/addCategoryBlog")
	public String addCategoryBlog(@Validated @ModelAttribute("categoryBlog") CategoryBlog categoryBlog, Model model,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "failure");

			return "admin/categoryBlogs";
		}
		categoryBlogService.add(categoryBlog, model);
//		categoryRepository.save(category);
//		model.addAttribute("message", "successful!");

		return "redirect:/admin/category-blogs";
	}

	// get Edit category
	@GetMapping(value = "/editCategoryBlog/{id}")
	public String editCategoryBlog(@PathVariable("id") Long id, ModelMap model) {
		CategoryBlog categoryBlog = categoryBlogService.getId(id);

		model.addAttribute("categoryBlog", categoryBlog);

		return "admin/editCategoryBlog";
	}

	// edit category blog
	@PostMapping(value = "/editCategoryBlog/{id}")
	public String editCategoryBlog(@PathVariable("id") Long id,
			@Validated @ModelAttribute("categoryBlog") CategoryBlog categoryBlog, Model model,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "failure");
			return "admin/categories";
		}
		categoryBlogService.update(id, categoryBlog, model);
//			categoryRepository.save(category);
//			model.addAttribute("message", "successful!");

		return "redirect:/admin/category-blogs";
	}

	// delete category
	@GetMapping("/deleteCategoryBlog/{id}")
	public String delCategory(@PathVariable("id") Long id, Model model) {
		categoryBlogService.delete(id, model);
		return "redirect:/admin/category-blogs";
	}
}
