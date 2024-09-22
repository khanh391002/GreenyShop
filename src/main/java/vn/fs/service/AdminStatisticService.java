package vn.fs.service;

import java.util.List;

import vn.fs.model.response.CategoryStatisticResponse;
import vn.fs.model.response.CustomerStatisticResponse;
import vn.fs.model.response.OrderStatisticResponse;
import vn.fs.model.response.ProductStatisticResponse;
import vn.fs.model.response.TimeStatisticResponse;

public interface AdminStatisticService {
	List<ProductStatisticResponse> getProductStatistics();
	
	List<CategoryStatisticResponse> getCategoryStatistics();
	
	List<CustomerStatisticResponse> getCustomerStatistics();
	
	List<TimeStatisticResponse> getYearStatistics();
	
	List<TimeStatisticResponse> getMonthStatistics();

	List<TimeStatisticResponse> getQuarterStatistics();

	OrderStatisticResponse countOrderStatisticToday();
}
