/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author thangphan
 */
public class User {
	private int id;
	private String name;
	private String phoneNumber;
	private int pointsEarned = 0;
	
	public User(int id, String name, String phoneNumber, int pointsEarned) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.pointsEarned = pointsEarned;
	}
	
	public User(String name, String phoneNumber, int pointsEarned) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.pointsEarned = pointsEarned;
	}
	
	public User(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	public int getId() {
            return id;
	}
	
	public void setId(int id) {
            this.id = id;
	}
	
	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getPhoneNumber() {
	    return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
	    this.phoneNumber = phoneNumber;
	}

	public int getPointsEarned() {
	    return pointsEarned;
	}

	public void setPointsEarned(int pointsEarned) {
	    this.pointsEarned = pointsEarned;
	}
}
