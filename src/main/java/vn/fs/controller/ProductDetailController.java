package vn.fs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.commom.CommomDataService;
import vn.fs.commom.utils.Utils;
import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Comment;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.model.response.ProductResponse;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.CommentService;

@Controller
public class ProductDetailController extends CommomController{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
    private CommentService commentService;
	
	@Autowired
    private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	CommomDataService commomDataService;
	
	@Autowired
	FavoriteRepository favoriteRepository;

	@GetMapping(value = "productDetail")
	public String productDetail(@RequestParam("id") Long id, Model model, User user) {

		ProductDTO productDTO = productRepository.findOneById(id);
		if (Objects.isNull(productDTO)) {
			return null;
		}
		Product product = productRepository.findById(id).orElse(null);
		List<Long> productFavorites = favoriteRepository.getProductIdByUserId(user.getUserId());
		product.setEvaluate(productDTO.getEvaluate());
		model.addAttribute("product", product);

		commomDataService.commonData(model, user);
		listProductByCategory10(model, product.getCategory().getCategoryId(), productFavorites);
		
		List<Comment> comments = commentService.getRecentComments(id);
	    model.addAttribute("comments", comments);
		
		// Kiểm tra xem người dùng đã bình luận chưa
	    if (user != null) {
	        boolean hasCommented = comments.stream().anyMatch(comment -> comment.getUser().getUserId().equals(user.getUserId()));
	        Integer checkOrderIsSuccess = orderDetailRepository.getCustomerIsOrderSuccessByProductId(id, user.getUserId());
	        if (checkOrderIsSuccess == 1 && !hasCommented) {
	        	hasCommented = false;
			} else {
				hasCommented = true;
			}
	        model.addAttribute("hasCommented", hasCommented);
	    }

		return "web/productDetail";
	}
	
	// Gợi ý top 10 sản phẩm cùng loại
	public void listProductByCategory10(Model model, Long categoryId, List<Long> favoriteProducts) {
		List<ProductResponse> productResponses = new ArrayList<>();
		List<ProductDTO> products = productRepository.listProductByCategory(categoryId);
		Utils.buildProductResponses(products, productResponses, favoriteProducts);
		model.addAttribute("productByCategory", productResponses);
	}
	
	@GetMapping("/api/product-comments")
	@ResponseBody
	public List<Comment> getProductComments(@RequestParam Long productId, Model model, User user) {
		model.addAttribute("comments", commentService.getRecentComments(productId));  
		Product product = productRepository.findById(productId).orElse(null);
		List<Long> productFavorites = favoriteRepository.getProductIdByUserId(user.getUserId());
		model.addAttribute("product", product);

		commomDataService.commonData(model, user);
		listProductByCategory10(model, product.getCategory().getCategoryId(), productFavorites);
		return commentService.getRecentComments(productId);
	}
	
	@PostMapping("/productDetail/comment")
	public String addReview(@RequestParam("productId") Long productId, @RequestParam("starNumber") Integer starNumber,
			@RequestParam("review") String reviewContent, User user, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			commentService.addComment(productId, starNumber, reviewContent, user, model);
			redirectAttributes.addFlashAttribute("success", "Review added successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error adding review.");
		}
		commomDataService.commonData(model, user);
		return "redirect:/productDetail?id=" + productId;
	}
}
