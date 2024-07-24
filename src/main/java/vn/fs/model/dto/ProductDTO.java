package vn.fs.model.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fs.model.entities.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	private Long productId;

	@NotNull(message = "Product name must not be null")
	private String productName;
	
	@NotNull(message = "Product code must not be null")
	private String productCode;
	
	@NotNull(message = "Quantity must not be null")
	private int quantity;
	
	@NotNull(message = "Price must not be null")
	private double price;
	
	@NotNull(message = "Discount must not be null")
	private int discount;
	
	@NotNull(message = "Product_Image must not be null")
	private String productImage;
	
	private String description;
	
	private Date enteredDate;
	
	public boolean favorite = false;
	
	private boolean isDeleted = false;

	private Category category;
	
}
