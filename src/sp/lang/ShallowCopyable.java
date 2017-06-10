/*
sp.base.NonNullReturnValue

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.lang;

/**
 * 浅いコピーを実装していることを表す.
 *
 * @author Se-foo
 * @param <R>
 *            コピー後のクラス.
 * @since 0.1
 */
public interface ShallowCopyable<R> extends Cloneable {

    /**
     * このオブジェクトを浅くコピーする.
     *
     * @return このインスタンスの複製.
     * @since 0.1
     */
    R clone();
}
