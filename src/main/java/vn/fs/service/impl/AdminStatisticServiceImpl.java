package vn.fs.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import vn.fs.model.dto.CategoryStatisticDTO;
import vn.fs.model.dto.CountFavoriteProductUserDTO;
import vn.fs.model.dto.CountProductOfCategoryDTO;
import vn.fs.model.dto.CustomerStatisticDTO;
import vn.fs.model.dto.MonthStatisticDTO;
import vn.fs.model.dto.OrderStatisticDTO;
import vn.fs.model.dto.OrderYearStatisticDTO;
import vn.fs.model.dto.ProductStatisticDTO;
import vn.fs.model.dto.QuarterStatisticDTO;
import vn.fs.model.dto.RatingProductDTO;
import vn.fs.model.dto.YearStatisticDTO;
import vn.fs.model.entities.Category;
import vn.fs.model.entities.Product;
import vn.fs.model.response.CategoryStatisticResponse;
import vn.fs.model.response.CustomerStatisticResponse;
import vn.fs.model.response.OrderStatisticResponse;
import vn.fs.model.response.ProductStatisticResponse;
import vn.fs.model.response.TimeStatisticResponse;
import vn.fs.repository.CategoryRepository;
import vn.fs.repository.CommentRepository;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.AdminStatisticService;

