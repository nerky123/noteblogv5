package me.wuwenbin.noteblogv5.model.entity;

import java.util.Date;

public class Vip {
    private Long vipId;

    private Long userId;

    private Integer state;

    private Date createTime;

    private Integer source;

    private Integer count;

    public Vip(Long vipId, Long userId, Integer state, Date createTime, Integer source,Integer count) {
        this.vipId = vipId;
        this.userId = userId;
        this.state = state;
        this.createTime = createTime;
        this.source = source;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Vip() {
        super();
    }

    public Long getVipId() {
        return vipId;
    }

    public void setVipId(Long vipId) {
        this.vipId = vipId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}