package com.example.store.bean;

public class GcParameter
{
	private String kind_id;
	private float weight_m;
	private float gc_long;
	private float inpay_m;
	private float outpay_m;
	public String getKind_id() {
		return kind_id;
	}
	public void setKind_id(String kind_id) {
		this.kind_id = kind_id;
	}
	public float getWeight_m() {
		return weight_m;
	}
	public void setWeight_m(float weight_m) {
		this.weight_m = weight_m;
	}
	public float getGc_long() {
		return gc_long;
	}
	public void setGc_long(float gc_long) {
		this.gc_long = gc_long;
	}
	public float getInpay_m() {
		return inpay_m;
	}
	public void setInpay_m(float inpay_m) {
		this.inpay_m = inpay_m;
	}
	public float getOutpay_m() {
		return outpay_m;
	}
	public void setOutpay_m(float outpay_m) {
		this.outpay_m = outpay_m;
	}
	
	
	
	@Override
	public String toString() {
		return "GcParameter [kind_id=" + kind_id + ", weight_m=" + weight_m
				+ ", gc_long=" + gc_long + ", inpay_m=" + inpay_m
				+ ", outpay_m=" + outpay_m + "]";
	}
	
	
}
