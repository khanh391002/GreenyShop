package vn.fs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;

@Service
public interface ProductService {
	public ProductDTO findById(Long id);
	
	public List<ProductDTO> findAllByOrderByEnteredDateAsc();
	
	public List<ProductDTO> findByInventoryIds(List<Integer> ids);
	
	public List<ProductDTO> listProductByCategory10(Long categoryId);
	
	public Page<Product> findPaginated(Pageable pageable);
	
	public Page<Product> findPaginatSearch(Pageable pageable, String keyword);
	
	public List<ProductDTO> listProductByCategory(Long categoryId, User user); // shopController
	
	public String addProduct(ProductDTO productDTO, ModelMap model, MultipartFile file);
	
	public String update(Long id, ProductDTO productDTO, ModelMap model, MultipartFile file);
	
	public String delete(Long id, Model model);

	public List<Product> getByCategory(Long id);
	
	List<ProductDTO> getRelatedByName(String name);
}
