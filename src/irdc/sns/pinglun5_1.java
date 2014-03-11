//评论页面      日期：2011-7-25  作者： 俞欣彤   
package irdc.sns;

import java.io.IOException;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class pinglun5_1 extends Activity implements OnClickListener,OnScrollListener ,
OnItemSelectedListener
{
	private int length=1;    //jsonArray.length();  表示list长度
	private ListView lv1;                                      	
	private ViewAdapter viewAdapter;
	private int selectedIndex = -1;
	private LinearLayout mLoadLayout; 
  	private LinearLayout mProgressLoadLayout;    
  	private final Handler mHandler = new Handler();// 在Handler中加载数据      
	private final LayoutParams WW = new LinearLayout.LayoutParams
	(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);   
	private int mLastItem = 0;  
	int scrollState;// 全局变量，用来记录ScrollView的滚动状态，1表示开始滚动，2表示正在滚动，0表示滚动停止     
  	int visibleItemCount;// 当前可见页面中的Item总数      
  	ArrayList<HashMap<String, Object>> pinglun = new ArrayList<HashMap<String, Object>>();
  	
	String newpl;
	String blogid=null;
	boolean panduan;
	
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
	
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
			return pinglun.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public ViewAdapter(Context context)
		{
			this.context = context;
		}
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			HashMap<String, Object> map  = new HashMap<String, Object>();
			map=pinglun.get(position);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(inflater);
			LinearLayout linearLayout = null;
			linearLayout = (LinearLayout) layoutInflater.inflate(
					R.layout.pinglun_list, null);
			TextView tvzhengwen = ((TextView) linearLayout
					.findViewById(R.id.zhengwen1));
			TextView tvzuozhe = (TextView) linearLayout
					.findViewById(R.id.zuozhe1);
			TextView tvshijian = (TextView) linearLayout
			        .findViewById(R.id.shijian1);
			ImageView ivtouxiang =(ImageView)linearLayout
                    .findViewById(R.id.iv1);
			if (map.get("bitmap") != null)   ivtouxiang.setImageBitmap((Bitmap) map.get("bitmap") );
			tvzhengwen.setText((CharSequence) map.get("zhengwen"));
			tvzuozhe.setText((CharSequence) map.get("zuozhe")); 
			tvshijian.setText((CharSequence) map.get("shijian"));
			return linearLayout;
		}
	}
	public void onClick(View view)  //“显示更多”  点击一下添加三条记录内容
	{
	}
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id)
	{
		selectedIndex = position;
	}
	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO Auto-generated method stub
	}
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinglun5_1);
        
        Bundle bundle=this.getIntent().getExtras();
        blogid=bundle.getString("blogid");
         
        /**           * "加载项"布局，此布局被添加到ListView的Footer中。           */          
		mLoadLayout = new LinearLayout(this);          
		mLoadLayout.setMinimumHeight(30);          
		mLoadLayout.setGravity(Gravity.CENTER);          
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);  
		/** 当点击按钮的时候显示这个View，此View使用水平方式布局，左边是一个进度条，右边是文本， * 默认设为不可见           */     
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
        tongxin( blogid);
        
      //创建列表
        lv1= (ListView) findViewById(R.id.lv1);
        lv1.addFooterView(mLoadLayout);    //增加列表最后一行！！

		viewAdapter = new ViewAdapter(this);
		lv1.setAdapter(viewAdapter);
		lv1.setOnItemSelectedListener(this); 
		viewAdapter.setCount();
		lv1.setOnItemSelectedListener(this);  
		lv1.setOnScrollListener(this);   
		lv1.setOnItemClickListener(lv1_listener); 
		
		Button b1 = (Button) findViewById(R.id.b1);  
		b1.setOnClickListener(b1_listener);
    }
    
    public void tongxin(String blogid)  //获取评论内容
    {
    	whole a = (whole) getApplicationContext();
        String url = URL+ "/uc/uchome/space.php?do=blog_api&api=comment_list&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&blogid="+blogid;
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
					JSONArray jsonArray = jsonObject.getJSONArray("comment_list"); 
					length=jsonArray.length();
					for(int i=0;i<jsonArray.length();i++){    
			            JSONObject jsonObject1 = (JSONObject)jsonArray.opt(i);  //数组中的第i个取为jsonObject1
			            HashMap<String, Object> map  = new HashMap<String, Object>();
			            map.put("zhengwen", Html.fromHtml(jsonObject1.getString("message")).toString());
			            map.put("shijian", jsonObject1.getString("dateline"));
			            map.put("zuozhe", jsonObject1.getString("author"));
			            map.put("authorid", jsonObject1.getString("authorid"));
			            System.out.println(jsonObject1.getString("authorid"));
			            String avatar=jsonObject1.getString("avatar");
			            //获取头像
			            avatar=jsonObject1.getString("avatar");
			            String url2=null;
			            if(!avatar.equals("0"))  
			            {
			            	url2=URL+avatar;
			            	map.put("bitmap", a.get_pic(url2));
	                    }
			            pinglun.add(map);
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
    
    public Button.OnClickListener b1_listener = new Button.OnClickListener()
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
 		new AlertDialog.Builder(pinglun5_1.this)     
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
					
					Intent intent=new Intent();  //刷新
					Bundle bundle=new Bundle(); 
					bundle.putString("blogid",blogid); 
		      		intent.putExtras(bundle); 
					intent.setClass(pinglun5_1.this, pinglun5_1.class);
					startActivity(intent);
					finish();
				}      
 			}                        
 		})
 		.show();
     }
     public void usetoast(){
 		Toast textToast=Toast.makeText(this, "评论成功", Toast.LENGTH_LONG);
 	    textToast.show();
 	}
     
     private void getwhole()
 	{
 		whole a = (whole) getApplicationContext();
 		USERNAME=a.getUsername();
 	  	USERID=a.getUserid(); 
 	  	PSW=a.getPsw();
 	  	URL=a.getURL();
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
	
	private OnItemClickListener lv1_listener = new OnItemClickListener(){ 
	  	  @Override
	  	  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	  	  {
	  		  //传递已取得的信息
	  		  whole a = (whole) getApplicationContext();
	  		  HashMap<String, Object> map  = new HashMap<String, Object>();
			  map=pinglun.get(arg2);
	  		  a.setOtherid((String) map.get("authorid")); 
			  Intent intent=new Intent();
	  		  intent.setClass(pinglun5_1.this,grzhuye1.class); 
	  		  startActivity(intent);
	  		  finish();
	  	  }
	  };
	  public void usetoast(String result){  //评论提醒
	    	panduan=false;
	    	String tixing = "";
	    	if(result.equals("wrong_auth")) tixing="用户验证出错";
	    	if(result.equals("wrong_operation")) tixing="操作错误";
	    	if(result.equals("succeed")) tixing="评论成功";
	    	if(result.equals("fail")) tixing="评论失败";
	    	if(result.equals("record_not_exist")) tixing="纪录不存在"; 
	    	if(result.equals("comment_too_short")) tixing="评论太短";
	    	if(!tixing.equals(""))
	    	{
	    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
	    	    textToast.show();
	    	}
	    	else  panduan=true;
		}
}