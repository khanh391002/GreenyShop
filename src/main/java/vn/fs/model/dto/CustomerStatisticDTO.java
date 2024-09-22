package vn.fs.model.dto;

public interface CustomerStatisticDTO {
	String getUserName();
	
	String getEmail();
	
	int getTotalSold();
	
	int getTotalSpent();
	
	int getSuccessOrders();
	
	int getCancelOrders();
	
	String getMostPurchasedProduct();
	
	int getMostPurchasedQuantity();
	
}
