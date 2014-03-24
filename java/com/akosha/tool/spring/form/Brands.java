package com.akosha.tool.spring.form;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Brands")
public class Brands 
{
	@Id
	@Column(name="brandId",nullable=false)
	private String brandId;
	
	@Column(name="brandName",nullable=false)
	private String brandName;

	@Column(name="likes",nullable=true)
	private int likes;
	
	@Column(name="pictureLink",nullable=true)
	private String pictureLink;
	
	@Column(name="about",nullable=true)
	private String about;
	
	
	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getPictureLink() {
		return pictureLink;
	}

	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	
	
}
