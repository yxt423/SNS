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

public class tongguo6 extends Activity {
    
	public Button b3; 
	private TextView tvzhengwen;
	private TextView tvzuozhe,tvbiaoti,tvbiaoqian,tvshijian;
	private String blogid;
	String biaoqian=null;
	String biaoqianid=null;
	String zuozhe;
	private String newpl;
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
        setContentView(R.layout.tongguo6);       
        tvzuozhe=(TextView)findViewById(R.id.zuozhe1);
        tvbiaoti=(TextView)findViewById(R.id.biaoti1);
        tvbiaoqian=(TextView)findViewById(R.id.biaoqian1);
        tvshijian=(TextView)findViewById(R.id.shijian1);
        tvzhengwen=(TextView)findViewById(R.id.zhengwen1);
        iv0=(ImageView)findViewById(R.id.iv0);
        mLoadLayout=(LinearLayout)findViewById(R.id.tupian);
        //取得传递来的信息
        Bundle bundle=this.getIntent().getExtras();
        blogid=bundle.getString("blogid"); 
        
        getwhole();
        //if (BITMAPTX != null)  iv0.setImageBitmap(BITMAPTX);
        tongxin();
        
        Button b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(new Button.OnClickListener() //查看评论
        {
          public void onClick(View v)
          {
              Intent intent = new Intent();
        	  intent.setClass(tongguo6.this, pinglun5_1.class);
        	  Bundle bundle=new Bundle();
      		  bundle.putString("blogid",blogid); 
      		  intent.putExtras(bundle); 
        	  startActivity(intent);
        	  //finish();
          }
        });
        Button b2 = (Button) findViewById(R.id.b2);      //发表评论
        b2.setOnClickListener(b2_listener); 
        b3 = (Button) findViewById(R.id.b3);   //发起挑战
        b3.setOnClickListener(b3_listener); 
        
        Button b4 = (Button) findViewById(R.id.b4);      //返回
        b4.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v) 
          {finish(); }
        });
    }
    
    Button.OnClickListener b2_listener = new Button.OnClickListener()
    { 
		public void onClick(View v) {
			fbpl();  }  //发表评论
    };
    
    public void fbpl()   //发表评论
    { 
    	LayoutInflater factory = LayoutInflater.from(this);        
		View textEntryView = factory.inflate(R.layout.fbpl_edit, null);    
		//内部局部类，只能访问方法的final类型的变量        
		final EditText et1 = (EditText) textEntryView                
		.findViewById(R.id.et1);        
		new AlertDialog.Builder(tongguo6.this)     
		.setTitle("发表评论")                
		.setView(textEntryView)                
		.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{  @Override                            
			public void onClick(DialogInterface d,int which) {d.dismiss(); }                        
		})                
	    .setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{                            
			@Override                            
			public void onClick(DialogInterface dialog,int which) 
			{                           
				if (!et1.getText().toString().equals("")) 
				{
					newpl = et1.getText().toString();   
					String url = URL+"/uc/uchome/space.php?do=comment_api";
				  	HttpResponse httpResponse = null;
					System.out.println("url = "+url);
					HttpPost httpPost = new HttpPost(url);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("api","comment_publish")); 
					params.add(new BasicNameValuePair("userid",USERID)); 
					params.add(new BasicNameValuePair("username",USERNAME)); 
					params.add(new BasicNameValuePair("psw",PSW)); 
					params.add(new BasicNameValuePair("id",blogid)); 
					params.add(new BasicNameValuePair("idtype","blogid")); 
					params.add(new BasicNameValuePair("message",newpl));  
					try
					{
						httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
						httpResponse= new DefaultHttpClient().execute(httpPost);
						if(httpResponse.getStatusLine().getStatusCode() == 200)
						{
							String result = EntityUtils.toString(httpResponse.getEntity());
							System.out.println(result); 
							usetoast(result);
						}
						else
						{System.out.println("错误！");}
					}
					catch(ClientProtocolException e)
					{e.printStackTrace();}
					catch(IOException e)
					{e.printStackTrace();}
				}
				
			}                        
		})
		.show();
    }
    
    Button.OnClickListener b3_listener = new Button.OnClickListener()    //发起挑战
    {
      public void onClick(View arg0)
      {
    	  if(zuozhe.equals(USERNAME))
    	  {usetoast("不能挑战自己");}
    	  else 
    	  {
    		  new AlertDialog.Builder(tongguo6.this)
              .setTitle("请选择挑战方式")
              .setItems(R.array.tzfs,
              new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface dialog, int whichcountry)
                {
                	whole a = (whole) getApplicationContext();
                	a.setPK("1");
                	Intent intent = new Intent();
                	Bundle bundle=new Bundle();
          		    bundle.putString("pkid",blogid); 
          		    bundle.putString("pkbiaoqian",biaoqian); 
          		    bundle.putString("pkbiaoqianid",biaoqianid); 
          		    intent.putExtras(bundle); 
                	if(whichcountry==0)
                	{
                		a.setWode("all");
            	        intent.setClass(tongguo6.this, fbjilu7.class);
            	    }
                	else
                	{
                		a.setWode("draft");
                		a.setOtherid(a.getUserid());
            	        intent.setClass(tongguo6.this, wodejilu7_1.class);
                	}
                	startActivity(intent);
        	        finish();
                }
              })
              .setNegativeButton("取消", new DialogInterface.OnClickListener()
              { @Override 
                public void onClick(DialogInterface d, int which)
                { d.dismiss(); } 
              })
              .show();
		  }
      } /*End: public void onClick(View arg0)*/
    };
    
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
					zuozhe=jsonObject1.getString("username");
					tvzuozhe.setText(jsonObject1.getString("username"));
			        tvbiaoti.setText(jsonObject1.getString("subject"));
			        biaoqian=jsonObject1.getString("stagname");
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
    
    public void usetoast(String result){  //评论提醒
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="用户验证出错";
    	if(result.equals("wrong_operation")) tixing="操作错误";
    	if(result.equals("succeed")) tixing="评论成功";
    	if(result.equals("fail")) tixing="评论失败";
    	if(result.equals("wrong_blogid")) tixing="该纪录不存在";
    	if(result.equals("record_not_exist")) tixing="纪录不存在"; 
    	if(result.equals("comment_too_short")) tixing="评论太短";
    	if(result.equals("不能挑战自己")) tixing="不能挑战自己";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}
