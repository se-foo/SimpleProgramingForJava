/*
sp.base.var.Variable

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.base.var;

import sp.base.NonNullReturnValue;

/**
 * 変数の前提条件記述のファクトリークラス.
 *
 * @author Se-foo
 * @since 0.1
 */
public final class Variable {

    /**
     * 指定された引数オブジェクトの前提条件を作成する.
     *
     * @param <T>
     *            指定された引数クラス.
     * @param target
     *            引数.
     * @return 引数の前提条件.
     */
    @NonNullReturnValue
    public static <T> Argument.OfObj<T> arg(T target) {
        return new ArgumentImpl.OfObj<>(target);
    }

    /**
     * 指定された変数のチェックリストを作成する.
     *
     * @param <T>
     *            指定された変数クラス.
     * @return 変数のチェックリスト.
     */
    @NonNullReturnValue
    public static <T> Checklist.OfObj<T> checklist() {
        // TODO : Implements Checklist.OfObj
        return null;
    }

    /**
     * Constractor.
     */
    Variable() {
        super();
    }
}
