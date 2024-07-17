package vn.fs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import vn.fs.model.dto.FavoriteDTO;

@Service
public interface FavoriteService {
	public List<FavoriteDTO> selectAllSaves(Long userId);
	
	public FavoriteDTO selectSaves(Long id, Long userId);
	
	String save(FavoriteDTO favorite, Model model);

    String update(FavoriteDTO favorite, Model model);

    String delete(Long id, Model model);
    
    Integer selectCountSave(Long userId);
}
