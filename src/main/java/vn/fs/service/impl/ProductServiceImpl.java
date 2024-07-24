package vn.fs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import vn.fs.model.dto.ProductDTO;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.repository.ProductRepository;
import vn.fs.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
	
	@Override
	public ProductDTO findById(Long id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isPresent()) {
			ProductDTO productDTO = mapper.map(productOpt.get(), ProductDTO.class);
			return productDTO;
		}
		return null;
	}

	@Override
	public List<ProductDTO> findAllByOrderByEnteredDateAsc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDTO> findByInventoryIds(List<Integer> ids) {
		List<Product> listProducts = productRepository.findByInventoryIds(ids);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for(Product product : listProducts) {
			ProductDTO productDTO = mapper.map(product, ProductDTO.class);
			productDTOs.add(productDTO);
		}
		return productDTOs;
	}

	@Override
	public List<ProductDTO> listProductByCategory10(Long categoryId) {
		List<Product> products = productRepository.listProductByCategory10(categoryId);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for(Product product : products) {
			ProductDTO productDTO = mapper.map(product, ProductDTO.class);
			productDTOs.add(productDTO);
		}
		return productDTOs;
	}

	@Override
	public Page<Product> findPaginated(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> findPaginatSearch(Pageable pageable, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDTO> listProductByCategory(Long categoryId, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addProduct(ProductDTO productDTO, ModelMap model, MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(Long id, ProductDTO productDTO, ModelMap model, MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(Long id, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getByCategory(Long id) {
		List<Product> products = productRepository.findAllByCategoryCategoryIdAndIsDeletedIsFalse(id);
		return products;
	}

	@Override
    public List<ProductDTO> getRelatedByName(String name) {
		List<Product> products = productRepository.getByProductNameAndQuantityGreaterThanAndIsDeletedIsFalse(name, 0);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for(Product product : products) {
			ProductDTO productDTO = mapper.map(product, ProductDTO.class);
			productDTOs.add(productDTO);
		}
        return productDTOs;
    }

}
