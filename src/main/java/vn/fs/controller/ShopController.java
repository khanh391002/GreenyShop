package vn.fs.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.fs.commom.CommomDataService;
import vn.fs.commom.utils.Utils;
import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Favorite;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.model.response.ProductResponse;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.BlogService;
import vn.fs.service.CategoryBlogService;

@Controller
public class ShopController extends CommomController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	CategoryBlogService categoryBlogService;

	@Autowired
	BlogService blogService;

	@Autowired
	CommomDataService commomDataService;

	@GetMapping(value = "/products")
	public String shop(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, User user) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(12);

		Page<ProductResponse> productPage = findPaginated(PageRequest.of(currentPage - 1, pageSize), user);

		int totalPages = productPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		commomDataService.commonData(model, user);
		model.addAttribute("products", productPage);

		return "web/shop";
	}

	public Page<ProductResponse> findPaginated(Pageable pageable, User user) {

//		List<Product> productPage = productRepository.findAll();
		List<ProductDTO> productDTOs = productRepository.findAllProductAndAvgRating();
		List<Long> productFavorites = favoriteRepository.getProductIdByUserId(user.getUserId());
		List<ProductResponse> productResponses = new ArrayList<>();
		Utils.buildProductResponses(productDTOs, productResponses, productFavorites);

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<ProductResponse> list;

		if (productResponses.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, productResponses.size());
			list = productResponses.subList(startItem, toIndex);
		}

		Page<ProductResponse> productPages = new PageImpl<ProductResponse>(list, PageRequest.of(currentPage, pageSize),
				productResponses.size());

		return productPages;
	}

	// search product
	@GetMapping(value = "/searchProduct")
	public String showsearch(Model model, Pageable pageable, @RequestParam("keyword") String keyword,
			@RequestParam("size") Optional<Integer> size, @RequestParam("page") Optional<Integer> page, User user) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(12);

		Page<Product> productPage = findPaginatSearch(PageRequest.of(currentPage - 1, pageSize), keyword);

		int totalPages = productPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		commomDataService.commonData(model, user);
		model.addAttribute("products", productPage);
		return "web/shop";
	}

	// search product
	public Page<Product> findPaginatSearch(Pageable pageable, @RequestParam("keyword") String keyword) {

		List<Product> productPage = productRepository.searchProduct(keyword);

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Product> list;

		if (productPage.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, productPage.size());
			list = productPage.subList(startItem, toIndex);
		}

		Page<Product> productPages = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize),
				productPage.size());

		return productPages;
	}

	// list books by category
	@GetMapping(value = "/productByCategory")
	public String listProductbyid(Model model, @RequestParam("id") Long id, User user) {
		List<ProductDTO> products = productRepository.listProductByCategory(id);
		List<Long> productFavorites = favoriteRepository.getProductIdByUserId(user.getUserId());
		List<ProductResponse> productResponses = new ArrayList<>();
		Utils.buildProductResponses(products, productResponses, productFavorites);

		List<ProductResponse> listProductNew = new ArrayList<>();

		for (ProductResponse product : productResponses) {

			ProductResponse productEntity = new ProductResponse();

			BeanUtils.copyProperties(product, productEntity);

			Optional<Favorite> save = favoriteRepository.selectSaves(productEntity.getProductId(), user.getUserId());

			if (save.isPresent()) {
				productEntity.setFavorite(true);
			} else {
				productEntity.setFavorite(false);
			}
			listProductNew.add(productEntity);
		}

		model.addAttribute("products", listProductNew);
		commomDataService.commonData(model, user);
		return "web/shop";
	}
}
