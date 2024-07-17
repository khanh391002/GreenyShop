package vn.fs.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	private Long id;
	
	private Double rating;
	
	private String content;
	
	private Date rateDate;

	private UserDTO user;

	private ProductDTO product;

	private OrderDetailDTO orderDetail;
}
