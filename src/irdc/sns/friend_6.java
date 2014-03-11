package irdc.sns;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse; 
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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

public class friend_6 extends Activity implements OnClickListener,OnScrollListener ,
OnItemSelectedListener
{
	private int length=1;    //jsonArray.length();  ��ʾlist����
	private ListView lv1;                                      	
	private ViewAdapter viewAdapter;
	//private int selectedIndex = -1;
	private LinearLayout mLoadLayout; 
  	private LinearLayout mProgressLoadLayout;    
  	private final Handler mHandler = new Handler();// ��Handler�м�������      
	private final LayoutParams WW = new LinearLayout.LayoutParams
	(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);   
	private int mLastItem = 0;  
	int scrollState;// ȫ�ֱ�����������¼ScrollView�Ĺ���״̬��1��ʾ��ʼ������2��ʾ���ڹ�����0��ʾ����ֹͣ     
  	int visibleItemCount;// ��ǰ�ɼ�ҳ���е�Item����      
	ArrayList<HashMap<String, Object>> haoyou = new ArrayList<HashMap<String, Object>>();
	boolean panduan;
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
  	int STATUS=0;
  	int ZHUYE=0; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_6);
        
        /**           * "������"���֣��˲��ֱ���ӵ�ListView��Footer�С�           */          
		mLoadLayout = new LinearLayout(this);          
		mLoadLayout.setMinimumHeight(30);          
		mLoadLayout.setGravity(Gravity.CENTER);          
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);  
		/** �������ť��ʱ����ʾ���View����Viewʹ��ˮƽ��ʽ���֣������һ�����������ұ����ı��� * Ĭ����Ϊ���ɼ�           */     
		mProgressLoadLayout = new LinearLayout(this);         
		mProgressLoadLayout.setMinimumHeight(30);          
		mProgressLoadLayout.setGravity(Gravity.CENTER);          
		mProgressLoadLayout.setOrientation(LinearLayout.HORIZONTAL);           
		ProgressBar mProgressBar = new ProgressBar(this);          
		mProgressBar.setPadding(0, 0, 15, 0);          
		mProgressLoadLayout.addView(mProgressBar, WW);// Ϊ������ӽ�����           
		TextView mTipContent = new TextView(this);         
		mTipContent.setText("������...");          
		mProgressLoadLayout.addView(mTipContent, WW);// Ϊ��������ı�         
		mProgressLoadLayout.setVisibility(View.GONE);// Ĭ����Ϊ���ɼ���ע��View.GONE��View.INVISIBLE������
		mLoadLayout.addView(mProgressLoadLayout);// ��֮ǰ�Ĳ�����View������ӽ���          
		final Button bgd = new Button(this);          
		bgd.setText("���ظ���");          // ��Ӱ�ť         
		mLoadLayout.addView(bgd, WW);  
		bgd.setOnClickListener(bgd_listener);
        
        getwhole();
        tongxin();
        
        //�����б�
        lv1= (ListView) findViewById(R.id.lv1);
        lv1.addFooterView(mLoadLayout);    //�����б����һ�У���

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
        String url = URL+ "/uc/uchome/space.php?do=friends_api&api=friends_list&userid="+USERID+"&username="+USERNAME+"&psw="+PSW;
		System.out.println("url = "+url);
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
					JSONArray jsonArray = jsonObject.getJSONArray("friends_info");
					length=jsonArray.length();
					for(int i=0;i<jsonArray.length();i++){   //���鳤�� jsonArray.length()
			            JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);  //�����еĵ�i��ȡΪjsonObject1
			            HashMap<String, Object> map  = new HashMap<String, Object>();
			            //map.put("",); 
			            map.put("jifen",jsonObject2.getString("credit"));
			            map.put("jingyan",jsonObject2.getString("experience"));
			            map.put("fname",jsonObject2.getString("fusername"));
			            map.put("fid",jsonObject2.getString("fuid")); 
			            
			            //���ͷ��
			            String avatar=jsonObject2.getString("avatar"); 
			            String url2=null;
			            if(!avatar.equals("0"))  
			            {
			            	url2=URL+avatar;
			            	map.put("bitmap", a.get_pic(url2));
	                    }
			            haoyou.add(map);
	                 }
				}
			}
			else  
				System.out.println("100");
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
			{                      // �����ʱ�Ѵ���������Layout��Ϊ�ɼ�����Button��Ϊ���ɼ�               
				mProgressLoadLayout.setVisibility(View.VISIBLE);              
				v.setVisibility(View.GONE);                   
				if (viewAdapter.count <= length) 
				{                          
					mHandler.postDelayed(new Runnable() 
					{                              
						@Override                  
						public void run() 
						{               
							if(viewAdapter.count+5>length)  //��������ࡱ�������ӵ�length�ĸ���
								viewAdapter.count=length;
							else
								viewAdapter.count += 5;
							viewAdapter.notifyDataSetChanged();                 
							lv1.setSelection(mLastItem                  
									- visibleItemCount + 1);                 
							// ��ȡ���ݳɹ�ʱ��Layout��Ϊ���ɼ�����Button��Ϊ�ɼ�                          
							mProgressLoadLayout.setVisibility(View.GONE);       
							v.setVisibility(View.VISIBLE);                
							}                         
						}, 0);                  
					}                 
				}
		}  
    };
    
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
			return haoyou.get(position);
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
			map=haoyou.get(position);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(inflater);
			LinearLayout linearLayout = null;
				linearLayout = (LinearLayout) layoutInflater.inflate(
						R.layout.friend_list, null);
				TextView tvjifen = (TextView) linearLayout
				        .findViewById(R.id.jifen1);
				TextView tvjingyan = ((TextView) linearLayout
						.findViewById(R.id.jingyan1)); 
				TextView tvfname = (TextView) linearLayout
						.findViewById(R.id.fname1); 
				ImageView ivtouxiang =(ImageView)linearLayout
		                .findViewById(R.id.iv1);
		        if (map.get("bitmap") != null)   ivtouxiang.setImageBitmap((Bitmap) map.get("bitmap"));
				tvjifen.setText((CharSequence) map.get("jifen"));
				tvjingyan.setText((CharSequence) map.get("jingyan")); 
				tvfname.setText((CharSequence) map.get("fname"));  
			return linearLayout;
		} 
	}
    
  //�����б��� ��ת��   ��ϸ����
    
	private OnItemClickListener lv1_listener = new OnItemClickListener(){
    	  
    	  @Override
    	  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
    	  {
    		  //������ȡ�õ���Ϣ
    		  whole a = (whole) getApplicationContext();
    		  HashMap<String, Object> map  = new HashMap<String, Object>();
  			  map=haoyou.get(arg2);
    		  a.setOtherid((String) map.get("fid"));
		      Intent intent=new Intent();
    		  intent.setClass(friend_6.this,grzhuye1.class); 
    		  startActivity(intent);
    		  //finish();
    	  }
    };
    
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) { 
	}
	@Override
	public void onClick(View arg0) {
		
	}
	
	private void getwhole()
	{
		whole a = (whole) getApplicationContext();
		USERNAME=a.getUsername();
	  	USERID=a.getUserid(); 
	  	PSW=a.getPsw();
	  	URL=a.getURL();
        ZHUYE=a.getZhuye();
        STATUS=a.getStatus();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;      
		mLastItem = firstVisibleItem + visibleItemCount - 1;   
		if (viewAdapter.count >= length)  //�����е��б���Ŀ����length�ĸ�������������ʾ�����ࡱ��ť
		{             
			lv1.removeFooterView(mLoadLayout);        
		} 
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}
	public void usetoast(String result){  //��������
    	panduan=false;
    	String tixing = "";
    	if(result.equals("wrong_auth")) tixing="�û���֤����";
    	if(result.equals("wrong_operation")) tixing="��������";
    	if(result.equals("wrong_status")) tixing="״̬����";
    	if(result.equals("wrong_present")) tixing="�鿴�������"; 
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}