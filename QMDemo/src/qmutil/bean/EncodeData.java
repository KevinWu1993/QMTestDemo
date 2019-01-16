package qmutil.bean;

/**
 * author: KevinWu
 * date: 2019/1/16
 * description: 未解码的数据（将String视为一种未解码类型）
 */

public class EncodeData implements CompatibleBean {
    private String data;

    public EncodeData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
