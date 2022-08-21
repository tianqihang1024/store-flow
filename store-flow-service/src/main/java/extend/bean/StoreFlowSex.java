package extend.bean;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺客流表-性别
 *
 * @TableName store_flow_sex
 */
@Data
public class StoreFlowSex implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 店铺ID
     */
    private String storeId;
    /**
     * 男性
     */
    private Long maleCount;
    /**
     * 女性
     */
    private Long femaleCount;
    /**
     * 未知
     */
    private Long unknownCount;
    /**
     * 是否删除 0：未删除 1：删除
     */
    private Integer del;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        StoreFlowSex other = (StoreFlowSex) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTenantId() == null ? other.getTenantId() == null : this.getTenantId().equals(other.getTenantId()))
                && (this.getStoreId() == null ? other.getStoreId() == null : this.getStoreId().equals(other.getStoreId()))
                && (this.getMaleCount() == null ? other.getMaleCount() == null : this.getMaleCount().equals(other.getMaleCount()))
                && (this.getFemaleCount() == null ? other.getFemaleCount() == null : this.getFemaleCount().equals(other.getFemaleCount()))
                && (this.getUnknownCount() == null ? other.getUnknownCount() == null : this.getUnknownCount().equals(other.getUnknownCount()))
                && (this.getDel() == null ? other.getDel() == null : this.getDel().equals(other.getDel()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getStoreId() == null) ? 0 : getStoreId().hashCode());
        result = prime * result + ((getMaleCount() == null) ? 0 : getMaleCount().hashCode());
        result = prime * result + ((getFemaleCount() == null) ? 0 : getFemaleCount().hashCode());
        result = prime * result + ((getUnknownCount() == null) ? 0 : getUnknownCount().hashCode());
        result = prime * result + ((getDel() == null) ? 0 : getDel().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tenantId=").append(tenantId);
        sb.append(", storeId=").append(storeId);
        sb.append(", maleCount=").append(maleCount);
        sb.append(", femaleCount=").append(femaleCount);
        sb.append(", unknownCount=").append(unknownCount);
        sb.append(", del=").append(del);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}