package com.example.testbatch.domain.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
 

@Entity
@Table(name = "person", catalog = "batch-test")
public class Person {
 
    @Id
    @GeneratedValue
    private int id;
 
    @Transient
    
    private String lastName;
 
    @Transient
    
    private String firstName;
 
    
    private String name;
    
    public Person() {
	}
 
    public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void concatenateName(){
        this.setName(this.firstName+" "+this.lastName);
    }



	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + "]";
	}
	
}