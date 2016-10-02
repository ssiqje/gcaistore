package com.example.store.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.store.CodeId;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class Network_util {
	

	/**
	 * ע��һ���û�
	 * @param url �����ַ
	 * @param object ע�����
	 * @param handler 
	 * @return ����HANDLER MSG,�ɹ����� WHAT=REGEDIT_USER_OK  ʧ�ܷ��� WHAT=REGEDIT_USER_FAIL
	 */
	public static void regeditUser(final String url, final JSONObject object,
			final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					int i = isNameOk(object.getString("user_name"));
					if (i == 1) {
						try {
							HttpURLConnection connection = (HttpURLConnection) new URL(
									url).openConnection();
							connection.setReadTimeout(1000 * 5);
							connection.setRequestMethod("POST");
							OutputStream outputStream = connection
									.getOutputStream();
							outputStream
									.write(("action=user_regedit&userJson=" + object
											.toString()).getBytes());
							if (connection.getResponseCode() == 200) {
								BufferedReader brReader = new BufferedReader(
										new InputStreamReader(connection
												.getInputStream()));
								StringBuffer sbBuffer = new StringBuffer();
								String lineString = null;
								while ((lineString = brReader.readLine()) != null) {
									sbBuffer.append(lineString);
								}
								String resultString = sbBuffer.toString();
								if (!"".equals(resultString)) {
									JSONObject resulJsonObject = new JSONObject(
											resultString);
									System.out.println("���յ�ע��ɹ���JSON:"
											+ resulJsonObject.toString());
									handler.sendMessage(handler.obtainMessage(
											CodeId.MessageID.REGEDIT_USER_OK, resulJsonObject));
								}

							} else {
								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.REGEDIT_USER_FAIL, "ע��ʧ�ܣ�"));
							}

						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendMessage(handler.obtainMessage(
									CodeId.MessageID.REGEDIT_USER_FAIL, "ע��ʧ�ܣ�"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendMessage(handler.obtainMessage(
									CodeId.MessageID.REGEDIT_USER_FAIL, "ע��ʧ�ܣ�"));
						}
					} else if (i == 2) {
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.IS_NAME_OK,
								"�û����Ѵ��ڣ�"));
					} else {
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.IS_NAME_OK,
								"������˼����������С���ˣ������Ժ����ԣ�"));
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * �ж��û����ڷ��������Ƿ��Ѵ���
	 * 
	 * @param user_nameҪ��ѯ���û���
	 * @return 1�ϸ� ��2���ϸ�3����������
	 */
	public static int isNameOk(final String user_name) {
		// TODO Auto-generated method stub

		System.out
				.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// TODO Auto-generated method stub
		String url = "http://192.168.254.101/datapackage/userdata";
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setReadTimeout(1000 * 5);
			connection.setRequestMethod("POST");
			OutputStream outputStream = connection.getOutputStream();
			outputStream
					.write(("action=get_user_by_name&user_name=" + user_name)
							.getBytes());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			StringBuffer sbBuffer = new StringBuffer();
			String line;
			while ((line = bReader.readLine()) != null) {
				System.out.println(line);
				sbBuffer.append(line);
			}
			String resultString = connection.getResponseMessage();
			System.out.println("���صĽ��:" + resultString + "���ص����ݣ�"
					+ sbBuffer.toString());
			JSONObject resultJsonObject = null;
			try {
				resultJsonObject = new JSONObject(sbBuffer.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String result = null;
			try {
				result = resultJsonObject.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result.equals("yes")) {
				return 1;
			} else {
				return 2;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 3;
	}
	/**
	 * ��ȡ�������ϵ���Ƭ�б�
	 * @param handler
	 * @return ����HANDLER MSG,�ɹ����� WHAT=GET_PHOTO_LIST_OK
	 */
	public static void getPhotoList(final Handler handler) {
		// TODO Auto-generated method stub
		final ArrayList<HashMap<String, Object>> array_list_photo = new ArrayList<HashMap<String, Object>>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata?action=get_photo_add_list")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("GET");
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject photo_infJsonObject = new JSONObject(
								sbBuffer.toString());
						String action = photo_infJsonObject.getString("action");
						if ("get_photo_add_list".equals(action)) {
							if ("ok".equals(photo_infJsonObject
									.getString("result"))) {
								JSONArray jsonArray = photo_infJsonObject
										.getJSONArray("json_message");
								for (int i = 0; i < jsonArray.length(); i++) {
									Object object = jsonArray.get(i);
									System.out.println("netwoke:" + object);
									String urlString = "http://192.168.254.101/"
											+ object;
									System.out.println("url:" + urlString);
									connection = (HttpURLConnection) new URL(
											urlString).openConnection();
									System.out.println();
									connection.setReadTimeout(1000 * 5);
									connection.setRequestMethod("GET");
									Bitmap bitmap = BitmapFactory
											.decodeStream(connection
													.getInputStream());
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("bitmap", bitmap);
									map.put("url", urlString);
									map.put("title", urlString.subSequence(
											urlString.lastIndexOf("/"),
											urlString.length()));
									array_list_photo.add(map);
								}
								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.GET_PHOTO_LIST_OK, array_list_photo));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * �ӷ������ϻ�ȡָ������Ƭ
	 * @param url ��Ƭ��ַ
	 * @return ����HANDLER MSG,�ɹ����� WHAT=GET_PHOTO_ONE_OK
	 */
	public static void getPhotoBuyOne(final Handler handler, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(
							url).openConnection();
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					connection.setRequestMethod("GET");
					Bitmap bitmap = BitmapFactory.decodeStream(connection
							.getInputStream());
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("url", url);
					map.put("bitmap", bitmap);
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.GET_PHOTO_ONE_OK,
							map));

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).start();
	}

	/**
	 * �����û��Ƿ���Ե���
	 * @param handler
	 * @param user_json ��������û�
	 * @return ����HANDLER MSG,�ɹ����� WHAT=LOGIN_USER_PASS  ʧ�ܷ��� WHAT=LOGIN_USER_FAIL
	 */
	public static void login_user(final Handler handler, final String user_json) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("POST");
					OutputStream outputStream = connection.getOutputStream();
					outputStream
							.write(("action=login_user&user_json=" + user_json)
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject login_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("�����û����ϲ��ҽ����"+login_resultJsonObject.toString());
						String action = login_resultJsonObject
								.getString("action");
						if ("login_user".equals(action)) {
							if ("pass".equals(login_resultJsonObject
									.getString("result"))) {
								

								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.LOGIN_USER_PASS, login_resultJsonObject.getString("json_message")));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "�û�ID���������"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "json:�����쳣�����Ժ����ԣ�"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "MalformedURL:�����쳣�����Ժ����ԣ�"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "IO:�����쳣�����Ժ����ԣ�"));
				}

			}
		}).start();
	}

	public static void upUser(final Handler handler, final JSONObject userJsonObject) {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("POST");
					OutputStream outputStream = connection.getOutputStream();
					outputStream
							.write(("action=up_user_data&user_json=" + userJsonObject.toString())
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject login_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("�û����ϸ��½����"+login_resultJsonObject.toString());
						String action = login_resultJsonObject
								.getString("action");
						if ("up_user_data".equals(action)) {
							if ("pass".equals(login_resultJsonObject
									.getString("result"))) {
								

								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.UP_USER_DATA_OK, "�����ϴ��ɹ���"));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "�����ϴ�ʧ�ܣ�"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "json:�����쳣�����Ժ����ԣ�"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "MalformedURL:�����쳣�����Ժ����ԣ�"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "IO:�����쳣�����Ժ����ԣ�"));
				}

			}
		}).start();
		
		
		
	}
}