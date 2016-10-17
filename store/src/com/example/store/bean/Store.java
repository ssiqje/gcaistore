package com.example.store.bean;

import java.util.ArrayList;

public class Store extends T
{
	private String kind_id;
	private float weight_m;
	private float gc_long;
	private int count;
	private float wight;
	private int add_flag;
	private int modfiy_flag;
	
	public Store(){}	
	
	public Store(String kind_id, float weight_m, float gc_long, int count,
			float wight, int add_flag, int modfiy_flag) {
		super();
		this.kind_id = kind_id;
		this.weight_m = weight_m;
		this.gc_long = gc_long;
		this.count = count;
		this.wight = wight;
		this.add_flag = add_flag;
		this.modfiy_flag = modfiy_flag;
	}



	@Override
	public String toString() {
		return "Store [kind_id=" + kind_id + ", weight_m=" + weight_m
				+ ", gc_long=" + gc_long + ", count=" + count + ", wight="
				+ wight + ", add_flag=" + add_flag + ", modfiy_flag="
				+ modfiy_flag + "]";
	}



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



	public int getAdd_flag() {
		return add_flag;
	}



	public void setAdd_flag(int add_flag) {
		this.add_flag = add_flag;
	}



	public int getModfiy_flag() {
		return modfiy_flag;
	}



	public void setModfiy_flag(int modfiy_flag) {
		this.modfiy_flag = modfiy_flag;
	}



	/**
	 * ��ȡ���ı����������ֶ�����Ӣ���ֶ���
	 * @return �������ı����������ֶ�����Ӣ���ֶ���
	 */
	public String[][] excelGetMessage()
	{
		return new String[][]{
								{"����"},
								{"������","��/��","��/M","����","����(��)"},
								{"kind_id","weight_m","gc_long","count","wight"}
								
							  };
		
	}
	
}
