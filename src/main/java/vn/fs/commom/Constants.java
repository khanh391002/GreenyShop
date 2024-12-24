package vn.fs.commom;

public class Constants {
	public static final int ORDER_STATUS_PENDING = 0;
	public static final int ORDER_STATUS_DELIVERY = 1;
	public static final int ORDER_STATUS_APPROVED = 2;
	public static final int ORDER_STATUS_REJECTED = 3;
	
	public static final String STATUS_PENDING = "Chờ xác nhận";
	public static final String STATUS_DELIVERY = "Đang giao hàng";
	public static final String STATUS_APPROVED = "Đã thanh toán";
	public static final String STATUS_REJECTED = "Đã hủy";
	
	public static final String STATUS_ORDER_HTML = "<span style=\"color: %s; font-weight: bold;\">%s</span>";
	
	public static final String COUPON_NEW_MEMBER = "NEW_MEMBER";
	public static final String COUPON_DISCOUNT_50 = "SALE_50";
	
	public static final int TOTAL_PRICE_500K = 500000;
}
