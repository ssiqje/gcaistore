package com.example.store.dao;

import java.util.List;

public interface InfDao 
{
	/**
	 * ����һ����¼
	 * @param object Ҫ��ӵĶ���
	 * @return �ɹ� true��ʧ�� false
	 */
	public boolean save(Object object);
	/**
	 * ɾ��һ������
	 * @param object Ҫɾ���Ķ���
	 * @return �ɹ� true��ʧ�� false
	 */
	public boolean del(Object object);
	/**
	 * ����һ������
	 * @param object Ҫ���µĶ���
	 * @return �ɹ� true��ʧ�� false
	 */
		public boolean upData(Object object);
	/**
	 * ��ȡ�б�
	 * @return �����б�
	 */
	public List<Object> getList(Class className);
	/**
	 * ��ȡָ��ID��
	 * @param className ����
	 * @param id ID
	 * @return  ����ָ������
	 */
	public Object	getById(Class className,String id);
	
	
	
	

}
