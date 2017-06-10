/*
sp.lang.DeepCopyable

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.lang;

/**
 * 深いコピーを実装していることを表す.
 *
 * @author Se-foo
 * @param <R>
 *            コピー後のクラス.
 * @since 0.1
 */
public interface DeepCopyable<R> extends Cloneable {

    /**
     * このオブジェクトを深くコピーする.
     *
     * @return このインスタンスの複製.
     * @since 0.1
     */
    R deepclone();
}
