package vn.fs.model.request;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fs.model.entities.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

	@NotNull(message = "Product name must not be null")
	@Size(max = 225, message = "Product_Name's length should be less than 255 characters")
	private String productName;
	
	@NotNull(message = "Product code must not be null")
	@Size(max = 50, message = "Product_Code's length should be less than 50 characters")
	private String productCode;
	
	@NotNull(message = "Quantity must not be null")
	private int quantity;
	
	@NotNull(message = "Price must not be null")
	private double price;
	
	@NotNull(message = "Discount must not be null")
	private int discount;
	
	@NotNull(message = "Product_Image must not be null")
	@Size(max = 2048, message = "Product_Image's length should be less than 2048 characters")
	private String productImage;
	
	@Size(max = 2048, message = "description's length should be less than 2048 characters")
	private String description;
	
	@Temporal(TemporalType.DATE)
	private Date enteredDate;
	
	@NotNull(message = "Favorite must not be null")
	public boolean favorite = false;
	
	@NotNull(message = "Is_Deleted must not be null")
	private boolean isDeleted = false;

	private Category category;
	
}
