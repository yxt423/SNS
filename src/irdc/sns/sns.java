/**
 * 第一页    登陆或注册     
 * 日期：2011-7-25  
 * 作者： 俞欣彤   武逸超
 */
package irdc.sns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sns extends Activity {
	private Button loginButton;
	//private Button registerButton;
	private EditText et1;
	private EditText et2;
	boolean panduan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //registerButton=(Button)findViewById(R.id.main_registerButton);
        loginButton=(Button)findViewById(R.id.main_loginButton);
        //registerButton.setOnClickListener(new RegisterButtonListener());
        loginButton.setOnClickListener(loginButton_listener);
        et1= (EditText)findViewById(R.id.main_userName_editText);
        et2= (EditText)findViewById(R.id.main_passWord_editText);
    }
    /*
    //注册按钮
    class RegisterButtonListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent intent=new Intent();
			intent.setClass(sns.this, RegisterActivity_2.class);
			sns.this.startActivity(intent);
		}
    } */
    //登陆按钮
    public Button.OnClickListener loginButton_listener = new Button.OnClickListener()
    {
    		@Override
    		public void onClick(View v) {
    			//实现通信
    			/*
    			whole a = (whole) getApplicationContext();
    	        String URL=a.getURL();
    	        String url = URL+ "/uc/uchome/space.php?do=login_api";
				HttpResponse httpResponse = null;
				System.out.println("url = "+url);
				HttpPost httpPost = new HttpPost(url);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username",et1.getText().toString()));
				params.add(new BasicNameValuePair("psw",et2.getText().toString()));
				params.add(new BasicNameValuePair("api","login"));
				try
				{
					httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
					httpResponse= new DefaultHttpClient().execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200)
					{
						//用getEntity方法获得结果
						String result = EntityUtils.toString(httpResponse.getEntity());
						System.out.println(result);
						usetoast(result);
						if(panduan==true)
						{ 
							a.setUserid(result);
							a.setUsername(et1.getText().toString());
							a.setPsw(et2.getText().toString());
							Intent intent=new Intent();
							intent.setClass(sns.this, FirstPage_3.class);
							sns.this.startActivity(intent);
							finish();
						} 
					}
					else
					{System.out.println("错误！！！！");}
				}
				catch(ClientProtocolException e)
				{e.printStackTrace();}
				catch(IOException e)
				{e.printStackTrace();}
				*/
    			
    			//不通信
    			Intent intent=new Intent();
				intent.setClass(sns.this, FirstPage_3.class);
				sns.this.startActivity(intent);
				sns.this.finish();
				
    		}
    };
    public void usetoast(String result){  //评论提醒
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="用户验证出错";
    	if(result.equals("wrong_operation")) tixing="操作错误";
    	if(result.equals("no_user")) tixing="无此用户"; 
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}