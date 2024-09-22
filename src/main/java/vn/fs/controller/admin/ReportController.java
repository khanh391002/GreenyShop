package vn.fs.controller.admin;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.OrderDetail;
import vn.fs.model.entities.User;
import vn.fs.model.response.CategoryStatisticResponse;
import vn.fs.model.response.CustomerStatisticResponse;
import vn.fs.model.response.ProductStatisticResponse;
import vn.fs.model.response.TimeStatisticResponse;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.AdminStatisticService;

@Controller
public class ReportController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AdminStatisticService adminStatisticService;

	// Statistics by product sold
	@GetMapping(value = "/admin/reports")
	public String report(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<ProductStatisticResponse> productStatisticResponses = adminStatisticService.getProductStatistics();
		model.addAttribute("listReportCommon", productStatisticResponses);

		return "admin/statisticalProduct";
	}

	// Statistics by category sold
	@RequestMapping(value = "/admin/reportCategory")
	public String reportcategory(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<CategoryStatisticResponse> listReportCommon = adminStatisticService.getCategoryStatistics();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/statisticalCategory";
	}

	// Statistics of products sold by year
	@RequestMapping(value = "/admin/reportYear")
	public String reportyear(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<TimeStatisticResponse> listReportCommon = adminStatisticService.getYearStatistics();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/statisticalYear";
	}

	// Statistics of products sold by month
	@RequestMapping(value = "/admin/reportMonth")
	public String reportmonth(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<TimeStatisticResponse> listReportCommon = adminStatisticService.getMonthStatistics();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/statisticalMonth";
	}

	// Statistics of products sold by quarter
	@RequestMapping(value = "/admin/reportQuarter")
	public String reportquarter(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
//		List<Object[]> listReportCommon = orderDetailRepository.repoWhereQUARTER();
		List<TimeStatisticResponse> listReportCommon = adminStatisticService.getQuarterStatistics();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/statisticalQuarter";
	}

	// Statistics by user
	@RequestMapping(value = "/admin/reportOrderCustomer")
	public String reportordercustomer(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<CustomerStatisticResponse> listReportCommon = adminStatisticService.getCustomerStatistics();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/statisticalCustomer";
	}
	
	// end task developer by Khanh.NguyenQuoc

}
