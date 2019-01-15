package demo.entity;

import java.util.Objects;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 数据实体，以{id, groupId, float}形式
 */

public class DataBean {
    private String id;
    private String groupId;
    private float quota;

    public DataBean(String id, String groupId, float quota) {
        this.id = id;
        this.groupId = groupId;
        this.quota = quota;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public float getQuota() {
        return quota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBean dataBean = (DataBean) o;
        return Float.compare(dataBean.quota, quota) == 0 &&
                Objects.equals(id, dataBean.id) &&
                Objects.equals(groupId, dataBean.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, quota);
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", quota=" + quota +
                '}';
    }
}
