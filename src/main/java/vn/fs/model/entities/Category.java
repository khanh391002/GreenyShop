package vn.fs.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	@NotNull(message = "Category_Name must not be null")
	@Size(max = 225, message = "Category_Name's length should be less than 255 characters")
	@Column(name = "category_name", length = 225)
	private String categoryName;
	
	@Size(max = 2048, message = "Category_Image's length should be less than 2048 characters")
	@Column(name = "category_image", length = 2048)
	private String categoryImage;

}
