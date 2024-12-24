package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.model.dto.CountProductOfCategoryDTO;
import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query(value = "SELECT * FROM greeny_shop.products "
			+ "WHERE is_deleted = false "
			+ "ORDER BY entered_date desc ", nativeQuery = true)
	List<Product> findAllByIsDeletedIsFalseAndOrderByEnteredDateDesc();

//	// List product by category
//	@Query(value = "SELECT * FROM products WHERE category_id = ?", nativeQuery = true)
//	public List<Product> listProductByCategory(Long categoryId);

	// List product by category
	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, p.product_code AS productCode, p.description, "
			+ "    p.entered_date AS enteredDate, p.price, p.product_image AS productImage, p.quantity, p.favorite, p.is_deleted AS isDeleted, "
			+ "    p.status, p.discount, p.category_id AS categoryId, cate.category_name AS categoryName, FLOOR(COALESCE(AVG(c.rating), 5) + 0.5) AS evaluate "
			+ "FROM products p " + "LEFT JOIN comments c ON p.product_id = c.product_id "
			+ "LEFT JOIN categories cate ON p.category_id = cate.category_id "
			+ "WHERE p.is_deleted = false AND p.category_id = :categoryId "
			+ "GROUP BY p.product_id ", nativeQuery = true)
	public List<ProductDTO> listProductByCategory(@Param("categoryId") Long categoryId);

	// Top 10 product by category
	@Query(value = "SELECT * FROM products AS p WHERE p.category_id = ? AND p.is_deleted = false;", nativeQuery = true)
	List<Product> listProductByCategory10(Long categoryId);

	// List product new
//	@Query(value = "SELECT * FROM products ORDER BY entered_date DESC limit 20;", nativeQuery = true)
//	public List<Product> listProductNew20();

	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, p.product_code AS productCode, p.description, "
			+ "    p.entered_date AS enteredDate, p.price, p.product_image AS productImage, p.quantity, p.favorite, p.is_deleted AS isDeleted, "
			+ "    p.status, p.discount, p.category_id AS categoryId, cate.category_name AS categoryName, FLOOR(COALESCE(AVG(c.rating), 5) + 0.5) AS evaluate "
			+ "FROM products p " + "LEFT JOIN comments c ON p.product_id = c.product_id "
			+ "LEFT JOIN categories cate ON p.category_id = cate.category_id " + "WHERE p.is_deleted = false "
			+ "GROUP BY p.product_id " + "ORDER BY p.entered_date DESC limit 20 ", nativeQuery = true)
	List<ProductDTO> listProductNew20();

	// Search Product
	@Query(value = "SELECT * FROM products WHERE product_name LIKE %?1% AND is_deleted = false ", nativeQuery = true)
	public List<Product> searchProduct(String productName);

	// count quantity by product
	@Query(value = "SELECT c.category_id,c.category_name, " + "COUNT(*) AS SoLuong " + "FROM products p "
			+ "JOIN categories c ON p.category_id = c.category_id "
			+ "GROUP BY c.category_id, c.category_name;", nativeQuery = true)
	List<Object[]> listCategoryByProductName();

	// Top 20 product best sale
	@Query(value = "SELECT p.product_id, " + "COUNT(*) AS SoLuong " + "FROM order_details p "
			+ "JOIN products c ON p.product_id = c.product_id " + "GROUP BY p.product_id "
			+ "ORDER by SoLuong DESC limit 20;", nativeQuery = true)
	public List<Object[]> bestSaleProduct20();

//	@Query(value = "select * from products o where product_id in :ids", nativeQuery = true)
//	List<Product> findByInventoryIds(@Param("ids") List<Integer> listProductId);

	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, p.product_code AS productCode, p.description, "
			+ "    p.entered_date AS enteredDate, p.price, p.product_image AS productImage, p.quantity, p.favorite, p.is_deleted AS isDeleted, "
			+ "    p.status, p.discount, p.category_id AS categoryId, cate.category_name AS categoryName, FLOOR(COALESCE(AVG(c.rating), 5) + 0.5) AS evaluate "
			+ "FROM products p " + "LEFT JOIN comments c ON p.product_id = c.product_id "
			+ "LEFT JOIN categories cate ON p.category_id = cate.category_id "
			+ "WHERE p.product_id IN (:ids) AND p.is_deleted = false " + "GROUP BY p.product_id ", nativeQuery = true)
	List<ProductDTO> findByInventoryIds(@Param("ids") List<Integer> listProductId);

	Optional<Product> findByProductCodeAndIsDeletedIsFalse(String productCode);

	Optional<Product> findByProductCode(String productCode);

	@Query(value = "SELECT COUNT(product_id) FROM greeny_shop.products WHERE is_deleted = false", nativeQuery = true)
	int countAllProductByIsDeletedIsFalse();

	@Query(value = "SELECT c.category_id as categoryId, COUNT(p.product_id) as totalProduct, SUM(p.quantity) as quantity "
			+ "FROM greeny_shop.products p " + "LEFT JOIN categories c ON c.category_id = p.category_id "
			+ "WHERE p.category_id IN (:categoryIds) AND p.is_deleted = false "
			+ "GROUP BY c.category_id ", nativeQuery = true)
	List<CountProductOfCategoryDTO> countProductByCategoryIdsByIsDeletedIsFalse(
			@Param("categoryIds") List<Long> categoryIds);

	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, p.product_code AS productCode, p.description, "
			+ "    p.entered_date AS enteredDate, p.price, p.product_image AS productImage, p.quantity, p.favorite, p.is_deleted AS isDeleted, "
			+ "    p.status, p.discount, p.category_id AS categoryId, cate.category_name AS categoryName, FLOOR(COALESCE(AVG(c.rating), 5) + 0.5) AS evaluate "
			+ "FROM products p " + "LEFT JOIN comments c ON p.product_id = c.product_id "
			+ "LEFT JOIN categories cate ON p.category_id = cate.category_id " + "WHERE p.is_deleted = false "
			+ "GROUP BY p.product_id ", nativeQuery = true)
	List<ProductDTO> findAllProductAndAvgRating();

	@Query(value = "SELECT p.product_id AS productId, p.product_name AS productName, p.product_code AS productCode, p.description, "
			+ "    p.entered_date AS enteredDate, p.price, p.product_image AS productImage, p.quantity, p.favorite, p.is_deleted AS isDeleted, "
			+ "    p.status, p.discount, p.category_id AS categoryId, cate.category_name AS categoryName, FLOOR(COALESCE(AVG(c.rating), 5) + 0.5) AS evaluate "
			+ "FROM products p " + "LEFT JOIN comments c ON p.product_id = c.product_id "
			+ "LEFT JOIN categories cate ON p.category_id = cate.category_id "
			+ "WHERE p.product_id = :id AND p.is_deleted = false ", nativeQuery = true)
	ProductDTO findOneById(@Param("id") Long id);

	List<Product> findAllByCategoryCategoryId(Long categoryId);

}
