package com.awang.WMI.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**

* @author awang
* @version 1.0
* @date 2020年12月14日 下午1:45:36
* 
*/

/**
 * 需要安装ssh服务器
 * @author awang
 *
 */
public class GetRemoteBySSH {

	private String HOST_NAME = "192.168.1.9"; //IP地址
//	private int PORT = 22; //端口号
	private String LOGIN_NAME = "leo\\administrator"; //登录账号
	private String PASS_WORD = "Erp20200307"; //登录密码
	
	private String CMD_WINDOWS="E:"; 
		//windows命令
 
	private long totalSpace = 0;
	private	long freeSpace = 0;
	private	long usedSpace = 0;
	
	/**
	 * 获取磁盘信息
	 * 
	 * @return
	 */
	public Map<String, String> getDiskInfo() {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = new Connection(HOST_NAME);
		Session ssh = null;
 
		try {
			conn.connect();
			boolean flag = conn.authenticateWithPassword(LOGIN_NAME, PASS_WORD);
			if (!flag) {
				System.out.println("用户名或密码错误");
			} else {
				System.out.println("连接成功");
				ssh = conn.openSession();
					//windows
				map = this.getDiskByWindows(ssh,map);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ssh != null) {
					ssh.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
		return map;
	}
 
	/**
	 * 用于windows
	 * @param map
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> getDiskByWindows(Session ssh,Map<String, String> map) throws IOException{
		ssh.execCommand("wmic LogicalDisk where \"Caption='"+
					CMD_WINDOWS+"'\" get FreeSpace,Size /value");
		InputStream stdout = new StreamGobbler(ssh.getStdout());
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		String len = null;
		while((len = br.readLine()) != null){
			if(len.startsWith("FreeSpace")){
				String[] str = len.split("=");
				freeSpace = Long.parseLong(str[1])/ 1024 / 1024 / 1024;
			}
			if(len.startsWith("Size")){
				String[] str = len.split("=");
				totalSpace = Long.parseLong(str[1])/ 1024 / 1024 / 1024;
			}
		}
		usedSpace = totalSpace - freeSpace;
		System.out.println("总空间大小 : " + totalSpace + "G");
		System.out.println("剩余空间大小 : " + freeSpace + "G");
		System.out.println("已用空间大小 : " + usedSpace + "G");
		map.put("total", new Long(totalSpace).toString());
		map.put("free", new Long(freeSpace).toString());
		map.put("used", new Long(usedSpace).toString());
		
		br.close();
		return map;
	}
	
	public static void main(String[] args) {
		GetRemoteBySSH ssh = new GetRemoteBySSH();
		ssh.getDiskInfo();
	}

}
