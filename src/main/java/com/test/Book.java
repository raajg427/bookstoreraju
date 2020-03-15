package com.test;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@ManagedBean
@SessionScoped
@Entity
@Table(name = "books")
public class Book {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "b_id")
	private int id;
	@Column(name = "b_title")
	private String title;
	@Column(name = "b_year")
	private String year;
	@Column(name = "b_price")
	private String price;
	
	@Transient
	private String authors;
	@Transient
	private boolean editable = false;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public Book(String title, String year, String price) {
		super();
		this.title = title;
		this.year = year;
		this.price = price;
	}
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
	
	

}
