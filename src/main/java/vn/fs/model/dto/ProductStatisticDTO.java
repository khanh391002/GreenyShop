package vn.fs.model.dto;

public interface ProductStatisticDTO {
	
	Long getProductId();
	
	String getProductImage();
	
	String getProductName();
	
	int getTotalSold();
	
	double getTotalRevenue();
	
	int getInventory();
	
	int getFavorite();
	
	double getRating();
	
	double getCancelRate();
	
	double getSuccessRate();
}
