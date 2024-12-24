package vn.fs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatisticResponse {
	String productImage;
	
	String productName;
	
	int totalSold;
	
	double totalRevenue;
	
	int inventory;
	
	int favorite;
	
	double rating;
	
	long cancelRate;
	
	long successRate;
}
