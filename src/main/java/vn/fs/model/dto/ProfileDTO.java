package vn.fs.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
	@NotNull(message = "Name must not be null")
	private String name;
	
	@NotNull(message = "Email must not be null")
	private String email;
	
	private String avatar;
	
	private String address;
}
