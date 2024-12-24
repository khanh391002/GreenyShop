package vn.fs.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import vn.fs.model.entities.Blog;
import vn.fs.model.entities.CategoryBlog;
import vn.fs.repository.BlogRepository;
import vn.fs.repository.CategoryBlogRepository;
import vn.fs.service.CategoryBlogService;

@Service
public class CategoryBlogServiceImpl implements CategoryBlogService{
	
	@Autowired
	private CategoryBlogRepository categoryBlogRepository;
	
	@Autowired
	private BlogRepository blogRepository;

	@Override
	public List<CategoryBlog> getAll() {
		return categoryBlogRepository.findAll();
	}

	@Override
	public CategoryBlog getId(Long id) {
		Optional<CategoryBlog> categoryBlogOpt = categoryBlogRepository.findById(id);
		if (categoryBlogOpt.isPresent()) {
			return categoryBlogOpt.get();
		}
		return null;
	}

	@Override
	public void add(CategoryBlog categoriesBlogDTO, Model model) {
		Optional<CategoryBlog> categoryBlogOpt = categoryBlogRepository.findByCode(categoriesBlogDTO.getCode());
		if (categoryBlogOpt.isPresent()) {
			model.addAttribute("error", "Mã thể loại Blog đã tồn tại!");
		} else {
			CategoryBlog categoryBlog = new CategoryBlog();
			categoryBlog.setCode(categoriesBlogDTO.getCode());
			categoryBlog.setName(categoriesBlogDTO.getName());
			categoryBlog = categoryBlogRepository.save(categoryBlog);
			if (null != categoryBlog) {
				model.addAttribute("message", "Tạo Category Blog thành công!");
				model.addAttribute("categoryBlog", categoryBlog);
			} else {
				model.addAttribute("message", "Tạo Category Blog thất bại");
				model.addAttribute("categoryBlog", categoryBlog);
			}
		}
	}

	@Override
	public CategoryBlog getById(Long id, Model model) {
		Optional<CategoryBlog> categoryBlogOpt = categoryBlogRepository.findById(id);
		if (categoryBlogOpt.isPresent()) {
			return categoryBlogOpt.get();
		}
		model.addAttribute("error", "Không tìm thấy thể loại Blog!");
		return null;
	}

	@Override
	public void update(Long id, CategoryBlog categoriesBlogDTO, Model model) {
		Optional<CategoryBlog> categoryBlogOpt = categoryBlogRepository.findById(id);
		if (!categoryBlogOpt.isPresent()) {
			model.addAttribute("error", "Thể loại Blog không tồn tại!");
		} else {
			CategoryBlog categoryBlog = categoryBlogOpt.get();
			categoryBlog.setName(categoriesBlogDTO.getName());
			categoryBlog = categoryBlogRepository.save(categoryBlog);
			if (null != categoryBlog) {
				model.addAttribute("message", "Tạo Category Blog thành công!");
				model.addAttribute("categoryBlog", categoryBlog);
			} else {
				model.addAttribute("message", "Tạo Category Blog thất bại");
				model.addAttribute("categoryBlog", categoryBlog);
			}
		}
	}

	@Override
	public void delete(Long id, Model model) {
		Optional<CategoryBlog> categoryBlogOpt = categoryBlogRepository.findById(id);
		if (!categoryBlogOpt.isPresent()) {
			model.addAttribute("error", "Thể loại Blog không tồn tại!");
		} else {
			List<Blog> blogs = blogRepository.findAllByCategoryBlogCategoryBlogId(id);
			if(!CollectionUtils.isEmpty(blogs)) {
				blogRepository.deleteAll(blogs);
			}
			categoryBlogRepository.deleteById(id);
			model.addAttribute("message", "Xoá thể loại blog thành công!");
		}
	}

}
