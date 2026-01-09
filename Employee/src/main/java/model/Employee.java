package model;

import java.util.ArrayList;

public class Employee {
	private String id;
	private String name;
	private String mail;
	private String address;
	private String department;
	private ArrayList<String> role;
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

	public ArrayList<String> getRole() {
		return role;
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

	public void setRole(ArrayList<String> role) {
		this.role = (role != null) ? new ArrayList<>(role) : new ArrayList<>();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "Details: Id: " + id + " Name: " + name + " Mail:" + mail + " Address:" + address + " Department:"
				+ department + " Role:" + role;
	}
}
