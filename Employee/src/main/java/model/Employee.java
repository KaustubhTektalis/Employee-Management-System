package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Employee {
	private String id;
	private String name;
	private String mail;
	private String address;
	private String department;
	private List<String> role;
	private String password;

	public Employee(String id, String name, String mail, String address, String department, ArrayList<String> role,
			String password) {
		this.id = id;
		this.name = name;
		this.mail = mail;
		this.address = address;
		this.department = department;
		this.role = role;
		this.password = password;
	}

	public Employee() {
		this.role = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMail() {
		return mail;
	}

	public String getAddress() {
		return address;
	}

	public String getDepartment() {
		return department;
	}

	public List<String> getRole() {
		return Collections.unmodifiableList(role);
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void addRole(String role) {
		 if (role != null && !this.role.contains(role)) {
	            this.role.add(role);
	        }
	}
	
	public void removeRole(String role) {
		this.role.remove(role);
	}

	public void setPassword(String password) {
		this.password = password;
	}
	 
	public String toString() {
	    String details = "Details: Id: " + id
	                   + " Name: " + name
	                   + " Mail:" + mail
	                   + " Address:" + address
	                   + " Department:" + department;

	    if (role != null && !role.isEmpty()) {
	        details += " Role:" + role;
	    }

	    return details;
	}

}
