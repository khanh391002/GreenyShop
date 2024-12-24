package vn.fs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import vn.fs.commom.CommomDataService;
import vn.fs.commom.utils.Utils;
import vn.fs.model.dto.CommentDTO;
import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Favorite;
import vn.fs.model.entities.User;
import vn.fs.model.response.CommentResponse;
import vn.fs.model.response.ProductResponse;
import vn.fs.repository.CommentRepository;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.ProductRepository;

@Controller
public class HomeController extends CommomController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CommomDataService commomDataService;

	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Autowired
	CommentRepository commentRepository;

	@GetMapping(value = "/")
	public String home(Model model, User user) {

		commomDataService.commonData(model, user);
		bestSaleProduct20(model, user);
		lastestComments(model);
		return "web/home";
	}

	// list product ở trang chủ limit 10 sản phẩm mới nhất
	@ModelAttribute("listProduct10")
	public List<ProductResponse> listproduct10(Model model) {
//		List<Product> productList = productRepository.listProductNew20();
		List<ProductDTO> productList = productRepository.listProductNew20();
		List<ProductResponse> productResponses = new ArrayList<>();
		Utils.buildProductResponses(productList, productResponses, new ArrayList<>());
		model.addAttribute("productList", productResponses);
		return productResponses;
	}

	// Top 20 best sale.
	public void bestSaleProduct20(Model model, User customer) {
		List<Object[]> productList = productRepository.bestSaleProduct20();
		if (productList != null) {
			ArrayList<Integer> listIdProductArrayList = new ArrayList<>();
			for (int i = 0; i < productList.size(); i++) {
				String id = String.valueOf(productList.get(i)[0]);
				listIdProductArrayList.add(Integer.valueOf(id));
			}
//			List<Product> listProducts = productRepository.findByInventoryIds(listIdProductArrayList);
			List<ProductDTO> productDTOs = productRepository.findByInventoryIds(listIdProductArrayList);
			List<ProductResponse> listProducts = new ArrayList<>();
			Utils.buildProductResponses(productDTOs, listProducts, new ArrayList<>());

			List<ProductResponse> listProductNew = new ArrayList<>();
//			List<Product> listProductNew = new ArrayList<>();

			for (ProductResponse product : listProducts) {

				ProductResponse productEntity = new ProductResponse();

				BeanUtils.copyProperties(product, productEntity);

				Optional<Favorite> save = favoriteRepository.selectSaves(productEntity.getProductId(),
						customer.getUserId());

				if (save.isPresent()) {
					productEntity.setFavorite(true);
				} else {
					productEntity.setFavorite(false);
				}
				listProductNew.add(productEntity);

			}

			model.addAttribute("bestSaleProduct20", listProductNew);
		}
	}

	// Top 5 latest comments .
	@ModelAttribute("lastestComments")
	public List<CommentResponse> lastestComments(Model model) {
		List<CommentDTO> commentDTOs = commentRepository.getTopLastestComments(5);
		List<CommentResponse> commentResponses = new ArrayList<>();
		Utils.buildCommentResponses(commentDTOs, commentResponses);
		model.addAttribute("lastestComments", commentResponses);
		return commentResponses;
	}

}
