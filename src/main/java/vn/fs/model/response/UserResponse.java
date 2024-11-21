package vn.fs.model.response;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.fs.model.entities.Role;

@Getter
@Setter
public class UserResponse {
	private Long id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String avatar;
	
	private Date registerDate;
	
	private String address;
	
	private String phone;
	
	private Boolean status;

	private List<Role> roles;
}
