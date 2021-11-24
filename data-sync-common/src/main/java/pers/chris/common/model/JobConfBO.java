package pers.chris.common.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobConfBO {

    @Id
    public String jobId;
    public String srcType;
    public String srcConfId;
    public String dstType;
    public String dstConfId;
    public String syncType;
    public Integer syncInterval;
    public String syncFieldName;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public String getSrcConfId() {
        return srcConfId;
    }

    public void setSrcConfId(String srcConfId) {
        this.srcConfId = srcConfId;
    }

    public String getDstType() {
        return dstType;
    }

    public void setDstType(String dstType) {
        this.dstType = dstType;
    }

    public String getDstConfId() {
        return dstConfId;
    }

    public void setDstConfId(String dstConfId) {
        this.dstConfId = dstConfId;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public Integer getSyncInterval() {
        return syncInterval;
    }

    public void setSyncInterval(Integer syncInterval) {
        this.syncInterval = syncInterval;
    }

    public String getSyncFieldName() {
        return syncFieldName;
    }

    public void setSyncFieldName(String syncFieldName) {
        this.syncFieldName = syncFieldName;
    }

    @Override
    public String toString() {
        return "JobConfBO{" +
                "jobId='" + jobId + '\'' +
                ", srcType='" + srcType + '\'' +
                ", srcConfId='" + srcConfId + '\'' +
                ", dstType='" + dstType + '\'' +
                ", dstConfId='" + dstConfId + '\'' +
                ", syncType='" + syncType + '\'' +
                ", syncInterval=" + syncInterval +
                ", syncFieldName='" + syncFieldName + '\'' +
                '}';
    }
}
