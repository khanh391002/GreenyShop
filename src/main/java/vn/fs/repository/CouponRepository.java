package vn.fs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>{
	Optional<Coupon> findByCodeAndExpiresFalseAndDeletedFalse(String code);
}
