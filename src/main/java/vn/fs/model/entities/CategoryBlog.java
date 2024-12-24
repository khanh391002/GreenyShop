package vn.fs.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "categorie_blogs", uniqueConstraints = { @UniqueConstraint(name = "code_uq", columnNames = { "code" }) })
public class CategoryBlog implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_blog_id", length = 225)
	private Long categoryBlogId;
	
	@NotNull(message = "Code must not be null")
	@Size(max = 50, message = "Code length should be less than 255 characters")
	@Column(name = "code", length = 50)
	private String code;
	
	@NotNull(message = "Name must not be null")
	@Size(max = 225, message = "Name's length should be less than 255 characters")
	@Column(name = "name", length = 225)
	private String name;
}
