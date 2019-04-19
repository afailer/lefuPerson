package com.lefuyun.bean;

import java.io.Serializable;

public class TourismReserve implements Serializable {

	private static final long serialVersionUID = 1L;

	private long product_id; // 所预约产品ID
	private long company_id; // 公司ID
	private String name; // 联系人名称
	private String cellphone; // 联系人号码
	private int people_amount; // 预约产品人数

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public int getPeople_amount() {
		return people_amount;
	}

	public void setPeople_amount(int people_amount) {
		this.people_amount = people_amount;
	}

	@Override
	public String toString() {
		return "TourismReserve [product_id=" + product_id + ", company_id="
				+ company_id + ", name=" + name + ", cellphone=" + cellphone
				+ ", people_amount=" + people_amount + "]";
	}

}
