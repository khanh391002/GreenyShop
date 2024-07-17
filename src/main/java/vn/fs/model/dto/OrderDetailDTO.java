package vn.fs.model.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fs.model.entities.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
	private Long orderDetailId;
	
	@NotNull(message = "Quantity must not be null")
	private int quantity;
	
	private Double price;
	
	private Product product;
	
	private OrderDTO order;
}
