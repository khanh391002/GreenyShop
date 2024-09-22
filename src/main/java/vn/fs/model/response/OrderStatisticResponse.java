package vn.fs.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatisticResponse {
	int orderNew;
	
	int orderDelivery;
	
	int orderSuccess;
}
