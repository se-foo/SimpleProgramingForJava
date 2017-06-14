/*
sp.base.var.Checklist
sp.base.var.Checklist.*

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.base.var;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 変数のチェックリスト.
 *
 * @author Se-foo
 * @since 0.1
 */
public interface Checklist {

    /**
     * オブジェクト変数のチェックリスト.
     *
     * @author Se-foo
     * @param <T>
     *            チェック対象のクラス.
     * @since 0.1
     */
    static interface OfObj<T> extends Checklist {

        /**
         * <p>
         * チェック対象のチェック結果を取得する.
         * </p>
         * <p>
         * チェック中に発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @param target
         *            チェック対象.
         * @return チェック対象のチェック結果.
         * @see #check(Object, Function)
         * @since 0.1
         */
        default boolean check(T target) {
            return this.check(target, cause -> new RuntimeException(cause));
        }

        /**
         * <p>
         * チェック対象のチェック結果を取得する.
         * </p>
         * <p>
         * チェック中に発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param <X>
         *            チェック中に発生したエラークラス.
         * @param target
         *            チェック対象.
         * @param throwable
         *            非チェック例外生成関数.
         * @return チェック対象のチェック結果.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @since 0.1
         */
        <X extends Throwable> boolean check(T target, Function<? super Throwable, ? extends X> throwable) throws X;

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 指定された値 other を返す.
         * </p>
         * <p>
         * チェック中に発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の値.
         * @return チェック対象又は other.
         * @see #orElse(Object, Function)
         * @since 0.1
         */
        default T orElse(T other) {
            return this.orElse(other, cause -> new RuntimeException(cause));
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 指定された値 other を返す.
         * </p>
         * <p>
         * チェック中に発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param <X>
         *            チェック中に発生したエラークラス.
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の値.
         * @param throwable
         *            非チェック例外生成関数.
         * @return チェック対象又は other.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @see #orElseGet(Supplier)
         * @since 0.1
         */
        default <X extends Throwable> T orElse(T other, Function<? super Throwable, ? extends X> throwable) throws X {
            return this.orElseGet(() -> other, throwable);
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 戻り値生成関数 other を呼び出し, その結果を返す.
         * </p>
         * <p>
         * チェック中に発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の戻り値生成関数.
         * @return チェック対象又は戻り値生成関数 other の呼び出し結果.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の戻り値生成関数が NULL の場合.
         * @since 0.1
         */
        default T orElseGet(Supplier<? extends T> other) {
            return this.orElseGet(other, cause -> new RuntimeException(cause));
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 戻り値生成関数 other を呼び出し, その結果を返す.
         * </p>
         * <p>
         * チェック中に発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param <X>
         *            チェック中に発生したエラークラス.
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の戻り値生成関数.
         * @param throwable
         *            非チェック例外生成関数.
         * @return チェック対象又は戻り値生成関数 other の呼び出し結果.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の戻り値生成関数が NULL の場合.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @since 0.1
         */
        <X extends Throwable> T orElseGet(Supplier<? extends T> other,
                Function<? super Throwable, ? extends X> throwable) throws X;

    }
}
