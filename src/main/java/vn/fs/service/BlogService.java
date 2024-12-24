package vn.fs.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.model.entities.Blog;

public interface BlogService {
	
	List<Blog> getAll();

    List<Blog> getByCategoryBlogId(Long id);

    Blog getById(Long id);

    Blog getById(Long id, Model model);

    void add(Blog dto, MultipartFile file, Model model);

    void update(Long id, Blog dto, MultipartFile file, Model model);

    void delete(Long id, Model model);

    void delete(List<Blog> list, Model model);

    String getBlogForBlogPage(Model model, HttpServletRequest request);

    String blogDetail(Long id, Model model, HttpServletRequest request);
}
