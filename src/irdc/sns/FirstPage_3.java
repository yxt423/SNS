//主页面 9个功能按钮       日期：2011-7-25  作者：武逸超     俞欣彤   
package irdc.sns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FirstPage_3 extends Activity {
	private ImageButton b1;
	private ImageButton b2;
	private ImageButton b3;
	private ImageButton b4;
	private ImageButton b5;
	private ImageButton b6;
	private ImageButton b7;
	private ImageButton b8;
	private ImageButton b9;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstpage_3);
		b1=(ImageButton)findViewById(R.id.b1);
		b1.setOnClickListener(b1_listener);
		b2=(ImageButton)findViewById(R.id.b2);
		b2.setOnClickListener(b2_listener);
		b3=(ImageButton)findViewById(R.id.b3);
		b3.setOnClickListener(b3_listener);
		b4=(ImageButton)findViewById(R.id.b4);
		b4.setOnClickListener(b4_listener);
		b5=(ImageButton)findViewById(R.id.b5);
		b5.setOnClickListener(b5_listener);
		b6=(ImageButton)findViewById(R.id.b6);
		b6.setOnClickListener(b6_listener);
	
	}
	//图按钮1号     个人主页
	public ImageButton.OnClickListener b1_listener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			whole a = (whole) getApplicationContext();
			a.setOtherid(a.getUserid());
			a.setZhuye(1);
			a.setPK("0");
			Intent intent=new Intent();
			intent.setClass(FirstPage_3.this,grzhuye1.class );
			FirstPage_3.this.startActivity(intent);
		}
	};
	//图按钮2号     校园吉尼斯  
	public ImageButton.OnClickListener b2_listener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View v) {
			whole a = (whole) getApplicationContext();
			a.setStatus(1);
			Intent intent=new Intent();
			intent.setClass(FirstPage_3.this,Record_4.class );
			FirstPage_3.this.startActivity(intent);
		}
	};
	//图按钮3号     评定中…
	public ImageButton.OnClickListener b3_listener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View v) {
			whole a = (whole) getApplicationContext();
			a.setStatus(0);
			Intent intent=new Intent();
			intent.setClass(FirstPage_3.this,Record_4.class );
			FirstPage_3.this.startActivity(intent);
		}
	};
	//图按钮4号    PK中
	public ImageButton.OnClickListener b4_listener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View v) {
			whole a = (whole) getApplicationContext();
			a.setStatus(2);
			Intent intent=new Intent();
			intent.setClass(FirstPage_3.this,Record_4.class );
			FirstPage_3.this.startActivity(intent);
		}
	};
	//图按钮5号     发表纪录
	public ImageButton.OnClickListener b5_listener = new ImageButton.OnClickListener()
	{
			@Override
			public void onClick(View v) {
				whole a = (whole) getApplicationContext();
				a.setPK("0");
				new AlertDialog.Builder(FirstPage_3.this)
		          .setTitle("请选择发布纪录的方式")
		          .setItems(R.array.fbjilu,
		          new DialogInterface.OnClickListener()
		          {
		            public void onClick(DialogInterface dialog, int whichcountry)
		            {
		            	if(whichcountry==0)
		            	{
		            		whole a = (whole) getApplicationContext();
		            		a.setWode("all");
		            		Intent intent = new Intent();
		        	        intent.setClass(FirstPage_3.this, fbjilu7.class);
		        	        startActivity(intent);
		        	        //finish();
		        	    }
		            	else
		            	{
		            		whole a = (whole) getApplicationContext();
		            		a.setWode("draft");
		            		a.setOtherid(a.getUserid());
		            		Intent intent = new Intent();
		        	        intent.setClass(FirstPage_3.this, wodejilu7_1.class);
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
	};
	//图按钮6号    我的好友
	public ImageButton.OnClickListener b6_listener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View v) {
			whole a = (whole) getApplicationContext();
			a.setZhuye(2);
			Intent intent=new Intent();
			intent.setClass(FirstPage_3.this,friend_6.class );
			FirstPage_3.this.startActivity(intent);
		}
	};
}
