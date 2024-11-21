package vn.fs.model.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	Long productId;

	String productName;

	String productCode;

	int quantity;
	
	double price;
	
	int discount;

	String productImage;

	String description;
	
	Date enteredDate;
	
	Boolean status;

	boolean favorite;

	boolean isDeleted;
	
	Long categoryId;
	
	String categoryName;
	
	double evaluate;
}
