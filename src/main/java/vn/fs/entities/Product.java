package vn.fs.entities;

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
@Table(name = "products")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	
	@NotNull(message = "Product name must not be null")
	@Size(max = 225, message = "Product_Name's length should be less than 255 characters")
	@Column(name = "product_name", length = 225)
	private String productName;
	
	@NotNull(message = "Quantity must not be null")
	private int quantity;
	
	@NotNull(message = "Price must not be null")
	private double price;
	
	@NotNull(message = "Discount must not be null")
	private int discount;
	
	@NotNull(message = "Product_Image must not be null")
	@Size(max = 2048, message = "Product_Image's length should be less than 2048 characters")
	@Column(name = "product_image", length = 2048)
	private String productImage;
	
	@Size(max = 2048, message = "description's length should be less than 2048 characters")
	@Column(length = 2048)
	private String description;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "entered_date")
	private Date enteredDate;
	
	private Boolean status;
	
	@NotNull(message = "Favorite must not be null")
	public boolean favorite = false;
	
	@NotNull(message = "Is_Deleted must not be null")
	private boolean isDeleted = false;

	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;

}
