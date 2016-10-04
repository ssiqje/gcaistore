package com.example.store.bean;

import java.util.ArrayList;

public class Store
{
	private String kind_id;
	private float weight_m;
	private float gc_long;
	private int count;
	private float wight;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public float getWight() {
		return wight;
	}
	public void setWight(float wight) {
		this.wight = wight;
	}
	
	@Override
	public String toString() {
		return "Store [kind_id=" + kind_id + ", weight_m=" + weight_m
				+ ", gc_long=" + gc_long + ", count=" + count + ", wight="
				+ wight +  "]";
	}
	
	
	
}
