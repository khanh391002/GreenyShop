package vn.fs.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vn.fs.commom.Constants;
import vn.fs.commom.logging.FXLogger;
import vn.fs.model.entities.Role;
import vn.fs.model.entities.User;
import vn.fs.repository.RoleRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.SendMailService;

@Controller
public class RegisterController {

	private static final FXLogger logger = new FXLogger(RegisterController.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	SendMailService sendMailService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	HttpSession session;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("/register")
	public ModelAndView registerForm(ModelMap model) {
		model.addAttribute("user", new User());
		logger.logApi(RequestMethod.GET, "web/register", model);
		return new ModelAndView("web/register", model);
	}

	@PostMapping("/register")
	public String register(ModelMap model, @Validated @ModelAttribute("user") User dto, BindingResult result,
			@RequestParam("password") String password) {
		if (result.hasErrors()) {
			return "web/register";
		}
		if (!checkEmail(dto.getEmail())) {
			model.addAttribute("error", "Email này đã được sử dụng!");
			return "web/register";
		}
		session.removeAttribute("otp");
		session.removeAttribute("otpTimestamp"); // Xóa thời gian OTP cũ nếu có
		// tạo mã otp mới
		int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
		session.setAttribute("otp", random_otp);
		long otpTimestamp = System.currentTimeMillis(); // Thời gian hiện tại (millisecond)
		session.setAttribute("otpTimestamp", otpTimestamp);
		String body = "<div>\r\n" + "<h2>Xin chào, <span style=\"color: #119744\" font-weight: bold;>" + dto.getName()
				+ "</span></h2>\r\n" + "<p>Cảm ơn bạn đã sử dụng ứng dụng của chúng tôi!</p>\r\n"
				+ "<h3>Mã xác thực OTP của bạn là: <span style=\"color:#119744; font-weight: bold;\">" + random_otp
				+ "</span></h3>\r\n" + "</div>\r\n" + "<p>Mã OTP có hiệu lực trong 5 phút</p>";
		sendMailService.queue(dto.getEmail(), "Đăng kí tài khoản", body);

		model.addAttribute("user", dto);
		model.addAttribute("message",
				"Mã xác thực OTP đã được gửi tới Email : " + dto.getEmail() + " , hãy kiểm tra Email của bạn!");

		return "/web/confirmOtpRegister";
	}

	@PostMapping("/confirmOtpRegister")
	public ModelAndView confirmRegister(ModelMap model, @ModelAttribute("user") User dto,
			@RequestParam("password") String password, @RequestParam("otp") String otp) {
//		@SuppressWarnings("unchecked")
//		LinkedHashMap<String, User> users = (LinkedHashMap<String, User>) model.get("user");
		User user = (User) model.get("user");
		Long otpTimestamp = (Long) session.getAttribute("otpTimestamp");
		if (otpTimestamp == null) {
			model.addAttribute("errorOTP", "Mã OTP đã hết hạn, vui lòng yêu cầu mã OTP mới!");
			return new ModelAndView("web/register", model);
		}
		// Kiểm tra xem mã OTP có hết hạn chưa (5 phút = 300000 milliseconds)
		long currentTime = System.currentTimeMillis();
		if (currentTime - otpTimestamp > 300000) { // Nếu thời gian đã quá 5 phút
			session.removeAttribute("otp"); // Xóa OTP cũ
			session.removeAttribute("otpTimestamp"); // Xóa thời gian OTP
			model.addAttribute("errorOTP", "Mã OTP đã hết hạn, vui lòng yêu cầu mã OTP mới!");
			return new ModelAndView("web/register", model);
		}
		if (otp.equals(String.valueOf(session.getAttribute("otp")))) {
			dto = new User();
			dto.setPassword(bCryptPasswordEncoder.encode(password));
			dto.setRegisterDate(new Date());
			dto.setStatus(true);
			dto.setAvatar("user_default.png");
			dto.setEmail(user.getEmail());
			dto.setName(user.getName());
			Optional<Role> roleOpt = roleRepository.findByName("ROLE_USER");
			if (roleOpt.isPresent()) {
				dto.setRoles(Arrays.asList(roleOpt.get()));
			} else {
				dto.setRoles(Arrays.asList(new Role("ROLE_USER")));
			}
			userRepository.save(dto);

			session.removeAttribute("otp");
			session.removeAttribute("otpTimestamp");
			String body = "<div>\r\n" + "<h2>Xin chào, <span style=\"color: #119744\" font-weight: bold;>"
					+ dto.getName() + "</span></h2>\r\n" + "<p>Chúc mừng bạn đã đăng ký tài khoản thành công!</p>\r\n"
					+ "<h3>Cửa hàng xin gửi tặng bạn mã giảm giá 20% dành riêng cho người dùng mới: <span style=\"color:#119744; font-weight: bold;\">"
					+ Constants.COUPON_NEW_MEMBER + "</span></h3>\r\n" + "</div>";
			sendMailService.queue(dto.getEmail(), "Đăng kí tài khoản thành công", body);
			model.addAttribute("message", "Đăng kí thành công");
			return new ModelAndView("web/login");
		}

		model.addAttribute("user", dto);
		model.addAttribute("error", "Mã xác thực OTP không chính xác, hãy thử lại!");
		return new ModelAndView("web/confirmOtpRegister", model);
	}

	// check email
	public boolean checkEmail(String email) {
		List<User> list = userRepository.findAll();
		for (User c : list) {
			if (c.getEmail().equalsIgnoreCase(email)) {
				return false;
			}
		}
		return true;
	}

}
