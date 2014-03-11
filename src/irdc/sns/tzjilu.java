//已通过的纪录详细信息    日期：2011-7-25  作者： 俞欣彤   
package irdc.sns;

import java.io.IOException;
import java.util.ArrayList;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class tzjilu extends Activity {
    
	public Button b3; 
	private TextView tvzhengwen;
	private TextView tvzuozhe,tvbiaoti,tvbiaoqian,tvshijian;
	private String blogid;  //选中用于pk的纪录id
	private String pkid;  //要pk的纪录id
	String biaoqian=null;
	String biaoqianid=null;
	String zuozhe;
	boolean panduan;
	private LinearLayout mLoadLayout;    
	private final LayoutParams WW = new LinearLayout.LayoutParams
	( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
	
	private Bitmap bitmap;  
	private ImageView iv0;
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null;  
  	//Bitmap BITMAPTX;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tzjilu);       
        tvzuozhe=(TextView)findViewById(R.id.zuozhe1);
        tvbiaoti=(TextView)findViewById(R.id.biaoti1);
        tvbiaoqian=(TextView)findViewById(R.id.biaoqian1);
        tvshijian=(TextView)findViewById(R.id.shijian1);
        tvzhengwen=(TextView)findViewById(R.id.zhengwen1);
        iv0=(ImageView)findViewById(R.id.iv0);
        mLoadLayout=(LinearLayout)findViewById(R.id.tupian);
        //取得传递来的信息
        Bundle bundle=this.getIntent().getExtras();
        //zuozhe=bundle.getString("zuozhe");
        //String biaoti=bundle.getString("biaoti");
        //biaoqian=bundle.getString("biaoqian");
        //String shijian=bundle.getString("shijian"); 
        blogid=bundle.getString("blogid"); 
        pkid=bundle.getString("pkid"); 
        //tvzuozhe.setText(zuozhe);
        //tvbiaoti.setText(biaoti);
        //tvbiaoqian.setText(biaoqian);
        //tvshijian.setText(shijian); 
        
        getwhole();
        //if (BITMAPTX != null)  iv0.setImageBitmap(BITMAPTX);
        tongxin();
        
        Button b1 = (Button) findViewById(R.id.b1);      //确认挑战
        b1.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v) 
          {querentz(); }
        });
    }
    
    private void querentz()  //确认挑战
    {
        String url = URL+ "/uc/uchome/space.php?do=record_publish_api";
		HttpResponse httpResponse = null;
		System.out.println("url = "+url);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username",USERNAME));
		params.add(new BasicNameValuePair("userid",USERID));
		params.add(new BasicNameValuePair("psw",PSW));
		params.add(new BasicNameValuePair("api","select_pk"));
		params.add(new BasicNameValuePair("pkid",pkid));
		params.add(new BasicNameValuePair("blogid",blogid));
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
					Intent intent=new Intent();
					intent.setClass(tzjilu.this,pk8.class );
					Bundle bundle=new Bundle();
					bundle.putString("blogid", result);
					intent.putExtras(bundle);
					tzjilu.this.startActivity(intent);
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
    }
    
    public void tongxin()
    {
    	whole a = (whole) getApplicationContext();
        String url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_view&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&blogid="+blogid;  
		System.out.println("url = "+url);
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			httpResponse= new DefaultHttpClient().execute(httpGet);
			//判断响应码
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
					tvzhengwen.setText(Html.fromHtml(jsonObject1.getString("message"))); 
					tvzuozhe.setText(jsonObject1.getString("username"));
			        tvbiaoti.setText(jsonObject1.getString("subject"));
			        tvbiaoqian.setText(jsonObject1.getString("stagname"));
			        tvshijian.setText(jsonObject1.getString("dateline"));
					biaoqianid=jsonObject1.getString("stagid");
					String avatar=jsonObject1.getString("avatar");  //获得头像
		            if(!avatar.equals("0"))  
		            {
		            	String tx_url=URL+avatar;
		            	iv0.setImageBitmap(a.get_pic(tx_url));
	                }
					//获得图片及图片描述
					JSONArray jsonArray_pic=jsonObject1.getJSONArray("pic");				
					for(int i=0;i<jsonArray_pic.length();i++)
					{
						JSONObject jsonObject2=(JSONObject)jsonArray_pic.opt(i);
						String pic_title=Html.fromHtml(jsonObject2.getString("pic_title")).toString();
						String pic_url=URL+"/uc/uchome/"+jsonObject2.getString("pic_url");
						bitmap=a.get_pic(pic_url);
				        if (bitmap != null)
				        {
				        	ImageView iv1= new ImageView(this);
				        	TextView tv1=new TextView(this);
				        	TextView tv2=new TextView(this);
					        iv1.setImageBitmap(bitmap);
					        iv1.setAdjustViewBounds(true);
					        mLoadLayout.addView(iv1, WW);
					        if(!pic_title.equals(""))
					        {
					        	tv1.setText(pic_title);
						        tv1.setTextColor(Color.BLACK);
						        tv1.setTextSize(16);
						        mLoadLayout.addView(tv1, WW);
					        }
					        tv2.setText("");
					        tv2.setTextSize(16);
					        mLoadLayout.addView(tv2, WW);
				        }
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
    
    public void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
	  	USERID=a.getUserid(); 
	  	PSW=a.getPsw();
	  	URL=a.getURL(); 
	  	//BITMAPTX=a.getBitmap();
	}
    
    public void usetoast(String result){  
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="用户验证出错";
    	if(result.equals("wrong_operation")) tixing="操作错误";
    	if(result.equals("fail")) tixing="操作失败";
    	if(result.equals("wrong_blogid")) tixing="该纪录不存在";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}
