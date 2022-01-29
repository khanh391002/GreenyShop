package vn.fs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.fs.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}