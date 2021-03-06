/*
sp.util.function.PredicateWithThrown
sp.util.function.PredicateWithThrown.*

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.util.function;

import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import sp.base.NonNullReturnValue;

/**
 * <p>
 * {@link Throwable} がスローされる可能性がある 1 つの引数の述語 (boolean 値関数) を表す.
 * </p>
 *
 * @author Se-foo
 * @param <X>
 *            評価中に発生するエラークラス.
 * @since 0.1
 */
public interface PredicateWithThrown<X extends Throwable> {

    /**
     * <p>
     * 1 つの引数の述語 (boolean 値関数) を表す.
     * </p>
     * <p>
     * これは, {@link #test(Object)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <T>
     *            述語の入力クラス.
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfObj<T, X extends Throwable> extends PredicateWithThrown<X> {

        /**
         * 指定された引数でこの述語を評価する.
         *
         * @param target
         *            入力引数.
         * @return 入力引数が述語に一致する場合 TRUE.
         * @throws X
         *             評価中にエラーが発生した場合.
         */
        boolean test(T target) throws X;

        /**
         * この述語の論理否定を表す述語を返す.
         *
         * @return この述語の論理否定を表す述語.
         * @see #test(Object)
         */
        @NonNullReturnValue
        @Override
        default PredicateWithThrown.OfObj<T, X> negate() {
            return target -> !this.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(Object)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfObj<T, X> and(PredicateWithThrown.OfObj<? super T, ? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(Object)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfObj<T, X> andPredicate(Predicate<? super T> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(Object)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfObj<T, X> or(PredicateWithThrown.OfObj<? super T, ? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(Object)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfObj<T, X> orPredicate(Predicate<? super T> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * {@link java.util.function.Predicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param throwable
         *            非チェック例外生成関数.
         * @return 変換後の述語 (boolean 値関数).
         * @throws NullPointerException
         *             指定された非チェック例外生成関数が NULL, 又は生成された例外が NULL の場合.
         * @see #test(Object)
         */
        @NonNullReturnValue
        default Predicate<T> toPredicate(Function<? super Throwable, ? extends RuntimeException> throwable) {
            Objects.requireNonNull(throwable);
            return target -> {
                try {
                    return this.test(target);
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
         * {@link java.util.function.Predicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の述語 (boolean 値関数).
         * @see #toPredicate(Function)
         */
        @NonNullReturnValue
        default Predicate<T> toPredicate() {
            return this.toPredicate(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 1 つの引数の述語 (boolean 値関数) を表す.
     * </p>
     * <p>
     * これは, {@link #test(double)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfDouble<X extends Throwable> extends PredicateWithThrown<X> {

        /**
         * 指定された引数でこの述語を評価する.
         *
         * @param target
         *            入力引数.
         * @return 入力引数が述語に一致する場合 TRUE.
         * @throws X
         *             評価中にエラーが発生した場合.
         */
        boolean test(double target) throws X;

        /**
         * この述語の論理否定を表す述語を返す.
         *
         * @return この述語の論理否定を表す述語.
         * @see #test(double)
         */
        @NonNullReturnValue
        @Override
        default PredicateWithThrown.OfDouble<X> negate() {
            return target -> !this.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(double)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfDouble<X> and(PredicateWithThrown.OfDouble<? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(double)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfDouble<X> andPredicate(DoublePredicate other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(double)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfDouble<X> or(PredicateWithThrown.OfDouble<? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(double)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfDouble<X> orPredicate(DoublePredicate other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * {@link java.util.function.DoublePredicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param throwable
         *            非チェック例外生成関数.
         * @return 変換後の述語 (boolean 値関数).
         * @throws NullPointerException
         *             指定された非チェック例外生成関数が NULL, 又は生成された例外が NULL の場合.
         * @see #test(double)
         */
        @NonNullReturnValue
        default DoublePredicate toPredicate(Function<? super Throwable, ? extends RuntimeException> throwable) {
            Objects.requireNonNull(throwable);
            return target -> {
                try {
                    return this.test(target);
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
         * {@link java.util.function.DoublePredicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の述語 (boolean 値関数).
         * @see #toPredicate(Function)
         */
        @NonNullReturnValue
        default DoublePredicate toPredicate() {
            return this.toPredicate(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 1 つの引数の述語 (boolean 値関数) を表す.
     * </p>
     * <p>
     * これは, {@link #test(int)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfInt<X extends Throwable> extends PredicateWithThrown<X> {

        /**
         * 指定された引数でこの述語を評価する.
         *
         * @param target
         *            入力引数.
         * @return 入力引数が述語に一致する場合 TRUE.
         * @throws X
         *             評価中にエラーが発生した場合.
         */
        boolean test(int target) throws X;

        /**
         * この述語の論理否定を表す述語を返す.
         *
         * @return この述語の論理否定を表す述語.
         * @see #test(int)
         */
        @NonNullReturnValue
        @Override
        default PredicateWithThrown.OfInt<X> negate() {
            return target -> !this.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(int)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfInt<X> and(PredicateWithThrown.OfInt<? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(int)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfInt<X> andPredicate(IntPredicate other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(int)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfInt<X> or(PredicateWithThrown.OfInt<? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(int)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfInt<X> orPredicate(IntPredicate other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * {@link java.util.function.IntPredicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param throwable
         *            非チェック例外生成関数.
         * @return 変換後の述語 (boolean 値関数).
         * @throws NullPointerException
         *             指定された非チェック例外生成関数が NULL, 又は生成された例外が NULL の場合.
         * @see #test(int)
         */
        @NonNullReturnValue
        default IntPredicate toPredicate(Function<? super Throwable, ? extends RuntimeException> throwable) {
            Objects.requireNonNull(throwable);
            return target -> {
                try {
                    return this.test(target);
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
         * {@link java.util.function.IntPredicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の述語 (boolean 値関数).
         * @see #toPredicate(Function)
         */
        @NonNullReturnValue
        default IntPredicate toPredicate() {
            return this.toPredicate(cause -> new RuntimeException(cause));
        }
    }

    /**
     * <p>
     * 1 つの引数の述語 (boolean 値関数) を表す.
     * </p>
     * <p>
     * これは, {@link #test(long)} を関数メソッドに持つ関数型インタフェースである.
     * </p>
     *
     * @author Se-foo
     * @param <X>
     *            評価中に発生するエラークラス.
     * @since 0.1
     */
    @FunctionalInterface
    static interface OfLong<X extends Throwable> extends PredicateWithThrown<X> {

        /**
         * 指定された引数でこの述語を評価する.
         *
         * @param target
         *            入力引数.
         * @return 入力引数が述語に一致する場合 TRUE.
         * @throws X
         *             評価中にエラーが発生した場合.
         */
        boolean test(long target) throws X;

        /**
         * この述語の論理否定を表す述語を返す.
         *
         * @return この述語の論理否定を表す述語.
         * @see #test(long)
         */
        @NonNullReturnValue
        @Override
        default PredicateWithThrown.OfLong<X> negate() {
            return target -> !this.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(long)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfLong<X> and(PredicateWithThrown.OfLong<? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理積を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理積を取る述語.
         * @return この述語と述語 other の短絡論理積を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(long)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfLong<X> andPredicate(LongPredicate other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) && other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(long)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfLong<X> or(PredicateWithThrown.OfLong<? extends X> other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * この述語と別の述語の短絡論理和を表す合成述語を返す.
         * </p>
         * <p>
         * 合成述語の評価時にこの述語が FALSE だった場合, 述語 other は評価されない.
         * </p>
         *
         * @param other
         *            この述語との論理和を取る述語.
         * @return この述語と述語 other の短絡論理和を表す合成述語.
         * @throws NullPointerException
         *             指定された述語 other が NULL の場合.
         * @see #test(long)
         */
        @NonNullReturnValue
        default PredicateWithThrown.OfLong<X> orPredicate(LongPredicate other) {
            Objects.requireNonNull(other);
            return target -> this.test(target) || other.test(target);
        }

        /**
         * <p>
         * {@link java.util.function.LongPredicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * 非チェック例外生成関数 throwable を呼び出し, その結果がスローされる.
         * </p>
         *
         * @param throwable
         *            非チェック例外生成関数.
         * @return 変換後の述語 (boolean 値関数).
         * @throws NullPointerException
         *             指定された非チェック例外生成関数が NULL, 又は生成された例外が NULL の場合.
         * @see #test(long)
         */
        @NonNullReturnValue
        default LongPredicate toPredicate(Function<? super Throwable, ? extends RuntimeException> throwable) {
            Objects.requireNonNull(throwable);
            return target -> {
                try {
                    return this.test(target);
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
         * {@link java.util.function.LongPredicate} に変換する.
         * </p>
         * <p>
         * 発生するエラー又は非チェック例外はそのままスローされる. チェック例外又は左記以外の {@link Throwable} は
         * {@link RuntimeException} でラッピングされてスローされる.
         * </p>
         *
         * @return 変換後の述語 (boolean 値関数).
         * @see #toPredicate(Function)
         */
        @NonNullReturnValue
        default LongPredicate toPredicate() {
            return this.toPredicate(cause -> new RuntimeException(cause));
        }
    }

    /**
     * この述語の論理否定を表す述語を返す.
     *
     * @return この述語の論理否定を表す述語.
     */
    @NonNullReturnValue
    PredicateWithThrown<X> negate();

}
