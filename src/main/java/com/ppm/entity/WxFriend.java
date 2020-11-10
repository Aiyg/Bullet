package com.ppm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class WxFriend implements Serializable {
    private Integer id;

    private Integer wxMemberId;

    private Integer wxMemberFriendId;

    private String content;

    @JsonFormat
    private Date createTime;


    private String nickName;

    private String headImg;

    private static final long serialVersionUID = 1L;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWxMemberId() {
        return wxMemberId;
    }

    public void setWxMemberId(Integer wxMemberId) {
        this.wxMemberId = wxMemberId;
    }

    public Integer getWxMemberFriendId() {
        return wxMemberFriendId;
    }

    public void setWxMemberFriendId(Integer wxMemberFriendId) {
        this.wxMemberFriendId = wxMemberFriendId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxMemberId=").append(wxMemberId);
        sb.append(", wxMemberFriendId=").append(wxMemberFriendId);
        sb.append(", content=").append(content);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}