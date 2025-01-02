package vn.fs.model.dto;

public interface MonthStatisticDTO {
	String getMonth();
	
	String getYear();
	
	int getQuantitySold();
	
	int getRevenue();
	
	String getBestSellingProduct();
	
	String getBestSellingCategory();
	
	String getTopCustomer();
}
