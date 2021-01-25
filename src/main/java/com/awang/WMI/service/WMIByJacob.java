package com.awang.WMI.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.awang.WMI.entity.Computer;
import com.awang.WMI.entity.ComputerInfo;
import com.awang.hibernate.session.HibernateSessionFactory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;

/**

* @author awang
* @version 1.0
* @date 2020年12月14日 下午2:37:52
* 
*/

public class WMIByJacob {
	
	private String remoteComputer;
	private String userName = "administrator";
	private String passWord = "123456";
//	private String errorOutput = "error.txt";
	
	/**
	 * 该构造方法用于配置远程电脑信息
	 * @param remoteComputerNameOrIp 远程电脑名或者ip
	 */
	public WMIByJacob(String remoteComputerNameOrIp) {
		this.setRemoteComputer(remoteComputerNameOrIp);
	}
	
	/**
	 * 该构造方法用于配置远程电脑信息
	 * @param remoteComputerNameOrIp 远程电脑名或者ip
	 * @param userName  远程电脑的管理员用户名
	 * @param passWord  远程电脑的管理员密码
	 */
	public WMIByJacob(String remoteComputerNameOrIp, String userName, String passWord) {
		this.setRemoteComputer(remoteComputerNameOrIp);
		this.userName = userName;
		this.passWord = passWord;
	}
	
	/**
	 * 该构造方法对象可调用获取本地电脑信息方法，也可以构造后再配置并获取远程电脑
	 */
	public WMIByJacob() {
		
	}
	
	/**
	 * 进行获取远程电脑信息操作
	 * @return
	 */
	public Computer getRemoteComputerInfo() {
		if (this.remoteComputer == null || this.remoteComputer == "") {
			return null;
		}
//		DecimalFormat df = new DecimalFormat("########.00");
		float f;
		String info;
		
		Computer computer = new Computer();
		ComputerInfo computerInfo = new ComputerInfo();
		Set<ComputerInfo> infoSet = new HashSet<ComputerInfo>(); 
		
		Variant WMI_CMP_Name = this.getRemoteWMI("SELECT name,userName FROM Win32_ComputerSystem");
		info = this.getGenericInfo(WMI_CMP_Name, "name", "userName");
		if (info.contains(",")) {
			computer.setName(info.substring(0, info.indexOf(",")));
			computerInfo.setUserName(info.substring(info.indexOf(",") + 1, info.length()));			
		} else {
			computer.setName(info);
		}
		
		Variant WMI_Disk = this.getRemoteWMI("SELECT deviceid,size,freeSpace FROM Win32_LogicalDisk WHERE drivetype = 3");
		computerInfo = this.getDisk(computerInfo, WMI_Disk, "deviceid", "size", "freeSpace");
        
        Variant WMI_CPU = this.getRemoteWMI("SELECT name FROM Win32_Processor");
        info = this.getGenericInfo(WMI_CPU, "name");
        computer.setCPUName(info);
        
        Variant WMI_IP = this.getRemoteWMI("SELECT IPAddress FROM Win32_NetworkAdapterConfiguration");
        String ip = this.getGenericInfo(WMI_IP, "IPAddress");
        info = ip.substring(1, ip.lastIndexOf(" "));
        computerInfo.setIP(info);
        
        Variant WMI_PHYSICAL = this.getRemoteWMI("SELECT TotalPhysicalMemory FROM Win32_ComputerSystem");
        f = Float.parseFloat(this.getGenericInfo(WMI_PHYSICAL, "TotalPhysicalMemory"))/1024/1024/1024;
        computerInfo.setUnit("GB");
        computerInfo.setPhysicalSize(f);   //除两次1024后为MB
        
        Variant WMI_VIRTUAL = this.getRemoteWMI("SELECT TotalVirtualMemorySize FROM Win32_OperatingSystem");
        f = Float.parseFloat(this.getGenericInfo(WMI_VIRTUAL, "TotalVirtualMemorySize"))/1024/1024;
        computerInfo.setVirtualSize(f);  //除一次1024后为MB
        
//        Variant WMI_Printer = this.getRemoteWMI("SELECT name FROM Win32_Printer WHERE default = true");
//        info = this.getGenericInfo(WMI_Printer, "name");
//        computerInfo.setPrinter(info);
        
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        computerInfo.setCreateTime(new Date(System.currentTimeMillis()));
        computerInfo.setComputer(computer);
        infoSet.add(computerInfo);
        computer.setComputerInfo(infoSet);
        
        return computer;
	}
	
