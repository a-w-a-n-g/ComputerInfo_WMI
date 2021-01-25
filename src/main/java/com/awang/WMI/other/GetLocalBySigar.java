package com.awang.WMI.other;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/**

* @author awang
* @version 1.0
* @date 2020年12月14日 上午11:06:42
* 
*/

public class GetLocalBySigar {

	public static void main(String[] args) {
		try {
		// System信息，从jvm获取
		property();
		System.out.println("----------------------------------");
		// cpu信息
		cpu();
		System.out.println("----------------------------------");
		// 内存信息
		memory();
		System.out.println("----------------------------------");
		// 操作系统信息
		os();
		System.out.println("----------------------------------");
		// 用户信息
//		who();
		System.out.println("----------------------------------");
		// 文件系统信息
//		file();
		System.out.println("----------------------------------");
		// 网络信息
//		net();
		System.out.println("----------------------------------");
		// 以太网信息
//		ethernet();
		System.out.println("----------------------------------");
		} catch (Exception e1) {
		e1.printStackTrace();
		}
		}

		private static void property() throws UnknownHostException {
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		InetAddress addr;
		addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress();
		Map<String, String> map = System.getenv();
		String userName = map.get("USERNAME");// 获取用户名
		String computerName = map.get("COMPUTERNAME");// 获取计算机名
		String userDomain = map.get("USERDOMAIN");// 获取计算机域名
		System.out.println("用户名: " + userName);
		System.out.println("计算机名: " + computerName);
		System.out.println("计算机域名: " + userDomain);
		System.out.println("本地ip地址: " + ip);
		System.out.println("本地主机名: " + addr.getHostName());
		System.out.println("JVM可以使用的总内存: " + r.totalMemory());
		System.out.println("JVM可以使用的剩余内存: " + r.freeMemory());
		System.out.println("JVM可以使用的处理器个数: " + r.availableProcessors());
		System.out.println("Java的运行环境版本： " + props.getProperty("java.version"));
		System.out.println("Java的运行环境供应商： " + props.getProperty("java.vendor"));
		System.out.println("Java供应商的URL： " + props.getProperty("java.vendor.url"));
		System.out.println("Java的安装路径： " + props.getProperty("java.home"));
		System.out.println("Java的虚拟机规范版本： " + props.getProperty("java.vm.specification.version"));
		System.out.println("Java的虚拟机规范供应商： " + props.getProperty("java.vm.specification.vendor"));
		System.out.println("Java的虚拟机规范名称： " + props.getProperty("java.vm.specification.name"));
		System.out.println("Java的虚拟机实现版本： " + props.getProperty("java.vm.version"));
		System.out.println("Java的虚拟机实现供应商： " + props.getProperty("java.vm.vendor"));
		System.out.println("Java的虚拟机实现名称： " + props.getProperty("java.vm.name"));
		System.out.println("Java运行时环境规范版本： " + props.getProperty("java.specification.version"));
		System.out.println("Java运行时环境规范供应商： " + props.getProperty("java.specification.vender"));
		System.out.println("Java运行时环境规范名称： " + props.getProperty("java.specification.name"));
		System.out.println("Java的类格式版本号： " + props.getProperty("java.class.version"));
		System.out.println("Java的类路径： " + props.getProperty("java.class.path"));
		System.out.println("加载库时搜索的路径列表： " + props.getProperty("java.library.path"));
		System.out.println("默认的临时文件路径： " + props.getProperty("java.io.tmpdir"));
		System.out.println("一个或多个扩展目录的路径： " + props.getProperty("java.ext.dirs"));
		System.out.println("操作系统的名称： " + props.getProperty("os.name"));
		System.out.println("操作系统的构架： " + props.getProperty("os.arch"));
		System.out.println("操作系统的版本： " + props.getProperty("os.version"));
		System.out.println("文件分隔符： " + props.getProperty("file.separator"));
		System.out.println("路径分隔符： " + props.getProperty("path.separator"));
		System.out.println("行分隔符： " + props.getProperty("line.separator"));
		System.out.println("用户的账户名称： " + props.getProperty("user.name"));
		System.out.println("用户的主目录： " + props.getProperty("user.home"));
		System.out.println("用户的当前工作目录： " + props.getProperty("user.dir"));
		}

		private static void memory() throws SigarException {
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		// 内存总量
		System.out.println("内存总量: " + mem.getTotal() / 1024L + "K av");
		// 当前内存使用量
		System.out.println("当前内存使用量: " + mem.getUsed() / 1024L + "K used");
		// 当前内存剩余量
		System.out.println("当前内存剩余量: " + mem.getFree() / 1024L + "K free");
		Swap swap = sigar.getSwap();
		// 交换区总量
		System.out.println("交换区总量: " + swap.getTotal() / 1024L + "K av");
		// 当前交换区使用量
		System.out.println("当前交换区使用量: " + swap.getUsed() / 1024L + "K used");
		// 当前交换区剩余量
		System.out.println("当前交换区剩余量: " + swap.getFree() / 1024L + "K free");
		}

		private static void cpu() throws SigarException {
		Sigar sigar = new Sigar();
		CpuInfo infos[] = sigar.getCpuInfoList();
		CpuPerc cpuList[] = null;
		cpuList = sigar.getCpuPercList();
		for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
		CpuInfo info = infos[i];
		System.out.println("第" + (i + 1) + "块CPU信息");
		System.out.println("CPU的总量MHz: " + info.getMhz());// CPU的总量MHz
		System.out.println("CPU生产商: " + info.getVendor());// 获得CPU的卖主，如：Intel
		System.out.println("CPU类别: " + info.getModel());// 获得CPU的类别，如：Celeron
		System.out.println("CPU缓存数量: " + info.getCacheSize());// 缓冲存储器数量
		printCpuPerc(cpuList[i]);
		}
		}

		private static void printCpuPerc(CpuPerc cpu) {
		System.out.println("CPU用户使用率: " + CpuPerc.format(cpu.getUser()));// 用户使用率
		System.out.println("CPU系统使用率: " + CpuPerc.format(cpu.getSys()));// 系统使用率
		System.out.println("CPU当前等待率: " + CpuPerc.format(cpu.getWait()));// 当前等待率
		System.out.println("CPU当前错误率: " + CpuPerc.format(cpu.getNice()));//
		System.out.println("CPU当前空闲率: " + CpuPerc.format(cpu.getIdle()));// 当前空闲率
		System.out.println("CPU总的使用率: " + CpuPerc.format(cpu.getCombined()));// 总的使用率
		}

		private static void os() {
		OperatingSystem OS = OperatingSystem.getInstance();
		// 操作系统内核类型如： 386、486、586等x86
		System.out.println("操作系统: " + OS.getArch());
		System.out.println("操作系统CpuEndian(): " + OS.getCpuEndian());//
		System.out.println("操作系统DataModel(): " + OS.getDataModel());//
		// 系统描述
		System.out.println("操作系统的描述: " + OS.getDescription());
		// 操作系统类型
		// System.out.println("OS.getName(): " + OS.getName());
		// System.out.println("OS.getPatchLevel(): " + OS.getPatchLevel());//
		// 操作系统的卖主
		System.out.println("操作系统的卖主: " + OS.getVendor());
		// 卖主名称
		System.out.println("操作系统的卖主名: " + OS.getVendorCodeName());
		// 操作系统名称
		System.out.println("操作系统名称: " + OS.getVendorName());
		// 操作系统卖主类型
		System.out.println("操作系统卖主类型: " + OS.getVendorVersion());
		// 操作系统的版本号
		System.out.println("操作系统的版本号: " + OS.getVersion());
		}
}
