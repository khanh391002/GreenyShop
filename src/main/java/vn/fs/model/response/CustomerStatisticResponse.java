package vn.fs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerStatisticResponse {
	String userName;
	
	String email;
	
	int totalSold;
	
	int totalSpent;
	
	int successOrders;
	
	int cancelOrders;
	
	String mostPurchasedProduct;
	
	int mostPurchasedQuantity;
}
