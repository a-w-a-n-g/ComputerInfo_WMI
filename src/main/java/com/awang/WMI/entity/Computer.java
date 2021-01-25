package com.awang.WMI.entity;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**

* @author awang
* @version 1.0
* @date 2020年11月17日 上午11:30:46
* 
*/

@Entity
@Table(name = "computerTable")
public class Computer {

	private String name;
	private String CPUName;
	private Set<ComputerInfo> computerInfo;
	
	
	public Computer() {
		
	}
	
	public Computer(String computerName, String CPUName) {
		this.name = computerName;
		this.CPUName = CPUName;
	}
	
	@Id
	@Column(length = 20, unique = true, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CPU", length = 50, nullable = false)
	public String getCPUName() {
		return CPUName;
	}
	
	public void setCPUName(String cPUName) {
		CPUName = cPUName;
	}
	
	@OneToMany(mappedBy = "computer", cascade = CascadeType.ALL)
	public Set<ComputerInfo> getComputerInfo() {
		return computerInfo;
	}
	
	public void setComputerInfo(Set<ComputerInfo> computerInfo) {
		this.computerInfo = computerInfo;
	}
	
}
