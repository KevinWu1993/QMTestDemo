package qmutil.callback;

/**
 * author: KevinWu
 * date: 2019/1/16
 * description: 通用回调接口
 */

public interface CallBack<M> {
    void onResult(M m);
}
