package vn.fs.model.dto;

import java.util.Date;

public interface CommentDTO {
	Long getId();
	
	String getContent();
	
	Date getRateDate();
	
	int getRating();
	
	Long getOrderDetailId();
	
	Long getProductId();
	
	Long getUserId();
	
	String getName();
	
	String getAvatar();
}
