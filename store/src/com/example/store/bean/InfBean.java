package com.example.store.bean;

import java.util.ArrayList;

public interface InfBean 
{
	/**
	 * ��������
	 * @return ��������
	 */
	public String getClassName();
	/**
	 * ���������ֶ���
	 * @return ���������ֶ���
	 */
	public String getFiledNameAll();
	/**
	 * ��ȡid�ֶ���
	 * @return ����id�ֶ���
	 */
	public String getKind_id_nameString();
	/**
	 * ��ȡ�����ֶε�ֵ�����ַ�������ʽ
	 * @return ��ȡ�����ֶε�ֵ�����ַ�������ʽ
	 */
	public String getFiledValuses();
	public String getKind_id();
	/**
	 * ��ȡ���м�ֵ�����ַ�������ʽ
	 * @return ���м�ֵ�����ַ�������ʽ
	 */
	public String getAllKeyValues();
}
