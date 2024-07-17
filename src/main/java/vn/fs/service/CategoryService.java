package vn.fs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import vn.fs.model.dto.CategoryDTO;

@Service
public interface CategoryService {

	public List<CategoryDTO> findAll();
	
	public CategoryDTO findById(Long id, Model model);

    String save(CategoryDTO categoryDTO, Model model);

    String update(CategoryDTO categoryDTO, Model model);

    String delete(Long id, Model model);
	
}
