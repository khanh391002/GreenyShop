package vn.fs.model.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
	private Long id;
	
	private String content;
	
	private Date rateDate;
	
	private int rating;
	
	private Long orderDetailId;
	
	private Long productId;
	
	private Long userId;
	
	String name;
	
	String avatar;
}
