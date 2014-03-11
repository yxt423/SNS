//我的纪录       日期：2011-7-25  作者： 俞欣彤   
package irdc.sns;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class wodejilu7_1 extends Activity implements OnClickListener, OnScrollListener ,
OnItemSelectedListener
{
	private int length=1;    //jsonArray.length();  表示list长度
	private ListView lv1;                                      	
	private ViewAdapter viewAdapter;
	private LinearLayout mLoadLayout; 
  	private LinearLayout mProgressLoadLayout;    
  	private final Handler mHandler = new Handler();// 在Handler中加载数据      
	private final LayoutParams WW = new LinearLayout.LayoutParams
	(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);   
	private int mLastItem = 0;  
	int scrollState;// 全局变量，用来记录ScrollView的滚动状态，1表示开始滚动，2表示正在滚动，0表示滚动停止     
  	int visibleItemCount;// 当前可见页面中的Item总数      
  	ArrayList<HashMap<String, Object>> jilu = new ArrayList<HashMap<String, Object>>();
	private int selectedIndex = -1; 
	boolean panduan;
	
	String USERNAME=null;
  	String USERID=null; 
  	String ID=null; 
  	String PSW=null;
  	String URL=null; 
  	String WODE=null;
  	String pkbiaoqianid="",pkid,blogid;
  	String PK;
	
	private class ViewAdapter extends BaseAdapter
	{
		int count =5;
		public void setCount()
		{if (length<5)  count=length;}
		private Context context; 
		@Override
		public int getCount() {
			return count;
		}
		@Override
		public Object getItem(int position) {
			return jilu.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public ViewAdapter(Context context)
		{
			this.context = context;
			//layoutInflater = (LayoutInflater) context
			//.getSystemService(inflater);
		}
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			HashMap<String, Object> map  = new HashMap<String, Object>();
			map=jilu.get(position);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(inflater);
			LinearLayout linearLayout = null;
			linearLayout = (LinearLayout) layoutInflater.inflate(
					R.layout.record_4_wode_list, null);
			TextView tvzuozhe = (TextView) linearLayout.findViewById(R.id.zuozhe1);
			TextView tvbiaoti = ((TextView) linearLayout.findViewById(R.id.biaoti1));
			TextView tvzhuangtai = (TextView) linearLayout.findViewById(R.id.zhuangtai1);
			TextView tvbiaoqian = (TextView) linearLayout.findViewById(R.id.biaoqian1);
			TextView tvshijian = (TextView) linearLayout.findViewById(R.id.shijian1); 
			ImageView ivtouxiang =(ImageView)linearLayout.findViewById(R.id.iv1);
			if (map.get("bitmap") != null)   ivtouxiang.setImageBitmap((Bitmap) map.get("bitmap") );
			tvbiaoti.setText((CharSequence) map.get("biaoti"));
			int status= (Integer) map.get("zhuangtai");
			switch (status) {
			case 1: tvzhuangtai.setText("已通过"); break;
			case 0: tvzhuangtai.setText("评定中"); break;
			case 2: tvzhuangtai.setText("PK中"); break;
			case -1: tvzhuangtai.setText("未发布"); break;
			default: break; }
			tvzuozhe.setText((CharSequence) map.get("zuozhe"));
			tvbiaoqian.setText((CharSequence) map.get("biaoqian"));
			tvshijian.setText((CharSequence) map.get("shijian")); 
			return linearLayout;
		} 
	}
	public void onClick(View view) 
	{
	}
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id)
	{
		selectedIndex = position;
	}
	public void onNothingSelected(AdapterView<?> parent)
	{
	}
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wodejilu7_1);
        
        /**           * "加载项"布局，此布局被添加到ListView的Footer中。           */          
		mLoadLayout = new LinearLayout(this);          
		mLoadLayout.setMinimumHeight(30);          
		mLoadLayout.setGravity(Gravity.CENTER);          
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);  
		/** 当点击按钮的时候显示这个View，此View使用水平方式布局，左边是一个进度条，右边是文本， * 默认设为不可见     

      */     
		mProgressLoadLayout = new LinearLayout(this);         
		mProgressLoadLayout.setMinimumHeight(30);          
		mProgressLoadLayout.setGravity(Gravity.CENTER);          
		mProgressLoadLayout.setOrientation(LinearLayout.HORIZONTAL);           
		ProgressBar mProgressBar = new ProgressBar(this);          
		mProgressBar.setPadding(0, 0, 15, 0);          
		mProgressLoadLayout.addView(mProgressBar, WW);// 为布局添加进度条           
		TextView mTipContent = new TextView(this);         
		mTipContent.setText("加载中...");          
		mProgressLoadLayout.addView(mTipContent, WW);// 为布局添加文本         
		mProgressLoadLayout.setVisibility(View.GONE);// 默认设为不可见，注意View.GONE和View.INVISIBLE的区别
		mLoadLayout.addView(mProgressLoadLayout);// 把之前的布局以View对象添加进来          
		final Button bgd = new Button(this);          
		bgd.setText("加载更多");          // 添加按钮         
		mLoadLayout.addView(bgd, WW);  
		bgd.setOnClickListener(bgd_listener);
        
		getwhole();
		System.out.println("1");
        if(PK.equals("1"))  //pk时获得标签id  作为url的一部分
        {
        	Bundle bundle=this.getIntent().getExtras();
        	pkbiaoqianid="&stagid="+bundle.getString("pkbiaoqianid");
        	pkid=bundle.getString("pkid");
        	WODE="no_pk";
        }
		
        tongxin();
      //创建列表
        lv1= (ListView) findViewById(R.id.lv1);
        lv1.addFooterView(mLoadLayout);    //增加列表最后一行！！

		viewAdapter = new ViewAdapter(this);
		lv1.setAdapter(viewAdapter);
		lv1.setOnItemSelectedListener(this); 
		lv1.setOnItemClickListener(lv1_listener); 
		viewAdapter.setCount();
		lv1.setOnItemSelectedListener(this);  
		lv1.setOnScrollListener(this);
    }
    
    public void tongxin()
    {
    	whole a = (whole) getApplicationContext();
    	String url = URL+"/uc/uchome/space.php?do=index_blog_api&api=index_blog&status="+WODE+"&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&uid="+ID+pkbiaoqianid;
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
				//usetoast(result);
				//if(panduan==true)
				//{
				JSONObject jsonObject = new JSONObject(result); 
				JSONArray jsonArray = jsonObject.getJSONArray("blog_list");
				length=jsonArray.length();
				System.out.println(jsonArray);
				for(int i=0;i<jsonArray.length();i++){   //数组长度 jsonArray.length()
		            JSONObject jsonObject1 = (JSONObject)jsonArray.opt(i);  //数组中的第i个取为jsonObject1
		            JSONObject jsonObject2 =jsonObject1.getJSONObject("blog_itme");
		            HashMap<String, Object> map  = new HashMap<String, Object>();
		            map.put("biaoti",  Html.fromHtml(jsonObject2.getString("subject")).toString());
		            map.put("shijian", jsonObject2.getString("dateline"));
		            map.put("biaoqian", jsonObject2.getString("stagname"));
		            map.put("zuozhe", jsonObject2.getString("username"));
		            map.put("zhuangtai", jsonObject2.getInt("status"));
		            map.put("blogid", jsonObject2.getString("blogid")); 
		            if(jsonObject2.getInt("status")==0)
		            	map.put("score", jsonObject2.getInt("score"));	
		            String avatar=jsonObject2.getString("avatar"); 
		            String url2=null;
		            if(!avatar.equals("0"))  
		            {
		            	url2=URL+avatar;
		            	map.put("bitmap", a.get_pic(url2));
                    }
		            if(jsonObject2.getInt("status")==2)  //PK状态下的第二条纪录
		            {
		            	 JSONObject jsonObject3 =jsonObject2.getJSONObject("pk_info");
			             avatar=jsonObject3.getString("avatar"); 
			             if(!avatar.equals("0"))  
				            {
				            	url2=URL+avatar;
				            	map.put("bitmap2", a.get_pic(url2));
		                    }
		            }
		            jilu.add(map);
				}
				//}
				
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
    //点击查看我的纪录列表每一项的正文
    private OnItemClickListener lv1_listener = new OnItemClickListener()
	{
    	  @Override
    	  public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) 
    	  {
    		  if(PK.equals("1"))
    		  {
    			  new AlertDialog.Builder(wodejilu7_1.this)
		          .setTitle("你选择此记录：")
		          .setItems(R.array.tzjilu,
		          new DialogInterface.OnClickListener()
		          {
		            public void onClick(DialogInterface dialog, int whichcountry)
		            {
		            	HashMap<String, Object> map  = new HashMap<String, Object>();
		        		map=jilu.get(arg2);
	            		blogid=(String) map.get("blogid");
		            	if(whichcountry==0)
		            	{
		            		querentz(blogid);  //确认挑战
		        	    }
		            	else
		            	{
		            		whole a = (whole) getApplicationContext();
		            		//a.setBitmap((Bitmap) map.get("bitmap")); 
		            		Intent intent = new Intent();  //查看正文
		        			Bundle bundle=new Bundle();
		        			//bundle.putString("zuozhe", (String) map.get("zuozhe"));  //通过arg2判断单击的是列表中的第几条
		            		//bundle.putString("biaoti", (String) map.get("biaoti")); 
		            		//bundle.putString("shijian", (String) map.get("shijian"));
		            		//bundle.putString("biaoqian", (String) map.get("biaoqian"));
		            		bundle.putString("blogid", (String) map.get("blogid"));
		            		bundle.putString("pkid", pkid);  //要pk的纪录的id
		            		intent.putExtras(bundle);
		        	        intent.setClass(wodejilu7_1.this, tzjilu.class);
		        	        startActivity(intent);
		        	        //finish();
		            	}
		            }
		          })
		          .setNegativeButton("取消", new DialogInterface.OnClickListener()
		          { @Override 
		            public void onClick(DialogInterface d, int which)
		            {d.dismiss(); } 
		          })
		          .show();
    		  }
    		  else
    		  {
    			    //传递已取得的信息 
    			    HashMap<String, Object> map  = new HashMap<String, Object>();
	        		map=jilu.get(arg2);
    			    whole a = (whole) getApplicationContext();
    		        //a.setBitmap((Bitmap) map.get("bitmap")); 
    		        int status=(Integer) map.get("zhuangtai");
    		 
	    		    Intent intent=new Intent();
	    			Bundle bundle=new Bundle();
	    			switch (status) {
	    			case 1:
	    				intent.setClass(wodejilu7_1.this,tongguo6.class); 
	    				break;
	    			case 0:
	    				intent.setClass(wodejilu7_1.this,pingding5.class); 
	    				//bundle.putInt("score",(Integer) map.get("score"));
	    				break;
	    			case 2:
	    				//a.setpkBitmap((Bitmap)map.get("bitmap"), (Bitmap)map.get("bitmap2"));
	    				intent.setClass(wodejilu7_1.this,pk8.class); 
	    				break;
	    			case -1:
	    				a.setWode("draft");
	    				intent.setClass(wodejilu7_1.this,fbjilu7.class); 
	    				break;
	    			default:
	    				break;
	    			}					
	        		//bundle.putString("zuozhe", (String) map.get("zuozhe"));  //通过arg2判断单击的是列表中的第几条
	        		//bundle.putString("biaoti", (String) map.get("biaoti")); 
	        		//bundle.putString("shijian", (String) map.get("shijian"));
	        		//bundle.putString("biaoqian", (String) map.get("biaoqian"));
	        		bundle.putString("blogid", (String) map.get("blogid"));
	        		intent.putExtras(bundle);
	        		startActivity(intent);
	        		//finish();
    		  }
    	  }
    };
    
    private void querentz(String blogid)  //确认挑战
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
					intent.setClass(wodejilu7_1.this,pk8.class );
					Bundle bundle=new Bundle();
					bundle.putString("blogid", result);
					intent.putExtras(bundle);
					wodejilu7_1.this.startActivity(intent);
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
    
    Button.OnClickListener bgd_listener = new Button.OnClickListener()  
    { 
		public void onClick(final View v) 
		{ 
			if (mLastItem == viewAdapter.count                          
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) 
			{                      // 当点击时把带进度条的Layout设为可见，把Button设为不可见               
				mProgressLoadLayout.setVisibility(View.VISIBLE);              
				v.setVisibility(View.GONE);                   
				if (viewAdapter.count <= length) 
				{                          
					mHandler.postDelayed(new Runnable() 
					{                              
						@Override                  
						public void run() 
						{               
							if(viewAdapter.count+5>length)  //点击“更多”后最多添加到length的个数
								viewAdapter.count=length;
							else
								viewAdapter.count += 5;
							viewAdapter.notifyDataSetChanged();                 
							lv1.setSelection(mLastItem                  
									- visibleItemCount + 1);                 
							// 获取数据成功时把Layout设为不可见，把Button设为可见                 

         
							mProgressLoadLayout.setVisibility(View.GONE);       
							v.setVisibility(View.VISIBLE);                
							}                         
						}, 0);                  
					}                 
				}
		}  
    };
    
    private void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
	  	USERID=a.getUserid(); 
	  	ID=a.getOtherid(); 
	  	PSW=a.getPsw();
	  	URL=a.getURL();
	  	WODE=a.getWode();
	  	PK=a.getPK();
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;      
		mLastItem = firstVisibleItem + visibleItemCount - 1;   
		if (viewAdapter.count >= length)  //当已有的列表条目多于length的个数，将不再显示“更多”按钮
		{             
			lv1.removeFooterView(mLoadLayout);        
		} 
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}
	public void usetoast(String result){  //评论提醒
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="用户验证出错";
    	if(result.equals("wrong_operation")) tixing="操作错误"; 
    	if(result.equals("wrong_status")) tixing="状态错误";
    	if(result.equals("wrong_blogid")) tixing="该纪录不存在";
    	if(result.equals("wrong_present")) tixing="查看对象错误 ";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}