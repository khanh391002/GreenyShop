package vn.fs.model.dto;

public interface CategoryStatisticDTO {
	
	Long getId();
	
	String getCategoryName();
	
	int getProductCount();
	
	int getTotalSold();
	
	double getTotalRevenue();
	
	int getInventory();
	
	int getCancelRate();
	
	int getSuccessRate();
	
	double getRating();
}
