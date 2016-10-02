package com.example.store.bean;

import java.util.ArrayList;

public interface InfBean 
{
	/**
	 * 返回类名
	 * @return 返回类名
	 */
	public String getClassName();
	/**
	 * 返回所有字段名
	 * @return 返回所有字段名
	 */
	public String getFiledNameAll();
	/**
	 * 获取id字段名
	 * @return 返回id字段名
	 */
	public String getKind_id_nameString();
	/**
	 * 获取所有字段的值，以字符串的形式
	 * @return 获取所有字段的值，以字符串的形式
	 */
	public String getFiledValuses();
	public String getKind_id();
	/**
	 * 获取所有键值对以字符串的形式
	 * @return 所有键值对以字符串的形式
	 */
	public String getAllKeyValues();
}
