package com.example.store.server;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.widget.Toast;

import com.example.store.InAndOutAty;
import com.example.store.WaterAty;
import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.bean.Store;
import com.example.store.bean.Summary;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.network.Network_util;

public class Server {

	private InStoreDaoInf dao;

	public Server(Context context) {
		dao = new InStoreDaoInf(context);
	}

	/**
	 * 判断获取的值是不是为空
	 * @param field 需要判断的值
	 * @return true 不为空，false 为空
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
	 * 添加一条进物记录
	 * 
	 * @param id
	 *            记录编号
	 * @param kind_id
	 *            货物编号
	 * @param weight_m
	 *            每米重
	 * @param gc_long
	 *            每根长度
	 * @param inpay_m
	 *            进价
	 * @param count
	 *            根数
	 * @param date
	 *            日期
	 * @return true 成功，false 失败
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
		if (!dao.save(inStore)) {
			b = false;
		}

		// 通过查询更新Store
		Store in_db_store = (Store) dao.getById(Store.class,
				inStore.getKind_id());
		if (in_db_store != null) {
			in_db_store.setCount(in_db_store.getCount() + inStore.getCount());
			in_db_store.setWight(in_db_store.getWight() + inStore.getWight());
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
			if (!dao.save(store)) {
				b = false;
			}
		}

		// 更新总表
		ArrayList<Object> list = dao.getList(Summary.class);
		if (list != null) {
			Summary summary = (Summary) list.get(0);
			System.out.println("更新前重：" + summary.getWeight_all());
			summary.setWeight_all(summary.getWeight_all() + inStore.getWight());
			System.out.println("更新后重：" + summary.getWeight_all());
			summary.setInpay_all(summary.getInpay_all() + inStore.getAllpay());
			summary.setIn_or_out_pay(summary.getOutpay_all()
					- summary.getInpay_all());
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
			if (!dao.save(summary)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * 添加一条出物记录
	 * 
	 * @param id
	 *            记录编号
	 * @param kind_id
	 *            货物编号
	 * @param weight_m
	 *            每米重
	 * @param gc_long
	 *            每根长度
	 * @param inpay_m
	 *            售价
	 * @param count
	 *            根数
	 * @param date
	 *            日期
	 * @return 1、库存里什么也没，2、出货的数量不足，3出货成功，、4数据 表更新失败，出货失败
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

			// 通过查询更新Store
			
			if (store != null) {
				store.setCount(store.getCount() - out_store.getCount());
				store.setWight(store.getWight() - out_store.getWight());
			} 

			// 更新总表
			
			ArrayList<Object> list = dao.getList(Summary.class);
			if (list != null) {
				summary = (Summary) list.get(0);
				summary.setWeight_all(summary.getWeight_all() - out_store.getWight());
				summary.setOutpay_all(summary.getOutpay_all() + out_store.getAllpay());
				summary.setIn_or_out_pay(summary.getOutpay_all()
						- summary.getInpay_all());
			} else {
				System.out.println("没有拿到总表！");
			}
			boolean b=dao.saveOutItemWater(out_store,store,summary);
			return b?3:4;
		}else {
			return 2;
		}
		
	}

	/**
	 * 删除一条进货流水
	 * 
	 * @param hashMap
	 *            流水条目
	 * @return true成功 false失败
	 */
	public boolean delItemFromInStore(HashMap<String, String> hashMap) {
		//要删除的出货流水对象
		In_store in_store = new In_store();
		in_store.setId(Long.parseLong(hashMap.get("id")));
		//要更新的库存对象
		Store store = (Store) dao.getById(Store.class, hashMap.get("kind_id"));
		store.setWight(store.getWight()- Float.parseFloat(hashMap.get("weight")));
		store.setCount(store.getCount()- Integer.parseInt(hashMap.get("count")));
		//要更新的总表对象
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
			
		}
		return dao.delInStoreWater(in_store,store,summary);
	}

	/**
	 * 删除一条出货流水
	 * 
	 * @param hashMap
	 *            流水条目
	 * @return true成功 false失败
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
			if (!dao.upData(summary)) {
				b = false;
			}
		}
		return b;
	}

}
