//纪录页面tab1 已通过的list       日期：2011-7-25  作者： 俞欣彤   
package irdc.sns;
import android.app.TabActivity; 
import android.content.Intent;
import android.os.Bundle; 
import android.view.LayoutInflater; 
import android.widget.TabHost; 

public class Record_4 extends TabActivity  
{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
      //创建三个tab标签 
        TabHost tabHost = getTabHost(); 
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("好友的纪录")
				.setContent(new Intent(this, Record_4_1.class))); 
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("大家的纪录")
				.setContent(new Intent(this, Record_4_2.class))); 
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("我的纪录")
				.setContent(new Intent(this, Record_4_3.class)));   
    }
 
}