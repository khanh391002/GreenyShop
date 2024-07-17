package vn.fs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
	private Long id;
	private String name;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	private ProductDTO products;
}
