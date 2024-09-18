package vn.fs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query(value = "SELECT * FROM comments WHERE product_id = :productId ORDER BY rate_date DESC LIMIT :limit", nativeQuery = true)
	List<Comment> getTopCommentsByProductId(Long productId, @Param("limit") int limit);
}
