package vn.fs.model.dto;

import java.util.Collection;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long userId;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String avatar;
	
	private String address;
	
	private Date registerDate;
	
	private Boolean status;

	private Collection<RoleDTO> roles; 
}
