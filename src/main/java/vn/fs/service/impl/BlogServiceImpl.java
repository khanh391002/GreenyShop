package vn.fs.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.model.entities.Blog;
import vn.fs.repository.BlogRepository;
import vn.fs.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService {

	@Value("${upload.path}")
	private String pathUploadImage;
	
	@Autowired
	private BlogRepository blogRepository;

	@Override
	public List<Blog> getAll() {
		List<Blog> blogs = blogRepository.findAllByIsDeletedIsFalse();
		if (CollectionUtils.isEmpty(blogs)) {
			return null;
		}
		return blogs;
	}

	@Override
	public List<Blog> getByCategoryBlogId(Long categoryId) {
		List<Blog> blogs = blogRepository.findAllByCategoryBlogCategoryBlogIdAndIsDeletedIsFalse(categoryId);
		if (CollectionUtils.isEmpty(blogs)) {
			return null;
		}
		return blogs;
	}

	@Override
	public Blog getById(Long id) {
		Optional<Blog> blogOpt = blogRepository.findById(id);
		if (!blogOpt.isPresent()) {
			return null;
		}
		return blogOpt.get();
	}

	@Override
	public Blog getById(Long id, Model model) {
		Optional<Blog> blogOpt = blogRepository.findById(id);
		if (!blogOpt.isPresent()) {
			model.addAttribute("error", "Blog không tồn tại!");
			return null;
		}
		return blogOpt.get();
	}

	@Override
	public void add(Blog dto, MultipartFile file, Model model) {
		Optional<Blog> blogOpt = blogRepository.findOneByCodeAndIsDeletedIsFalse(dto.getCode());
		if (blogOpt.isPresent()) {
			model.addAttribute("errorCode", "Mã Blog đã tồn tại!");
		} else {
			try {
				File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("error", "Lỗi tải ảnh!");
			}
			Blog blog = new Blog();
			Date currentDate = Date.from(Instant.now());
			blog.setCode(dto.getCode());
			blog.setName(dto.getName());
			blog.setImage(file.getOriginalFilename());
			blog.setTitle(dto.getTitle());
			blog.setCreateDate(currentDate);
			blog.setDescription(dto.getDescription());
			blog.setUrl(dto.getUrl());
			blog.setCategoryBlog(dto.getCategoryBlog());
			blog = blogRepository.save(blog);
			if (null != blog) {
				model.addAttribute("message", "Tạo Blog thành công!");
				model.addAttribute("blog", blog);
			} else {
				model.addAttribute("message", "Tạo Blog thất bại");
				model.addAttribute("blog", blog);
			}
		}
	}

	@Override
	public void update(Long id, Blog dto, MultipartFile file, Model model) {
		Optional<Blog> blogOpt = blogRepository.findById(id);
		if (!blogOpt.isPresent()) {
			model.addAttribute("errorBlog", "Blog không tồn tại!");
		} else {
			String blogImage = null;
			if (file.getSize() > 0) {
				blogImage = file.getOriginalFilename();
				try {
					if (file.getSize() > 0) {
						blogImage = file.getOriginalFilename();
					} else {
						blogImage = dto.getImage();
					}
					File convFile = new File(pathUploadImage + "/" + (blogImage));
					FileOutputStream fos = new FileOutputStream(convFile);
					fos.write(file.getBytes());
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("error", "Lỗi tải ảnh!");
				}
			} else {
				blogImage = dto.getImage();
			}
			Blog blog = blogOpt.get();
//			model.addAttribute("blog", blog);
//			blog.setCode(dto.getCode());
			blog.setName(dto.getName());
			blog.setImage(blogImage);
			blog.setTitle(dto.getTitle());
			blog.setDescription(dto.getDescription());
			blog.setUrl(dto.getUrl());
			blog.setCategoryBlog(dto.getCategoryBlog());
			blog = blogRepository.save(blog);
			if (null != blog) {
				model.addAttribute("message", "Cập nhật Blog thành công!");
				model.addAttribute("blog", blog);
			} else {
				model.addAttribute("message", "Cập nhật Blog thất bại");
				model.addAttribute("blog", blog);
			}
		}
	}

	@Override
	public void delete(Long id, Model model) {
		Optional<Blog> blogOpt = blogRepository.findById(id);
		if (!blogOpt.isPresent()) {
			model.addAttribute("errorBlog", "Blog không tồn tại!");
		} else {
			Blog blog = blogOpt.get();
			blog.setDeleted(true);
			blogRepository.save(blog);
			model.addAttribute("message", "Xoá Blog thành công!");
		}
	}

	@Override
	public void delete(List<Blog> list, Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBlogForBlogPage(Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String blogDetail(Long id, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
