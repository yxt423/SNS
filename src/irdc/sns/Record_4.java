//��¼ҳ��tab1 ��ͨ����list       ���ڣ�2011-7-25  ���ߣ� ����ͮ   
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
        
      //��������tab��ǩ 
        TabHost tabHost = getTabHost(); 
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("���ѵļ�¼")
				.setContent(new Intent(this, Record_4_1.class))); 
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("��ҵļ�¼")
				.setContent(new Intent(this, Record_4_2.class))); 
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("�ҵļ�¼")
				.setContent(new Intent(this, Record_4_3.class)));   
    }
 
}