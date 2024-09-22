package vn.fs.model.dto;

public interface ProductStatisticDTO {
	String getProductImage();
	
	String getProductName();
	
	int getTotalSold();
	
	double getTotalRevenue();
	
	int getInventory();
	
	int getFavorite();
	
	double getRating();
	
	int getCancelRate();
	
	int getSuccessRate();
}
