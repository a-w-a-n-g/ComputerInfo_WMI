package com.awang.WMI.other;

import org.jinterop.dcom.common.IJIAuthInfo;
import org.jinterop.dcom.common.JIDefaultAuthInfoImpl;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JIProgId;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;
import org.jinterop.dcom.impls.automation.IJIEnumVariant;

/**

* @author awang
* @version 1.0
* @date 2020年12月15日 下午5:42:53
* 
*/

public class WMIByJinterop2 {

	private static String domainName = "leo";
    private static String userName = "administrator";
    private static String password = "Erp20200307";
    private static String hostIP = "it-005";
    private static final String win32_namespace = "ROOT\\CIMV2";
    
    public static void main(String[] args) {
    	IJIAuthInfo authInfo = new JIDefaultAuthInfoImpl("192.168.1.9", "", "");
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Closing registry connection");
        } 
    	JISession session = JISession.createSession(domainName, userName, password);
    	try {
    		//Connect
    		session.useSessionSecurity(false);
    		JIComServer comServer = new JIComServer(JIProgId.valueOf("WbemScripting.SWbemLocator"), hostIP, session);
    		IJIDispatch dispatch = (IJIDispatch)JIObjectFactory.narrowObject(comServer.createInstance().queryInterface(IJIDispatch.IID));
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
    		JIVariant results[] = dispatch.callMethodA("ConnectServer",params);
    		IJIDispatch wbemServices = (IJIDispatch)JIObjectFactory.narrowObject(results[0].getObjectAsComObject());
    		
    		//Query
    		final int RETURN_IMMEDIATE = 0x10;
    		final int FORWARD_ONLY = 0x20;
    		Object[] params2 = new Object[] {
    			new JIString("SELECT*FROMWin32_Service"),
    			JIVariant.OPTIONAL_PARAM(),
    			new JIVariant(new Integer(RETURN_IMMEDIATE+FORWARD_ONLY))
    			};
    		JIVariant[] servicesSet = wbemServices.callMethodA("ExecQuery",params2);
    		IJIDispatch wbemObjectSet = (IJIDispatch)JIObjectFactory.narrowObject(servicesSet[0].getObjectAsComObject());
    		
    		//execute
    		JIVariant newEnumvariant = wbemObjectSet.get("_NewEnum");
    		IJIComObject enumComObject = newEnumvariant.getObjectAsComObject();
    		IJIEnumVariant enumVariant = (IJIEnumVariant)JIObjectFactory.narrowObject(enumComObject.queryInterface(IJIEnumVariant.IID));
    		
    		IJIDispatch wOSd = (IJIDispatch) JIObjectFactory.narrowObject((results[0]).getObjectAsComObject());
	    	int count = wOSd.get("Count").getObjectAsInt();
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
//    		Object[] elements = enumVariant.next(1);
//    		JIArray aJIArray = (JIArray) elements[0];
//    		JIVariant[] array = (JIVariant[]) aJIArray.getArrayInstance();
//    		for (JIVariant variant : array) {
//    		    IJIDispatch wbemObjectDispatch = (IJIDispatch)JIObjectFactory.narrowObject(variant.getObjectAsComObject());
//    		    JIVariant returnStatus = wbemObjectDispatch.callMethodA("StopService");
//    		    System.out.println(returnStatus.getObjectAsInt());
//    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		if(session != null) {
    			try {
    				JISession.destroySession(session);
    			} catch (Exception e2) {
    				e.printStackTrace();
    			}
    		}
    	}
	}
}
