package com.xgg.blesdk;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class BleState {
	//构造函数
	private BleState() {
		
	}
	
	//单例
	private static BleState single=null;  
	
	//静态工厂方法   
    public static BleState getInstance() {  
         if (single == null) {    
             single = new BleState();  
         }    
        return single;  
    }  
	
    //打印日志
	boolean m_log = true;
	public void setLogEnabled(boolean enabled)
	{
		if (enabled == true)
			Log.d("blesdk", "zh打开调试日志");
		else
			Log.d("blesdk", "zh关闭调试日志");
		m_log = enabled;
	}
	public boolean getLogEnabled()
	{
		return m_log;
	}	
	void Log(String content)
	{
		if (getLogEnabled())
		{
			Log.d("blesdk", content);
		}
	}
	
	boolean m_showprogressbar = true;
	public void setShowProgress(boolean show)
	{
		m_showprogressbar = show;
	}
	public boolean getShowProgress()
	{
		return m_showprogressbar;
	}
	
	//检查本设备是否支持蓝牙4.0
	public boolean IsBleEnabled(Context context)
	{
		
			
			if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
				Log("zh本设备不支持蓝牙4.0");
				return false;
			}
			else
			{
				Log("zh本设备支持蓝牙4.0");
				return true;
			}
		
		
	}
	
	
}
