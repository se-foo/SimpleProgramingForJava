/*
sp.util.function.FunctionWithThrown
sp.util.function.FunctionWithThrown.*

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.util.function;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import sp.base.NonNullReturnValue;

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
        @NonNullReturnValue
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
        @NonNullReturnValue
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
        @NonNullReturnValue
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
        @NonNullReturnValue
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
        @NonNullReturnValue
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
        @NonNullReturnValue
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

    /**
     * <p>
     * 1 つの引数を受け取って double 値の結果を生成する関数を表す.
     * </p>
     * <p>
     * これは, {@link #apply(Object)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <T>
     *            関数の入力クラス.
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfObjToDouble<T, X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        double apply(T target) throws X;

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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToDouble<V, X> compose(
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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToDouble<V, X> composeFunction(Function<? super V, ? extends T> before) {
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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToDouble<T, X> andThen(FunctionWithThrown.OfDouble<? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfObjToDouble<T, X> andThenFunction(DoubleUnaryOperator after) {
            Objects.requireNonNull(after);
            return target -> after.applyAsDouble(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.ToDoubleFunction} に変換する.
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
        @NonNullReturnValue
        default ToDoubleFunction<T> toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
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
         * {@link java.util.function.ToDoubleFunction} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        @NonNullReturnValue
        default ToDoubleFunction<T> toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 1 つの引数を受け取って int 値の結果を生成する関数を表す.
     * </p>
     * <p>
     * これは, {@link #apply(Object)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <T>
     *            関数の入力クラス.
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfObjToInt<T, X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        int apply(T target) throws X;

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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToInt<V, X> compose(
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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToInt<V, X> composeFunction(Function<? super V, ? extends T> before) {
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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToInt<T, X> andThen(FunctionWithThrown.OfInt<? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfObjToInt<T, X> andThenFunction(IntUnaryOperator after) {
            Objects.requireNonNull(after);
            return target -> after.applyAsInt(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.ToIntFunction} に変換する.
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
        @NonNullReturnValue
        default ToIntFunction<T> toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
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
         * {@link java.util.function.ToIntFunction} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        @NonNullReturnValue
        default ToIntFunction<T> toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 1 つの引数を受け取って long 値の結果を生成する関数を表す.
     * </p>
     * <p>
     * これは, {@link #apply(Object)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <T>
     *            関数の入力クラス.
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfObjToLong<T, X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        long apply(T target) throws X;

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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToLong<V, X> compose(
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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToLong<V, X> composeFunction(Function<? super V, ? extends T> before) {
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
        @NonNullReturnValue
        default <V> FunctionWithThrown.OfObjToLong<T, X> andThen(FunctionWithThrown.OfLong<? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfObjToLong<T, X> andThenFunction(LongUnaryOperator after) {
            Objects.requireNonNull(after);
            return target -> after.applyAsLong(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.ToLongFunction} に変換する.
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
        @NonNullReturnValue
        default ToLongFunction<T> toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
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
         * {@link java.util.function.ToLongFunction} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        @NonNullReturnValue
        default ToLongFunction<T> toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 単一のオペランドに作用してオペランドと同じ型の結果を生成する演算を表す.
     * </p>
     * <p>
     * これは, {@link #apply(double)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfDouble<X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        double apply(double target) throws X;

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfDouble<X> compose(FunctionWithThrown.OfDouble<? extends X> before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.apply(target));
        }

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfDouble<X> composeFunction(DoubleUnaryOperator before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.applyAsDouble(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfDouble<X> andThen(FunctionWithThrown.OfDouble<? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfDouble<X> andThenFunction(DoubleUnaryOperator after) {
            Objects.requireNonNull(after);
            return target -> after.applyAsDouble(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.DoubleUnaryOperator} に変換する.
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
        @NonNullReturnValue
        default DoubleUnaryOperator toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
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
         * {@link java.util.function.DoubleUnaryOperator} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        @NonNullReturnValue
        default DoubleUnaryOperator toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 単一のオペランドに作用してオペランドと同じ型の結果を生成する演算を表す.
     * </p>
     * <p>
     * これは, {@link #apply(int)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfInt<X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        int apply(int target) throws X;

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfInt<X> compose(FunctionWithThrown.OfInt<? extends X> before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.apply(target));
        }

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfInt<X> composeFunction(IntUnaryOperator before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.applyAsInt(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfInt<X> andThen(FunctionWithThrown.OfInt<? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfInt<X> andThenFunction(IntUnaryOperator after) {
            Objects.requireNonNull(after);
            return target -> after.applyAsInt(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.IntUnaryOperator} に変換する.
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
        @NonNullReturnValue
        default IntUnaryOperator toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
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
         * {@link java.util.function.IntUnaryOperator} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        @NonNullReturnValue
        default IntUnaryOperator toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 単一のオペランドに作用してオペランドと同じ型の結果を生成する演算を表す.
     * </p>
     * <p>
     * これは, {@link #apply(long)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfLong<X extends Throwable> extends FunctionWithThrown<X> {

        /**
         * 指定された引数にこの関数を適用する.
         *
         * @param target
         *            入力引数.
         * @return 関数の結果.
         * @throws X
         *             結果生成中にエラーが発生した場合.
         */
        long apply(long target) throws X;

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfLong<X> compose(FunctionWithThrown.OfLong<? extends X> before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.apply(target));
        }

        /**
         * まず入力に関数 before を適用し, 次に結果にこの関数を適用する合成関数を返す.
         *
         * @param before
         *            この関数を適用する前に適用する関数.
         * @return まず before 関数を適用し, 次にこの関数を適用する合成関数.
         * @throws NullPointerException
         *             before 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfLong<X> composeFunction(LongUnaryOperator before) {
            Objects.requireNonNull(before);
            return target -> this.apply(before.applyAsLong(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfLong<X> andThen(FunctionWithThrown.OfLong<? extends X> after) {
            Objects.requireNonNull(after);
            return target -> after.apply(this.apply(target));
        }

        /**
         * まず入力にこの関数を適用し, 次に結果に関数 after を適用する合成関数を返す.
         *
         * @param after
         *            この関数を適用した後で適用する関数.
         * @return まずこの関数を適用し, 次に after 関数を適用する合成関数.
         * @throws NullPointerException
         *             after 関数が NULL の場合.
         */
        @NonNullReturnValue
        default FunctionWithThrown.OfLong<X> andThenFunction(LongUnaryOperator after) {
            Objects.requireNonNull(after);
            return target -> after.applyAsLong(this.apply(target));
        }

        /**
         * <p>
         * {@link java.util.function.LongUnaryOperator} に変換する.
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
        @NonNullReturnValue
        default LongUnaryOperator toFunction(Function<? super Throwable, ? extends RuntimeException> throwable) {
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
         * {@link java.util.function.LongUnaryOperator} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の関数.
         */
        @NonNullReturnValue
        default LongUnaryOperator toFunction() {
            return this.toFunction(cause -> new RuntimeException(cause));
        }
    }

}
