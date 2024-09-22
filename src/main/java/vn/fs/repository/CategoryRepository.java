package vn.fs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query(value = "SELECT COUNT(category_id) FROM greeny_shop.categories ", nativeQuery = true)
	int countAllCategory();
}
