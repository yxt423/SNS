//ע��ҳ��      ���ڣ�2011-7-25  ���ߣ����ݳ�
package irdc.sns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity_2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeractivity_2);
		
		Button register_registerButton = (Button) findViewById(R.id.register_registerButton);
		register_registerButton.setOnClickListener(new Button.OnClickListener()  //ע�ᰴť
        {
          public void onClick(View v)
          {
            Intent intent = new Intent();
        	  intent.setClass(RegisterActivity_2.this, sns.class);
        	  startActivity(intent);
        	  finish();
          }
        });
	}

}
