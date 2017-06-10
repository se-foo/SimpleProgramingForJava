/*
sp.util.function.FunctionWithThrown
sp.util.function.FunctionWithThrown.*

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * {@link Throwable} がスローされる可能性がある 1 つの引数を受け取って結果を生成する関数を表す.
 * </p>
 *
 * @author Se-foo
 * @param <X>
 *            評価中に発生するエラークラス.
 * @since 0.1
 */
public interface FunctionWithThrown<X extends Throwable> {

    /**
     * <p>
     * 1 つの引数を受け取って結果を生成する関数を表す.
     * </p>
     * <p>
     * これは, {@link #apply(Object)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <T>
     *            関数の入力クラス.
     * @param <R>
     *            関数の結果クラス.
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfObj<T, R, X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        R apply(T target) throws X;

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param <V>
         *            before 関数および合成関数の入力クラス.
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        default <V> FunctionWithThrown.OfObj<V, R, X> compose(
                FunctionWithThrown.OfObj<? super V, ? extends T, ? extends X> before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.apply(target));
        }

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param <V>
         *            before 関数および合成関数の入力クラス.
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        default <V> FunctionWithThrown.OfObj<V, R, X> composeFunction(Function<? super V, ? extends T> before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param <V>
         *            after 関数および合成関数の出力クラス.
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        default <V> FunctionWithThrown.OfObj<T, V, X> andThen(
                FunctionWithThrown.OfObj<? super R, ? extends V, ? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param <V>
         *            after 関数および合成関数の出力クラス.
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        default <V> FunctionWithThrown.OfObj<T, V, X> andThenFunction(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.Function} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param throwable
         *            非チェック例外生成関数.
         * @return 変換後の関数.
         * @throws NullPointerException
         *             指定された非チェック例外生成関数が NULL, 又は生成された例外が NULL の場合.
         */
        default Function<T, R> toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
            Objects.requireNonNull(throwable);
            return target -> {
                try {
                    return this.apply(target);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw Objects.requireNonNull(throwable.apply(e));
                } catch (Error e) {
                    throw e;
                } catch (Throwable e) {
                    throw Objects.requireNonNull(throwable.apply(e));
                }
            };
        }

        /**
         * <p>
         * {@link java.util.function.Function} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        default Function<T, R> toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 単一のオペランドに作用してオペランドと同じ型の結果を生成する演算を表す.
     * </p>
     * <p>
     * これは, {@link #apply(Object)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <T>
     *            演算子のオペランドと結果のクラス.
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfObjUnary<T, X extends Throwable> extends FunctionWithThrown.OfObj<T, T, X> {
    }

}
