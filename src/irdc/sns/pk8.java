//PKҳ��       ���ڣ�2011-7-25  ���ߣ� ����ͮ   
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class pk8 extends Activity
{
	private TextView tvzuozhe;
	private TextView tvbiaoqian;
	private TextView tvbiaoti;
	private TextView tvshijian; 
	private TextView tvzhengwen;
	private TextView tvdaedline;
	private TextView tvzhichi;
	private TextView tvzuozhe2;
	private TextView tvbiaoqian2;
	private TextView tvbiaoti2;
	private TextView tvshijian2; 
	private TextView tvzhengwen2;
	private TextView tvdaedline2;
	private TextView tvzhichi2;
	private ImageView iv1;
	private ImageView iv2;
	private Button b1;
	private Button b2;
	
	String newpl;
	boolean panduan;
	private LinearLayout mLoadLayout;    
	private LinearLayout mLoadLayout2; 
	private final LayoutParams WW = new LinearLayout.LayoutParams
	( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
	
	private Bitmap bitmap;  
	//private ImageView iv0;

    String zuozhe;
    String biaoti;
    String biaoqian;
    String shijian;
    String blogid;
	String blogid2;
	
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
  	//Bitmap BITMAPPK1=null;
  	//Bitmap BITMAPPK2=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pk8);
        
        tvzuozhe=(TextView)findViewById(R.id.zuozhe1);
        tvbiaoti=(TextView)findViewById(R.id.biaoti1);
        tvbiaoqian=(TextView)findViewById(R.id.biaoqian1);
        tvshijian=(TextView)findViewById(R.id.shijian1);
        tvzhengwen=(TextView)findViewById(R.id.zhengwen1);
        tvdaedline=(TextView)findViewById(R.id.deadline1);
        tvzhichi=(TextView)findViewById(R.id.zhichi1);
        tvzuozhe2=(TextView)findViewById(R.id.zuozhe2);
        tvbiaoti2=(TextView)findViewById(R.id.biaoti2);
        tvbiaoqian2=(TextView)findViewById(R.id.biaoqian2);
        tvshijian2=(TextView)findViewById(R.id.shijian2);
        tvzhengwen2=(TextView)findViewById(R.id.zhengwen2);
        tvdaedline2=(TextView)findViewById(R.id.deadline2); 
        tvzhichi2=(TextView)findViewById(R.id.zhichi2);
        iv1=(ImageView)findViewById(R.id.iv1);
        iv2=(ImageView)findViewById(R.id.iv2);
        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        mLoadLayout=(LinearLayout)findViewById(R.id.tupian1);
        mLoadLayout2=(LinearLayout)findViewById(R.id.tupian2);
        
        //ȡ�ô���������Ϣ
        Bundle bundle=this.getIntent().getExtras();
        //String zuozhe=bundle.getString("zuozhe");
        //String biaoti=bundle.getString("biaoti");
        //String biaoqian=bundle.getString("biaoqian");
        //String shijian=bundle.getString("shijian");
        blogid=bundle.getString("blogid");
        //tvzuozhe.setText(zuozhe);
        //tvbiaoti.setText(biaoti);
        //tvbiaoqian.setText(biaoqian);
        //tvshijian.setText(shijian); 
        
        getwhole();
        //if (BITMAPPK1 != null) iv1.setImageBitmap(BITMAPPK1);
        //if (BITMAPPK2 != null) iv2.setImageBitmap(BITMAPPK2);
        //���¼����
        tongxin(blogid);
        
        b1.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          { dafen(v); }
        });
        b2.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          { dafen(v); }
        });
        
		Button b3 = (Button) findViewById(R.id.b3);   //�鿴����
        b3.setOnClickListener(new Button.OnClickListener() 
        {
          public void onClick(View v)
          {
        	  Intent intent = new Intent();
        	  intent.setClass(pk8.this, pinglun5_1.class);
        	  Bundle bundle=new Bundle();
      		  bundle.putString("blogid",blogid); 
      		  intent.putExtras(bundle); 
        	  startActivity(intent);
        	  //finish();
          }
        });
        Button b5 = (Button) findViewById(R.id.b5);   //�鿴����
        b5.setOnClickListener(new Button.OnClickListener() 
        {
          public void onClick(View v)
          {
        	  Intent intent = new Intent();
        	  intent.setClass(pk8.this, pinglun5_1.class);
        	  Bundle bundle=new Bundle();
      		  bundle.putString("blogid",blogid2); 
      		  intent.putExtras(bundle); 
        	  startActivity(intent);
        	  //finish();
          }
        });
        Button b4 = (Button) findViewById(R.id.b4);   //��������
        b4.setOnClickListener(new Button.OnClickListener() 
        {
          public void onClick(View v)
          {  fbpl(blogid); }
        });
        Button b6 = (Button) findViewById(R.id.b6);   //��������
        b6.setOnClickListener(new Button.OnClickListener() 
        {
          public void onClick(View v)
          {  fbpl(blogid2); }
        });
    }
    
    public void tongxin(String blogid)
    {
    	whole a = (whole) getApplicationContext();
        String url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_view&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&blogid="+blogid;
		System.out.println("url = "+url);
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			httpResponse= new DefaultHttpClient().execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200) //�ж���Ӧ��
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println(result);
				usetoast(result);
				if(panduan==true)
				{
					JSONObject jsonObject = new JSONObject(result); 
					JSONArray jsonArray = jsonObject.getJSONArray("blog_view");
					//��һ����¼
					JSONObject jsonObject1 = (JSONObject)jsonArray.opt(0);
					//Spanned text = Html.fromHtml(jsonObject1.getString("message"));  //xml��������
					tvzhengwen.setText(Html.fromHtml(jsonObject1.getString("message"))); 
					tvzuozhe.setText(jsonObject1.getString("username"));
			        tvbiaoti.setText(jsonObject1.getString("subject"));
			        tvbiaoqian.setText(jsonObject1.getString("stagname"));
			        tvshijian.setText(jsonObject1.getString("dateline"));
					tvdaedline.setText(jsonObject1.getString("deadline")); 
					tvzhichi.setText(jsonObject1.getString("click_2")); 
					String avatar=jsonObject1.getString("avatar");  //���ͷ��1
		            if(!avatar.equals("0"))  
		            {
		            	String tx_url=URL+avatar;
		            	iv1.setImageBitmap(a.get_pic(tx_url));
	                }
					
					//���ͼƬ��ͼƬ����
					JSONArray jsonArray_pic=jsonObject1.getJSONArray("pic");				
					for(int i=0;i<jsonArray_pic.length();i++)
					{
						JSONObject jsonObject12=(JSONObject)jsonArray_pic.opt(i);
						String pic_title=Html.fromHtml(jsonObject12.getString("pic_title")).toString();
						String pic_url=URL+"/uc/uchome/"+jsonObject12.getString("pic_url");
						
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
					//�ڶ�����¼
					JSONObject jsonObject2 = (JSONObject)jsonArray.opt(1);		
					tvzuozhe2.setText(jsonObject2.getString("username")); 
					tvbiaoqian2.setText(jsonObject2.getString("stagname")); 
					tvbiaoti2.setText(Html.fromHtml(jsonObject2.getString("subject"))); 
					tvshijian2.setText(jsonObject2.getString("dateline")); 
					tvzhengwen2.setText(Html.fromHtml(jsonObject2.getString("message"))); 
					tvdaedline2.setText(jsonObject2.getString("deadline")); 
					tvzhichi2.setText(jsonObject2.getString("click_2")); 
					blogid2=jsonObject2.getString("blogid");
					String avatar2=jsonObject2.getString("avatar");  //���ͷ��2
		            if(!avatar2.equals("0"))  
		            {
		            	String tx_url=URL+avatar2;
		            	iv2.setImageBitmap(a.get_pic(tx_url));
	                }
					
					//���ͼƬ��ͼƬ����
					JSONArray jsonArray_pic2=jsonObject2.getJSONArray("pic");				
					for(int i=0;i<jsonArray_pic2.length();i++)
					{
						JSONObject jsonObject22=(JSONObject)jsonArray_pic2.opt(i);
						String pic_title=Html.fromHtml(jsonObject22.getString("pic_title")).toString();
						String pic_url=URL+"/uc/uchome/"+jsonObject22.getString("pic_url");
						bitmap=a.get_pic(pic_url);
						
				        if (bitmap != null)
				        {
				        	ImageView iv21= new ImageView(this);
				        	TextView tv21=new TextView(this);
				        	TextView tv22=new TextView(this);
					        iv21.setImageBitmap(bitmap);
					        iv21.setAdjustViewBounds(true);
					        mLoadLayout2.addView(iv21, WW);
					        if(!pic_title.equals(""))
					        {
					        	tv21.setText(pic_title);
						        tv21.setTextColor(Color.BLACK);
						        tv21.setTextSize(16);
						        mLoadLayout2.addView(tv21, WW);
					        }
					        tv22.setText("");
					        tv22.setTextSize(16);
					        mLoadLayout2.addView(tv22, WW);
				        }
					}//�ڶ�����¼����
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
    
    public void fbpl(final String Blogid)   //��������
    {
    	LayoutInflater factory = LayoutInflater.from(this);        
		View textEntryView = factory.inflate(R.layout.fbpl_edit, null);    
		//�ڲ��ֲ��ֻ࣬�ܷ��ʷ�����final���͵ı���        
		final EditText et1 = (EditText) textEntryView                
		.findViewById(R.id.et1);        
		new AlertDialog.Builder(pk8.this)     
		.setTitle("��������")                
		.setView(textEntryView)                
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() 
		{  @Override                            
			public void onClick(DialogInterface d,int which) {d.dismiss(); }                        
		})                
	    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
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
					params.add(new BasicNameValuePair("id",Blogid)); 
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
							usetoast_pl(result);
						}
						else
						{System.out.println("����");}
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

    public void dafen(View v) //��� �����
    {
    	String url = URL+"/uc/uchome/space.php?do=vote_api";
    	HttpResponse httpResponse = null;
		System.out.println("url = "+url);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("api","vote")); 
		params.add(new BasicNameValuePair("userid",USERID)); 
		params.add(new BasicNameValuePair("username",USERNAME)); 
		params.add(new BasicNameValuePair("psw",PSW));  
		params.add(new BasicNameValuePair("idtype","blogid"));   
		params.add(new BasicNameValuePair("op","add"));  
		params.add(new BasicNameValuePair("clickid","2"));  //��
		
		if(v.getId()==R.id.b1) params.add(new BasicNameValuePair("id",blogid));  
		else  params.add(new BasicNameValuePair("id",blogid2));  
		
		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			httpResponse= new DefaultHttpClient().execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == 200)
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println(result); 
				usetoast(result);
				if(result.equals("succeed"))
				{
					Intent intent=new Intent();  //ˢ��
					Bundle bundle=new Bundle();  
					intent.setClass(pk8.this, pk8.class);  
					bundle.putString("blogid",blogid); 
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
			}
			else
			{System.out.println("���󣡣�����");}
		}
		catch(ClientProtocolException e)
		{e.printStackTrace();}
		catch(IOException e)
		{e.printStackTrace();} 
    }
    
    private void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
	  	USERID=a.getUserid(); 
	  	PSW=a.getPsw();
	  	URL=a.getURL();
	  	//BITMAPPK1=a.getpkBitmap1();
	  	//BITMAPPK2=a.getpkBitmap2();
	}
    
    public void usetoast(String result){  //ͶƱ����
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="�û���֤����";
    	if(result.equals("wrong_operation")) tixing="��������";
    	if(result.equals("no_click")) tixing="ͶƱ��������";
    	if(result.equals("click_item_error")) tixing="��¼��ó���";
    	if(result.equals("succeed")) tixing="�ɹ�ͶƱ";
    	if(result.equals("click_no_self")) tixing="���ܶ��Լ�ͶƱ";
    	if(result.equals("click_have")) tixing="�Ѿ�Ͷ��Ʊ��"; 
    	if(result.equals("record_not_exist")) tixing="��¼������"; 
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
    public void usetoast_pl(String result){  //��������
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="�û���֤����";
    	if(result.equals("wrong_operation")) tixing="��������";
    	if(result.equals("succeed")) tixing="���۳ɹ�";
    	if(result.equals("fail")) tixing="����ʧ��";
    	if(result.equals("record_not_exist")) tixing="��¼������"; 
    	if(result.equals("comment_too_short")) tixing="����̫��";
    	if(result.equals("wrong_blogid")) tixing="�ü�¼������";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}