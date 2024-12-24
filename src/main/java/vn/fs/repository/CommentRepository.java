package vn.fs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.model.dto.CommentDTO;
import vn.fs.model.dto.RatingProductDTO;
import vn.fs.model.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findAllByOrderByRateDateDesc();
	
	List<Comment> findAllByOrderByIdDesc();
	
	@Query(value = "SELECT * FROM comments WHERE product_id = :productId ORDER BY rate_date DESC LIMIT :limit", nativeQuery = true)
	List<Comment> getTopCommentsByProductId(Long productId, @Param("limit") int limit);
	
	@Query(value = "SELECT COALESCE(AVG(rating), 0) as rating FROM comments WHERE YEAR(rate_date) = :year", nativeQuery = true)
	double getRatingOfTheYear(@Param("year") String year);
	
	@Query(value = "SELECT COALESCE(AVG(rating), 0) as rating FROM comments WHERE MONTH(rate_date) = :month", nativeQuery = true)
	double getRatingOfTheMonth(@Param("month") String month);
	
	@Query(value = "SELECT COALESCE(AVG(rating), 0) as rating FROM comments WHERE QUARTER(rate_date) = :quarter", nativeQuery = true)
	double getRatingOfTheQuarter(@Param("quarter") String quarter);
	
	@Query(value = "SELECT c.id, c.content, c.rate_date AS rateDate, c.rating, c.order_detail_id AS orderDetailId, c.product_id AS productId, c.user_id AS userId, "
			+ "u.name, u.avatar "
			+ "FROM comments c  "
			+ "LEFT JOIN user u ON c.user_id = u.user_id "
			+ "WHERE (product_id, rate_date, rating) IN ( "
			+ "    SELECT product_id, rate_date, rating "
			+ "    FROM comments "
			+ "    ORDER BY rating DESC, rate_date DESC "
			+ ") AND u.status = true "
			+ "ORDER BY c.rate_date DESC, c.rating DESC "
			+ "LIMIT :limit ", nativeQuery = true)
	List<CommentDTO> getTopLastestComments(@Param("limit") int limit);
	
	@Query(value = "SELECT product_id AS productId, AVG(rating) AS rating "
			+ "FROM greeny_shop.comments "
			+ "GROUP BY product_id; ", nativeQuery = true)
	List<RatingProductDTO> getAverateRatingByProduct();
}
