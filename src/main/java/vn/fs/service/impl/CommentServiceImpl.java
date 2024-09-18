package vn.fs.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.fs.model.entities.Comment;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.repository.CommentRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	@ReadOnlyProperty
	public List<Comment> getRecentComments(Long productId) {
		return commentRepository.getTopCommentsByProductId(productId, 3);
	}

	@Override
	@Transactional
	public void addComment(Long productId, Integer starNumber, String reviewContent, User user) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

		Comment comment = new Comment();
		comment.setRating(starNumber.doubleValue());
		comment.setContent(reviewContent);
		comment.setRateDate(new Date()); // Set the current date
		comment.setUser(user);
		comment.setProduct(product);

		commentRepository.save(comment);
	}

}
