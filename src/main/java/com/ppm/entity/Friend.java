package com.ppm.entity;

import java.io.Serializable;
import java.util.Date;

public class Friend implements Serializable {
    private Integer id;

    private String wxMemberId;

    private String wxMemberFriendId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWxMemberId() {
        return wxMemberId;
    }

    public void setWxMemberId(String wxMemberId) {
        this.wxMemberId = wxMemberId == null ? null : wxMemberId.trim();
    }

    public String getWxMemberFriendId() {
        return wxMemberFriendId;
    }

    public void setWxMemberFriendId(String wxMemberFriendId) {
        this.wxMemberFriendId = wxMemberFriendId == null ? null : wxMemberFriendId.trim();
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
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}