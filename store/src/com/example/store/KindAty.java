package com.example.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.store.bean.GcParameter;
import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.server.Server;
import com.example.store.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class KindAty extends Activity implements OnClickListener {
	private EditText et_kind_id, et_weight_m, et_gc_long, et_inpay_m,
			et_outpay_m;
	private Button but_water, but_store, but_home, but_seting, but_about;
	private ListView kind_seting_lv;
	private SimpleAdapter kind_seting_Adapter;
	private InStoreDaoInf dao;
	private Server server;
	private ArrayList<Object> list;
	private List<HashMap<String, String>> listMaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kind_seting);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		et_kind_id = (EditText) findViewById(R.id.edittext_kind_id);
		et_weight_m = (EditText) findViewById(R.id.edittext_weight_m);
		et_gc_long = (EditText) findViewById(R.id.edittext_gc_long);
		et_inpay_m = (EditText) findViewById(R.id.edittext_inpay_m);
		et_outpay_m = (EditText) findViewById(R.id.edittext_outpay_m);

		but_water = (Button) findViewById(R.id.but_water);
		but_store = (Button) findViewById(R.id.but_store);
		but_home = (Button) findViewById(R.id.but_home);
		but_seting = (Button) findViewById(R.id.but_seting);
		but_about = (Button) findViewById(R.id.but_about);

		but_water.setOnClickListener(this);
		but_store.setOnClickListener(this);
		but_home.setOnClickListener(this);

		but_seting.setOnClickListener(this);
		but_about.setOnClickListener(this);

		dao = new InStoreDaoInf(this);
		server=new Server(this);

		// ��ʵ���б�����
		initKindSetLvValues();

		kind_seting_lv = (ListView) findViewById(R.id.kind_seting_lv);
		kind_seting_Adapter = new SimpleAdapter(this, listMaps,
				R.layout.item_seting, new String[] { "kind_id", "weight_m",
						"gc_long", "inpay_m", "outpay_m" }, new int[] {
						R.id.item_set_kind_id, R.id.item_set_weight_m,
						R.id.item_set_gc_long, R.id.item_set_inpay_m,
						R.id.item_set_outpay_m });
		kind_seting_lv.setAdapter(kind_seting_Adapter);
		kind_seting_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if(dao.getList(In_store.class)==null&&dao.getList(Out_store.class)==null)
				{
					AlertDialog.Builder menuDialog = new Builder(KindAty.this);
					View menuView = LayoutInflater.from(KindAty.this).inflate(
							R.layout.kind_set_dialog, null);
					ImageButton but_delButton = (ImageButton) menuView
							.findViewById(R.id.imageBut_del);
					ImageButton but_modifyButton = (ImageButton) menuView
							.findViewById(R.id.imageBut_modify);
					
					menuDialog.setTitle("��Ҫ��ɶ��").setView(menuView)
					.setNegativeButton("ȡ��", null).create();
					final AlertDialog fmenudDialog = menuDialog.show();
					// �����¼�
					but_delButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String kind_id = ((TextView) view
									.findViewById(R.id.item_set_kind_id)).getText()
									.toString();
							float weight_m = Float.parseFloat(((TextView) view
									.findViewById(R.id.item_set_weight_m))
									.getText().toString());
							float gc_long = Float.parseFloat(((TextView) view
									.findViewById(R.id.item_set_gc_long)).getText()
									.toString());
							float inpay_m = Float.parseFloat(((TextView) view
									.findViewById(R.id.item_set_inpay_m)).getText()
									.toString());
							float outpay_m = Float.parseFloat(((TextView) view
									.findViewById(R.id.item_set_outpay_m))
									.getText().toString());
							final GcParameter gcParameter = new GcParameter();
							gcParameter.setKind_id(kind_id);
							gcParameter.setWeight_m(weight_m);
							gcParameter.setGc_long(gc_long);
							gcParameter.setInpay_m(inpay_m);
							gcParameter.setOutpay_m(outpay_m);
							String msg = "��ȷ��Ҫɾ��������?\n���ID:" + kind_id + "��/M:"
									+ weight_m + "��/��:" + gc_long + "����:" + inpay_m
									+ "����:" + outpay_m;
							AlertDialog.Builder delDialog = new AlertDialog.Builder(
									KindAty.this);
							delDialog
									.setTitle("��ʾ��")
									.setMessage(msg)
									.setNegativeButton("��", null)
									.setPositiveButton("��",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated method
													// stub
													if (dao.del(gcParameter)) {
														listMaps.remove(position);
														kind_seting_Adapter
																.notifyDataSetChanged();
														Toast.makeText(
																KindAty.this,
																"ɾ���ɹ���",
																Toast.LENGTH_SHORT)
																.show();
													} else {
														Toast.makeText(
																KindAty.this,
																"ɾ��ʧ�ܣ�",
																Toast.LENGTH_SHORT)
																.show();
													}
												}
											}).create().show();
							fmenudDialog.dismiss();
							fmenudDialog.cancel();

						}
					});

					but_modifyButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							fmenudDialog.dismiss();
							fmenudDialog.cancel();
							final View modifyView = LayoutInflater.from(KindAty.this).inflate(R.layout.kind_set_modify_dialog,null);
							final EditText et__kind_id=(EditText)modifyView.findViewById(R.id.edittext_kind_id_mod);
							final EditText et__weight_m=(EditText)modifyView.findViewById(R.id.edittext_weight_m_mod);
							final EditText et__gc_long=(EditText)modifyView.findViewById(R.id.edittext_gc_long_mod);
							final EditText et__inpay_m=(EditText)modifyView.findViewById(R.id.edittext_inpay_m_mod);
							final EditText et__outpay_m=(EditText)modifyView.findViewById(R.id.edittext_outpay_m_mod);
							
							HashMap<String, String> map =listMaps.get(position);
							et__kind_id.setText(map.get("kind_id"));
							et__weight_m.setText(map.get("weight_m"));
							et__gc_long.setText(map.get("gc_long"));
							et__inpay_m.setText(map.get("inpay_m"));
							et__outpay_m.setText(map.get("outpay_m"));
							
							
							AlertDialog.Builder modifyBuilder = new Builder(KindAty.this).setTitle("�޸ģ�").setView(modifyView)
									.setNegativeButton("ȡ��", null).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											
											
											String kind_id = et__kind_id.getText().toString();
											float weight_m = Float.parseFloat(et__weight_m.getText().toString());
											float gc_long = Float.parseFloat(et__gc_long.getText().toString());
											float inpay_m = Float.parseFloat(et__inpay_m.getText().toString());
											float outpay_m = Float.parseFloat(et__outpay_m.getText().toString());
											GcParameter gcParameter = new GcParameter();
											gcParameter.setKind_id(kind_id);
											gcParameter.setWeight_m(weight_m);
											gcParameter.setGc_long(gc_long);
											gcParameter.setInpay_m(inpay_m);
											gcParameter.setOutpay_m(outpay_m);
											if(dao.upData(gcParameter))
											{
												Toast.makeText(KindAty.this, "�޸ĳɹ���", Toast.LENGTH_SHORT).show();
												init();
											}else {
												Toast.makeText(KindAty.this, "�޸�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
											}
											
										}
									});
							modifyBuilder.create().show();
						}
					});

				}else {
					new Builder(KindAty.this).setTitle("��ʾ��").setMessage("����Ŀ���������ݣ����ܸ��Ļ�ɾ����").setNeutralButton("ȷ��", null).create().show();
				}
			}
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		});

	}

	// ��ʵ���б�����
	private void initKindSetLvValues() {
		list = dao.getList(GcParameter.class);
		if(list!=null)
		{
			listMaps = new ArrayList<HashMap<String, String>>();
			for (Object object : list) 
			{
				GcParameter gcParameter = (GcParameter) object;
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("kind_id", gcParameter.getKind_id());
				map.put("weight_m", gcParameter.getWeight_m() + "");
				map.put("gc_long", gcParameter.getGc_long() + "");
				map.put("inpay_m", gcParameter.getInpay_m() + "");
				map.put("outpay_m", gcParameter.getOutpay_m() + "");
				listMaps.add(map);

			}
		}else {
			listMaps = new ArrayList<HashMap<String, String>>();
		}
		
	}

	// ���Ӱ�Ť
	public void add_kind(View view) {
		final GcParameter gcParameter = new GcParameter();
		String kind_id = et_kind_id.getText().toString();
		String weight_m = et_weight_m.getText().toString();
		String gc_long = et_gc_long.getText().toString();
		String inpay_m = et_inpay_m.getText().toString();
		String outpya_m = et_outpay_m.getText().toString();
		if(!server.isNull(kind_id,weight_m,gc_long,inpay_m,outpya_m))
		{
			Toast.makeText(KindAty.this, "���ݴ������Ϊ�գ�", Toast.LENGTH_LONG).show();
			return;
		}
		System.out.println(kind_id + "@" + weight_m + "@" + gc_long + "@"
				+ inpay_m + "@" + outpya_m);
		gcParameter.setKind_id(kind_id);
		gcParameter.setWeight_m(Float.parseFloat(weight_m));
		gcParameter.setGc_long(Float.parseFloat(gc_long));
		gcParameter.setInpay_m(Float.parseFloat(inpay_m));
		gcParameter.setOutpay_m(Float.parseFloat(outpya_m));
		String msg = "��ȷ��Ҫ���Ӵ�����?\n���ID:" + kind_id + "��/M:" + weight_m + "��/��:"
				+ gc_long + "����:" + inpay_m + "����:" + outpya_m;
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("��ʾ��").setMessage(msg)
				.setNegativeButton("��", null)
				.setPositiveButton("��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (dao.save(gcParameter)) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("kind_id", gcParameter.getKind_id());
							map.put("weight_m", gcParameter.getWeight_m() + "");
							map.put("gc_long", gcParameter.getGc_long() + "");
							map.put("inpay_m", gcParameter.getInpay_m() + "");
							map.put("outpay_m", gcParameter.getOutpay_m() + "");
							listMaps.add(map);
							kind_seting_Adapter.notifyDataSetChanged();
						} else {
							System.out.println("��������ʧ�ܣ�~~~~~~~~~~~~");
							Toast.makeText(KindAty.this, "��������ʧ�ܣ�",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).create().show();

	}

	// ����Ť
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.but_water) {
			startActivity(new Intent(KindAty.this, WaterAty.class));
			finish();
		}
		if (v.getId() == R.id.but_store) {
			startActivity(new Intent(KindAty.this, StoreAty.class));
			finish();
		}
		if (v.getId() == R.id.but_home) {
			startActivity(new Intent(KindAty.this, HomeActivity.class));
			finish();
		}
		if (v.getId() == R.id.but_about) {
		}
	}
}