package vn.fs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.fs.model.dto.OrderDTO;

@Service
public interface OrderService {
	public List<OrderDTO> listAll();
	
	public List<OrderDTO> findOrderByUserId(Long userId);
	
	public OrderDTO findById(Long id);
}
