package com.wcy.databasemodule.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Objects;

/**
 * 某个传感器的某个量程的实验数据表
 * Created by acorn on 2022/7/27.
 */
@Entity
public class ExperimentRecord {
    @Id(autoincrement = true)
    private Long id;
    private String rangeUid;
    private String mac;
    private int rangeId;
    private double value;
    private String valueStr;
    private long timeStamp;
    @Transient
    private int pageNo;

    @Generated(hash = 673889728)
    public ExperimentRecord(Long id, String rangeUid, String mac, int rangeId,
                            double value, String valueStr, long timeStamp) {
        this.id = id;
        this.rangeUid = rangeUid;
        this.mac = mac;
        this.rangeId = rangeId;
        this.value = value;
        this.valueStr = valueStr;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 1855854530)
    public ExperimentRecord() {
    }

    public String getRangeUid() {
        return this.rangeUid;
    }

    public void setRangeUid(String rangeUid) {
        this.rangeUid = rangeUid;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRangeId() {
        return this.rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getValueStr() {
        return this.valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExperimentRecord)) return false;
        ExperimentRecord record = (ExperimentRecord) o;
        return getRangeId() == record.getRangeId() && Double.compare(record.getValue(), getValue()) == 0 && getTimeStamp() == record.getTimeStamp() && getPageNo() == record.getPageNo() && Objects.equals(getId(), record.getId()) && Objects.equals(getRangeUid(), record.getRangeUid()) && Objects.equals(getMac(), record.getMac()) && Objects.equals(getValueStr(), record.getValueStr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRangeUid(), getMac(), getRangeId(), getValue(), getValueStr(), getTimeStamp(), getPageNo());
    }
}
