package com.akosha.tool.spring.form;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BrandStory")
public class BrandStory 
{
	@Id
	@Column(name="storyId",nullable=false)
	private String storyId;
	
	@Column(name="brandId",nullable=false)
	private String brandId;
	
	@Column(name="postedByUserId",nullable=false)
	private String postedByUserId;
	
	@Column(name="message",nullable=false)
	private String message;
	
	@Column(name="likeCount",nullable=true)
	private int likeCount;
	
	@Column(name="commentCount",nullable=true)
	private int commentCount;
	
	@Column(name="picLink",nullable=true)
	private String picLink;
	
	@Column(name="videoLink",nullable=true)
	private String videoLink;

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getPostedByUserId() {
		return postedByUserId;
	}

	public void setPostedByUserId(String postedByUserId) {
		this.postedByUserId = postedByUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getPicLink() {
		return picLink;
	}

	public void setPicLink(String picLink) {
		this.picLink = picLink;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}
	
	
}
