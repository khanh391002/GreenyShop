package vn.fs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.CategoryBlog;

@Repository
public interface CategoryBlogRepository extends JpaRepository<CategoryBlog, Long> {
	Optional<CategoryBlog> findByCode(String code);
}
