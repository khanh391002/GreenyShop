package vn.fs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryStatisticResponse {

	String categoryName;
	
	int productCount;
	
	int totalSold;
	
	double totalRevenue;
	
	int inventory;
	
	int cancelRate;
	
	int successRate;
	
	double rating;
}
