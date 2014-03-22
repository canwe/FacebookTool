package com.akosha.tool.spring.form;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Comment
{
	private String commentId;
	
	private String postId;
	
	private String subPostId;
	
	private String message;
	
	private int likeCount;
	
	private String commentUserId;
	
	private String userPictureLink;
	
	private Date commentPostedDate;

	private List<Comment> subComment;
	
	public List<Comment> getSubComment() {
		return subComment;
	}

	public void setSubComment(List<Comment> subComment) {
		this.subComment = subComment;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getSubPostId() {
		return subPostId;
	}

	public void setSubPostId(String subPostId) {
		this.subPostId = subPostId;
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

	public String getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}

	public String getUserPictureLink() {
		return userPictureLink;
	}

	public void setUserPictureLink(String userPictureLink) {
		this.userPictureLink = userPictureLink;
	}

	public Date getCommentPostedDate() {
		return commentPostedDate;
	}

	public void setCommentPostedDate(Date commentPostedDate) {
		this.commentPostedDate = commentPostedDate;
	}
	
}
