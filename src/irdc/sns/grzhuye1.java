//个人主页  8月3日   俞欣彤
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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
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
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class grzhuye1 extends Activity implements OnScrollListener ,OnClickListener,
OnItemSelectedListener
{
	private TextView tvyonghuming;
	private TextView tvjifen;
	private TextView tvjingyan; 
	private TextView tvxingbie;
	private TextView tvshengri;
	private TextView tvjuzhudi;
	private TextView tvhunlian;
	private ImageView iv1;
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
  	private int length=1;    //jsonArray.length();  表示list长度
  	ArrayList<HashMap<String, Object>> liuyan = new ArrayList<HashMap<String, Object>>();
  	
  	//private int selectedIndex = -1;
  	String newly;
  	String avatar0;
  	boolean panduan;
  	
  	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
  	String ID=null;  //查看别人或自己主页时使用的具体ID
  	String PK,WODE;
  	//Bitmap BITMAPTX;
  	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.grzhuye);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
        
        Button b1 = (Button) findViewById(R.id.b1);      //发表留言
        b1.setOnClickListener(b1_listener); 
        Button b2 = (Button) findViewById(R.id.b2);      //查看我的纪录列表
        b2.setOnClickListener(b2_listener);
        
        tvjifen=(TextView)findViewById(R.id.jifen1);
        tvjingyan=(TextView)findViewById(R.id.jingyan1);
        tvxingbie=(TextView)findViewById(R.id.xingbie1);
        tvshengri=(TextView)findViewById(R.id.shengri1);
        tvjuzhudi=(TextView)findViewById(R.id.juzhudi1);
        tvhunlian=(TextView)findViewById(R.id.hunlian1);
        tvyonghuming=(TextView)findViewById(R.id.yonghuming1);
        iv1=(ImageView)findViewById(R.id.iv1);
        
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
        tongxin();
        
        lv1= (ListView) findViewById(R.id.lv1);
        lv1.addFooterView(mLoadLayout);    //增加列表最后一行！！
        viewAdapter = new ViewAdapter(this);
		lv1.setAdapter(viewAdapter);
		viewAdapter.setCount();
		lv1.setOnItemSelectedListener(this);  
		lv1.setOnScrollListener(this);
		lv1.setOnItemClickListener(lv1_listener); 
		
    }
	
	Button.OnClickListener b1_listener = new Button.OnClickListener()  //发表留言
    { 
		public void onClick(View v) 
		{ 
			fbly();  
		}  
    };
    Button.OnClickListener b2_listener = new Button.OnClickListener()  //查看我的纪录
    { 
		public void onClick(View v) 
		{ 
			whole a = (whole) getApplicationContext();
			a.setWode("all");
			Intent intent=new Intent();
			intent.setClass(grzhuye1.this,wodejilu7_1.class );
			grzhuye1.this.startActivity(intent);
		}  
    };
    
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
    
    public void fbly()   //发表留言  弹出EditText框
    {
    	LayoutInflater factory = LayoutInflater.from(this);        
		View textEntryView = factory.inflate(R.layout.fbpl_edit, null);    
		//内部局部类，只能访问方法的final类型的变量        
		final EditText et1 = (EditText) textEntryView                
		.findViewById(R.id.et1);        
		new AlertDialog.Builder(grzhuye1.this)     
		.setTitle("留言：")                
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
					newly = et1.getText().toString();   //上传留言内容
					String url = URL+"/uc/uchome/space.php?do=wall_api";
					HttpResponse httpResponse = null;
					System.out.println("url = "+url);
					HttpPost httpPost = new HttpPost(url);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("api","wall_publish")); 
					params.add(new BasicNameValuePair("userid",USERID));  
					params.add(new BasicNameValuePair("username",USERNAME)); 
					params.add(new BasicNameValuePair("psw",PSW)); 
					params.add(new BasicNameValuePair("id",ID)); 
					params.add(new BasicNameValuePair("idtype","uid")); 
					params.add(new BasicNameValuePair("message",newly));
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
						{System.out.println("错误！！！！");}
					}
					catch(ClientProtocolException e)
					{e.printStackTrace();}
					catch(IOException e)
					{e.printStackTrace();}
					
					Intent intent=new Intent();  //刷新
					intent.setClass(grzhuye1.this, grzhuye1.class);
					startActivity(intent);
					finish();
				}
			}                        
		})
		.show();
    }
	
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
			return liuyan.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public ViewAdapter(Context context)
		{
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HashMap<String, Object> map  = new HashMap<String, Object>();
			map=liuyan.get(position);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(inflater);
			LinearLayout linearLayout = null;
			linearLayout = (LinearLayout) layoutInflater.inflate(
					R.layout.liuyan_list, null);
			TextView tvzhengwen = ((TextView) linearLayout
					.findViewById(R.id.zhengwen1));
			TextView tvzuozhe = (TextView) linearLayout
					.findViewById(R.id.zuozhe1);
			TextView tvshijian = (TextView) linearLayout
			        .findViewById(R.id.shijian1);
			ImageView ivtouxiang =(ImageView)linearLayout
                    .findViewById(R.id.iv1);
			
            if (map.get("bitmap") != null)   ivtouxiang.setImageBitmap((Bitmap) map.get("bitmap"));
			tvzhengwen.setText((CharSequence) map.get("zhengwen"));
			tvzuozhe.setText((CharSequence) map.get("zuozhe")); 
			tvshijian.setText((CharSequence) map.get("shijian"));
			return linearLayout;
		}
	}
	
	public void tongxin()
	{
		whole a = (whole) getApplicationContext();
        String url = URL+ "/uc/uchome/space.php?do=user_info_api&userid="+USERID+"&username="+USERNAME+"&psw="+PSW+"&api=user_info&info_userid="+ID;
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
					JSONObject jsonObject1 =jsonObject.getJSONObject("user_info");
					tvyonghuming.setText(jsonObject1.getString("username")); 
					tvjifen.setText(jsonObject1.getString("credit")); 
					tvjingyan.setText(jsonObject1.getString("experience"));  
					String shengri=jsonObject1.getString("birthyear")+"."+
					       jsonObject1.getString("birthmonth")+"."+jsonObject1.getString("birthday");
					tvshengri.setText(shengri);
					String juzhudi=jsonObject1.getString("resideprovince")+
				       jsonObject1.getString("residecity");
					tvjuzhudi.setText(juzhudi);
					String marry=jsonObject1.getString("marry");
					if (marry.equals(1)) {tvhunlian.setText("单身");}  else {tvhunlian.setText("非单身");}
					String xingbie=jsonObject1.getString("sex");
					if (xingbie.equals(1)) {tvxingbie.setText("男");}  else {tvxingbie.setText("女");}
					//获取头像
					avatar0=jsonObject1.getString("avatar");
					String url2=null;
					if(!avatar0.equals("0"))  
		            {
		            	url2=URL+avatar0;
		            	iv1.setImageBitmap(a.get_pic(url2));
				    }
					//获取留言
					JSONArray jsonArray = jsonObject1.getJSONArray("space_comment");
					length=jsonArray.length();
					for(int i=0;i<jsonArray.length();i++)
					{    
			            JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);  //数组中的第i个取为jsonObject1
			            
			            HashMap<String, Object> map  = new HashMap<String, Object>();
			            map.put("zhengwen", Html.fromHtml(jsonObject2.getString("message")));
			            map.put("shijian", jsonObject2.getString("dateline"));
			            map.put("zuozhe", jsonObject2.getString("author"));
			            map.put("authorid", jsonObject2.getString("authorid"));
			            String avatar=jsonObject2.getString("avatar");
			            String url3=null;
			            if(!avatar.equals("0"))  
			            {
			            	url3=URL+avatar;
			            	map.put("bitmap", a.get_pic(url3));
			             }
			             liuyan.add(map);
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
		USERID=a.getUserid();
	  	ID=a.getOtherid();
	  	PSW=a.getPsw();
	  	URL=a.getURL(); 
        //BITMAPTX=a.getBitmap();
        a.setWode("all");
        PK=a.getPK();
        WODE=a.getWode();
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
		  map=liuyan.get(arg2);
  		  a.setOtherid((String) map.get("authorid")); 
		  Intent intent=new Intent();
  		  intent.setClass(grzhuye1.this,grzhuye1.class); 
  		  startActivity(intent);
  		  finish();
  	  }
  };
	@Override
	public void onClick(View v) {}
	
	public void usetoast(String result){
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="用户验证出错";
    	if(result.equals("wrong_operation")) tixing="操作错误";
    	if(result.equals("no_user")) tixing="无此用户";
    	if(result.equals("comment_too_short")) tixing="留言过短";
    	if(result.equals("succeed")) tixing="成功留言";
    	if(result.equals("wrong_status")) tixing="状态错误";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}