/**
 * 发布纪录页面       日期：2011-7-25  
 * 作者： 俞欣彤    武逸超
 */
package irdc.sns;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class fbjilu7 extends Activity
{
	private Spinner sp1;
	private ArrayAdapter<String> adapter;
	ArrayList<String> biaoqianid = new ArrayList<String>();
	ArrayList<String> biaoqian = new ArrayList<String>();
	ArrayList<HashMap<String, Object>> tupian= new ArrayList<HashMap<String, Object>>();
	
	int length=1;
	String blogid=null;  //如果是编辑已有纪录，blogid是已有记录的id
	//String blogid2=null; 
	String pkid=null;   //如果是用于pk  pkid是被pk的纪录的id
	String stagid="0";  //本记录标签id
	private EditText et1; //标题
	private EditText et2; //正文
	private Button capturePicture,uploadPicture,publish,saveToDrafts;
	private int distinguisher;
	boolean panduan;
	String result;
	String result0;   //用于提示
	String resultString1;
	private File photoFile;
	private int counter=1;    //计数  初始为1 有一张照片后为2
	private Bitmap bitmap;   //图片   通信时重复使用
	String pic_title="";  //图片描述 通信时重复使用
	Cursor cursor ;  //用于得到图片存储路径
	//String tixing=null;
	String[] p=new String[5];
	String[] miaoshu=new String[5];
	private LinearLayout mLoadLayout;    
	private final LayoutParams WW = new LinearLayout.LayoutParams
	( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
	private final LayoutParams FW = new LinearLayout.LayoutParams
	( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
	
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
  	int STATUS=0;
  	String WODE=null;
  	String FRIEND;  //发布到草稿箱  3  发布 0
  	String PK=null;  //用于判断发记录是用于PK 还是 发布新纪录    1  PK  0  非PK
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbjilu7);
        capturePicture=(Button)findViewById(R.id.b1);
        uploadPicture =(Button)findViewById(R.id.b2);
        publish=(Button)findViewById(R.id.b4);
        saveToDrafts=(Button)findViewById(R.id.b3);
        et1= (EditText) findViewById(R.id.et1);
        et2= (EditText) findViewById(R.id.et2);
        sp1= (Spinner) findViewById(R.id.sp1);
        capturePicture.setOnClickListener(new ButtonListener()); 
        uploadPicture.setOnClickListener(new ButtonListener()); 
        publish.setOnClickListener(new ButtonListener()); 
        saveToDrafts.setOnClickListener(new ButtonListener()); 
        mLoadLayout=(LinearLayout)findViewById(R.id.tupian);
        
        getwhole();
        Bundle bundle=this.getIntent().getExtras();System.out.println("0");
        if(PK.equals("1")) //如果发布纪录是用于PK的  ，设置固定标签，不可选择
        {
        	pkid=bundle.getString("pkid"); System.out.println("0");
        	stagid=pkid;  //本纪录标签与被PK的纪录标签相同
        	biaoqian.add(bundle.getString("pkbiaoqian")); System.out.println(bundle.getString("pkbiaoqian"));
        	biaoqianid.add(bundle.getString("pkbiaoqianid")); 
        }
        else   //如果是发布新纪录
        {
        	tongxin_getbiaoqian();  //获得标签列表
		}
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,biaoqian);
        if(WODE.equals("draft"))  //如果是从草稿箱选择一个来编辑
        {
        	blogid=bundle.getString("blogid");  //要编辑的纪录的blogid
        	tongxin_bianji();
        	if(PK.equals("0"))
	        	for(int i=0;i<biaoqianid.size();i++)  //设置显示的标签   根据biaoqianid与biaoqian之间的一一对应关系
	            	if(biaoqianid.get(i).equals(stagid))  {sp1.setSelection(i);break;}
        }
        
        sp1.setAdapter(adapter); 
        adapter.setDropDownViewResource(R.layout.myspinner_dropdown); //设置下拉菜单格式 myspinner_dropdown.xml
         sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() //下拉菜单选择
        {
          @Override
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  stagid=biaoqianid.get(arg2);
          }
          @Override
          public void onNothingSelected(AdapterView<?> arg0)
          {}
        });
         /* sp1.setOnTouchListener(new Spinner.OnTouchListener()
        {
          @Override
          public boolean onTouch(View v, MotionEvent event)
          {
            v.setVisibility(View.INVISIBLE);
            return false;
          }
        });
        sp1.setOnFocusChangeListener(new Spinner.OnFocusChangeListener()
        {
          @Override
          public void onFocusChange(View v, boolean hasFocus)
          {}
        });*/
    }
    public void tongxin_getbiaoqian()  //获得标签
    {
    	whole a = (whole) getApplicationContext();
    	String url = URL+ "/uc/uchome/space.php?do=stag_api";
    	System.out.println("url = "+url);
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(url);
		try { 
			httpResponse= new DefaultHttpClient().execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200)
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println(result);
				usetoast(result);
				if(panduan==true)
				{
					JSONObject jsonObject = new JSONObject(result); 
					JSONArray jsonArray = jsonObject.getJSONArray("stag_list");
					length=jsonArray.length();
					for(int i=0;i<jsonArray.length();i++)
					{
						JSONObject jsonObject1 = (JSONObject)jsonArray.opt(i);
						biaoqianid.add(jsonObject1.getString("stagid"));
						biaoqian.add(jsonObject1.getString("stagname"));
					}
				}
			}
			else  
				System.out.println("100");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void tongxin_bianji()  //编辑已有草稿
    {
    	whole a = (whole) getApplicationContext();
        String url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_view&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&blogid="+blogid;  
		System.out.println("url = "+url);
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			httpResponse= new DefaultHttpClient().execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200)
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println(result);
				usetoast(result);
				if(panduan==true)
				{
					JSONObject jsonObject = new JSONObject(result); 
					JSONArray jsonArray = jsonObject.getJSONArray("blog_view");
					JSONObject jsonObject1 = (JSONObject)jsonArray.opt(0);
					Spanned biaoti=Html.fromHtml(jsonObject1.getString("subject"));
					et1.setText(biaoti); 
					Spanned zhengwen=Html.fromHtml(jsonObject1.getString("message"));
					et2.setText(zhengwen); 
					stagid=jsonObject1.getString("stagid");
					//获得图片及图片描述
					JSONArray jsonArray_pic=jsonObject1.getJSONArray("pic");				
					for(int i=0;i<jsonArray_pic.length();i++)
					{
						System.out.println("1");
						JSONObject jsonObject2=(JSONObject)jsonArray_pic.opt(i);
						pic_title=Html.fromHtml(jsonObject2.getString("pic_title")).toString();
						String pic_url="/uc/uchome/"+jsonObject2.getString("pic_url");
						
						bitmap=a.save_pic(pic_url);   //保存图片到本地并显示出来
						p[counter-1]=a.getLujing();
						miaoshu[counter-1]=pic_url;
						counter++;
				        if (bitmap != null)  
				        { addpic(pic_title); }
					}
				}
			}
			else  
				System.out.println("100");
		}
		catch (IOException e) { e.printStackTrace(); } 
		catch (JSONException e) { e.printStackTrace(); }
    }
    
    public void addpic(String pic_title)
    {
    	final HashMap<String, Object> map  = new HashMap<String, Object>();
    	map.put("bitmap", bitmap);
    	final ImageView iv1= new ImageView(this);  //图片
    	final EditText et_pic=new EditText(this);  //图片描述编辑框
    	final Button b_pic = new Button(this);   //删除图片
    	final Button b_tv = new Button(this);    //保存描述文字
    	final LinearLayout btwo = new LinearLayout(this);     //btwo 包括两个按钮
    	btwo.setOrientation(LinearLayout.HORIZONTAL);          
        iv1.setImageBitmap(bitmap);
        iv1.setAdjustViewBounds(true);
        mLoadLayout.addView(iv1, WW);
        if(pic_title.equals(""))  
    	{
        	et_pic.setHint("请输入图片说明");
    	}
        else  
        {
        	et_pic.setText(pic_title);
        }
        map.put("pic_title", pic_title); 
    	et_pic.setTextColor(Color.BLACK);
    	et_pic.setTextSize(16);
        mLoadLayout.addView(et_pic, FW);
        b_pic.setText("删除此图片");
        b_pic.setOnClickListener(new Button.OnClickListener()
        {   @Override
	        public void onClick(View v)
	        {mLoadLayout.removeView(iv1);
	        mLoadLayout.removeView(et_pic);
	        mLoadLayout.removeView(btwo);
	        tupian.remove(map);
	        counter--;
	        }
        });
        b_tv.setText("保存文字");
        b_tv.setOnClickListener(new Button.OnClickListener()
        {   @Override
	        public void onClick(View v)
	        {
        	map.put("pic_title", et_pic.getText().toString());
        	miaoshu[counter-1]=et_pic.getText().toString();
        	usetoast("已保存");
	        }
        });
        btwo.addView(b_tv,WW);
        btwo.addView(b_pic,WW);
        mLoadLayout.addView(btwo);
        tupian.add(map);
    }
    
    class ButtonListener implements android.view.View.OnClickListener
    {
		@Override
		public void onClick(View v) {
		  switch(v.getId())
		  {
	       case R.id.b1:
	    	   distinguisher=0;
	           if(counter<=5)
	           {
		           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		           String file = Environment.getExternalStorageDirectory().toString()+"/"+System.currentTimeMillis()

+".jpg";
			       System.out.println(file);
			       photoFile = new File(file);
			       p[counter-1]=file;
		           try { photoFile.createNewFile();} catch (Exception e) { }
		           startActivityForResult(intent, 1);
               }
               else
               {
            	   usetoast("最多上传5张图片");
               }
	           capturePicture.setText("再拍一张");
	           break;
	       case R.id.b2:
	    	   distinguisher=1;
	           if(counter<=5)
	           {
		           Intent intent = new Intent();
		           intent.setType("image/*");
		           intent.setAction(Intent.ACTION_GET_CONTENT);
		           startActivityForResult(intent, 1);
               }
	           else{ usetoast("最多上传5张图片"); }
	           uploadPicture.setText("再传一张");
	           break;
	       case R.id.b4:
	    	   panduan=true;
	    	   if(et1.getText().toString().equals("")) usetoast("标题不能为空");
	    	   if(et2.getText().toString().equals("")) usetoast("正文不能为空");
	    	   if(panduan==true)
	    	   {
	    		   FRIEND="0";
		    	   publish.setText("正在上传，请稍等");
		    	   fabu(R.id.b4);
	    	   }
	    	   break;
	       case R.id.b3:
	    	   panduan=true;
	    	   if(et1.getText().toString().equals("")) usetoast("标题不能为空");
	    	   if(et2.getText().toString().equals("")) usetoast("正文不能为空");
	    	   if(panduan==true)
	    	   {
		    	   FRIEND="3";
		    	   saveToDrafts.setText("正在上传，请稍等");
		    	   fabu(R.id.b3);
		    	   break;
	    	   }
		  }
	   }
	}
    
    public void fabu(int anniu)  
    {
    	String uploadUrl = URL+ "/uc/uchome/space.php?do=record_publish_api";
        HttpResponse httpResponse=null;
        //String resultString = null;
        HttpPost httpPost=new HttpPost(uploadUrl);
        System.out.println("ready");  //上传文字部分
        List<NameValuePair>paramsList=new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("userid", USERID));
        paramsList.add(new BasicNameValuePair("username", USERNAME));
        paramsList.add(new BasicNameValuePair("psw", PSW));
        paramsList.add(new BasicNameValuePair("api", "blog_publish"));
        paramsList.add(new BasicNameValuePair("subject", et1.getText().toString()));
        paramsList.add(new BasicNameValuePair("stagid", stagid));
        paramsList.add(new BasicNameValuePair("message", et2.getText().toString()));
        paramsList.add(new BasicNameValuePair("friend", FRIEND));
        if(WODE.equals("draft"))  //如果是从草稿箱中发布
        	paramsList.add(new BasicNameValuePair("blogid", blogid)); 
        if(PK.equals("1")) //如果发布纪录是用于PK的
        	paramsList.add(new BasicNameValuePair("pkid", pkid)); 
		//设置HTTPPOST请求参数
        try
        {
			httpPost.setEntity(new UrlEncodedFormEntity(paramsList,HTTP.UTF_8));
			httpResponse=new DefaultHttpClient().execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				resultString1 = EntityUtils.toString(httpResponse.getEntity());
		        //去掉返回结果中的"/r"字符，否则会在结果字符串中显示一个小方格
				result=resultString1.replaceAll("/r", "");
				System.out.println(result);
				usetoast(result);
            }
        }
        catch(ClientProtocolException e)
		{e.printStackTrace();}
		catch(IOException e)
		{e.printStackTrace();}
		
        //上传图片部分
		if(!(counter==1))
		{
			System.out.println("begin");
	        int i;
	        String end = "\r\n";
	        String twoHyphens = "--";
	        String boundary = "******";
	        try
	        {
	        	for(i=0;i<counter-1;i++)
	    		{
	        		System.out.println(counter);
	            	String uploadpicUrl = URL+ "/uc/uchome/space.php?do=record_pic_api";
					URL url = new URL(uploadpicUrl);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setDoInput(true);
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setUseCaches(false);
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
					httpURLConnection.setRequestProperty("Charset", "UTF-8");
					//必须在Content-type请求头中指定分界符中的任意字符串
					httpURLConnection.setRequestProperty("Content-Type",
							"multipart/form-data;boundary=" + boundary);
					
					DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; " +"name=\"username\";"+end+end+USERNAME+end);
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; " +"name=\"userid\";"+end+end+USERID+end);
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; " +"name=\"psw\";"+end+end+PSW+end);
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; " +"name=\"api\";"+end+end+"pic_upload"+end);
					
					HashMap<String, Object> map  = new HashMap<String, Object>();
					map=tupian.get(i);
					dos.writeBytes(twoHyphens + boundary + end);
					dos.writeBytes("Content-Disposition: form-data; " +"name=\"title[]\";"+end+end+map.get("pic_title")+end);
		            if(WODE.equals("draft"))  //如果是从草稿箱中发布
		            {
		            	dos.writeBytes(twoHyphens + boundary + end);
						dos.writeBytes("Content-Disposition: form-data; " +"name=\"blogid\";"+end+end+blogid+end);
					}
		            else 
		            {
		            	dos.writeBytes(twoHyphens + boundary + end);
						dos.writeBytes("Content-Disposition: form-data; " +"name=\"blogid\";"+end+end+resultString1+end);
					}
		            if(PK.equals("1")) //如果发布纪录是用于PK的
		            {
		            	dos.writeBytes(twoHyphens + boundary + end);
						dos.writeBytes("Content-Disposition: form-data; " +"name=\"pkid\";"+end+end+pkid+end);
		            }
					//设置分界符
					System.out.println("ready");
					dos.writeBytes(twoHyphens + boundary + end);
					//设置与上传文件相关的信息
					
					dos.writeBytes("Content-Disposition: form-data;  name=\"attach[]\"; filename=\""
									+ p[i].substring(p[i].lastIndexOf("/") + 1)
									+ "\"" + end);
					dos.writeBytes(end);
					FileInputStream fis = new FileInputStream(p[i]);
					byte[] buffer = new byte[8192]; // 8k
					int count = 0;
					while ((count = fis.read(buffer)) != -1)
					   {dos.write(buffer, 0, count);}
					fis.close();
					dos.writeBytes(end);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
					dos.flush();
					InputStream is = httpURLConnection.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					result = br.readLine();
					System.out.println(result); 
	                usetoast(result);
					dos.close();
					is.close();
					/*
					Intent intent=new Intent();   //有图片时跳转
					if(PK.equals("1"))
						intent.setClass(fbjilu7.this,pk8.class );
					else
						intent.setClass(fbjilu7.this,pingding5.class );
					Bundle bundle=new Bundle();
					bundle.putString("blogid", tixing);
					intent.putExtras(bundle);
					fbjilu7.this.startActivity(intent);
					finish();*/
				}
	        }
	        catch (Exception e){setTitle(e.getMessage());}
		} //上传图片部分if结束
		//else //无图片时跳转
		//{
		if(panduan==true)
		{
			if(anniu==R.id.b3)  
			{
				if(PK.equals("1"))
					usetoast("用于PK的纪录不能保存到草稿箱");
				else
				{
					whole a = (whole) getApplicationContext();
					a.setOtherid(a.getUserid());
		    		Intent intent = new Intent();
			        intent.setClass(fbjilu7.this, wodejilu7_1.class);
			        startActivity(intent);
			        finish();
				}
			}
			else 
			{
				Intent intent=new Intent();
				if(PK.equals("1"))
					intent.setClass(fbjilu7.this,pk8.class );
				else
					intent.setClass(fbjilu7.this,pingding5.class );
				Bundle bundle=new Bundle();
				bundle.putString("blogid", result);
				intent.putExtras(bundle);
				fbjilu7.this.startActivity(intent);
				finish();
			}
		}
		//}
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)//拍照上传
	{
	  switch(distinguisher){
	  case 0:if (requestCode == 1)
		     {
			   if (resultCode == Activity.RESULT_OK)
			  {
				try {
					bitmap = (Bitmap) data.getExtras().get("data");
					pic_title="";
			        addpic(pic_title);
			        //bitmap=bitmapRoom(bitmap,500,500);
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(photoFile));
					bitmap.compress(Bitmap.CompressFormat.JPEG,100, bos);

					//  存成320*240和50*50  如果是垂直方向，需要对cameraBitmap旋转90度
					MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null);
					counter++;
				} catch (Exception e) {}
			   }
		    }break;
	        case 1:if (resultCode == RESULT_OK)
		    {
		      Uri uri = data.getData();
		      ContentResolver cr = this.getContentResolver();
		      cursor = this.getContentResolver().query(uri, null, null, null, null);
              cursor.moveToFirst();
		      try
		      {
		        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
		        pic_title="";
		        p[counter-1]=cursor.getString(1);
		        addpic(pic_title);
				counter++;
		      } 
		      catch (FileNotFoundException e){e.printStackTrace();}
		    }break;
	  }
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    private void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
	  	USERID=a.getUserid(); 
	  	PSW=a.getPsw();
	  	URL=a.getURL();
        STATUS=a.getStatus();
        WODE=a.getWode();
        PK=a.getPK();
	}
    public void usetoast(String result){
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="用户验证出错";
    	if(result.equals("wrong_operation")) tixing="操作错误";
    	if(result.equals("wrong_blogid")) tixing="纪录不存在";
    	if(result.equals("已保存")) tixing="已保存";
    	if(result.equals("最多上传5张图片")) tixing="最多上传5张图片";
    	if(result.equals("用于PK的纪录不能保存到草稿箱")) {tixing="用于PK的纪录不能保存到草稿箱";saveToDrafts.setText("保存到草稿箱");}
    	if(result.equals("标题不能为空")) tixing="标题不能为空";
    	if(result.equals("正文不能为空")) tixing="正文不能为空";
    	if(result.equals("file_size_error")) tixing="文件尺寸错误";
    	if(result.equals("file_type_error")) tixing="文件类型错误";
    	if(result.equals("dir_server_error")) tixing="文件目录出错";
    	if(result.equals("fail_to_upload")) tixing="失败上传";
    	if(result.equals("only_allows_upload_file_types")) tixing="只允许上传图片文件";
    	if(result.equals("ftp_upload_file_size")) tixing="ftp上传出错";
    	if(result.equals("<br />")) tixing="图片过大，上传失败";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}
