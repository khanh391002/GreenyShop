package vn.fs.commom.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import vn.fs.model.dto.CommentDTO;
import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Product;
import vn.fs.model.response.CommentResponse;
import vn.fs.model.response.ProductResponse;

public class Utils {

	public static String getBaseURL(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);
		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		if (url.toString().endsWith("/")) {
			url.append("/");
		}
		return url.toString();
	}

	public static void buildProductResponses(List<ProductDTO> productDTOs, List<ProductResponse> productResponses,
			List<Long> favoriteProducts) {
		for (ProductDTO dto : productDTOs) {
			ProductResponse productResponse = new ProductResponse();
			productResponse.setProductId(dto.getProductId());
			productResponse.setProductName(dto.getProductName());
			productResponse.setProductCode(dto.getProductCode());
			productResponse.setQuantity(dto.getQuantity());
			productResponse.setPrice(dto.getPrice());
			productResponse.setDiscount(dto.getDiscount());
			productResponse.setProductImage(dto.getProductImage());
			productResponse.setDescription(dto.getDescription());
			productResponse.setEnteredDate(dto.getEnteredDate());
			productResponse.setStatus(dto.getStatus());
			if (!CollectionUtils.isEmpty(favoriteProducts) && favoriteProducts.contains(dto.getProductId())) {
				productResponse.setFavorite(true);
			} else {
				productResponse.setFavorite(false);
			}
			productResponse.setDeleted(dto.getIsDeleted());
			productResponse.setCategoryId(dto.getCategoryId());
			productResponse.setCategoryName(dto.getCategoryName());
			productResponse.setEvaluate(dto.getEvaluate());
			productResponses.add(productResponse);
		}
	}

	public static void buildProductResponse(ProductDTO productDTO, ProductResponse productResponse) {
		productResponse.setProductId(productDTO.getProductId());
		productResponse.setProductName(productDTO.getProductName());
		productResponse.setProductCode(productDTO.getProductCode());
		productResponse.setQuantity(productDTO.getQuantity());
		productResponse.setPrice(productDTO.getPrice());
		productResponse.setDiscount(productDTO.getDiscount());
		productResponse.setProductImage(productDTO.getProductImage());
		productResponse.setDescription(productDTO.getDescription());
		productResponse.setEnteredDate(productDTO.getEnteredDate());
		productResponse.setStatus(productDTO.getStatus());
		productResponse.setFavorite(productDTO.getFavorite());
		productResponse.setDeleted(productDTO.getIsDeleted());
		productResponse.setCategoryId(productDTO.getCategoryId());
		productResponse.setCategoryName(productDTO.getCategoryName());
		productResponse.setEvaluate(productDTO.getEvaluate());
	}
	
	public static void buildProductResponse(Product product, ProductResponse productResponse) {
		productResponse.setProductId(product.getProductId());
		productResponse.setProductName(product.getProductName());
		productResponse.setProductCode(product.getProductCode());
		productResponse.setQuantity(product.getQuantity());
		productResponse.setPrice(product.getPrice());
		productResponse.setDiscount(product.getDiscount());
		productResponse.setProductImage(product.getProductImage());
		productResponse.setDescription(product.getDescription());
		productResponse.setEnteredDate(product.getEnteredDate());
		productResponse.setStatus(product.getStatus());
		productResponse.setFavorite(product.isFavorite());
		productResponse.setDeleted(product.isDeleted());
		productResponse.setCategoryId(product.getCategory().getCategoryId());
		productResponse.setCategoryName(product.getCategory().getCategoryName());
		productResponse.setCategory(product.getCategory());
//		productResponse.setEvaluate(product.getEvaluate());
	}

	public static void buildCommentResponses(List<CommentDTO> commentDTOs, List<CommentResponse> commentResponses) {
		for (CommentDTO dto : commentDTOs) {
			CommentResponse commentResponse = new CommentResponse();
			commentResponse.setId(dto.getId());
			commentResponse.setRateDate(dto.getRateDate());
			commentResponse.setContent(dto.getContent());
			commentResponse.setRating(dto.getRating());
			commentResponse.setOrderDetailId(dto.getOrderDetailId());
			commentResponse.setProductId(dto.getProductId());
			commentResponse.setUserId(dto.getUserId());
			commentResponse.setName(dto.getName());
			commentResponse.setAvatar(dto.getAvatar());
			commentResponses.add(commentResponse);
		}
	}

}
