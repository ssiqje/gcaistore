package com.example.store.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.example.store.bean.GcParameter;
import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.bean.Store;
import com.example.store.bean.Summary;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.db_excle.ExcleUtil;
import com.example.store.network.Network_util;

public class Server {

	private InStoreDaoInf dao;
	private static JSONArray del_list;
	private JSONObject jsonObject=null;
	public Server(Context context) {
		dao = new InStoreDaoInf(context);
		del_list = new JSONArray();
	}

	
	
	public static JSONArray getDel_list() {
		return del_list;
	}



	public static void setDel_list(JSONArray del_list) {
		Server.del_list = del_list;
	}



	/**
	 * �жϻ�ȡ��ֵ�ǲ���Ϊ��
	 * @param field ��Ҫ�жϵ�ֵ
	 * @return true ��Ϊ�գ�false Ϊ��
	 */
	public boolean isNull(String... field) {
		boolean isNull = true;
		for (String string : field) {
			if (string == null || "".equals(string)) {
				isNull = false;
				break;
			}
		}
		return isNull;

	}

	/**
	 * ���һ�������¼
	 * 
	 * @param id
	 *            ��¼���
	 * @param kind_id
	 *            ������
	 * @param weight_m
	 *            ÿ����
	 * @param gc_long
	 *            ÿ������
	 * @param inpay_m
	 *            ����
	 * @param count
	 *            ����
	 * @param date
	 *            ����
	 * @return true �ɹ���false ʧ��
	 */
	public boolean addInStore(long id, String kind_id, String weight_m,
			String gc_long, String inpay_m, String count, String date) {
		// TODO Auto-generated method stub

		boolean b = true;

		In_store inStore = new In_store();
		inStore.setId(id);
		inStore.setKind_id(kind_id);
		inStore.setWeight_m(Float.parseFloat(weight_m));
		inStore.setGc_long(Float.parseFloat(gc_long));
		inStore.setInpay_m(Float.parseFloat(inpay_m));
		inStore.setCount(Integer.parseInt(count));
		inStore.setWight(Float.parseFloat(weight_m) * Float.parseFloat(gc_long)
				* Integer.parseInt(count));
		inStore.setAllpay(Float.parseFloat(inpay_m) * Float.parseFloat(gc_long)
				* Integer.parseInt(count));
		inStore.setDate(date);
		inStore.setAdd_flag(1);
		if (!dao.save(inStore)) {
			b = false;
		}

		// ͨ����ѯ����Store
		Store in_db_store = (Store) dao.getById(Store.class,inStore.getKind_id());
		if (in_db_store != null) {
			in_db_store.setCount(in_db_store.getCount() + inStore.getCount());
			in_db_store.setWight(in_db_store.getWight() + inStore.getWight());
			in_db_store.setAdd_flag(1);
			in_db_store.setModfiy_flag(1);
			if (!dao.upData(in_db_store)) {
				b = false;
			}
		} else {
			Store store = new Store();
			store.setKind_id(inStore.getKind_id());
			store.setWeight_m(inStore.getWeight_m());
			store.setGc_long(inStore.getGc_long());
			store.setCount(inStore.getCount());
			store.setWight(inStore.getWight());
			store.setAdd_flag(1);
			if (!dao.save(store)) {
				b = false;
			}
		}

		// �����ܱ�
		ArrayList<Object> list = dao.getList(Summary.class);
		if (list != null) {
			Summary summary = (Summary) list.get(0);
			System.out.println("����ǰ�أ�" + summary.getWeight_all());
			summary.setWeight_all(summary.getWeight_all() + inStore.getWight());
			System.out.println("���º��أ�" + summary.getWeight_all());
			summary.setInpay_all(summary.getInpay_all() + inStore.getAllpay());
			summary.setIn_or_out_pay(summary.getOutpay_all()
					- summary.getInpay_all());
			summary.setAdd_flag(1);
			summary.setModfiy_flag(1);
			if (!dao.upData(summary)) {
				b = false;
			}
		} else {
			Summary summary = new Summary();
			summary.setWeight_all(inStore.getWight());
			summary.setInpay_all(inStore.getAllpay());
			summary.setOutpay_all(0f);
			summary.setIn_or_out_pay(summary.getOutpay_all()
					- summary.getInpay_all());
			summary.setAdd_flag(1);
			if (!dao.save(summary)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * ���һ�������¼
	 * 
	 * @param id
	 *            ��¼���
	 * @param kind_id
	 *            ������
	 * @param weight_m
	 *            ÿ����
	 * @param gc_long
	 *            ÿ������
	 * @param inpay_m
	 *            �ۼ�
	 * @param count
	 *            ����
	 * @param date
	 *            ����
	 * @return 1�������ʲôҲû��2���������������㣬3�����ɹ�����4���� �����ʧ�ܣ�����ʧ��
	 */
	public int addOutStore(long id, String kind_id, String weight_m,
			String gc_long, String outpay_m, String count, String date) {
		// TODO Auto-generated method stub

		Store store = (Store) dao.getById(Store.class,
				kind_id);
		Out_store out_store=null;
		Summary summary=null;
		if(store==null)
		{
			return 1;
		}
		if(Integer.parseInt(count)<=store.getCount())
		{
			out_store = new Out_store();
			out_store.setId(id);
			out_store.setKind_id(kind_id);
			out_store.setWeight_m(Float.parseFloat(weight_m));
			out_store.setGc_long(Float.parseFloat(gc_long));
			out_store.setOutpay_m(Float.parseFloat(outpay_m));
			out_store.setCount(Integer.parseInt(count));
			out_store.setWight(Float.parseFloat(weight_m) * Float.parseFloat(gc_long)
					* Integer.parseInt(count));
			out_store.setAllpay(Float.parseFloat(outpay_m)
					* Float.parseFloat(gc_long) * Integer.parseInt(count));
			out_store.setDate(date);
			out_store.setAdd_flag(1);
			// ͨ����ѯ����Store
			
			if (store != null) {
				store.setCount(store.getCount() - out_store.getCount());
				store.setWight(store.getWight() - out_store.getWight());
				store.setAdd_flag(1);
				store.setModfiy_flag(1);
			} 

			// �����ܱ�
			
			ArrayList<Object> list = dao.getList(Summary.class);
			if (list != null) {
				summary = (Summary) list.get(0);
				summary.setWeight_all(summary.getWeight_all() - out_store.getWight());
				summary.setOutpay_all(summary.getOutpay_all() + out_store.getAllpay());
				summary.setIn_or_out_pay(summary.getOutpay_all()
						- summary.getInpay_all());
				summary.setAdd_flag(1);
				summary.setModfiy_flag(1);
			} else {
				System.out.println("û���õ��ܱ�");
			}
			boolean b=dao.saveOutItemWater(out_store,store,summary);
			if(b)
			{
				jsonObject = new JSONObject();
				try {
					jsonObject.put("type", "store");
					jsonObject.put("kind_id",store.getKind_id());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				del_list.put(jsonObject);
				return 3;
			}
			return b?3:4;
		}else {
			return 2;
		}
		
	}

	/**
	 * ɾ��һ��������ˮ
	 * 
	 * @param hashMap
	 *            ��ˮ��Ŀ
	 * @return true�ɹ� falseʧ��
	 */
	public boolean delItemFromInStore(HashMap<String, String> hashMap) {
		//Ҫɾ���ĳ�����ˮ����
		In_store in_store = new In_store();
		in_store.setId(Long.parseLong(hashMap.get("id")));
		//Ҫ���µĿ�����
		Store store = (Store) dao.getById(Store.class, hashMap.get("kind_id"));
		if(store==null)
		//�޻�����
		{return false;}
		store.setWight(store.getWight()- Float.parseFloat(hashMap.get("weight")));
		store.setCount(store.getCount()- Integer.parseInt(hashMap.get("count")));
		store.setAdd_flag(1);
		store.setModfiy_flag(1);
		//Ҫ���µ��ܱ����
		Summary summary=null;
		ArrayList<Object> list = dao.getList(Summary.class);
		if (list != null) {
			summary = (Summary) list.get(0);
			summary.setWeight_all(summary.getWeight_all()
					- Float.parseFloat(hashMap.get("weight")));
			summary.setInpay_all(summary.getInpay_all()
					- Float.parseFloat(hashMap.get("allpay")));
			summary.setIn_or_out_pay(summary.getOutpay_all()
					- summary.getInpay_all());
			summary.setAdd_flag(1);
			summary.setModfiy_flag(1);
			
		}
		if(dao.delInStoreWater(in_store,store,summary))
		{
			
			try {
				jsonObject = new JSONObject();
				jsonObject.put("type", "in_store");
				jsonObject.put("id", in_store.getId()+"");
				del_list.put(jsonObject);
				if(store.getCount()==0)
				{jsonObject=null;
				jsonObject = new JSONObject();
				jsonObject.put("type", "store");
				jsonObject.put("kind_id",store.getKind_id());
				del_list.put(jsonObject);}
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

		}else {
			return false;
		}
		return false;
	}

	/**
	 * ɾ��һ��������ˮ
	 * 
	 * @param hashMap
	 *            ��ˮ��Ŀ
	 * @return true�ɹ� falseʧ��
	 */
	public boolean delItemFromOutStore(HashMap<String, String> hashMap) {
		// TODO Auto-generated method stub
		boolean b = true;
		Out_store out_store = new Out_store();
		out_store.setId(Long.parseLong(hashMap.get("id")));
		if (!dao.del(out_store)) {
			b = false;
		}

		Store store = (Store) dao.getById(Store.class, hashMap.get("kind_id"));

		store.setWight(store.getWight()+ Float.parseFloat(hashMap.get("weight")));
		store.setCount(store.getCount()+ Integer.parseInt(hashMap.get("count")));
		store.setAdd_flag(1);
		store.setModfiy_flag(1);
		if (!dao.upData(store)) {
			b = false;
		}

		ArrayList<Object> list = dao.getList(Summary.class);
		if (list != null) {
			Summary summary = (Summary) list.get(0);
			summary.setWeight_all(summary.getWeight_all()
					+ Float.parseFloat(hashMap.get("weight")));
			summary.setOutpay_all(summary.getOutpay_all()
					- Float.parseFloat(hashMap.get("allpay")));
			summary.setIn_or_out_pay(summary.getOutpay_all()
					- summary.getInpay_all());
			summary.setAdd_flag(1);
			summary.setModfiy_flag(1);
			if (!dao.upData(summary)) {
				b = false;
			}
		}
		if(b)
		{
			jsonObject = new JSONObject();
			try {
				jsonObject.put("type", "out_store");
				jsonObject.put("id", out_store.getId()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			del_list.put(jsonObject);
			
		}
		return b;
	}

	/**
	 *�����ݱ��浽�ڴ濨
	 *@return true�ɹ�,falseʧ��
	 */
	public boolean dataToSdcard() {
		// TODO Auto-generated method stub
		boolean b=true;
		File path = new  File(Environment.getExternalStorageDirectory()+"/user_excel");
		if(!path.exists())
		{
			path.mkdir();
		}
    	File kindFile = new File(path,"�����.xls");
    	File storeFile = new File(path,"�ⴢ��.xls");
    	File inStoreFile = new File(path,"������.xls");
    	File outStoreFile = new File(path,"������.xls");
    	File summaryFile = new File(path,"�ܱ�.xls");
    	
    	int gc=ExcleUtil.dataToExcel(dao.getList(GcParameter.class),GcParameter.class,kindFile);
    	int store=ExcleUtil.dataToExcel(dao.getList(Store.class),Store.class,storeFile);
    	int instore=ExcleUtil.dataToExcel(dao.getList(In_store.class),In_store.class,inStoreFile);
    	int outstore=ExcleUtil.dataToExcel(dao.getList(Out_store.class),Out_store.class,outStoreFile);
    	int summary=ExcleUtil.dataToExcel(dao.getList(Summary.class),Summary.class,summaryFile);
//    	return b=outstore==2?true:false;
		return b=(gc==2&&store==2&&instore==2&&outstore==2&&summary==2)?true:false;
	}

	/**
	 * �ϴ����ݵ�������
	 * @param handler 
	 * @return true�ɹ���falseʧ��
	 */
	public boolean dataToServer(Handler handler) {
		//�õ�Ҫ���µ����� 
		JSONObject jsonObject = dao.getByAddFlag();
		if(jsonObject!=null){
			try {
				jsonObject.put("del_list", del_list);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("��ȡ��Json�����ǣ�"+jsonObject.toString());
			Network_util.upDataToServer(jsonObject,handler);
		}else {
			System.out.println("û�л�ȡ������ ��");
		}
		return false;
		// TODO Auto-generated method stub
		
	}

	/**
	 * �û����߸���
	 * @param user_json ���ߵ��û�JSON
	 */
	public void unLine(String user_json,Handler handler) {
		// TODO Auto-generated method stub
		Network_util.unLine(user_json,handler);
		
	}
	/**
	 * ������¼Ǻ�
	 * @return �ɹ�true ��ʧ��false
	 */
	public boolean cancellationUpFlag() {
		// TODO Auto-generated method stub
		return dao.cancellationUpFlag();
	}

}
