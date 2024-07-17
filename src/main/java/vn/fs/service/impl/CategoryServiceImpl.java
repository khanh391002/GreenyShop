package vn.fs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import vn.fs.model.dto.CategoryDTO;
import vn.fs.model.entities.Category;
import vn.fs.repository.CategoryRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.CategoryService;
import vn.fs.service.ProductService;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryRepository.findAll();
		List<CategoryDTO> categoryDTOs = new ArrayList<>();
		for(Category category : categories) {
			CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);
			categoryDTOs.add(categoryDTO);
		}
		return categoryDTOs;
	}

	@Override
	public CategoryDTO findById(Long id, Model model) {
		Optional<Category> categoryOpt = categoryRepository.findById(id);
		if (!categoryOpt.isPresent()) {
			model.addAttribute("err", "Category is not existed");
			return null;
		}
		return mapper.map(categoryOpt.get(), CategoryDTO.class);
	}
	
	public CategoryDTO findByName(String name) {
		Optional<Category> categoryOpt = categoryRepository.findByCategoryName(name);
		if (!categoryOpt.isPresent()) {
			return null;
		}
		return mapper.map(categoryOpt.get(), CategoryDTO.class);
	}

	@Override
	@Transactional
	public String save(CategoryDTO categoryDTO, Model model) {
		if (findByName(categoryDTO.getCategoryName()) != null)
			return "admin/categories";
		categoryRepository.save(mapper.map(categoryDTO, Category.class));
        return "redirect:/admin/categories";
	}

	@Override
	@Transactional
	public String update(CategoryDTO categoryDTO, Model model) {
		CategoryDTO getById = findById(categoryDTO.getCategoryId(), model);
        if (getById == null || findByName(categoryDTO.getCategoryName()) != null)
            return "admin/categories";
        Category category = mapper.map(getById, Category.class);
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryImage(categoryDTO.getCategoryImage());
        categoryRepository.save(mapper.map(getById, Category.class));
        return "redirect:/admin/categories";
	}

	@Override
	@Transactional
	public String delete(Long id, Model model) {
		CategoryDTO getId = findById(id, model);
        if (getId != null) {
//            productService.deletes(productService.getByCategory(id), model);
            productRepository.deleteAll(productService.getByCategory(id));
            categoryRepository.deleteById(getId.getCategoryId());;
        }
        return "redirect:/admin/categories";
	}

}
