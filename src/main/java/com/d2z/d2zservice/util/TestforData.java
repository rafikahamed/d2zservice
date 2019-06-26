package com.d2z.d2zservice.util;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.jfree.util.HashNMap;


public class TestforData {
    public static void main(String[] args) {
    	HashMap<String, Employee> e = new HashMap<String, Employee>();
    	
    	
        List<Employee> employees = new ArrayList<>();
        Employee e1 = new Employee(1010, "Rajeev", 100000.00, LocalDate.of(2010, 7, 10));
        
        Employee e2 = new Employee(1004, "Chris", 95000.50, LocalDate.of(2017, 3, 19));
       
        e.put("Raja", e2);
        e.put("Abhi", e1);
        TreeMap<String, Employee> sorte = new TreeMap<String, Employee>(e);
        
        
        
        e.forEach( (k, v) -> System.out.println((k + ":" + v)));
      sorte.forEach( (k, v) -> System.out.println((k + ":" + v)));
      
      ArrayList<Employee> sortedKeys = 
              new ArrayList<Employee>(e.values()); 
      
      Collections.sort(sortedKeys, Comparator.comparing(Employee::getName));
      System.out.println("\nEmployees (Sorted by Name) : " + sortedKeys.toString());
        
        employees.add(new Employee(1010, "Rajeev", 100000.00, LocalDate.of(2010, 7, 10)));
        employees.add(new Employee(1004, "Chris", 95000.50, LocalDate.of(2017, 3, 19)));
        employees.add(new Employee(1015, "David", 134000.00, LocalDate.of(2017, 9, 28)));
        employees.add(new Employee(1009, "Steve", 100000.00, LocalDate.of(2016, 5, 18)));

        System.out.println("Employees : " + employees);

        // Sort employees by Name
        Collections.sort(employees, Comparator.comparing(Employee::getName));
        System.out.println("\nEmployees (Sorted by Name) : " + employees.toString());

        // Sort employees by Salary
        Collections.sort(employees, Comparator.comparingDouble(Employee::getSalary));
        System.out.println("\nEmployees (Sorted by Salary) : " + employees.toString());

        // Sort employees by JoiningDate
     /*   Collections.sort(employees, Comparator.comparing(Employee::getJoiningDate));
        System.out.println("\nEmployees (Sorted by JoiningDate) : " + employees);*/

        // Sort employees by Name in descending order
        Collections.sort(employees, Comparator.comparing(Employee::getName).reversed());
        System.out.println("\nEmployees (Sorted by Name in descending order) : " + employees);

        // Chaining multiple Comparators
        // Sort by Salary. If Salary is same then sort by Name
        Collections.sort(employees, Comparator.comparingDouble(Employee::getSalary).thenComparing(Employee::getName));
        System.out.println("\nEmployees (Sorted by Salary and Name) : " + employees);
    }
}
