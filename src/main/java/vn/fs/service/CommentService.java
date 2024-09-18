package vn.fs.service;

import java.util.List;

import vn.fs.model.entities.Comment;
import vn.fs.model.entities.User;

public interface CommentService {
	
	List<Comment> getRecentComments(Long productId);

    void addComment(Long productId, Integer starNumber, String reviewContent, User user);
}
