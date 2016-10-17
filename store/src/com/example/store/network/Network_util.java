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
	public static void regeditUser(final String url, final JSONObject object,final Handler handler) 
	{
		new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				try {
					
						try {
							HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
							
							connection.setReadTimeout(1000 * 10);
							connection.setRequestMethod("POST");
							OutputStream outputStream = connection.getOutputStream();
							outputStream.write(("action=user_regedit&userJson=" + object.toString()).getBytes());
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
									String result = resulJsonObject.getString("result");
									if("pass".equals(result))
									{
										handler.sendMessage(handler.obtainMessage(
												CodeId.MessageID.REGEDIT_USER_OK, resulJsonObject));
									}else {
										handler.sendMessage(handler.obtainMessage(
												CodeId.MessageID.REGEDIT_USER_FAIL, "ע��ʧ�ܣ�"));
									}
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
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}).start();
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
					connection.setReadTimeout(1000 * 10);
					connection.setConnectTimeout(1000 * 10);
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
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, login_resultJsonObject.getString("json_message")));
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

	/**
	 * �����û�����
	 * @param handler
	 * @param userJsonObject
	 */
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

//	/**
//	 * �ϴ����ݵ�������
//	 * @param path �ϴ����ļ�·��
//	 * @return 1,�ļ�Ϊ�գ�2�ɹ���3ʧ��
//	 */
//	public static  void  dataToServer(File path,Handler handler) {
//		// TODO Auto-generated method stub
//		if(path==null)
//		{
//			handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_DATA_FILE_IS_ENP, "�ļ�Ϊ�գ�"));
//		}else {
//			int count=1;
//			if(path.isDirectory())
//			{
//				File [] filelist = path.listFiles();
//				for (; count <= filelist.length; count++) {
//					dataToServerGo(filelist[count-1],handler);
//				}
//			}else {
//				dataToServerGo(path,handler);
//			}
//		}
//		
//	}
//
//	public static void dataToServerGo(final File file,final Handler handler) {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpURLConnection connection = null;
//					    String end = "/r/n";
//					    String Hyphens = "--";
//					    String boundary =UUID.randomUUID().toString();
//					    try
//					    {
//					      URL url = new URL("http://192.168.254.101/datapackage/userdata");
//					      HttpURLConnection con = (HttpURLConnection) url.openConnection();
//					      /* ����Input��Output����ʹ��Cache */
//					      con.setDoInput(true);
//					      con.setDoOutput(true);
//					      con.setUseCaches(false);
//					      /* �趨���͵�method=POST */
//					      con.setRequestMethod("POST");
//					      /* setRequestProperty */
//					      con.setRequestProperty("Connection", "Keep-Alive");
//					      con.setRequestProperty("Charset", "UTF-8");
//					      con.setRequestProperty("Content-Type",
//					          "multipart/form-data;boundary=" + boundary);
//					      /* �趨DataOutputStream */
//					      DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//					      ds.writeBytes("action=updata");
//					      ds.writeBytes(Hyphens + boundary + end);
//					      ds.writeBytes("Content-Disposition: form-data; "
//					          + "name=\"file1\";filename=\"" + "sdfe" + "\"" + end);
//					      ds.writeBytes(end);
//					      /* ȡ���ļ���FileInputStream */
//					      FileInputStream fStream = new FileInputStream(file);
//					      /* �趨ÿ��д��1024bytes */
//					      int bufferSize = 1024;
//					      byte[] buffer = new byte[bufferSize];
//					      int length = -1;
//					      /* ���ļ���ȡ���ݵ������� */
//					      while ((length = fStream.read(buffer)) != -1)
//					      {
//					        /* ������д��DataOutputStream�� */
//					        ds.write(buffer, 0, length);
//					      }
//					      ds.writeBytes(end);
//					      ds.writeBytes(Hyphens + boundary + Hyphens + end);
//					      fStream.close();
//					      ds.flush();
//					      /* ȡ��Response���� */
////					      InputStream is = con.getInputStream();
////					      int ch;
////					      StringBuffer b = new StringBuffer();
////					      while ((ch = is.read()) != -1)
////					      {
////					        b.append((char) ch);
////					      }
////					      is.close();
////					      ds.close();
////					      if(b.toString().equals("ok"))
////					      {
//					    	  //handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_DATA_FILE_IS_OK, "�ϴ��ɹ���"));
//					      System.out.println("�ϴ��ɹ�");
////					      }
////					      handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_DATA_FILE_IS_FAIL, "�ϴ�ʧ�ܣ�"));
//					    } catch (Exception e)
//					    {
//					    	e.printStackTrace();
//					    	System.out.println("�ϴ�ʧ��");
//					    }
//			}
//
//		}).start();
//	}


	/**
	 * �û����߸���
	 * @param user_json ���ߵ��û�JSON
	 * @param handler 
	 */
	public static void unLine(final String user_json, final Handler handler) {
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
							.write(("action=un_line&user_json=" + user_json)
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
						if ("un_line".equals(action)) {
							if ("pass".equals(login_resultJsonObject
									.getString("result"))) {
								
								System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~"+handler);
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_OK, "�������ߣ�"));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "�������᲻�����£����������ɡ�"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "json:�����쳣�����Ժ����ԣ�"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "MalformedURL:�����쳣�����Ժ����ԣ�"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "IO:�����쳣�����Ժ����ԣ�"));
				}

			}
		}).start();
	}
	public static void upDataToServer(final JSONObject jsonObject, final Handler handler) {
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
							.write(("action=uptable&up_data_json_string=" + jsonObject.toString())
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject updata_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("�����ϴ������"+updata_resultJsonObject.toString());
						String action = updata_resultJsonObject
								.getString("action");
						if ("uptable".equals(action)) {
							if ("pass".equals(updata_resultJsonObject
									.getString("result"))) {
								
								System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~"+handler);
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_OK, "�����ϴ��ɹ���"));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "���� �ϴ�ʧ���ˣ����Ժ����ԡ�"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "json:�����쳣�����Ժ����ԣ�"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "MalformedURL:�����쳣�����Ժ����ԣ�"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "IO:�����쳣�����Ժ����ԣ�"));
				}

			}
		}).start();
	}
	
	
}
