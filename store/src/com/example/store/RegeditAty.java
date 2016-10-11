package com.example.store;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.store.network.Network_util;
import com.example.store.server.Server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegeditAty extends Activity {
	

	private Handler handler;
	private Server server;
	private EditText editText_user_name, editText_user_psw, editText_qq;
	private RadioGroup radio_group_gender;
	private RadioButton radioButton_girl, radioButton_boy;
	private CheckBox checkBox_run, checkBox_cycling, checkBox_play_badminton,
			checkBox_play_basketball, checkBox_tai_ji, checkBox_free_combat,
			checkBox_limit, checkBox_look_book, checkBox_move, checkBox_music;
	
	private Button but_regedit;

	private String user_name;
	private String user_psw;
	private String gander;
	private String qq;
	private String hobbly = "";
	private CheckBox[] arrayBoxs;
	private JSONObject userJsonObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_regedit);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) 
			{
				// TODO Auto-generated method stub
				if (msg.what == CodeId.MessageID.REGEDIT_USER_OK) {
					final JSONObject jsonObject = (JSONObject) msg.obj;
					try {
					final JSONObject user_jsonJsonObject=new JSONObject(jsonObject.getString("json_message"));
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RegeditAty.this);
					
						builder.setCancelable(false).setTitle("��ϲ ��ϲ")
								.setMessage(("��ϲע��ɹ���\n����ͨ��ID�ǣ�"+user_jsonJsonObject.getInt("id")+"\n" +
										"�������ô�ID������������е��룡"))
								.setNeutralButton("ȷ��", new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

										System.out.println("ע��ɹ����ص�JSON:"+user_jsonJsonObject.toString());
										Intent intent = new Intent();
										intent.putExtra("user_json",user_jsonJsonObject.toString());
										setResult(Activity.RESULT_OK, intent);
										finish();
									}
								}).create().show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (msg.what == CodeId.MessageID.REGEDIT_USER_FAIL) {
					Toast.makeText(RegeditAty.this, msg.obj.toString(),
							Toast.LENGTH_LONG).show();
					but_regedit.setEnabled(true);
				}
				if(msg.what==CodeId.MessageID.IS_NAME_OK)
				{
					new AlertDialog.Builder(RegeditAty.this).setCancelable(false).setTitle("���棡")
					.setMessage(msg.obj.toString()).setNeutralButton("ȷ��", null)
					.create().show();
					but_regedit.setEnabled(true);
				}
					
			}

		};
		server = new Server(this);
		editText_user_name = (EditText) findViewById(R.id.editText_user_name);
		editText_user_psw = (EditText) findViewById(R.id.editText_user_psw);
		editText_qq = (EditText) findViewById(R.id.editText_qq);

		radio_group_gender = (RadioGroup) findViewById(R.id.radio_group_gender);
		radio_group_gender
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (radioButton_girl.getId() == checkedId) {
							try {
								gander = URLEncoder.encode(
										(radioButton_girl.getText().toString()),
										"utf-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							try {
								gander = URLEncoder.encode(radioButton_boy
										.getText().toString(), "utf-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

		radioButton_girl = (RadioButton) findViewById(R.id.radioButton_girl);
		radioButton_boy = (RadioButton) findViewById(R.id.radioButton_boy);

		checkBox_run = (CheckBox) findViewById(R.id.checkBox_run);
		checkBox_cycling = (CheckBox) findViewById(R.id.checkBox_cycling);
		checkBox_play_badminton = (CheckBox) findViewById(R.id.checkBox_play_badminton);
		checkBox_play_basketball = (CheckBox) findViewById(R.id.checkBox_play_basketball);
		checkBox_tai_ji = (CheckBox) findViewById(R.id.checkBox_tai_ji);
		checkBox_free_combat = (CheckBox) findViewById(R.id.checkBox_free_combat);
		checkBox_limit = (CheckBox) findViewById(R.id.checkBox_limit);
		checkBox_look_book = (CheckBox) findViewById(R.id.checkBox_look_book);
		checkBox_move = (CheckBox) findViewById(R.id.checkBox_move);
		checkBox_music = (CheckBox) findViewById(R.id.checkBox_music);

		arrayBoxs = new CheckBox[] { checkBox_run, checkBox_cycling,
				checkBox_play_badminton, checkBox_play_basketball,
				checkBox_tai_ji, checkBox_free_combat, checkBox_limit,
				checkBox_look_book, checkBox_move, checkBox_music };
		
		but_regedit = (Button) findViewById(R.id.but_regedit);

	}

	/**
	 * ע���¼�
	 * 
	 * @param view
	 */
	public void regedit_in(View view) {
		view.setEnabled(false);
		user_name = editText_user_name.getText().toString();
		user_psw = editText_user_psw.getText().toString();
		qq = (editText_qq.getText().toString().equals("")) ? "δ֪"
				: (editText_qq.getText().toString());
		for (CheckBox checkBox : arrayBoxs) {
			if (checkBox.isChecked()) {
				hobbly += checkBox.getText().toString() + ",";
			}
		}
		if (!hobbly.equals("")) {
			hobbly = hobbly.substring(0, hobbly.length() - 1);
		} else {
			hobbly = "���˺�����~~~";
		}

		if (server.isNull(user_name, user_psw, gander)) {
			JSONObject userJsonObject = new JSONObject();
			try {
				userJsonObject.put("photo_image", "0");
				userJsonObject.put("user_name", user_name);
				userJsonObject.put("user_psw", user_psw);
				userJsonObject.put("gander", gander);
				userJsonObject.put("qq", qq);
				userJsonObject.put("hobbly", hobbly);
				userJsonObject.put("signature", "������磬û�������κζ���~~");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String jsonString = userJsonObject.toString();
			Toast.makeText(RegeditAty.this, jsonString, Toast.LENGTH_LONG)
					.show();
			String urlString = null;
			urlString = "http://192.168.254.101/datapackage/userdata";
			// String
			// urlString="http://192.168.254.100/datapackage/Userdate?action=user_regedit";
			System.out.println(urlString);
				Network_util.regeditUser(urlString, userJsonObject, handler);
		} else {
			new AlertDialog.Builder(this).setCancelable(false).setTitle("���棡")
					.setMessage("��*�ŵ�Ϊ�����").setNeutralButton("ȷ��", null)
					.create().show();
			but_regedit.setEnabled(true);
		}

	}

	/**
	 * ע��ȡ���¼�
	 * 
	 * @param view
	 */
	public void regedit_cancel(View view) {
		startActivity(new Intent(RegeditAty.this, HomeActivity.class));
		finish();
	}
}
