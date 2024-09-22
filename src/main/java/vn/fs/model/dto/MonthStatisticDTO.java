package vn.fs.model.dto;

public interface MonthStatisticDTO {
	String getMonth();
	
	int getQuantitySold();
	
	int getRevenue();
	
	String getBestSellingProduct();
	
	String getBestSellingCategory();
	
	String getTopCustomer();
}
