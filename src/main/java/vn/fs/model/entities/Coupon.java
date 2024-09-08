package vn.fs.model.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "expires", columnDefinition = "boolean default false")
    private Boolean expires;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted;

    public Coupon update(Coupon coupon) {
        this.code = coupon.getCode();
        this.discount = coupon.getDiscount();
        this.expirationDate = coupon.getExpirationDate();
        return this;
    }
}