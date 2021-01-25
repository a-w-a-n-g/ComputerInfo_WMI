package com.awang.WMI.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**

* @author awang
* @version 1.0
* @date 2020年12月31日 下午3:36:14
* 
*/

@Entity
@Table(name = "computerLine")
public class ComputerInfo {

	private Integer id;
	private Computer computer;
	private String userName;		//电脑当前登录用户名
	private String IP;
	private String unit;
	private Integer total_c;			//C盘总容量
	private Integer free_c;		//C盘剩余容量
	private Integer total_d;
	private Integer free_d;
	private Integer total_e;
	private Integer free_e;
	private Integer total_f;
	private Integer free_f;
	private Integer total_g;
	private Integer free_g;
	private Integer total_h;
	private Integer free_h;
	private String otherDisk;
	private Float physicalSize;		//物理内存
	private Float virtualSize;			//虚拟内存
//	private Date createTime;		//获取信息的时间
	
	public ComputerInfo() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(optional = false, cascade = {CascadeType.MERGE,CascadeType.REFRESH}, targetEntity = Computer.class)
	@JoinColumn(name = "name")
	public Computer getComputer() {
		return computer;
	}

	public void setComputer(Computer computer) {
		this.computer = computer;
	}
	
	@Column(length = 20)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(length = 20)
	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	@Column(length = 5)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(nullable = false)
	public Integer getTotal_c() {
		return total_c;
	}

	public void setTotal_c(Integer total_c) {
		this.total_c = total_c;
	}

	@Column(nullable = false)
	public Integer getFree_c() {
		return free_c;
	}

	public void setFree_c(Integer free_c) {
		this.free_c = free_c;
	}

	@Column
	public Integer getTotal_d() {
		return total_d;
	}

	public void setTotal_d(Integer total_d) {
		this.total_d = total_d;
	}

	@Column
	public Integer getFree_d() {
		return free_d;
	}

	public void setFree_d(Integer free_d) {
		this.free_d = free_d;
	}

	@Column
	public Integer getTotal_e() {
		return total_e;
	}

	public void setTotal_e(Integer total_e) {
		this.total_e = total_e;
	}

	@Column
	public Integer getFree_e() {
		return free_e;
	}

	public void setFree_e(Integer free_e) {
		this.free_e = free_e;
	}

	@Column
	public Integer getTotal_f() {
		return total_f;
	}

	public void setTotal_f(Integer total_f) {
		this.total_f = total_f;
	}

	@Column
	public Integer getFree_f() {
		return free_f;
	}

	public void setFree_f(Integer free_f) {
		this.free_f = free_f;
	}

	@Column
	public Integer getTotal_g() {
		return total_g;
	}

	public void setTotal_g(Integer total_g) {
		this.total_g = total_g;
	}

	@Column
	public Integer getFree_g() {
		return free_g;
	}

	public void setFree_g(Integer free_g) {
		this.free_g = free_g;
	}

	@Column
	public Integer getTotal_h() {
		return total_h;
	}

	public void setTotal_h(Integer total_h) {
		this.total_h = total_h;
	}

	@Column
	public Integer getFree_h() {
		return free_h;
	}

	public void setFree_h(Integer free_h) {
		this.free_h = free_h;
	}

	@Column
	public String getOtherDisk() {
		return otherDisk;
	}

	public void setOtherDisk(String otherDisk) {
		this.otherDisk = otherDisk;
	}

	@Column(nullable = false)
	public Float getPhysicalSize() {
		return physicalSize;
	}
	
	public void setPhysicalSize(Float physicalSize) {
		this.physicalSize = physicalSize;
	}
	
	@Column(nullable = false)
	public Float getVirtualSize() {
		return virtualSize;
	}
	
	public void setVirtualSize(Float virtualSize) {
		this.virtualSize = virtualSize;
	}
	
//	@Column(name = "createTime", columnDefinition = "datetime")
//	public Date getCreateTime() {
//		return createTime;
//	}
//	
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}

}
