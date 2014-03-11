package irdc.sns;

import java.io.IOException;
import java.util.ArrayList; 
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Record_4_1 extends Activity implements OnClickListener, OnScrollListener ,
OnItemSelectedListener
{
	private int length=1;    //jsonArray.length();  ��ʾlist����
	private ListView lv1;                                      	
	private ViewAdapter viewAdapter;
	private LinearLayout mLoadLayout; 
  	private LinearLayout mProgressLoadLayout;    
  	private final Handler mHandler = new Handler();// ��Handler�м�������      
	private final LayoutParams WW = new LinearLayout.LayoutParams
	(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);   
	private int mLastItem = 0;  
	int scrollState;// ȫ�ֱ�����������¼ScrollView�Ĺ���״̬��1��ʾ��ʼ������2��ʾ���ڹ�����0��ʾ����ֹͣ     
  	int visibleItemCount;// ��ǰ�ɼ�ҳ���е�Item����      
  	ArrayList<HashMap<String, Object>> jilu = new ArrayList<HashMap<String, Object>>();
	private int selectedIndex = -1; 
	boolean panduan;
	
	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
  	int STATUS=0;
  	int ZHUYE=0; 
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.record_4_1);  
        
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
        String url=null;
        switch (STATUS) {
		case 1:
			url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_list&status=finished&present=we&userid="+USERID+"&username="+USERNAME+"&psw="+PSW; break;
		case 0:
			url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_list&status=verify&present=we&userid="+USERID+"&username="+USERNAME+"&psw="+PSW; break;
		case 2:
			url = URL+ "/uc/uchome/space.php?do=blog_api&api=blog_list&status=pk&present=we&userid="+USERID+"&username="+USERNAME+"&psw="+PSW; break;
		//case -1:
			// break;
		default:
			break;  } 
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
					JSONArray jsonArray = jsonObject.getJSONArray("blog_list");
					length=jsonArray.length();
					for(int i=0;i<jsonArray.length();i++){   //���鳤�� jsonArray.length()
			            JSONObject jsonObject1 = (JSONObject)jsonArray.opt(i);  //�����еĵ�i��ȡΪjsonObject1
			            JSONObject jsonObject2 =jsonObject1.getJSONObject("blog_itme");
			            HashMap<String, Object> map  = new HashMap<String, Object>();
			            map.put("biaoti",  Html.fromHtml(jsonObject2.getString("subject")).toString());
			            map.put("shijian", jsonObject2.getString("dateline"));
			            map.put("biaoqian", jsonObject2.getString("stagname"));
			            map.put("zuozhe", jsonObject2.getString("username"));
			            map.put("blogid", jsonObject2.getString("blogid")); 
			            if(STATUS==0)
			            	map.put("score", jsonObject2.getInt("score"));	
			            String avatar=jsonObject2.getString("avatar"); 
			            String url2=null;
			            if(!avatar.equals("0"))  
			            {
			            	url2=URL+avatar;
			            	map.put("bitmap", a.get_pic(url2));
	                    }
			            if(STATUS==2)  //PK״̬�µĵڶ�����¼
			            {
			            	 map.put("zhichi", jsonObject2.getString("click_2")); 
			            	 JSONObject jsonObject3 =jsonObject2.getJSONObject("pk_info");
			            	 map.put("blogid2", jsonObject3.getString("blogid")); 
			            	 map.put("biaoti2",  Html.fromHtml(jsonObject3.getString("subject")).toString());
				             map.put("zuozhe2", jsonObject3.getString("username"));
				             map.put("zhichi2", jsonObject3.getString("click_2")); 
				             avatar=jsonObject3.getString("avatar"); 
				             if(!avatar.equals("0"))  
					            {
					            	url2=URL+avatar;
					            	map.put("bitmap2", a.get_pic(url2));
			                    }
			            }
			            jilu.add(map);
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
	
	public class ViewAdapter extends BaseAdapter
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
			switch (STATUS) {
			case 1:
				linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.record_4_list, null);
				TextView tvzuozhe = (TextView) linearLayout.findViewById(R.id.zuozhe1);
				TextView tvbiaoti = ((TextView) linearLayout.findViewById(R.id.biaoti1));
				//TextView tvzhuangtai = (TextView) linearLayout.findViewById(R.id.zhuangtai1);
				TextView tvbiaoqian = (TextView) linearLayout.findViewById(R.id.biaoqian1);
				TextView tvshijian = (TextView) linearLayout.findViewById(R.id.shijian1);
				ImageView ivtouxiang =(ImageView)linearLayout.findViewById(R.id.iv1);
				if (map.get("bitmap") != null)   ivtouxiang.setImageBitmap((Bitmap) map.get("bitmap") );
				tvzuozhe.setText((CharSequence) map.get("zuozhe") );
				tvbiaoti.setText((CharSequence) map.get("biaoti") );
				//tvzhuangtai.setText("��ͨ��");
				tvbiaoqian.setText((CharSequence) map.get("biaoqian") );
				tvshijian.setText((CharSequence) map.get("shijian") );
				break;
			case 0:
				linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.record_4_pingding_list, null);
				ProgressBar pb1= (ProgressBar) linearLayout.findViewById(R.id.pb1);
				pb1.setProgress((Integer) map.get("score"));
				TextView tvscore=((TextView) linearLayout
						.findViewById(R.id.score1));
				tvscore.setText(""+(Integer) map.get("score"));
				TextView tvzuozhe0 = (TextView) linearLayout.findViewById(R.id.zuozhe1);
				TextView tvbiaoti0 = ((TextView) linearLayout.findViewById(R.id.biaoti1));
				//TextView tvzhuangtai0 = (TextView) linearLayout.findViewById(R.id.zhuangtai1);
				TextView tvbiaoqian0 = (TextView) linearLayout.findViewById(R.id.biaoqian1);
				TextView tvshijian0 = (TextView) linearLayout.findViewById(R.id.shijian1);
				ImageView ivtouxiang0 =(ImageView)linearLayout.findViewById(R.id.iv1);
				if (map.get("bitmap") != null)   ivtouxiang0.setImageBitmap((Bitmap) map.get("bitmap") );
				tvzuozhe0.setText((CharSequence) map.get("zuozhe") );
				tvbiaoti0.setText((CharSequence) map.get("biaoti") );
				//tvzhuangtai0.setText("������");
				tvbiaoqian0.setText((CharSequence) map.get("biaoqian") );
				tvshijian0.setText((CharSequence) map.get("shijian") );
				break;
			case 2:
				linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.record_4_pk_list, null);
				TextView tvzuozhe21 = (TextView) linearLayout.findViewById(R.id.zuozhe1);
				TextView tvbiaoti21 = ((TextView) linearLayout.findViewById(R.id.biaoti1));
				TextView tvzhichi21 = ((TextView) linearLayout.findViewById(R.id.zhichi1));
				ImageView ivtouxiang21 =(ImageView)linearLayout.findViewById(R.id.iv1);
				TextView tvzuozhe22 = (TextView) linearLayout.findViewById(R.id.zuozhe2);
				TextView tvbiaoti22 = ((TextView) linearLayout.findViewById(R.id.biaoti2));
				TextView tvzhichi22 = ((TextView) linearLayout.findViewById(R.id.zhichi2));
				ImageView ivtouxiang22 =(ImageView)linearLayout.findViewById(R.id.iv2);
				
				if (map.get("bitmap") != null)   ivtouxiang21.setImageBitmap((Bitmap) map.get("bitmap") );
				tvzuozhe21.setText((CharSequence) map.get("zuozhe") );
				tvbiaoti21.setText((CharSequence) map.get("biaoti") ); 
				tvzhichi21.setText((CharSequence) map.get("zhichi") ); 
				if (map.get("bitmap2") != null)   ivtouxiang22.setImageBitmap((Bitmap) map.get("bitmap2") );
				tvzuozhe22.setText((CharSequence) map.get("zuozhe2") );
				tvbiaoti22.setText((CharSequence) map.get("biaoti2") ); 
				tvzhichi22.setText((CharSequence) map.get("zhichi2") ); 
				break;
			default:break;
			}
			return linearLayout;
		} 
	} 
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		selectedIndex = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}

	@Override
	public void onClick(View v) { 
	}
	
	//�����б��� ��ת��   ��¼����ϸ����
	private OnItemClickListener lv1_listener = new OnItemClickListener()
	{
    	  @Override
    	  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
    	  {
		    //������ȡ�õ���Ϣ  
			  whole a = (whole) getApplicationContext();
			  HashMap<String, Object> map  = new HashMap<String, Object>();
			  map=jilu.get(arg2);
		      //a.setBitmap((Bitmap) map.get("bitmap")); 
		 
		    Intent intent=new Intent();
			Bundle bundle=new Bundle();
			switch (STATUS) {
			case 1:
				intent.setClass(Record_4_1.this,tongguo6.class); 
				break;
			case 0: 
				intent.setClass(Record_4_1.this,pingding5.class); 
				//bundle.putInt("score",(Integer) map.get("score"));
				break;
			case 2:
				//a.setpkBitmap((Bitmap)map.get("bitmap"), (Bitmap)map.get("bitmap2"));
				intent.setClass(Record_4_1.this,pk8.class); 
				break;
			case -1:
				intent.setClass(Record_4_1.this,tongguo6.class); 
				break;
			default:
				break;
			}					
    		//bundle.putString("zuozhe", (String) map.get("zuozhe"));  //ͨ��arg2�жϵ��������б��еĵڼ���
    		//bundle.putString("biaoti", (String) map.get("biaoti")); 
    		//bundle.putString("shijian", (String) map.get("shijian"));
    		//bundle.putString("biaoqian", (String) map.get("biaoqian"));
    		bundle.putString("blogid", (String) map.get("blogid"));
    		intent.putExtras(bundle);
    		startActivity(intent);
    		//finish();
    	  }
    };
	
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "ˢ��");
		menu.add(0, 1, 0, "����"); 
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
      super.onOptionsItemSelected(item);
      switch(item.getItemId())
      {
        case 0:  //��������
        	/*viewAdapter.removeAll();
          	tongxin(); 
          	for(int i=0;i<length;i++)
    		{
    			viewAdapter.addText(biaoti[i]);
    		}*/
          break;
        case 1:  //����
          finish();
          break;
      }
      return true;
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