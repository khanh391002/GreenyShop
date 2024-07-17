package vn.fs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.fs.model.dto.OrderDetailDTO;

@Service
public interface OrderDetailService {
	List<OrderDetailDTO> findByOrderId(Long orderId);
}
