package vn.fs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
