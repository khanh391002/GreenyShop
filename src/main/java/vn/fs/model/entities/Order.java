package vn.fs.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "orders")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "order_date")
	private Date orderDate;
	
	private Double amount;
	
	@Size(max = 225, message = "Address's length should be less than 255 characters")
	private String address;
	
	@Size(max = 12, message = "Phone's length should be less than 12 characters")
	private String phone;
	
	@NotNull(message = "Status must not be null")
	private int status;

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

}
