package vn.fs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {
	private Long favoriteId;

	private UserDTO user;

	private ProductDTO product;
}
