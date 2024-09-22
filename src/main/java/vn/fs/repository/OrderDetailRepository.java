package vn.fs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.fs.model.dto.CategoryStatisticDTO;
import vn.fs.model.dto.CustomerStatisticDTO;
import vn.fs.model.dto.MonthStatisticDTO;
import vn.fs.model.dto.ProductStatisticDTO;
import vn.fs.model.dto.QuarterStatisticDTO;
import vn.fs.model.dto.YearStatisticDTO;
import vn.fs.model.entities.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	@Query(value = "select * from order_details where order_id = ?;", nativeQuery = true)
	List<OrderDetail> findByOrderId(Long id);

	// Statistics by product sold
	@Query(value = "SELECT p.product_name , \r\n" + "SUM(o.quantity) as quantity ,\r\n"
			+ "SUM(o.quantity * o.price) as sum,\r\n" + "AVG(o.price) as avg,\r\n" + "Min(o.price) as min, \r\n"
			+ "max(o.price) as max\r\n" + "FROM order_details o\r\n"
			+ "INNER JOIN products p ON o.product_id = p.product_id\r\n"
			+ "GROUP BY p.product_name;", nativeQuery = true)
	public List<Object[]> repo();

	// Statistics by product sold
	@Query(value = "SELECT p.product_image as productImage, p.product_name as productName, SUM(o.quantity) as totalSold, "
			+ "SUM(o.quantity * o.price) as totalRevenue, p.quantity as inventory, "
			+ "COUNT(DISTINCT f.user_id) as favorite, COALESCE(AVG(c.rating), 0) as rating, " + "ROUND(CASE "
			+ "	WHEN COUNT(o.order_id) = 0 THEN 0  "
			+ "	ELSE (COALESCE(SUM(CASE WHEN ord.status = 3 THEN o.quantity END), 0) * 100.0 / COUNT(o.order_id)) "
			+ "END) AS cancelRate, " + "ROUND(CASE " + "	WHEN COUNT(o.order_id) = 0 THEN 0 "
			+ "	ELSE (COALESCE(SUM(CASE WHEN ord.status = 2 THEN o.quantity END), 0) * 100.0 / COUNT(o.order_id)) "
			+ "END) AS successRate " + "FROM order_details o " + "INNER JOIN products p ON o.product_id = p.product_id "
			+ "INNER JOIN orders ord ON o.order_id = ord.order_id "
			+ "LEFT JOIN comments c ON p.product_id = c.product_id "
			+ "LEFT JOIN favorites f ON p.product_id = f.product_id "
			+ "GROUP BY p.product_name, productImage, inventory " + "order by totalSold desc ", nativeQuery = true)
	public List<ProductStatisticDTO> getProductStatistic();

	// Statistics by category sold
	@Query(value = "SELECT c.category_name , \r\n" + "SUM(o.quantity) as quantity ,\r\n"
			+ "SUM(o.quantity * o.price) as sum,\r\n" + "AVG(o.price) as avg,\r\n" + "Min(o.price) as min,\r\n"
			+ "max(o.price) as max \r\n" + "FROM order_details o\r\n"
			+ "INNER JOIN products p ON o.product_id = p.product_id\r\n"
			+ "INNER JOIN categories c ON p.category_id = c.category_id\r\n"
			+ "GROUP BY c.category_name;", nativeQuery = true)
	public List<Object[]> repoWhereCategory();

	// Statistics by category sold
	@Query(value = "SELECT c.category_id as id, c.category_name as categoryName, COUNT(DISTINCT p.product_id) AS productCount, "
			+ "SUM(o.quantity) as totalSold, SUM(o.quantity * o.price) as totalRevenue, SUM(p.quantity) AS inventory,"
			+ "ROUND(CASE " + "	WHEN COUNT(o.order_id) = 0 THEN 0 "
			+ "	ELSE (COALESCE(SUM(CASE WHEN ord.status = 3 THEN o.quantity END), 0) * 100.0 / COUNT(o.order_id)) "
			+ "END) AS cancelRate, " + "ROUND(CASE " + "	WHEN COUNT(o.order_id) = 0 THEN 0 "
			+ "	ELSE (COALESCE(SUM(CASE WHEN ord.status = 2 THEN o.quantity END), 0) * 100.0 / COUNT(o.order_id)) "
			+ "END) AS successRate, COALESCE(AVG(cm.rating), 0) as rating " + "FROM order_details o "
			+ "INNER JOIN products p ON o.product_id = p.product_id "
			+ "INNER JOIN categories c ON p.category_id = c.category_id "
			+ "INNER JOIN orders ord ON o.order_id = ord.order_id "
			+ "LEFT JOIN comments cm ON p.product_id = cm.product_id " + "GROUP BY c.category_name, c.category_id "
			+ "order by totalSold desc ", nativeQuery = true)
	public List<CategoryStatisticDTO> getCategoryStatistic();

	// Statistics of products sold by year
	@Query(value = "Select YEAR(od.order_date) ,\r\n" + "SUM(o.quantity) as quantity ,\r\n"
			+ "SUM(o.quantity * o.price) as sum,\r\n" + "AVG(o.price) as avg,\r\n" + "Min(o.price) as min,\r\n"
			+ "max(o.price) as max \r\n" + "FROM order_details o\r\n"
			+ "INNER JOIN orders od ON o.order_id = od.order_id\r\n"
			+ "GROUP BY YEAR(od.order_date);", nativeQuery = true)
	public List<Object[]> repoWhereYear();

	// Statistics of products sold by year
	@Query(value = "WITH sales_data AS (\r\n" + "    SELECT \r\n" + "        YEAR(od.order_date) AS year,\r\n"
			+ "        o.product_id,\r\n" + "        SUM(o.quantity) AS total_quantity,\r\n"
			+ "        SUM(o.quantity * o.price) AS total_sales,\r\n" + "        p.product_name,\r\n"
			+ "        c.category_name\r\n" + "    FROM \r\n" + "        order_details o\r\n" + "    INNER JOIN \r\n"
			+ "        orders od ON o.order_id = od.order_id\r\n" + "    INNER JOIN \r\n"
			+ "        products p ON o.product_id = p.product_id\r\n" + "    INNER JOIN \r\n"
			+ "        categories c ON p.category_id = c.category_id\r\n" + "    WHERE \r\n"
			+ "        od.status = 2 -- chỉ lấy đơn hàng đã hoàn thành\r\n" + "    GROUP BY \r\n"
			+ "        YEAR(od.order_date), o.product_id\r\n" + "),\r\n" + "\r\n" + "top_products AS (\r\n"
			+ "    SELECT \r\n" + "        year,\r\n" + "        product_name,\r\n" + "        total_quantity,\r\n"
			+ "        total_sales,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY year ORDER BY total_quantity DESC) AS rankk\r\n"
			+ "    FROM \r\n" + "        sales_data\r\n" + "),\r\n" + "\r\n" + "top_categories AS (\r\n"
			+ "    SELECT \r\n" + "        year,\r\n" + "        category_name,\r\n"
			+ "        SUM(total_quantity) AS category_quantity,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY year ORDER BY SUM(total_quantity) DESC) AS rankk\r\n"
			+ "    FROM \r\n" + "        sales_data\r\n" + "    GROUP BY \r\n" + "        year, category_name\r\n"
			+ "),\r\n" + "\r\n" + "top_customers AS (\r\n" + "    SELECT \r\n"
			+ "        YEAR(od.order_date) AS year,\r\n" + "        u.name,\r\n"
			+ "        COUNT(DISTINCT od.order_id) AS total_orders,  -- Đếm số lượng đơn hàng\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY YEAR(od.order_date) ORDER BY COUNT(DISTINCT od.order_id) DESC) AS rankk\r\n"
			+ "    FROM \r\n" + "        orders od\r\n" + "    INNER JOIN \r\n"
			+ "        order_details o ON od.order_id = o.order_id\r\n" + "    INNER JOIN \r\n"
			+ "        user u ON od.user_id = u.user_id\r\n" + "    WHERE \r\n"
			+ "        od.status = 2 -- chỉ lấy đơn hàng đã hoàn thành\r\n" + "    GROUP BY \r\n"
			+ "        YEAR(od.order_date), u.name\r\n" + ")\r\n" + "\r\n" + "SELECT \r\n" + "    sd.year,\r\n"
			+ "    SUM(sd.total_quantity) AS quantitySold,\r\n" + "    SUM(sd.total_sales) AS revenue,\r\n"
			+ "    tp.product_name as bestSellingProduct,\r\n" + "    tc.category_name as bestSellingCategory,\r\n"
			+ "    cust.name AS topCustomer\r\n" + "FROM \r\n" + "    sales_data sd\r\n" + "JOIN \r\n"
			+ "    top_products tp ON sd.year = tp.year AND tp.rankk = 1\r\n" + "JOIN \r\n"
			+ "    top_categories tc ON sd.year = tc.year AND tc.rankk = 1\r\n" + "JOIN \r\n"
			+ "    top_customers cust ON sd.year = cust.year AND cust.rankk = 1\r\n" + "GROUP BY \r\n"
			+ "    sd.year, tp.product_name, tc.category_name, cust.name ", nativeQuery = true)
	public List<YearStatisticDTO> getYearStatistic();

	// Statistics of products sold by month
	@Query(value = "Select month(od.order_date) ,\r\n" + "SUM(o.quantity) as quantity ,\r\n"
			+ "SUM(o.quantity * o.price) as sum,\r\n" + "AVG(o.price) as avg,\r\n" + "Min(o.price) as min,\r\n"
			+ "max(o.price) as max\r\n" + "FROM order_details o\r\n"
			+ "INNER JOIN orders od ON o.order_id = od.order_id\r\n"
			+ "GROUP BY month(od.order_date);", nativeQuery = true)
	public List<Object[]> repoWhereMonth();

	// Statistics of products sold by month
	@Query(value = "WITH sales_data AS (\r\n" + "    SELECT \r\n" + "        MONTH(od.order_date) AS month,\r\n"
			+ "        o.product_id,\r\n" + "        SUM(o.quantity) AS total_quantity,\r\n"
			+ "        SUM(o.quantity * o.price) AS total_sales,\r\n" + "        p.product_name,\r\n"
			+ "        c.category_name\r\n" + "    FROM order_details o\r\n"
			+ "    INNER JOIN orders od ON o.order_id = od.order_id\r\n"
			+ "    INNER JOIN products p ON o.product_id = p.product_id\r\n"
			+ "    INNER JOIN categories c ON p.category_id = c.category_id\r\n" + "    WHERE od.status = 2 \r\n"
			+ "    GROUP BY MONTH(od.order_date), o.product_id\r\n" + "),\r\n" + "top_products AS (\r\n"
			+ "    SELECT month, product_name, total_quantity, total_sales,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY month ORDER BY total_quantity DESC) AS rankk\r\n"
			+ "    FROM sales_data\r\n" + "),\r\n" + "top_categories AS (\r\n" + "    SELECT \r\n"
			+ "        month,\r\n" + "        category_name,\r\n"
			+ "        SUM(total_quantity) AS category_quantity,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY month ORDER BY SUM(total_quantity) DESC) AS rankk\r\n"
			+ "    FROM sales_data\r\n" + "    GROUP BY month, category_name\r\n" + "),\r\n" + "top_customers AS (\r\n"
			+ "    SELECT MONTH(od.order_date) AS month, u.name, COUNT(DISTINCT od.order_id) AS total_orders, \r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY MONTH(od.order_date) ORDER BY COUNT(DISTINCT od.order_id) DESC) AS rankk\r\n"
			+ "    FROM orders od\r\n" + "    INNER JOIN order_details o ON od.order_id = o.order_id\r\n"
			+ "    INNER JOIN user u ON od.user_id = u.user_id\r\n" + "    WHERE od.status = 2 \r\n"
			+ "    GROUP BY  MONTH(od.order_date), u.name\r\n" + ")\r\n" + "SELECT \r\n" + "    sd.month,\r\n"
			+ "    SUM(sd.total_quantity) AS quantitySold,\r\n" + "    SUM(sd.total_sales) AS revenue,\r\n"
			+ "    tp.product_name AS bestSellingProduct,\r\n" + "    tc.category_name AS bestSellingCategory,\r\n"
			+ "    cust.name AS topCustomer\r\n" + "FROM sales_data sd\r\n"
			+ "JOIN top_products tp ON sd.month = tp.month AND tp.rankk = 1\r\n"
			+ "JOIN top_categories tc ON sd.month = tc.month AND tc.rankk = 1\r\n"
			+ "JOIN top_customers cust ON sd.month = cust.month AND cust.rankk = 1\r\n"
			+ "GROUP BY sd.month, tp.product_name, tc.category_name, cust.name ", nativeQuery = true)
	public List<MonthStatisticDTO> getMonthStatistic();

	// Statistics of products sold by quarter
	@Query(value = "Select QUARTER(od.order_date),\r\n" + "SUM(o.quantity) as quantity ,\r\n"
			+ "SUM(o.quantity * o.price) as sum,\r\n" + "AVG(o.price) as avg,\r\n" + "Min(o.price) as min,\r\n"
			+ "max(o.price) as max\r\n" + "FROM order_details o\r\n"
			+ "INNER JOIN orders od ON o.order_id = od.order_id\r\n"
			+ "GROUP By QUARTER(od.order_date);", nativeQuery = true)
	public List<Object[]> repoWhereQUARTER();

	// Statistics of products sold by quarter
	@Query(value = "WITH sales_data AS (\r\n" + "    SELECT \r\n" + "        QUARTER(od.order_date) AS quarter,\r\n"
			+ "        o.product_id,\r\n" + "        SUM(o.quantity) AS total_quantity,\r\n"
			+ "        SUM(o.quantity * o.price) AS total_sales,\r\n" + "        p.product_name,\r\n"
			+ "        c.category_name\r\n" + "    FROM \r\n" + "        order_details o\r\n" + "    INNER JOIN \r\n"
			+ "        orders od ON o.order_id = od.order_id\r\n" + "    INNER JOIN \r\n"
			+ "        products p ON o.product_id = p.product_id\r\n" + "    INNER JOIN \r\n"
			+ "        categories c ON p.category_id = c.category_id\r\n" + "    WHERE \r\n"
			+ "        od.status = 2 \r\n" + "    GROUP BY \r\n" + "        QUARTER(od.order_date), o.product_id\r\n"
			+ "),\r\n" + "top_products AS (\r\n" + "    SELECT \r\n" + "        quarter, \r\n"
			+ "        product_name, \r\n" + "        total_quantity, \r\n" + "        total_sales,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY quarter ORDER BY total_quantity DESC) AS rankk\r\n"
			+ "    FROM \r\n" + "        sales_data\r\n" + "),\r\n" + "top_categories AS (\r\n" + "    SELECT \r\n"
			+ "        quarter,\r\n" + "        category_name,\r\n"
			+ "        SUM(total_quantity) AS category_quantity,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY quarter ORDER BY SUM(total_quantity) DESC) AS rankk\r\n"
			+ "    FROM \r\n" + "        sales_data\r\n" + "    GROUP BY \r\n" + "        quarter, category_name\r\n"
			+ "),\r\n" + "top_customers AS (\r\n" + "    SELECT \r\n"
			+ "        QUARTER(od.order_date) AS quarter, \r\n" + "        u.name, \r\n"
			+ "        COUNT(DISTINCT od.order_id) AS total_orders, \r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY QUARTER(od.order_date) ORDER BY COUNT(DISTINCT od.order_id) DESC) AS rankk\r\n"
			+ "    FROM \r\n" + "        orders od\r\n" + "    INNER JOIN \r\n"
			+ "        order_details o ON od.order_id = o.order_id\r\n" + "    INNER JOIN \r\n"
			+ "        user u ON od.user_id = u.user_id\r\n" + "    WHERE \r\n" + "        od.status = 2 \r\n"
			+ "    GROUP BY  \r\n" + "        QUARTER(od.order_date), u.name\r\n" + ")\r\n" + "SELECT \r\n"
			+ "    sd.quarter,\r\n" + "    SUM(sd.total_quantity) AS quantitySold,\r\n"
			+ "    SUM(sd.total_sales) AS revenue,\r\n" + "    tp.product_name AS bestSellingProduct,\r\n"
			+ "    tc.category_name AS bestSellingCategory,\r\n" + "    cust.name AS topCustomer\r\n" + "FROM \r\n"
			+ "    sales_data sd\r\n" + "JOIN \r\n"
			+ "    top_products tp ON sd.quarter = tp.quarter AND tp.rankk = 1\r\n" + "JOIN \r\n"
			+ "    top_categories tc ON sd.quarter = tc.quarter AND tc.rankk = 1\r\n" + "JOIN \r\n"
			+ "    top_customers cust ON sd.quarter = cust.quarter AND cust.rankk = 1\r\n" + "GROUP BY \r\n"
			+ "    sd.quarter, tp.product_name, tc.category_name, cust.name ", nativeQuery = true)
	public List<QuarterStatisticDTO> getQuarterStatistic();

	// Statistics by user
	@Query(value = "SELECT c.user_id,\r\n" + "SUM(o.quantity) as quantity,\r\n"
			+ "SUM(o.quantity * o.price) as sum,\r\n" + "AVG(o.price) as avg,\r\n" + "Min(o.price) as min,\r\n"
			+ "max(o.price) as max\r\n" + "FROM order_details o\r\n"
			+ "INNER JOIN orders p ON o.order_id = p.order_id\r\n" + "INNER JOIN user c ON p.user_id = c.user_id\r\n"
			+ "GROUP BY c.user_id;", nativeQuery = true)
	public List<Object[]> reportCustommer();

	// Statistics by user
	@Query(value = "SELECT c.name as userName, c.email as email, " + "    COALESCE(SUM(od.quantity), 0) AS totalSold, "
			+ "	   COALESCE(SUM(CASE WHEN o.status = 2 THEN od.quantity * od.price END), 0) AS totalSpent, "
			+ "    COUNT(DISTINCT CASE WHEN o.status = 2 THEN o.order_id END) AS successOrders, "
			+ "    COUNT(DISTINCT CASE WHEN o.status = 3 THEN o.order_id END) AS cancelOrders, "
			+ "    mp.product_name AS mostPurchasedProduct, " + "    mp.total_quantity AS mostPurchasedQuantity "
			+ "FROM user c " + "LEFT JOIN orders o ON c.user_id = o.user_id "
			+ "LEFT JOIN users_roles ur ON c.user_id = ur.user_id " + "LEFT JOIN role r ON ur.role_id = r.id "
			+ "LEFT JOIN order_details od ON o.order_id = od.order_id " + "LEFT JOIN ( " + "    SELECT "
			+ "        ord.user_id, p.product_name, SUM(od.quantity) AS total_quantity, "
			+ "        ROW_NUMBER() OVER (PARTITION BY ord.user_id ORDER BY SUM(od.quantity) DESC) AS rn "
			+ "    FROM order_details od " + "    INNER JOIN orders ord ON od.order_id = ord.order_id "
			+ "    INNER JOIN products p ON od.product_id = p.product_id " + "    GROUP BY ord.user_id, p.product_name "
			+ ") mp ON c.user_id = mp.user_id AND mp.rn = 1 " + "WHERE r.name = 'ROLE_USER' "
			+ "GROUP BY c.user_id, mp.product_name, mp.total_quantity ", nativeQuery = true)
	public List<CustomerStatisticDTO> getCustomerStatistic();

}
