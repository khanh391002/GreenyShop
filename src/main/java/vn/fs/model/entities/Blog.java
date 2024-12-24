package vn.fs.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "blogs", uniqueConstraints = { @UniqueConstraint(name = "code_uq", columnNames = { "code" }) })
public class Blog implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Name must not be null")
	@Size(max = 225, message = "Name's length should be less than 255 characters")
	@Column(name = "name", length = 225)
	private String name;
	
	@NotNull(message = "Code must not be null")
	@Size(max = 50, message = "Code's length should be less than 50 characters")
	@Column(name = "code", length = 50) 
	private String code;
	
	@NotNull(message = "Title must not be null")
	private String title;

	@NotNull(message = "Product_Image must not be null")
	@Size(max = 2048, message = "Product_Image's length should be less than 2048 characters")
	@Column(name = "image", length = 2048)
	private String image;
	
	@Size(max = 2048, message = "description's length should be less than 2048 characters")
	@Column(length = 2048)
	private String description;
	
	@Size(max = 2048, message = "url's length should be less than 2048 characters")
	@Column(length = 2048)
	private String url;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date")
	private Date createDate;
	
	@NotNull(message = "Is_Deleted must not be null")
	private boolean isDeleted = false;

	@ManyToOne
	@JoinColumn(name = "category_blog_id")
	private CategoryBlog categoryBlog;

}
