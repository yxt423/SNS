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
	//已有账户  baga  891213  3   root  681528xrt  1  zero 681528xrt  4
	private String USERNAME = "root";        
	private String PSW = "681528xrt";
	private String USERID = "1";  //登陆人ID
	private String OTHERID = null;  //别人的ID  用于查看主页
	private String URL="http://192.168.1.104";
	private int STATUS=1; // status 0:评定中 1：已通过 2：PK中  -1：未发布
	private int ZHUYE=1;  //查看主页时用于区分    1：我的主页   2：别人的主页
	//private Bitmap BITMAP=null;  //用于头像图片
	//private Bitmap BITMAPPK1=null;  //用于pk头像图片第一个
	//private Bitmap BITMAPPK2=null;  //用于pk头像图片第二个
	private String WODE="all";    //all 查看我的纪录    draft  查看草稿箱     no_pk  pk时的列表
	String PK="0";  //用于判断发记录是用于PK 还是 发布新纪录    1  PK  0  非PK
	private String lujing=null;  //存储照片的路径  用于save_pic函数
	//private Bitmap bitmap=null;  //用于正文图片
	
	public void set(String userid,String username,String passWord) //设置：顺序为 ID、用户名、密码
	{        
		this.USERNAME = username;   
		this.PSW = passWord;
		this.USERID = userid;  
    }
	//得到URL
	public String getURL() 
	{        
		return this.URL;    
	} 
	// 操作用户名
	public void setUsername(String username) 
	{        
		this.USERNAME = username;    
	}    
	public String getUsername() 
	{        
		return this.USERNAME;    
	}   
	// 操作密码    
	public void setPsw(String passWord) 
	{        
		this.PSW = passWord;    
	}    
	public String getPsw() 
	{        
		return this.PSW;    
	} 
	// 操作用户ID    
	public void setUserid(String userid) 
	{        
		this.USERID = userid;    
	}    
	public String getUserid() 
	{        
		return this.USERID;    
	}
	//操作状态
	public void setStatus(int status) 
	{        
		this.STATUS = status;    
	}    
	public int getStatus() 
	{        
		return this.STATUS;    
	}
	//操作主页
	public void setZhuye(int s) 
	{        
		this.ZHUYE = s;    
	}    
	public int getZhuye() 
	{        
		return this.ZHUYE;    
	}
	//操作别人的ID
	public void setOtherid(String s) 
	{        
		this.OTHERID = s;    
	}    
	public String getOtherid() 
	{        
		return this.OTHERID;    
	}
	/*
	//操作头像
	public void setBitmap(Bitmap bitmap) 
	{        
		this.BITMAP = bitmap;    
	}    
	public Bitmap getBitmap() 
	{        
		return this.BITMAP;    
	}
	
	//操作pk纪录的头像
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
	// 操作我的纪录
	public void setWode(String wode) 
	{        
		this.WODE = wode;    
	}    
	public String getWode() 
	{        
		return this.WODE;    
	}
	
	// 操作PK的判断
	public void setPK(String pk) 
	{        
		this.PK = pk;    
	}    
	public String getPK() 
	{        
		return this.PK;    
	}

	public Bitmap get_pic(String pic_url)  //通过URL获得图片的bitmap格式
    {
		Bitmap   bitmap = null;
    	try {
	    	URL picUrl = new URL(pic_url);  //picUrl  图片的url
			HttpURLConnection conn = (HttpURLConnection)picUrl.openConnection();
			//设置流的参数
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
	
	public Bitmap save_pic (String pic_url)  //通过URL获得图片的bitmap格式并保存到本地手机sd卡中
	{
		Bitmap   bitmap = null;
		try {
	    	URL picUrl = new URL(URL+pic_url);  //picUrl  图片的url
	    	
			HttpURLConnection conn = (HttpURLConnection)picUrl.openConnection();
			//设置流的参数
			conn.setConnectTimeout(6*1000); // 注意要设置超时，设置时间不要超过10秒，避免被android系统回收
        	if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
			
			InputStream inStream  = conn.getInputStream(); 
		    File rootsdcardFile  =  Environment.getExternalStorageDirectory();//获取sd卡根目录
        	File mytestFile = new File(rootsdcardFile, "校园吉尼斯");
        	mytestFile.mkdir();//在根目录下创建一个叫校园吉尼斯的文件夹
        	String wenjianming= System.currentTimeMillis()+".jpg";  
        	File myjpgFile = new  File(mytestFile, wenjianming);
        	myjpgFile.createNewFile();//在mytestFile文件夹下创建一个叫wenjianming的文件
        	
        	FileOutputStream fileOutputStream = new FileOutputStream(myjpgFile);//打开本地文件写入流
        	byte buffer[] = new byte[1000];
        	int length = 0;
        	while((length=inStream.read(buffer))>=0)
        	{
        		fileOutputStream.write(buffer, 0, length);
        	}
        	inStream.close();
        	fileOutputStream.close();
        	lujing=Environment.getExternalStorageDirectory()+"/校园吉尼斯/"+wenjianming;  //文件路径
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
	//获得本地图片的路径
	public String getLujing() 
	{        
		return this.lujing;    
	}
	//错误提示
	
	public void usetoast(String tixing )
	{
 		Toast textToast=Toast.makeText(this, tixing, Toast.LENGTH_LONG);
 	    textToast.show();
 	}
}

/*调用
whole a = (whole) getApplicationContext();
a.getUsername();
a.setUsername("");
*/