@Service
public class AdminStatisticServiceImpl implements AdminStatisticService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FavoriteRepository favoriteRepository;

	@Override
	public List<ProductStatisticResponse> getProductStatistics() {
		List<ProductStatisticResponse> productStatisticResponses = new ArrayList<>();
		List<Product> products = productRepository.findAll();
		List<CountFavoriteProductUserDTO> countFavoriteProductUsers = favoriteRepository.countFavoriteProductUser();
		List<RatingProductDTO> ratingProductDTOs = commentRepository.getAverateRatingByProduct();
		Map<Long, Product> productMaps = products.stream()
				.collect(Collectors.toMap(Product::getProductId, product -> product));
		Map<Long, Integer> productFavoriteMaps = countFavoriteProductUsers.stream().collect(Collectors
				.toMap(CountFavoriteProductUserDTO::getProductId, CountFavoriteProductUserDTO::getCountUsers));
		Map<Long, Double> ratingMaps = ratingProductDTOs.stream()
				.collect(Collectors.toMap(RatingProductDTO::getProductId, RatingProductDTO::getRating));
		List<ProductStatisticDTO> productStatisticDTOs = orderDetailRepository.getProductStatistic();
		for (ProductStatisticDTO dto : productStatisticDTOs) {
			ProductStatisticResponse productStatisticResponse = new ProductStatisticResponse();
			if (!CollectionUtils.isEmpty(productMaps)) {
				productStatisticResponse.setProductImage(productMaps.get(dto.getProductId()).getProductImage());
				productStatisticResponse.setProductName(productMaps.get(dto.getProductId()).getProductName());
				productStatisticResponse.setInventory(productMaps.get(dto.getProductId()).getQuantity());
			}
			if (!CollectionUtils.isEmpty(productFavoriteMaps) && !ObjectUtils.isEmpty(productFavoriteMaps.get(dto.getProductId()))) {
				productStatisticResponse.setFavorite(productFavoriteMaps.get(dto.getProductId()));
			} else {
				productStatisticResponse.setFavorite(0);
			}
			if (!CollectionUtils.isEmpty(ratingMaps) && !ObjectUtils.isEmpty(ratingMaps.get(dto.getProductId()))) {
				productStatisticResponse.setRating(roundToOneDecimal(ratingMaps.get(dto.getProductId())));
			} else {
				productStatisticResponse.setRating(5.0);
			}
			productStatisticResponse.setTotalSold(dto.getTotalSold());
			productStatisticResponse.setTotalRevenue(dto.getTotalRevenue());
			productStatisticResponse.setCancelRate(Math.round(dto.getCancelRate()));
			productStatisticResponse.setSuccessRate(Math.round(dto.getSuccessRate()));
			productStatisticResponses.add(productStatisticResponse);
		}
		return productStatisticResponses;
	}

	public static double roundToOneDecimal(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(1, RoundingMode.HALF_UP); // Làm tròn lên xuống
		return bd.doubleValue();
	}

	@Override
	public List<CategoryStatisticResponse> getCategoryStatistics() {
		List<CategoryStatisticResponse> categoryStatisticResponses = new ArrayList<>();
		List<CategoryStatisticDTO> categoryStatisticDTOs = orderDetailRepository.getCategoryStatistic();
		List<Category> categories = categoryRepository.findAll();
		List<Long> categoryIds = categories.stream().map(Category::getCategoryId).distinct()
				.collect(Collectors.toList());
		List<CountProductOfCategoryDTO> productOfCategoryDTOs = productRepository
				.countProductByCategoryIdsByIsDeletedIsFalse(categoryIds);
		// Map product count of category
		Map<Long, Integer> categoryProductMap = productOfCategoryDTOs.stream().collect(
				Collectors.toMap(CountProductOfCategoryDTO::getCategoryId, category -> category.getTotalProduct()));

		// Map quantity product of category
		Map<Long, Integer> categoryQuantityMap = (Map<Long, Integer>) productOfCategoryDTOs.stream().collect(
				Collectors.toMap(CountProductOfCategoryDTO::getCategoryId, category -> category.getQuantity()));

		for (CategoryStatisticDTO dto : categoryStatisticDTOs) {
			CategoryStatisticResponse categoryStatisticResponse = new CategoryStatisticResponse();
			categoryStatisticResponse.setCategoryName(dto.getCategoryName());
			categoryStatisticResponse.setTotalSold(dto.getTotalSold());
			categoryStatisticResponse.setTotalRevenue(dto.getTotalRevenue());
			if (categoryProductMap.containsKey(dto.getId()) && categoryProductMap.get(dto.getId()) != null) {
				categoryStatisticResponse.setProductCount(categoryProductMap.get(dto.getId()));
			} else {
				categoryStatisticResponse.setProductCount(0);
			}
			if (categoryQuantityMap.containsKey(dto.getId()) && categoryQuantityMap.get(dto.getId()) != null) {
				categoryStatisticResponse.setInventory(categoryQuantityMap.get(dto.getId()));
			} else {
				categoryStatisticResponse.setInventory(0);
			}
			categoryStatisticResponse.setRating(roundToOneDecimal(dto.getRating()));
			categoryStatisticResponse.setCancelRate(dto.getCancelRate());
			categoryStatisticResponse.setSuccessRate(dto.getSuccessRate());
			categoryStatisticResponses.add(categoryStatisticResponse);
		}
		return categoryStatisticResponses;
	}

	@Override
	public List<CustomerStatisticResponse> getCustomerStatistics() {
		List<CustomerStatisticResponse> customerStatisticResponses = new ArrayList<>();
		List<CustomerStatisticDTO> customerStatisticDTOs = orderDetailRepository.getCustomerStatistic();
		for (CustomerStatisticDTO dto : customerStatisticDTOs) {
			CustomerStatisticResponse customerStatisticResponse = new CustomerStatisticResponse();
			customerStatisticResponse.setUserName(dto.getUserName());
			customerStatisticResponse.setEmail(dto.getEmail());
			customerStatisticResponse.setTotalSold(dto.getTotalSold());
			customerStatisticResponse.setTotalSpent(dto.getTotalSpent());
			customerStatisticResponse.setSuccessOrders(dto.getSuccessOrders());
			customerStatisticResponse.setCancelOrders(dto.getCancelOrders());
			customerStatisticResponse.setMostPurchasedProduct(dto.getMostPurchasedProduct());
			customerStatisticResponse.setMostPurchasedQuantity(dto.getMostPurchasedQuantity());
			customerStatisticResponses.add(customerStatisticResponse);
		}
		return customerStatisticResponses;
	}

	@Override
	public List<TimeStatisticResponse> getYearStatistics() {
		List<TimeStatisticResponse> yearStatisticResponses = new ArrayList<>();
		List<YearStatisticDTO> yearStatisticDTOs = orderDetailRepository.getYearStatistic();
		for (YearStatisticDTO dto : yearStatisticDTOs) {
			TimeStatisticResponse yearStatisticResponse = new TimeStatisticResponse();
			yearStatisticResponse.setTime(dto.getYear());
			yearStatisticResponse.setQuantitySold(dto.getQuantitySold());
			yearStatisticResponse.setRevenue(dto.getRevenue());
			yearStatisticResponse.setTopCustomer(dto.getTopCustomer());
			yearStatisticResponse.setBestSellingProduct(dto.getBestSellingProduct());
			yearStatisticResponse.setBestSellingCategory(dto.getBestSellingCategory());
			// tính trung bình đánh giá trong năm
			yearStatisticResponse.setRating(roundToOneDecimal(commentRepository.getRatingOfTheYear(dto.getYear())));
			// Lấy thông tin số đơn thành công, bị huỷ trong năm
			OrderYearStatisticDTO orderYearStatisticDTO = orderRepository.getOrderStatisticByYear(dto.getYear());
			if (!Objects.isNull(orderYearStatisticDTO)) {
				yearStatisticResponse.setTotalOrderSuccess(orderYearStatisticDTO.getTotalOrderSuccess());
				yearStatisticResponse.setTotalOrderCancel(orderYearStatisticDTO.getTotalOrderCancel());
			} else {
				yearStatisticResponse.setTotalOrderSuccess(0);
				yearStatisticResponse.setTotalOrderCancel(0);
			}
			yearStatisticResponses.add(yearStatisticResponse);
		}
		return yearStatisticResponses;
	}

	@Override
	public List<TimeStatisticResponse> getMonthStatistics() {
		List<TimeStatisticResponse> monthStatisticResponses = new ArrayList<>();
		List<MonthStatisticDTO> monthStatisticDTOs = orderDetailRepository.getMonthStatistic();
		for (MonthStatisticDTO dto : monthStatisticDTOs) {
			TimeStatisticResponse monthStatisticResponse = new TimeStatisticResponse();
			monthStatisticResponse.setTime(dto.getMonth());
			monthStatisticResponse.setQuantitySold(dto.getQuantitySold());
			monthStatisticResponse.setRevenue(dto.getRevenue());
			monthStatisticResponse.setTopCustomer(dto.getTopCustomer());
			monthStatisticResponse.setBestSellingProduct(dto.getBestSellingProduct());
			monthStatisticResponse.setBestSellingCategory(dto.getBestSellingCategory());
			// tính trung bình đánh giá trong năm
			monthStatisticResponse.setRating(roundToOneDecimal(commentRepository.getRatingOfTheMonth(dto.getMonth())));
			// Lấy thông tin số đơn thành công, bị huỷ trong năm
			OrderYearStatisticDTO orderMonthStatisticDTO = orderRepository.getOrderStatisticByMonth(dto.getMonth());
			if (!Objects.isNull(orderMonthStatisticDTO)) {
				monthStatisticResponse.setTotalOrderSuccess(orderMonthStatisticDTO.getTotalOrderSuccess());
				monthStatisticResponse.setTotalOrderCancel(orderMonthStatisticDTO.getTotalOrderCancel());
			} else {
				monthStatisticResponse.setTotalOrderSuccess(0);
				monthStatisticResponse.setTotalOrderCancel(0);
			}
			monthStatisticResponses.add(monthStatisticResponse);
		}
		return monthStatisticResponses;
	}

	@Override
	public List<TimeStatisticResponse> getQuarterStatistics() {
		List<TimeStatisticResponse> quarterStatisticResponses = new ArrayList<>();
		List<QuarterStatisticDTO> quarterStatisticDTOs = orderDetailRepository.getQuarterStatistic();
		for (QuarterStatisticDTO dto : quarterStatisticDTOs) {
			TimeStatisticResponse quarterStatisticResponse = new TimeStatisticResponse();
			quarterStatisticResponse.setTime(dto.getQuarter());
			quarterStatisticResponse.setQuantitySold(dto.getQuantitySold());
			quarterStatisticResponse.setRevenue(dto.getRevenue());
			quarterStatisticResponse.setTopCustomer(dto.getTopCustomer());
			quarterStatisticResponse.setBestSellingProduct(dto.getBestSellingProduct());
			quarterStatisticResponse.setBestSellingCategory(dto.getBestSellingCategory());
			// tính trung bình đánh giá trong năm
			quarterStatisticResponse
					.setRating(roundToOneDecimal(commentRepository.getRatingOfTheQuarter(dto.getQuarter())));
			// Lấy thông tin số đơn thành công, bị huỷ trong năm
			OrderYearStatisticDTO orderQuarterStatisticDTO = orderRepository
					.getOrderStatisticByQuarter(dto.getQuarter());
			if (!Objects.isNull(orderQuarterStatisticDTO)) {
				quarterStatisticResponse.setTotalOrderSuccess(orderQuarterStatisticDTO.getTotalOrderSuccess());
				quarterStatisticResponse.setTotalOrderCancel(orderQuarterStatisticDTO.getTotalOrderCancel());
			} else {
				quarterStatisticResponse.setTotalOrderSuccess(0);
				quarterStatisticResponse.setTotalOrderCancel(0);
			}
			quarterStatisticResponses.add(quarterStatisticResponse);
		}
		return quarterStatisticResponses;
	}

	@Override
	public OrderStatisticResponse countOrderStatisticToday() {
		OrderStatisticDTO orderStatisticDTO = orderRepository.countOrderStatisticToday();
		OrderStatisticResponse orderStatisticResponse = new OrderStatisticResponse();
		orderStatisticResponse.setOrderNew(orderStatisticDTO.getOrderNew());
		orderStatisticResponse.setOrderDelivery(orderStatisticDTO.getOrderDelivery());
		orderStatisticResponse.setOrderSuccess(orderStatisticDTO.getOrderSuccess());
		return orderStatisticResponse;
	}
}
