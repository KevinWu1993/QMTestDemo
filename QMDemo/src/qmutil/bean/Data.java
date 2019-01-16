package qmutil.bean;

import java.util.Objects;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 数据实体，以{id, groupId, float}形式
 */

public class Data implements CompatibleBean {
    private String id;
    private String groupId;
    private float quota;

    public Data(String id, String groupId, float quota) {
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
        Data data = (Data) o;
        return Float.compare(data.quota, quota) == 0 &&
                Objects.equals(id, data.id) &&
                Objects.equals(groupId, data.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, quota);
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", quota=" + quota +
                '}';
    }
}
