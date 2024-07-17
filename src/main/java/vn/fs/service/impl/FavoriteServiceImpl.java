package vn.fs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import vn.fs.model.dto.FavoriteDTO;
import vn.fs.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService{

	@Override
	public List<FavoriteDTO> selectAllSaves(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FavoriteDTO selectSaves(Long id, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(FavoriteDTO favorite, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(FavoriteDTO favorite, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(Long id, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectCountSave(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
