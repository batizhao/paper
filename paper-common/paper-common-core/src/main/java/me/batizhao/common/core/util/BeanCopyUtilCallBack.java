package me.batizhao.common.core.util;

/**
 *
 * 作者：Van_Fan
 * 链接：https://juejin.im/post/5e1b18b1f265da3e140fa377
 *
 * @author batizhao
 * @since 2020-03-20
 **/
@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {

    /**
     * 定义默认回调方法
     * @param t
     * @param s
     */
    void callBack(S t, T s);
}
