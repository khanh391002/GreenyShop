package vn.fs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeStatisticResponse {
	String time;
	
	int quantitySold;
	
	int revenue;
	
	int totalOrderSuccess;
	
	int totalOrderCancel;
	
	String topCustomer;
	
	String bestSellingProduct;
	
	String bestSellingCategory;
	
	double rating;
}