	/**
	 * 进行获取本地电脑信息操作
	 * @return
	 */
	public Computer getLocalComputerInfo() {
//		DecimalFormat df = new DecimalFormat("########.00");
		float f;
		String info;
		Computer computer = new Computer();
		ComputerInfo computerInfo = new ComputerInfo();
		Set<ComputerInfo> infoSet = new HashSet<ComputerInfo>();

		//userName获取只可以是电脑本地登录的用户，如果是远程连接登录的用户，则获取不到。
		Variant WMI_CMP_Name = this.getLocalWMI("SELECT name,userName FROM Win32_ComputerSystem");
		info = this.getGenericInfo(WMI_CMP_Name, "name", "userName");
		if (info.contains(",")) {
			computer.setName(info.substring(0, info.indexOf(",")));
			computerInfo.setUserName(info.substring(info.indexOf(",") + 1, info.length()));			
		} else {
			computer.setName(info);
		}
		
		Variant WMI_Disk = this.getLocalWMI("SELECT deviceid,size,freeSpace FROM Win32_LogicalDisk WHERE drivetype = 3");
		computerInfo = this.getDisk(computerInfo, WMI_Disk, "deviceid", "size", "freeSpace");
        
        Variant WMI_CPU = this.getLocalWMI("SELECT name FROM Win32_Processor");
        info = this.getGenericInfo(WMI_CPU, "name");
        computer.setCPUName(info);
        
        Variant WMI_IP = this.getLocalWMI("SELECT IPAddress FROM Win32_NetworkAdapterConfiguration");
        String ip = this.getGenericInfo(WMI_IP, "IPAddress");
        info = ip.substring(1, ip.lastIndexOf(" "));
        computerInfo.setIP(info);
        
        Variant WMI_PHYSICAL = this.getLocalWMI("SELECT TotalPhysicalMemory FROM Win32_ComputerSystem");
        f = Float.parseFloat(this.getGenericInfo(WMI_PHYSICAL, "TotalPhysicalMemory"))/1024/1024/1024;
        computerInfo.setPhysicalSize(f);   //除两次1024后为MB
        
        Variant WMI_VIRTUAL = this.getLocalWMI("SELECT TotalVirtualMemorySize FROM Win32_OperatingSystem");
        f = Float.parseFloat(this.getGenericInfo(WMI_VIRTUAL, "TotalVirtualMemorySize"))/1024/1024;
        computerInfo.setVirtualSize(f);  //除一次1024后为MB
        
//        Variant WMI_Printer = this.getLocalWMI("SELECT name FROM Win32_Printer WHERE default = true");
//        info = this.getGenericInfo(WMI_Printer, "name");
//        computerInfo.setPrinter(info);
        
        computerInfo.setComputer(computer);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        computerInfo.setCreateTime(new Date(System.currentTimeMillis()));
        infoSet.add(computerInfo);
        computer.setComputerInfo(infoSet);
        
        return computer;
	}
	
	/**
	 * 设置所要查询的远程电脑
	 * @param nameOrIP 远程电脑的电脑名或者IP
	 */
	public void setRemoteComputer(String nameOrIP) {
		this.remoteComputer = nameOrIP;
	}

	/**
	 * 获取硬盘信息
	 * @param vCollection  WQL语句
	 * @param properties   所要获取的属性名
	 */
    private ComputerInfo getDisk(ComputerInfo info, Variant vCollection, String... properties) {
        EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
        while (enumVariant.hasMoreElements()) {
            Dispatch item = enumVariant.nextElement().toDispatch();
            String devided = "";
            int index = 0;
            for (String property : properties) {
                Variant variant = Dispatch.call(item, property);
//                获取完毕
                if (variant.isNull()) {
                	break;
                }
                if (index == 0) {		//第一个属性值为硬盘名
                	devided = variant.getString();
                }
                if (index == 1) {		//第二个属性值为总容量
                	Integer total = Integer.valueOf(KB2GB(variant.getString()));
                	switch (devided) {
                	case "C:":
                		info.setTotal_c(total);
                		break;
                	case "D:":
                		info.setTotal_d(total);
                		break;
                	case "E:":
                		info.setTotal_e(total);
                		break;
                	case "F:":
                		info.setTotal_f(total);
                		break;
                	case "G:":
                		info.setTotal_g(total);
                		break;
                	case "H:":
                		info.setTotal_h(total);
                		break;
                	default:
                		String other = info.getOtherDisk();
                		if (other == null) {
                			info.setOtherDisk(devided + "," +KB2GB(variant.getString()));
                		} else {
                			info.setOtherDisk(other + "," + devided.substring(0, 1) + "," + KB2GB(variant.getString()));
                		}
                	}
                }
                if (index == 2) {		//第三个属性值为剩余容量
                	Integer remain = Integer.valueOf(KB2GB(variant.getString()));
                	switch (devided) {
                	case "C:":
                		info.setFree_c(remain);
                		break;
                	case "D:":
                		info.setFree_d(remain);
                		break;
                	case "E:":
                		info.setFree_e(remain);
                		break;
                	case "F:":
                		info.setFree_f(remain);
                		break;
                	case "G:":
                		info.setFree_g(remain);
                		break;
                	case "H:":
                		info.setFree_h(remain);
                		break;
                	default:
                		String other = info.getOtherDisk();
                		info.setOtherDisk(other + "," + KB2GB(variant.getString()));
                	}
                }
                index++;
            }
        }
        return info;
    }
    
