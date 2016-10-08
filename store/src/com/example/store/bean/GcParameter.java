package com.example.store.bean;

public class GcParameter extends T
{
	private String kind_id;
	private float weight_m;
	private float gc_long;
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
	
	
	
	@Override
	public String toString() {
		return "GcParameter [kind_id=" + kind_id + ", weight_m=" + weight_m
				+ ", gc_long=" + gc_long + "]";
	}
	/**
	 * 获取中文表名，种文字段名，英文字段名
	 * @return 返回中文表名，种文字段名，英文字段名
	 */
	public String[][] excelGetMessage()
	{
		return new String[][]{
								{"种类表"},
								{"种类编号","米/吨","根/M"},
								{"kind_id","weight_m","gc_long"}
								
							  };
		
	}
	
}
