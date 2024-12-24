package vn.fs.service;

import java.util.List;

import org.springframework.ui.Model;

import vn.fs.model.entities.CategoryBlog;

public interface CategoryBlogService {
	
	List<CategoryBlog> getAll();

	CategoryBlog getId(Long id);

    void add(CategoryBlog categoriesBlogDTO, Model model);

    CategoryBlog getById(Long id, Model model);

    void update(Long id, CategoryBlog categoriesBlogDTO, Model model);

    void delete(Long id, Model model);
}
