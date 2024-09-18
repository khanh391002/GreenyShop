package vn.fs.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "coupon", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"code"})} , indexes = {
	    @Index(name = "code_idx", columnList = "code")
	})
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "discount")
    private Integer discount;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "expires", columnDefinition = "boolean default false")
    private Boolean expires = false;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

    public Coupon update(Coupon coupon) {
        this.code = coupon.getCode();
        this.discount = coupon.getDiscount();
        this.expirationDate = coupon.getExpirationDate();
        return this;
    }
}