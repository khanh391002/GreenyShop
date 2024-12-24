package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
	Optional<Blog> findOneByCodeAndIsDeletedIsFalse(String code);

	List<Blog> findAllByIsDeletedIsFalse();

	List<Blog> findAllByCategoryBlogCategoryBlogIdAndIsDeletedIsFalse(Long id);

	List<Blog> findAllByCategoryBlogCategoryBlogId(Long id);

	// count quantity by blog
	@Query(value = "SELECT c.category_blog_id,c.name, COUNT(*) AS SoLuong "
			+ "FROM blogs b "
			+ "JOIN categorie_blogs c ON b.category_blog_id = c.category_blog_id "
			+ "GROUP BY c.category_blog_id, c.name; ", nativeQuery = true)
	List<Object[]> listCategoryByBlogName();
}
