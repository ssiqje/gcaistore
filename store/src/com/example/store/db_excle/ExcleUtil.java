package com.example.store.db_excle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.npu.fastexcel.ExcelException;
import edu.npu.fastexcel.FastExcel;
import edu.npu.fastexcel.Sheet;
import edu.npu.fastexcel.Workbook;

/**
 * sqlite ��excle��ת��������
 * @author Administrator
 *
 */
public class ExcleUtil {

	/**
	 * ��������Excel��ʽ���浽ָ����·����
	 * @param data  Ҫ���������
	 * @param typeClass Ҫ���������������
	 * @param descFile  �����·��
	 * @return 2����ɹ���3����ʧ��
	 */
	public static int dataToExcel(ArrayList<Object> data,Class typeClass,File descFile)
	{
		if(data==null||data.isEmpty())
		{
			return 2;
		}
		Workbook workbook = FastExcel.createWriteableWorkbook(descFile);
		StringBuffer rowdata= new StringBuffer();
		try {
			workbook.open();
			Sheet sheet = workbook.addStreamSheet("sheet1");
			Method listMethod = null;
			try {
				listMethod = typeClass.getDeclaredMethod("excelGetMessage", null);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[][] listMethodName = (String[][]) listMethod.invoke(data.get(0), null);
			sheet.addRow(listMethodName[0]);
			sheet.addRow(listMethodName[1]);
			for (Object object : data) {
				rowdata.delete(0, rowdata.length());
				
				for (String string : listMethodName[2]) {
					Method method = null;
					try {
						method = typeClass.getDeclaredMethod("get"+string.substring(0, 1).toUpperCase()+string.substring(1, string.length()), null);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String  resultString =method.invoke(object, null)+"";
					rowdata.append(resultString+",");
					
				}
				String[] rowStrings=rowdata.toString().split(",");
				for (String string : rowStrings) {
					System.out.println(string+"~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				}
				sheet.addRow(rowStrings);
			}
			workbook.close();
			return 2;
		} catch (ExcelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 3;
	}
	
}
