package demo.callback;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: callback抽象接口，具体使用在DecodeListener中，当完整解析出数据时回调
 */

public interface ResultCallBack<M> {
    void onResult(M m);
}
