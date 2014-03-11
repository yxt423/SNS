//�����еļ�¼   ��ϸ��Ϣ       ���ڣ�2011-7-25  ���ߣ� ����ͮ   
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class pingding5 extends Activity { 
	private TextView tvzhengwen;
	private TextView tvhaoscore;
	private TextView tvchascore; 
	private TextView tvscore; 
	private TextView tvdeadline,tvzuozhe,tvbiaoti,tvbiaoqian,tvshijian;
	private LinearLayout mLoadLayout;    
	private final LayoutParams WW = new LinearLayout.LayoutParams
	( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
	private Bitmap bitmap;
	private ImageView iv0;
	String zuozhe;
    String biaoti;
    String biaoqian;
    String shijian;
	String blogid;
	int score;
	String newpl;
	ProgressBar pb1;
	boolean panduan;
	
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null;  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingding5); 
        tvzuozhe=(TextView)findViewById(R.id.zuozhe1);
        tvbiaoti=(TextView)findViewById(R.id.biaoti1);
        tvbiaoqian=(TextView)findViewById(R.id.biaoqian1);
        tvshijian=(TextView)findViewById(R.id.shijian1);
        tvscore=(TextView)findViewById(R.id.score1);
        tvzhengwen=(TextView)findViewById(R.id.zhengwen1);
        tvhaoscore=(TextView)findViewById(R.id.haoscore1);
        tvchascore=(TextView)findViewById(R.id.chascore1);
        tvdeadline=(TextView)findViewById(R.id.deadline1);
        pb1= (ProgressBar) findViewById(R.id.pb1); 
        iv0=(ImageView)findViewById(R.id.iv0);
        mLoadLayout=(LinearLayout)findViewById(R.id.tupian);
        //ȡ�ô���������Ϣ
        Bundle bundle=this.getIntent().getExtras();
        //zuozhe=bundle.getString("zuozhe");
        //biaoti=bundle.getString("biaoti");
        //biaoqian=bundle.getString("biaoqian");
        //shijian=bundle.getString("shijian");
        blogid=bundle.getString("blogid"); 
        //tvzuozhe.setText(zuozhe);
        //tvbiaoti.setText(biaoti);
       // tvbiaoqian.setText(biaoqian);
        //tvshijian.setText(shijian);

        getwhole(); 
        //if (BITMAPTX != null)
        //    iv0.setImageBitmap(BITMAPTX);
        tongxin(blogid);
        System.out.println("1");
        Button b1 = (Button) findViewById(R.id.b1);  //����
        b1.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          { dafen(v); }
        });
        Button b2 = (Button) findViewById(R.id.b2);  //�ȣ�
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
        	  intent.setClass(pingding5.this, pinglun5_1.class);
        	  Bundle bundle=new Bundle();
      		  bundle.putString("blogid",blogid); 
      		  intent.putExtras(bundle); 
        	  startActivity(intent);
        	  //finish();
          }
        });
        Button b4 = (Button) findViewById(R.id.b4);     //��������
        b4.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          {  fbpl();  }
        });
        Button b5 = (Button) findViewById(R.id.b5);     //����
        b5.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          { finish(); }
        });
    }
    
    public void tongxin(String blogid)
    { 
    	whole a = (whole) getApplicationContext();
        String url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_view&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&blogid="+blogid;
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			httpResponse= new DefaultHttpClient().execute(httpGet);
			//�ж���Ӧ��
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
					tvhaoscore.setText(jsonObject1.getString("click_4"));
					tvchascore.setText(jsonObject1.getString("click_5"));
					tvdeadline.setText(jsonObject1.getString("deadline"));
					score=jsonObject1.getInt("score");
					pb1.setProgress(score);  
					tvscore.setText(""+score);
					String avatar=jsonObject1.getString("avatar");  //���ͷ��
		            if(!avatar.equals("0"))  
		            {
		            	String tx_url=URL+avatar;
		            	iv0.setImageBitmap(a.get_pic(tx_url));
	                }
					//���ͼƬ��ͼƬ����
					JSONArray jsonArray_pic=jsonObject1.getJSONArray("pic");
					for(int i=0;i<jsonArray_pic.length();i++)
					{
						JSONObject jsonObject2=(JSONObject)jsonArray_pic.opt(i);
						String pic_title=Html.fromHtml(jsonObject2.getString("pic_title")).toString();
						String pic_url=URL+"/uc/uchome/"+jsonObject2.getString("pic_url");
						bitmap=a.get_pic(pic_url);
				        if (bitmap != null)
				        {
				        	System.out.println(bitmap);
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
    
    public void fbpl()   //��������
    {
    	LayoutInflater factory = LayoutInflater.from(this);        
		View textEntryView = factory.inflate(R.layout.fbpl_edit, null);    
		//�ڲ��ֲ��ֻ࣬�ܷ��ʷ�����final���͵ı���        
		final EditText et1 = (EditText) textEntryView                
		.findViewById(R.id.et1);        
		new AlertDialog.Builder(pingding5.this)     
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
		params.add(new BasicNameValuePair("id",blogid));  
		params.add(new BasicNameValuePair("op","add"));  
		if(v.getId()==R.id.b1)  params.add(new BasicNameValuePair("clickid","4"));  //��
		else  params.add(new BasicNameValuePair("clickid","5"));  //��
		
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
					intent.setClass(pingding5.this,pingding5.class);  
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
    
    public void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
	  	USERID=a.getUserid(); 
	  	PSW=a.getPsw();
	  	URL=a.getURL(); 
	  	//BITMAPTX=a.getBitmap();
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