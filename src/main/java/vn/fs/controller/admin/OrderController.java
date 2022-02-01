package vn.fs.controller.admin;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vn.fs.dto.OrderExcelExporter;
import vn.fs.entities.Order;
import vn.fs.entities.OrderDetail;
import vn.fs.entities.Product;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.OrderDetailService;
import vn.fs.service.SendMailService;

@Controller
@RequestMapping("/admin")
public class OrderController {

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SendMailService sendMailService;

	// list order
	@GetMapping(value = "/orders")
	public String orders(Model model, Principal principal) {

		List<Order> orderDetails = orderRepository.findAll();
		model.addAttribute("orderDetails", orderDetails);

		return "admin/orders";
	}

	@GetMapping("/order/detail/{order_id}")
	public ModelAndView detail(ModelMap model, @PathVariable("order_id") Long id) {

		List<OrderDetail> listO = orderDetailRepository.findByOrderId(id);

		model.addAttribute("amount", orderRepository.findById(id).get().getAmount());
		model.addAttribute("orderDetail", listO);
		model.addAttribute("orderId", id);
		// set active front-end
		model.addAttribute("menuO", "menu");
		return new ModelAndView("admin/editOrder", model);
	}

	@RequestMapping("/order/cancel/{order_id}")
	public ModelAndView cancel(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/orders", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 3);
		orderRepository.save(oReal);

		return new ModelAndView("forward:/admin/orders", model);
	}

	@RequestMapping("/order/confirm/{order_id}")
	public ModelAndView confirm(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/orders", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 1);
		orderRepository.save(oReal);

//		sendMailAction(oReal, "Bạn có 1 đơn hàng ở KeyBoard Shop đã được xác nhận!",
//				"Chúng tôi sẽ sớm giao hàng cho bạn!", "Thông báo đơn hàng đã được xác nhận!");

		return new ModelAndView("forward:/admin/orders", model);
	}

	@RequestMapping("/order/delivered/{order_id}")
	public ModelAndView delivered(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/orders", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 2);
		orderRepository.save(oReal);

		Product p = null;
		List<OrderDetail> listDe = orderDetailRepository.findByOrderId(id);
		for (OrderDetail od : listDe) {
			p = od.getProduct();
			p.setQuantity(p.getQuantity() - od.getQuantity());
			productRepository.save(p);
		}

//		sendMailAction(oReal, "Bạn có 1 đơn hàng ở KeyBoard Shop đã thanh toán thành công!",
//				"Chúng tôi cám ơn bạn vì đã ủng hộ KeyBoard Shop!", "Thông báo thanh toán thành công!");

		return new ModelAndView("forward:/admin/orders", model);
	}

//	// send mail
//	public void sendMailAction(Order oReal, String status, String cmt, String notifycation) {
//		List<OrderDetail> list = orderDetailRepository.findByOrderId(oReal.getOrderId());
//		System.out.println(oReal.getOrderId());
//
//		StringBuilder stringBuilder = new StringBuilder();
//		int index = 0;
//		stringBuilder.append("<h3>Xin chào " + oReal.getUser().getName() + "!</h3>\r\n" + "    <h4>" + status + "</h4>\r\n"
//				+ "    <table style=\"border: 1px solid gray;\">\r\n"
//				+ "        <tr style=\"width: 100%; border: 1px solid gray;\">\r\n"
//				+ "            <th style=\"border: 1px solid gray;\">STT</th>\r\n"
//				+ "            <th style=\"border: 1px solid gray;\">Tên sản phẩm</th>\r\n"
//				+ "            <th style=\"border: 1px solid gray;\">Số lượng</th>\r\n"
//				+ "            <th style=\"border: 1px solid gray;\">Đơn giá</th>\r\n" + "        </tr>");
//		for (OrderDetail oD : list) {
//			index++;
//			stringBuilder.append("<tr>\r\n" + "            <td style=\"border: 1px solid gray;\">" + index + "</td>\r\n"
//					+ "            <td style=\"border: 1px solid gray;\">" + oD.getProduct().getName() + "</td>\r\n"
//					+ "            <td style=\"border: 1px solid gray;\">" + oD.getQuantity() + "</td>\r\n"
//					+ "            <td style=\"border: 1px solid gray;\">" + format(String.valueOf(oD.getUnitPrice()))
//					+ "</td>\r\n" + "        </tr>");
//		}
//		stringBuilder.append("\r\n" + "    </table>\r\n" + "    <h3>Tổng tiền: "
//				+ format(String.valueOf(oReal.getAmount())) + "</h3>\r\n" + "    <hr>\r\n" + "    <h5>" + cmt
//				+ "</h5>\r\n" + "    <h5>Chúc bạn 1 ngày tốt lành!</h5>");
//
//		sendMailService.queue(oReal.getUser().getEmail().trim(), notifycation, stringBuilder.toString());
//	}

	// to excel
	@GetMapping(value = "/export")
	public void exportToExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=orders.xlsx";

		response.setHeader(headerKey, headerValue);

		List<Order> lisOrders = orderDetailService.listAll();

		OrderExcelExporter excelExporter = new OrderExcelExporter(lisOrders);
		excelExporter.export(response);

	}

}