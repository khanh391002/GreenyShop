package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.fs.model.entities.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>{
	
	Optional<Coupon> getByCodeAndExpiresFalseAndDeletedFalse(String code);
	
	Optional<Coupon> findByCodeAndDeletedFalse(String code);
	
	Optional<Coupon> findByCode(String code);
	
	List<Coupon> findAllByDeletedFalse();
	
	Optional<Coupon> findByIdAndDeletedFalse(Long id);
}
