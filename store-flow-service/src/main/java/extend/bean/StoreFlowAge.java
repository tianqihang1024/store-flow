package extend.bean;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺客流表-年龄表
 *
 * @TableName store_flow_age
 */
@Data
public class StoreFlowAge implements Serializable {
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
     * 0-10
     */
    private Long age1;
    /**
     * 11-20
     */
    private Long age2;
    /**
     * 21-30
     */
    private Long age3;
    /**
     * 31-40
     */
    private Long age4;
    /**
     * 41-50
     */
    private Long age5;
    /**
     * 51-60
     */
    private Long age6;
    /**
     * 61以上
     */
    private Long age7;
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
        StoreFlowAge other = (StoreFlowAge) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTenantId() == null ? other.getTenantId() == null : this.getTenantId().equals(other.getTenantId()))
                && (this.getStoreId() == null ? other.getStoreId() == null : this.getStoreId().equals(other.getStoreId()))
                && (this.getAge1() == null ? other.getAge1() == null : this.getAge1().equals(other.getAge1()))
                && (this.getAge2() == null ? other.getAge2() == null : this.getAge2().equals(other.getAge2()))
                && (this.getAge3() == null ? other.getAge3() == null : this.getAge3().equals(other.getAge3()))
                && (this.getAge4() == null ? other.getAge4() == null : this.getAge4().equals(other.getAge4()))
                && (this.getAge5() == null ? other.getAge5() == null : this.getAge5().equals(other.getAge5()))
                && (this.getAge6() == null ? other.getAge6() == null : this.getAge6().equals(other.getAge6()))
                && (this.getAge7() == null ? other.getAge7() == null : this.getAge7().equals(other.getAge7()))
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
        result = prime * result + ((getAge1() == null) ? 0 : getAge1().hashCode());
        result = prime * result + ((getAge2() == null) ? 0 : getAge2().hashCode());
        result = prime * result + ((getAge3() == null) ? 0 : getAge3().hashCode());
        result = prime * result + ((getAge4() == null) ? 0 : getAge4().hashCode());
        result = prime * result + ((getAge5() == null) ? 0 : getAge5().hashCode());
        result = prime * result + ((getAge6() == null) ? 0 : getAge6().hashCode());
        result = prime * result + ((getAge7() == null) ? 0 : getAge7().hashCode());
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
        sb.append(", age1=").append(age1);
        sb.append(", age2=").append(age2);
        sb.append(", age3=").append(age3);
        sb.append(", age4=").append(age4);
        sb.append(", age5=").append(age5);
        sb.append(", age6=").append(age6);
        sb.append(", age7=").append(age7);
        sb.append(", del=").append(del);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}