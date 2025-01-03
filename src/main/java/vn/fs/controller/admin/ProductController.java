package vn.fs.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.commom.utils.Utils;
import vn.fs.model.entities.Category;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.model.request.ProductRequest;
import vn.fs.model.response.ProductResponse;
import vn.fs.repository.CategoryRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class ProductController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	public ProductController(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	// show list product - table list
	@ModelAttribute("products")
	public List<Product> showProduct(Model model) {
		List<Product> products = productRepository.findAllByIsDeletedIsFalseAndOrderByEnteredDateDesc();
		List<ProductResponse> productResponses = new ArrayList<>();
		for(Product product : products) {
			ProductResponse productResponse = new ProductResponse();
			Utils.buildProductResponse(product, productResponse);
			if (product.getDescription().length() > 100) {
				productResponse.setDescription(product.getDescription().substring(0, 100) + "...");
            }
			productResponses.add(productResponse);
		}
		model.addAttribute("products", productResponses);

		return products;
	}

	@GetMapping(value = "/products")
	public String products(Model model, Principal principal) {
		Product product = new Product();
		model.addAttribute("product", product);

		return "admin/products";
	}

	@GetMapping(value = "/addProduct")
	public String showPopUpAddProduct(Model model) {
		ProductRequest productRequest = new ProductRequest();
		model.addAttribute("productRequest", productRequest);
		return "admin/createProduct";
	}

	// add product
	@PostMapping(value = "/addProduct")
	public String addProduct(@ModelAttribute("productRequest") ProductRequest productRequest, ModelMap model,
			@RequestParam("file") MultipartFile file, BindingResult result, HttpServletRequest httpServletRequest) {

		try {
			File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "web/notFound";
		}
		Product product = new Product();
		Optional<Product> productOpt = productRepository.findByProductCode(productRequest.getProductCode());
		if (productOpt.isPresent()) {
			if (!productOpt.get().isDeleted()) {
				model.addAttribute("errorCode", "Mã sản phẩm đã tồn tại!");
				return "redirect:/admin/addProduct";
			}
			product = productOpt.get();
			product.setDeleted(false);
		}
		Date currentDate = Date.from(Instant.now());
		product.setProductCode(productRequest.getProductCode());
		product.setProductName(productRequest.getProductName());
		product.setPrice(productRequest.getPrice());
		product.setDiscount(productRequest.getDiscount());
		product.setDescription(productRequest.getDescription());
		product.setEnteredDate(currentDate);
		product.setQuantity(productRequest.getQuantity());
		product.setCategory(productRequest.getCategory());
		product.setProductImage(file.getOriginalFilename());
		product = productRepository.save(product);
		if (null != product) {
			model.addAttribute("message", "Update success");
			model.addAttribute("product", product);
		} else {
			model.addAttribute("message", "Update failure");
			model.addAttribute("product", product);
		}
		return "redirect:/admin/products";
	}

	// show select option ở add product
	@ModelAttribute("categoryList")
	public List<Category> showCategory(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);

		return categoryList;
	}

	// get Edit brand
	@GetMapping(value = "/editProduct/{id}")
	public String showEditPage(@PathVariable("id") Long id, ModelMap model) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (!productOpt.isPresent()) {
			return "error";
		}
		Product product = productOpt.get();
		model.addAttribute("product", product);

		ProductRequest productRequest = new ProductRequest();
		productRequest.setProductCode(product.getProductCode());
		productRequest.setProductName(product.getProductName());
		productRequest.setPrice(product.getPrice());
		productRequest.setDiscount(product.getDiscount());
		productRequest.setDescription(product.getDescription());
		productRequest.setEnteredDate(product.getEnteredDate());
		productRequest.setQuantity(product.getQuantity());
		productRequest.setCategory(product.getCategory());
		productRequest.setProductImage(product.getProductImage());

		model.addAttribute("productRequest", productRequest);

		return "admin/editProduct";
	}

	// add product
	@PostMapping(value = "/editProduct/{id}")
	public String editProduct(@PathVariable("id") Long id,
			@ModelAttribute("productRequest") ProductRequest productRequest, ModelMap model,
			@RequestParam("file") MultipartFile file, BindingResult result, HttpServletRequest httpServletRequest) {
		String productImage = null;
		if (file.getSize() > 0) {
			productImage = file.getOriginalFilename();
			try {
				if (file.getSize() > 0) {
					productImage = file.getOriginalFilename();
				} else {
					productImage = productRequest.getProductImage();
				}
				File convFile = new File(pathUploadImage + "/" + (productImage));
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		} else {
			productImage = productRequest.getProductImage();
		}

		Optional<Product> productOptional = productRepository.findById(id);
		if (!productOptional.isPresent()) {
			return "error";
		}
		Product product = productOptional.get();
		model.addAttribute("product", product);

		product.setProductCode(productRequest.getProductCode());
		product.setProductName(productRequest.getProductName());
		product.setPrice(productRequest.getPrice());
		product.setDiscount(productRequest.getDiscount());
		product.setDescription(productRequest.getDescription());
		product.setEnteredDate(productRequest.getEnteredDate());
		product.setQuantity(productRequest.getQuantity());
		product.setCategory(productRequest.getCategory());
		product.setProductImage(productImage);
		product = productRepository.save(product);
		if (null != product) {
			model.addAttribute("message", "Update success");
			model.addAttribute("product", product);
		} else {
			model.addAttribute("message", "Update failure");
			model.addAttribute("product", product);
		}
		return "redirect:/admin/products";
	}

	// delete category
	@GetMapping("/deleteProduct/{id}")
	public String delProduct(@PathVariable("id") Long id, Model model) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (!productOpt.isPresent()) {
			model.addAttribute("error", "Sản phẩm không tồn tại!");
			return "redirect:/admin/products";
		}
		Product product = productOpt.get();
		product.setDeleted(true);
		product.setCategory(null);
		productRepository.save(product);
		model.addAttribute("message", "Delete successful!");

		return "redirect:/admin/products";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
}
