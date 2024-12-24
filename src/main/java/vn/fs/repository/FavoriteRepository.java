package vn.fs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.model.dto.CountFavoriteProductUserDTO;
import vn.fs.model.entities.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	// favorite
	@Query(value = "SELECT * FROM favorites where product_id  = ? and user_id = ?;", nativeQuery = true)
	public Optional<Favorite> selectSaves(Long productId, Long userId);

	@Query(value = "SELECT * FROM favorites where user_id = ?;", nativeQuery = true)
	public List<Favorite> selectAllSaves(Long userId);

	@Query(value = "SELECT Count(favorite_id)  FROM favorites  where user_id = ?;", nativeQuery = true)
	public Integer selectCountSave(Long userId);
	
	@Query(value = "SELECT product_id FROM favorites where user_id = :userId ", nativeQuery = true)
	public List<Long> getProductIdByUserId(@Param("userId") Long userId);
	
	@Query(value = "SELECT product_id AS productId, COUNT(user_id) AS countUsers "
			+ "FROM greeny_shop.favorites "
			+ "GROUP BY product_id; ", nativeQuery = true)
	public List<CountFavoriteProductUserDTO> countFavoriteProductUser();

}
