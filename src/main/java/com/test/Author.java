package com.test;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ManagedBean
@SessionScoped
@Entity
@Table(name = "authors")
public class Author {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int a_id;
	@Column(name = "b_id")
	private int b_id;
	@Column(name = "author")
	private String author;
	
	public Author() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Author(int b_id, String author) {
		super();
	
		this.b_id = b_id;
		this.author = author;
	}

	public int getA_id() {
		return a_id;
	}

	public void setA_id(int a_id) {
		this.a_id = a_id;
	}

	public int getB_id() {
		return b_id;
	}

	public void setB_id(int b_id) {
		this.b_id = b_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	
	
	
	
	
}
