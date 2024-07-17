package vn.fs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.fs.model.dto.OrderDTO;
import vn.fs.model.entities.Order;
import vn.fs.repository.OrderRepository;
import vn.fs.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private ModelMapper mapper;

	public List<OrderDTO> listAll() {
		List<Order> orders = repo.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<OrderDTO>();
		for(Order order : orders) {
			OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}

	@Override
	public List<OrderDTO> findOrderByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDTO findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