    private static String KB2GB(String kb) {
    	return Long.toString((Long.valueOf(kb)/1024/1024/1024));
    }
    
    /**
     * 获取普通的电脑信息，所获取信息只需要通过普通处理
     * @param vCollection  WQL语句
	 * @param properties   所要获取的属性名
     */
    private String getGenericInfo(Variant vCollection, String... properties) {
    	EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
    	String info = "";
    	while (enumVariant.hasMoreElements()) {
    		if (!enumVariant.hasMoreElements()) {
    			return info;
    		}
    		Dispatch item = enumVariant.nextElement().toDispatch();
    		for (String property : properties) {
    			Variant variant = Dispatch.call(item, property);
    			if (variant.isNull()) {
    				break;
    			}
    			String var = variant.toString();
    			Pattern p = Pattern.compile("\\s+");
    			Matcher m = p.matcher(var);
    			var = m.replaceAll(" ");
    			info = info + var;
    			info = info + ",";
    		}
    	}
    	return info.substring(0, info.length() - 1);
    }

    /**
     * 配置所需查询远程计算机、WMI查询语句
     * @param query
     * @param param
     * @return
     */
    private Variant getRemoteWMI(String query, Object... param) {
        ActiveXComponent wmi = new ActiveXComponent("WbemScripting.SWbemLocator");
        Variant variantParameters[] = new Variant[4];
	    variantParameters[0] = new Variant(remoteComputer);
	    variantParameters[1] = new Variant("root\\CIMV2");
	    variantParameters[2] = new Variant(userName);
	    variantParameters[3] = new Variant(passWord);
	    Dispatch services = wmi.invoke("ConnectServer", variantParameters).toDispatch();
	    wmi = new ActiveXComponent(services);
        Variant[] vs = new Variant[param.length + 1];
        vs[0] = new Variant(query);
        for (int i = 0; i < param.length; i++) {
            vs[i + 1] = new Variant(param[i]);
        }
        Variant vCollection = wmi.invoke("ExecQuery", vs);
        return vCollection;
    }
    
    /**
     * 配置本地计算机WMI查询语句
     * @param query
     * @param param
     * @return
     */
    private Variant getLocalWMI(String query, Object... param) {
    	String host = "localhost";
    	String connectStr = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
    	ActiveXComponent wmi = new ActiveXComponent(connectStr);
    	Variant[] vs = new Variant[param.length + 1];
    	vs[0] = new Variant(query);
    	for (int i = 0; i < param.length; i++) {
    		vs[i + 1] = new Variant(param[i]);
    	}
    	Variant vCollection = wmi.invoke("ExecQuery", vs);
    	return vCollection;
	}
    
    public boolean getComputerInfo() {
		//获取信息
    	Session session = HibernateSessionFactory.getSession();
    	Computer c = new Computer();
    	if (this.remoteComputer.equals("local")) {
    		c = this.getLocalComputerInfo();
    	} else {
    		c = this.getRemoteComputerInfo();
    	}
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Computer> createQuery = builder.createQuery(Computer.class);
		Root<Computer> root = createQuery.from(Computer.class);
		Predicate name = builder.equal(root.get("name"), c.getName());
		createQuery.where(name);
		List<Computer> list = session.createQuery(createQuery).list();
		Iterator<ComputerInfo> iter = c.getComputerInfo().iterator();
		ComputerInfo info = new ComputerInfo();
		if (list.size() > 0) {
			info = iter.next();
			info.setComputer(list.get(0));
		}
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			if (list.size() > 0) {
				session.save(info);
			} else {
				session.save(c);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			//若此处输出到文件，那么调用该方法的类有错误输出到文件时，就会把此处的错误信息覆盖掉。
			//而直接print，则会和调用该方法的类的错误一起输出到文件中。
//			try {
//		    	//错误输出到文件中
//				File file = new File(errorOutput);
//				if(!file.exists()){
//					file.createNewFile();
//				}
//				OutputStream fos  = new FileOutputStream(errorOutput);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
//				String str = sdf.format(new Date());
//				fos.write((str + "：" + e.toString()).getBytes());
//				fos.flush();
//				fos.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//				return false;
//			}
		} finally {
			HibernateSessionFactory.closeSession(); // 关闭Session
		}
    	return true;
    }
    
//	public static void main(String[] args) {
//		WMIByJacob wmi = new WMIByJacob("192.168.1.15", "leo\\administrator", "Erp20200307");
////		WMIByJacob wmi = new WMIByJacob();
//		wmi.getComputerInfo();
//    }

}

