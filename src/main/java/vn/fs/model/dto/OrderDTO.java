package vn.fs.model.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	private Long orderId;
	
	private Date orderDate;
	
	private Double amount;
	
	private String address;
	
	private String phone;
	
	private int status;

	private List<OrderDetailDTO> orderDetails;

	private UserDTO user;
}
