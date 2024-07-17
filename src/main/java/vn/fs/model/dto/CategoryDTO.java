package vn.fs.model.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	private Long categoryId;
	
	@NotNull(message = "Category_Name must not be null")
	private String categoryName;
	
	private String categoryImage;
}
