package vn.fs.model.dto;

import java.util.Date;

public interface ProductDTO {
	Long getProductId();

	String getProductName();

	String getProductCode();

	int getQuantity();
	
	double getPrice();
	
	int getDiscount();

	String getProductImage();

	String getDescription();
	
	Date getEnteredDate();
	
	Boolean getStatus();

	boolean getFavorite();

	boolean getIsDeleted();
	
	Long getCategoryId();
	
	String getCategoryName();
	
	double getEvaluate();
}
