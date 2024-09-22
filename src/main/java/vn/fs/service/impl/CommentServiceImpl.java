package vn.fs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

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
	public void addComment(Long productId, Integer starNumber, String reviewContent, User user, Model model) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

		model.addAttribute("product", product);
		List<Product> products = productRepository.listProductByCategory10(product.getCategory().getCategoryId());
		model.addAttribute("productByCategory", products);
		Comment comment = new Comment();
		comment.setRating(starNumber.doubleValue());
		comment.setContent(reviewContent);
		comment.setRateDate(new Date()); // Set the current date
		comment.setUser(user);
		comment.setProduct(product);
		List<Comment> comments = new ArrayList<>(getRecentComments(productId));
		if (!CollectionUtils.isEmpty(comments) && comments.size() <= 3) {
			comments.remove(comments.size() - 1);
		}
		comments.add(comment);
		model.addAttribute("comments", comments);

		commentRepository.save(comment);
	}

}
