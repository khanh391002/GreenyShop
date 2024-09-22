package vn.fs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.model.dto.OrderStatisticDTO;
import vn.fs.model.dto.OrderYearStatisticDTO;
import vn.fs.model.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "select * from orders where user_id = ?1", nativeQuery = true)
	List<Order> findOrderByUserId(Long id);

	@Query(value = "SELECT "
			+ "    COUNT(CASE WHEN status = 2 THEN 1 END) AS totalOrderSuccess, "
			+ "    COUNT(CASE WHEN status = 3 THEN 1 END) AS totalOrderCancel "
			+ "FROM orders "
			+ "WHERE YEAR(order_date) =:year ", nativeQuery = true)
	OrderYearStatisticDTO getOrderStatisticByYear(@Param("year") String year); 
	
	@Query(value = "SELECT "
			+ "    COUNT(CASE WHEN status = 2 THEN 1 END) AS totalOrderSuccess, "
			+ "    COUNT(CASE WHEN status = 3 THEN 1 END) AS totalOrderCancel "
			+ "FROM orders "
			+ "WHERE MONTH(order_date) =:month ", nativeQuery = true)
	OrderYearStatisticDTO getOrderStatisticByMonth(@Param("month") String month); 
	
	@Query(value = "SELECT "
			+ "    COUNT(CASE WHEN status = 2 THEN 1 END) AS totalOrderSuccess, "
			+ "    COUNT(CASE WHEN status = 3 THEN 1 END) AS totalOrderCancel "
			+ "FROM orders "
			+ "WHERE QUARTER(order_date) =:quarter ", nativeQuery = true)
	OrderYearStatisticDTO getOrderStatisticByQuarter(@Param("quarter") String quarter); 
	
	@Query(value = "SELECT "
			+ "    COUNT(CASE WHEN status = 0 THEN 1 END) AS orderNew, "
			+ "    COUNT(CASE WHEN status = 1 THEN 1 END) AS orderDelivery, "
			+ "    COUNT(CASE WHEN status = 2 THEN 1 END) AS orderSuccess "
			+ "FROM orders "
			+ "WHERE DATE(order_date) = CURDATE() ", nativeQuery = true)
	OrderStatisticDTO countOrderStatisticToday(); 
	
	@Query(value = "select * FROM greeny_shop.orders order by order_date DESC ", nativeQuery = true)
	List<Order> findAllByOrderDateDesc();
}
