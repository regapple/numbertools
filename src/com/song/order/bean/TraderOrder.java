package com.song.order.bean;

import com.song.common.dao.Table;

@Table(tableName="trader_order")
public class TraderOrder {
	private int id;
	private String name;
	private String owner;
	private String tid;
	private String mobile;
	private String phone;
	private String shopName;
	private String shopLink;
	private String category;
	private String shopRank;
	private String address;
	private String email;
	private String epayId;
	private String qq;
	private String source;
	private String shopType;
	private String  sex;
	private String importDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopLink() {
		return shopLink;
	}
	public void setShopLink(String shopLink) {
		this.shopLink = shopLink;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getShopRank() {
		return shopRank;
	}
	public void setShopRank(String shopRank) {
		this.shopRank = shopRank;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getShopType() {
		return shopType;
	}
	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getImportDate() {
		return importDate;
	}
	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}
	public String getEpayId() {
		return epayId;
	}
	public void setEpayId(String epayId) {
		this.epayId = epayId;
	}
	
	
}
