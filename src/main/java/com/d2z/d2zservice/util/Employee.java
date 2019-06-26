package com.d2z.d2zservice.util;

import java.time.LocalDate;
import java.util.Date;

public class Employee {
	
	Employee(int id,String name,Double Salary,LocalDate localDate)
	{
		this.id = id;
		this.name = name;
		this.salary = Salary;
		this.joiningDate = joiningDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public LocalDate getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String name;
	private Double salary;
	private LocalDate joiningDate;
	private int id;
	
	public String toString() { 
	    return "Name: '" + this.name + "', Salary: '" + this.salary + "', ID: '" + this.id + "'";
	} 
	

}
