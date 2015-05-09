package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Coupons {
	public static final String IDCOUPONS = "idCoupons";
	public static final String APPROVED = "Approved"; //todo czemu tinyint?!
	//keys
	public static final String COUPON_DB_IDCOUPON_DB = "Coupon_DB_idCoupon_DB";
	public static final String RESERVATION_IDRESERVATION = "Reservation_idReservation";

	
	private int idCoupons;
	private int Approved;
	//keys
	private int Coupon_DB_idCoupon_DB;
	private int Reservation_idReservation;
	private Coupon_DB Coupon_DBEntity;
	private Reservation ReservationEntity;
	
	
	public int getIdCoupons() {
		return idCoupons;
	}
	public void setIdCoupons(int idCoupons) {
		this.idCoupons = idCoupons;
	}
	public int getApproved() {
		return Approved;
	}
	public void setApproved(int approved) {
		Approved = approved;
	}
	public int getCoupon_DB_idCoupon_DB() {
		return Coupon_DB_idCoupon_DB;
	}
	public void setCoupon_DB_idCoupon_DB(int coupon_DB_idCoupon_DB) {
		Coupon_DB_idCoupon_DB = coupon_DB_idCoupon_DB;
	}
	public int getReservation_idReservation() {
		return Reservation_idReservation;
	}
	public void setReservation_idReservation(int reservation_idReservation) {
		Reservation_idReservation = reservation_idReservation;
	}
	public Coupon_DB getCoupon_DBEntity() {
		return Coupon_DBEntity;
	}
	public void setCoupon_DBEntity(Coupon_DB coupon_DBEntity) {
		Coupon_DBEntity = coupon_DBEntity;
	}
	public Reservation getReservationEntity() {
		return ReservationEntity;
	}
	public void setReservationEntity(Reservation reservationEntity) {
		ReservationEntity = reservationEntity;
	}
	
	
	
}
