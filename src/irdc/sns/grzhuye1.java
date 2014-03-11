//������ҳ  8��3��   ����ͮ
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
  	private final Handler mHandler = new Handler();// ��Handler�м�������      
	private final LayoutParams WW = new LinearLayout.LayoutParams
	(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);   
	private int mLastItem = 0;  
	int scrollState;// ȫ�ֱ�����������¼ScrollView�Ĺ���״̬��1��ʾ��ʼ������2��ʾ���ڹ�����0��ʾ����ֹͣ     
  	int visibleItemCount;// ��ǰ�ɼ�ҳ���е�Item����      
  	private int length=1;    //jsonArray.length();  ��ʾlist����
  	ArrayList<HashMap<String, Object>> liuyan = new ArrayList<HashMap<String, Object>>();
  	
  	//private int selectedIndex = -1;
  	String newly;
  	String avatar0;
  	boolean panduan;
  	
  	String USERNAME=null;
  	String USERID=null; 
  	String PSW=null;
  	String URL=null; 
  	String ID=null;  //�鿴���˻��Լ���ҳʱʹ�õľ���ID
  	String PK,WODE;
  	//Bitmap BITMAPTX;
  	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        setContentView(R.layout.grzhuye);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
        
        Button b1 = (Button) findViewById(R.id.b1);      //��������
        b1.setOnClickListener(b1_listener); 
        Button b2 = (Button) findViewById(R.id.b2);      //�鿴�ҵļ�¼�б�
        b2.setOnClickListener(b2_listener);
        
        tvjifen=(TextView)findViewById(R.id.jifen1);
        tvjingyan=(TextView)findViewById(R.id.jingyan1);
        tvxingbie=(TextView)findViewById(R.id.xingbie1);
        tvshengri=(TextView)findViewById(R.id.shengri1);
        tvjuzhudi=(TextView)findViewById(R.id.juzhudi1);
        tvhunlian=(TextView)findViewById(R.id.hunlian1);
        tvyonghuming=(TextView)findViewById(R.id.yonghuming1);
        iv1=(ImageView)findViewById(R.id.iv1);
        
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
        
        lv1= (ListView) findViewById(R.id.lv1);
        lv1.addFooterView(mLoadLayout);    //�����б����һ�У���
        viewAdapter = new ViewAdapter(this);
		lv1.setAdapter(viewAdapter);
		viewAdapter.setCount();
		lv1.setOnItemSelectedListener(this);  
		lv1.setOnScrollListener(this);
		lv1.setOnItemClickListener(lv1_listener); 
		
    }
	
	Button.OnClickListener b1_listener = new Button.OnClickListener()  //��������
    { 
		public void onClick(View v) 
		{ 
			fbly();  
		}  
    };
    Button.OnClickListener b2_listener = new Button.OnClickListener()  //�鿴�ҵļ�¼
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
    
    public void fbly()   //��������  ����EditText��
    {
    	LayoutInflater factory = LayoutInflater.from(this);        
		View textEntryView = factory.inflate(R.layout.fbpl_edit, null);    
		//�ڲ��ֲ��ֻ࣬�ܷ��ʷ�����final���͵ı���        
		final EditText et1 = (EditText) textEntryView                
		.findViewById(R.id.et1);        
		new AlertDialog.Builder(grzhuye1.this)     
		.setTitle("���ԣ�")                
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
					newly = et1.getText().toString();   //�ϴ���������
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
						{System.out.println("���󣡣�����");}
					}
					catch(ClientProtocolException e)
					{e.printStackTrace();}
					catch(IOException e)
					{e.printStackTrace();}
					
					Intent intent=new Intent();  //ˢ��
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
			//�ж���Ӧ��
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
					if (marry.equals(1)) {tvhunlian.setText("����");}  else {tvhunlian.setText("�ǵ���");}
					String xingbie=jsonObject1.getString("sex");
					if (xingbie.equals(1)) {tvxingbie.setText("��");}  else {tvxingbie.setText("Ů");}
					//��ȡͷ��
					avatar0=jsonObject1.getString("avatar");
					String url2=null;
					if(!avatar0.equals("0"))  
		            {
		            	url2=URL+avatar0;
		            	iv1.setImageBitmap(a.get_pic(url2));
				    }
					//��ȡ����
					JSONArray jsonArray = jsonObject1.getJSONArray("space_comment");
					length=jsonArray.length();
					for(int i=0;i<jsonArray.length();i++)
					{    
			            JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);  //�����еĵ�i��ȡΪjsonObject1
			            
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
		if (viewAdapter.count >= length)  //�����е��б���Ŀ����length�ĸ�������������ʾ�����ࡱ��ť
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
  		  //������ȡ�õ���Ϣ
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
    	if(result.equals("wrong_auth")) tixing="�û���֤����";
    	if(result.equals("wrong_operation")) tixing="��������";
    	if(result.equals("no_user")) tixing="�޴��û�";
    	if(result.equals("comment_too_short")) tixing="���Թ���";
    	if(result.equals("succeed")) tixing="�ɹ�����";
    	if(result.equals("wrong_status")) tixing="״̬����";
    	if(!tixing.equals(""))
    	{
    		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
    	    textToast.show();
    	}
    	else  panduan=true;
	}
}