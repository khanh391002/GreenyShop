package vn.fs.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.fs.commom.CommomDataService;
import vn.fs.model.entities.Blog;
import vn.fs.model.entities.User;
import vn.fs.service.BlogService;
import vn.fs.service.CategoryBlogService;

@Controller
public class BlogWebController extends CommomController {

	@Autowired
	CategoryBlogService categoryBlogService;

	@Autowired
	BlogService blogService;

	@Autowired
	CommomDataService commomDataService;

	@GetMapping(value = "/blogs")
	public String shop(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, User user) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);
		List<Blog> blogs = blogService.getAll();
		Page<Blog> blogPage = findPaginated(PageRequest.of(currentPage - 1, pageSize), blogs);

		int totalPages = blogPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		commomDataService.commonData(model, user);
		model.addAttribute("blogs", blogPage);

		return "web/blog";
	}

	public Page<Blog> findPaginated(Pageable pageable, List<Blog> blogs) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Blog> list;

		if (blogs.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, blogs.size());
			list = blogs.subList(startItem, toIndex);
		}

		Page<Blog> blogPages = new PageImpl<Blog>(list, PageRequest.of(currentPage, pageSize), blogs.size());

		return blogPages;
	}

	// list blog by category
	@GetMapping(value = "/blogByCategory")
	public String listBlogById(Model model, @RequestParam("id") Long id, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, User user) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);
		List<Blog> blogs = blogService.getByCategoryBlogId(id);
		Page<Blog> blogPage = findPaginated(PageRequest.of(currentPage - 1, pageSize), blogs);
		int totalPages = blogPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		commomDataService.commonData(model, user);
		model.addAttribute("blogs", blogPage);
		model.addAttribute("categoryBlogId", id);
		return "web/blog";
	}

}
