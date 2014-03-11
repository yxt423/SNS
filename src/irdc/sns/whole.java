package irdc.sns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

public class whole extends Application 
{    
	//�����˻�  baga  891213  3   root  681528xrt  1  zero 681528xrt  4
	private String USERNAME = "root";        
	private String PSW = "681528xrt";
	private String USERID = "1";  //��½��ID
	private String OTHERID = null;  //���˵�ID  ���ڲ鿴��ҳ
	private String URL="http://192.168.1.104";
	private int STATUS=1; // status 0:������ 1����ͨ�� 2��PK��  -1��δ����
	private int ZHUYE=1;  //�鿴��ҳʱ��������    1���ҵ���ҳ   2�����˵���ҳ
	//private Bitmap BITMAP=null;  //����ͷ��ͼƬ
	//private Bitmap BITMAPPK1=null;  //����pkͷ��ͼƬ��һ��
	//private Bitmap BITMAPPK2=null;  //����pkͷ��ͼƬ�ڶ���
	private String WODE="all";    //all �鿴�ҵļ�¼    draft  �鿴�ݸ���     no_pk  pkʱ���б�
	String PK="0";  //�����жϷ���¼������PK ���� �����¼�¼    1  PK  0  ��PK
	private String lujing=null;  //�洢��Ƭ��·��  ����save_pic����
	//private Bitmap bitmap=null;  //��������ͼƬ
	
	public void set(String userid,String username,String passWord) //���ã�˳��Ϊ ID���û���������
	{        
		this.USERNAME = username;   
		this.PSW = passWord;
		this.USERID = userid;  
    }
	//�õ�URL
	public String getURL() 
	{        
		return this.URL;    
	} 
	// �����û���
	public void setUsername(String username) 
	{        
		this.USERNAME = username;    
	}    
	public String getUsername() 
	{        
		return this.USERNAME;    
	}   
	// ��������    
	public void setPsw(String passWord) 
	{        
		this.PSW = passWord;    
	}    
	public String getPsw() 
	{        
		return this.PSW;    
	} 
	// �����û�ID    
	public void setUserid(String userid) 
	{        
		this.USERID = userid;    
	}    
	public String getUserid() 
	{        
		return this.USERID;    
	}
	//����״̬
	public void setStatus(int status) 
	{        
		this.STATUS = status;    
	}    
	public int getStatus() 
	{        
		return this.STATUS;    
	}
	//������ҳ
	public void setZhuye(int s) 
	{        
		this.ZHUYE = s;    
	}    
	public int getZhuye() 
	{        
		return this.ZHUYE;    
	}
	//�������˵�ID
	public void setOtherid(String s) 
	{        
		this.OTHERID = s;    
	}    
	public String getOtherid() 
	{        
		return this.OTHERID;    
	}
	/*
	//����ͷ��
	public void setBitmap(Bitmap bitmap) 
	{        
		this.BITMAP = bitmap;    
	}    
	public Bitmap getBitmap() 
	{        
		return this.BITMAP;    
	}
	
	//����pk��¼��ͷ��
	public void setpkBitmap(Bitmap bitmap1,Bitmap bitmap2) 
	{        
		this.BITMAPPK1 = bitmap1;    
		this.BITMAPPK2 = bitmap2; 
	}    
	public Bitmap getpkBitmap1() 
	{        
		return this.BITMAPPK1;    
	}
	public Bitmap getpkBitmap2() 
	{        
		return this.BITMAPPK2;    
	}
	*/
	// �����ҵļ�¼
	public void setWode(String wode) 
	{        
		this.WODE = wode;    
	}    
	public String getWode() 
	{        
		return this.WODE;    
	}
	
	// ����PK���ж�
	public void setPK(String pk) 
	{        
		this.PK = pk;    
	}    
	public String getPK() 
	{        
		return this.PK;    
	}

	public Bitmap get_pic(String pic_url)  //ͨ��URL���ͼƬ��bitmap��ʽ
    {
		Bitmap   bitmap = null;
    	try {
	    	URL picUrl = new URL(pic_url);  //picUrl  ͼƬ��url
			HttpURLConnection conn = (HttpURLConnection)picUrl.openConnection();
			//�������Ĳ���
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			
			InputStream inStream  = conn.getInputStream(); 
		    bitmap = BitmapFactory.decodeStream(inStream);
	        if(bitmap == null){
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length = -1;
				while((length = inStream.read(buffer))!= -1 ){
					outStream.write(buffer, 0, length);
				}
				byte[] data = outStream.toByteArray();
		        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		        outStream.close();     
		        inStream.close();
	        }
    	}
    	catch (Exception e) {e.printStackTrace();}
		return bitmap;
	}
	
	public Bitmap save_pic (String pic_url)  //ͨ��URL���ͼƬ��bitmap��ʽ�����浽�����ֻ�sd����
	{
		Bitmap   bitmap = null;
		try {
	    	URL picUrl = new URL(URL+pic_url);  //picUrl  ͼƬ��url
	    	
			HttpURLConnection conn = (HttpURLConnection)picUrl.openConnection();
			//�������Ĳ���
			conn.setConnectTimeout(6*1000); // ע��Ҫ���ó�ʱ������ʱ�䲻Ҫ����10�룬���ⱻandroidϵͳ����
        	if (conn.getResponseCode() != 200) throw new RuntimeException("����urlʧ��");
			
			InputStream inStream  = conn.getInputStream(); 
		    File rootsdcardFile  =  Environment.getExternalStorageDirectory();//��ȡsd����Ŀ¼
        	File mytestFile = new File(rootsdcardFile, "У԰����˹");
        	mytestFile.mkdir();//�ڸ�Ŀ¼�´���һ����У԰����˹���ļ���
        	String wenjianming= System.currentTimeMillis()+".jpg";  
        	File myjpgFile = new  File(mytestFile, wenjianming);
        	myjpgFile.createNewFile();//��mytestFile�ļ����´���һ����wenjianming���ļ�
        	
        	FileOutputStream fileOutputStream = new FileOutputStream(myjpgFile);//�򿪱����ļ�д����
        	byte buffer[] = new byte[1000];
        	int length = 0;
        	while((length=inStream.read(buffer))>=0)
        	{
        		fileOutputStream.write(buffer, 0, length);
        	}
        	inStream.close();
        	fileOutputStream.close();
        	lujing=Environment.getExternalStorageDirectory()+"/У԰����˹/"+wenjianming;  //�ļ�·��
        	System.out.println(lujing);  
        	
        	HttpURLConnection conn2 = (HttpURLConnection) picUrl.openConnection();
        	conn2.setDoInput(true);
			conn2.setDoOutput(true);
			conn2.setUseCaches(false);
			conn2.setRequestMethod("GET");
        	InputStream inStream2  = conn2.getInputStream(); 
        	bitmap = BitmapFactory.decodeStream(inStream2);
	        if(bitmap == null){
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer2 = new byte[1024];
				int length2 = -1;
				while((length2 = inStream2.read(buffer2))!= -1 ){
					outStream.write(buffer2, 0, length2);
				}
				byte[] data = outStream.toByteArray();
		        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		        outStream.close();   
		        inStream2.close();
	        }
        	
    	}
    	catch (Exception e) {e.printStackTrace();}
		return bitmap;
	}
	//��ñ���ͼƬ��·��
	public String getLujing() 
	{        
		return this.lujing;    
	}
	//������ʾ
	
	public void usetoast(String tixing )
	{
 		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
 	    textToast.show();
 	}
}

/*����
whole a = (whole) getApplicationContext();
a.getUsername();
a.setUsername("");
*/





