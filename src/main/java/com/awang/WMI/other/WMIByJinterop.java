package com.awang.WMI.other;

import java.util.logging.Level;

import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import org.jinterop.dcom.impls.automation.IJIEnumVariant;
import org.jinterop.dcom.core.JIProgId;

public class WMIByJinterop 
{
	private static String domainName = "leo";
    private static String userName = "administrator";
    private static String password = "Erp20200307";
    private static String hostIP = "192.168.1.9";
    private static final String win32_namespace = "\\ROOT\\CIMV2";
    private static final String WMI_CLSID = "76A6415B-CB41-11d1-8B02-00600806D9B6";
    private static final String WMI_PROGID = "WbemScripting.SWbemLocator";
//    private static final int STOP_SERVICE = 0;
//    private static final int START_SERVICE = 1;
//
//    private JISession session = null;
//    private JIComServer comServer = null;
//    private IJIComObject comObject = null;
//    private IJIDispatch dispatch = null;
    
    public static void main( String[] args )
    {
    	//Connect
    	JISystem.setAutoRegisteration(true);
    	JISystem.getLogger().setLevel(Level.OFF);
    	JISession session = JISession.createSession(domainName, userName, password);
    	try {
    		
	    	session.useSessionSecurity(true);
	    	session.setGlobalSocketTimeout(5000);
	    	JIComServer comServer = new JIComServer(JIProgId.valueOf(WMI_PROGID), hostIP, session);
	    	IJIComObject unknown = comServer.createInstance();
	    	IJIComObject comObject = unknown.queryInterface(WMI_CLSID);
	    	IJIDispatch dispatch = (IJIDispatch)JIObjectFactory.narrowObject(comObject.queryInterface(IJIDispatch.IID));
	    	Object[] params = new Object[]{
	    		new JIString(hostIP),//strServer
	    		new JIString(win32_namespace),//strNamespace
	    		JIVariant.OPTIONAL_PARAM(),//strUser
	   			JIVariant.OPTIONAL_PARAM(),//strPassword
	   			JIVariant.OPTIONAL_PARAM(),//strLocale
	   			JIVariant.OPTIONAL_PARAM(),//strAuthority
	   			new Integer(0),//iSecurityFlags
	   			JIVariant.OPTIONAL_PARAM()//objwbemNamedValueSet
	    	};
	    	IJIDispatch wbemServices = (IJIDispatch)JIObjectFactory.narrowObject(dispatch.callMethodA("ConnectServer", params)[0].getObjectAsComObject());
	    	
	    	//Query
	    	//系统信息
	    	String strQuery = "SELECT * FROM Win32_ComputerSystem";
	    	//CPU信息
//   	 	String strQuery = "SELECT * FROM Win32_PerfFormattedData_PerfOS_Processor WHERE Name != '_Total'";
	    	//内存信息
//  	  	String strQuery = "SELECT * FROM Win32_PerfFormattedData_PerfOS_Memory";
	    	//磁盘信息
//   	 	String strQuery = "SELECT * FROM Win32_PerfRawData_PerfDisk_PhysicalDisk Where Name != '_Total'";
	    	JIVariant results[] = new JIVariant[0];
	    	results = dispatch.callMethodA("ExecQuery", new Object[]{new JIString(strQuery), JIVariant.OPTIONAL_PARAM(), JIVariant.OPTIONAL_PARAM(), JIVariant.OPTIONAL_PARAM()});
	    	IJIDispatch wOSd = (IJIDispatch) JIObjectFactory.narrowObject((results[0]).getObjectAsComObject());
	    	int count = wOSd.get("Count").getObjectAsInt();
	    	IJIComObject enumComObject = wOSd.get("_NewEnum").getObjectAsComObject();
	    	IJIEnumVariant enumVariant = (IJIEnumVariant) JIObjectFactory.narrowObject(enumComObject.queryInterface(IJIEnumVariant.IID));
	    	IJIDispatch wbemObject_dispatch = null;
	    	for (int c = 0; c < count; c++) {
	    		Object[] values = enumVariant.next(1);
	    		JIArray array = (JIArray) values[0];
	    		Object[] arrayObj = (Object[]) array.getArrayInstance();
	    		for (int j = 0; j < arrayObj.length; j++) {
	    			wbemObject_dispatch = (IJIDispatch) JIObjectFactory.narrowObject(((JIVariant) arrayObj[j]).getObjectAsComObject());
	    		}
	    		String str = (wbemObject_dispatch.callMethodA("GetObjectText_", new Object[]{1}))[0].getObjectAsString2();
	    		System.out.println("(" + c + "):");
	    		System.out.println(str);
	    		System.out.println();
	    	}
	    	
	    	//disconnect
	    	JISession.destroySession(session);
    	
    	} catch(Exception e) {
    		e.printStackTrace();
    		if(session != null) {
    			try {
    				JISession.destroySession(session);
    			} catch (Exception e2) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
//    	
//    	try {
//    		//parameterstoconnecttoWbemScripting.SWbemLocator
//    		System.out.println(params[0]);
//    		
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}
    	
    }
}
