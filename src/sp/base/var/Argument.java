/*
sp.base.var.Argument
sp.base.var.Argument.*

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.base.var;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import sp.base.NonNullReturnValue;
import sp.util.function.FunctionWithThrown;
import sp.util.function.PredicateWithThrown;

/**
 * 引数の前提条件.
 *
 * @author Se-foo
 * @since 0.1
 */
public interface Argument {

    /**
     * オブジェクト引数の前提条件.
     *
     * @author Se-foo
     * @param <T>
     *            チェック対象のクラス.
     * @since 0.1
     */
    static interface OfObj<T> extends Argument {

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 指定された値 other を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の値.
         * @return チェック対象又は other.
         * @see #orElseGet(Supplier)
         * @since 0.1
         */
        default T orElse(T other) {
            return this.orElseGet(() -> other);
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 戻り値生成関数 other を呼び出し, その結果を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の戻り値生成関数.
         * @return チェック対象又は戻り値生成関数 other の呼び出し結果.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の戻り値生成関数が NULL の場合.
         * @since 0.1
         */
        T orElseGet(Supplier<? extends T> other);

        /**
         * <p>
         * チェック対象が指定されたチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック中に発生する例外クラス.
         * @param filter
         *            チェック内容.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容が NULL の場合.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @since 0.1
         */
        @NonNullReturnValue
        <X extends Throwable> Argument.OfObj<T> filter(PredicateWithThrown.OfObj<? super T, ? extends X> filter)
                throws X;

        /**
         * <p>
         * チェック対象がチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック対象がチェック内容に沿っていない場合の例外クラス.
         * @param predicate
         *            チェック内容.
         * @param throwable
         *            チェック対象がチェック内容に沿っていない場合にスローする例外生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容及びエラー生成関数が NULL, 又はエラー生成関数の呼び出し結果が NULL の場合.
         * @throws X
         *             チェック対象がチェック内容に沿っていない場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfObj)
         * @since 0.1
         */
        @NonNullReturnValue
        default <X extends Throwable> Argument.OfObj<T> require(Predicate<? super T> predicate,
                Function<? super T, ? extends X> throwable) throws X {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(throwable);

            return this.filter(target -> {
                if (predicate.test(target)) {
                    return true;
                } else {
                    throw Objects.requireNonNull(throwable.apply(target));
                }
            });
        }

        /**
         * <p>
         * チェック対象が NULL でないことを確認する.
         * </p>
         * <p>
         * チェック対象が NULL の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック対象が NULL の場合.
         * @see #requireNonNull(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireNonNull() {
            return this.requireNonNull(() -> null);
        }

        /**
         * <p>
         * チェック対象が NULL でないことを確認する.
         * </p>
         * <p>
         * チェック対象が NULL の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param message
         *            チェック対象が NULL の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック対象が NULL の場合.
         * @see #requireNonNull(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireNonNull(CharSequence message) {
            return this.requireNonNull(() -> message);
        }

        /**
         * <p>
         * チェック対象が NULL でないことを確認する.
         * </p>
         * <p>
         * チェック対象が NULL の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param message
         *            チェック対象が NULL の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック対象が NULL の場合, エラーメッセージ生成関数が NULL の場合.
         * @see #require(Predicate, Function)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireNonNull(Supplier<? extends CharSequence> message) {
            Objects.requireNonNull(message);

            return this.require(target -> Objects.nonNull(target), target -> new NullPointerException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が指定されたクラスを継承していることを確認する.
         * </p>
         * <p>
         * チェック対象が指定されたクラスを継承していない場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @implSpec 継承関係の確認には {@link Class#isInstance(Object)} を用いている.
         * @param classObject
         *            クラス情報.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             クラス情報が NULL の場合.
         * @throws ClassCastException
         *             チェック対象が指定されたクラスを継承していない場合.
         * @see #requireInstanceOf(Class, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireInstanceOf(Class<?> classObject) {

            // throw NullPointerException if classObject is null at
            // #requireInstanceOf.
            return this.requireInstanceOf(classObject, () -> null);
        }

        /**
         * <p>
         * チェック対象が指定されたクラスを継承していることを確認する.
         * </p>
         * <p>
         * チェック対象が指定されたクラスを継承していない場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @implSpec 継承関係の確認には {@link Class#isInstance(Object)} を用いている.
         * @param classObject
         *            クラス情報.
         * @param message
         *            チェック対象が指定されたクラスを継承していない場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             クラス情報が NULL の場合.
         * @throws ClassCastException
         *             チェック対象が指定されたクラスを継承していない場合.
         * @see #requireInstanceOf(Class, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireInstanceOf(Class<?> classObject, CharSequence message) {

            // throw NullPointerException if classObject is null at
            // #requireInstanceOf.
            return this.requireInstanceOf(classObject, () -> message);
        }

        /**
         * <p>
         * チェック対象が指定されたクラスを継承していることを確認する.
         * </p>
         * <p>
         * チェック対象が指定されたクラスを継承していない場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @implSpec 継承関係の確認には {@link Class#isInstance(Object)} を用いている.
         * @param classObject
         *            クラス情報.
         * @param message
         *            チェック対象が指定されたクラスを継承していない場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             クラス情報が NULL の場合, エラーメッセージ生成関数が NULL の場合.
         * @throws ClassCastException
         *             チェック対象が指定されたクラスを継承していない場合.
         * @see #require(Predicate, Function)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireInstanceOf(Class<?> classObject, Supplier<? extends CharSequence> message) {
            Objects.requireNonNull(classObject);
            Objects.requireNonNull(message);

            return this.require(target -> classObject.isInstance(target), target -> new ClassCastException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が NULL でないことを確認する.
         * </p>
         * <p>
         * チェック対象が NULL の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @see #filter(sp.util.function.PredicateWithThrown.OfObj)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> nonNull() {
            return this.filter(target -> Objects.nonNull(target));
        }

        /**
         * <p>
         * チェック対象が指定されたクラスを継承していることを確認する.
         * </p>
         * <p>
         * チェック対象が指定されたクラスを継承していない場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @implSpec 継承関係の確認には {@link Class#isInstance(Object)} を用いている.
         * @param classObject
         *            クラス情報.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             クラス情報が NULL の場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfObj)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> instanceOf(Class<?> classObject) {
            Objects.requireNonNull(classObject);

            return this.filter(target -> classObject.isInstance(target));
        }

        /**
         * マッピングによるオブジェクト引数の前提条件の変換機構.
         *
         * @author Se-foo
         * @param <T>
         *            マッピング変換元のチェック対象クラス.
         * @since 0.1
         */
        static interface Mappper<T> extends Mapper.FromObj<T, Argument> {

            /**
             * <p>
             * 指定されたマッピングを用いてチェック対象を変換する.
             * </p>
             * <p>
             * 変換中にエラーが発生した場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
             * {@link Argument#check() check()} は FALSE を返す.
             * </p>
             *
             * @param <A>
             *            マッピングの変換先クラス.
             * @param <X>
             *            変換中に発生するエラークラス.
             * @param mapping
             *            チェック対象の変換に用いるマッピング.
             * @return チェック対象変換後のチェックストリーム.
             * @throws NullPointerException
             *             チェック対象の変換に用いるマッピングが NULL の場合.
             * @throws X
             *             変換中にエラーが発生した場合.
             * @since 0.1
             */
            @Override
            <A, X extends Throwable> Argument.OfObj<A> toObj(
                    FunctionWithThrown.OfObj<? super T, ? extends A, ? extends X> mapping) throws X;

            /**
             * <p>
             * チェック対象をキャストする.
             * </p>
             * <p>
             * チェック対象が指定されたクラスを継承していない場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
             * {@link #check()} は FALSE を返す.
             * </p>
             *
             * @param <A>
             *            マッピングの変換先クラス.
             * @param classObject
             *            クラス情報.
             * @return キャスト後のチェックストリーム.
             * @throws NullPointerException
             *             クラス情報が NULL の場合.
             * @see #instanceOf(Class)
             * @since 0.1
             */
            <A> Argument.OfObj<A> toCast(Class<? extends A> classObject);

            /**
             * <p>
             * 指定されたマッピングを用いてチェック対象を変換する.
             * </p>
             * <p>
             * 変換中にエラーが発生した場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
             * {@link Argument#check() check()} は FALSE を返す.
             * </p>
             *
             * @param <X>
             *            変換中に発生するエラークラス.
             * @param mapping
             *            チェック対象の変換に用いるマッピング.
             * @return チェック対象変換後のチェックストリーム.
             * @throws NullPointerException
             *             チェック対象の変換に用いるマッピングが NULL の場合.
             * @throws X
             *             変換中にエラーが発生した場合.
             * @since 0.1
             */
            @Override
            <X extends Throwable> Argument.OfDouble toDouble(
                    FunctionWithThrown.OfObjToDouble<? super T, ? extends X> mapping) throws X;

            /**
             * <p>
             * 指定されたマッピングを用いてチェック対象を変換する.
             * </p>
             * <p>
             * 変換中にエラーが発生した場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
             * {@link Argument#check() check()} は FALSE を返す.
             * </p>
             *
             * @param <X>
             *            変換中に発生するエラークラス.
             * @param mapping
             *            チェック対象の変換に用いるマッピング.
             * @return チェック対象変換後のチェックストリーム.
             * @throws NullPointerException
             *             チェック対象の変換に用いるマッピングが NULL の場合.
             * @throws X
             *             変換中にエラーが発生した場合.
             * @since 0.1
             */
            @Override
            <X extends Throwable> Argument.OfInt toInt(FunctionWithThrown.OfObjToInt<? super T, ? extends X> mapping)
                    throws X;

            /**
             * チェック対象をハッシュ値に変換する.
             *
             * @implSpec ハッシュ値変換には {@link Objects#hashCode(Object)} を用いている.
             * @return チェック対象変換後のチェックストリーム.
             * @see #mapToInt(ToIntFunction)
             * @since 0.1
             */
            default Argument.OfInt toHash() {
                return this.toInt(target -> Objects.hashCode(target));
            }

            /**
             * <p>
             * 指定されたマッピングを用いてチェック対象を変換する.
             * </p>
             * <p>
             * 変換中にエラーが発生した場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
             * {@link Argument#check() check()} は FALSE を返す.
             * </p>
             *
             * @param <X>
             *            変換中に発生するエラークラス.
             * @param mapping
             *            チェック対象の変換に用いるマッピング.
             * @return チェック対象変換後のチェックストリーム.
             * @throws NullPointerException
             *             チェック対象の変換に用いるマッピングが NULL の場合.
             * @throws X
             *             変換中にエラーが発生した場合.
             * @since 0.1
             */
            @Override
            <X extends Throwable> Argument.OfLong toLong(FunctionWithThrown.OfObjToLong<? super T, ? extends X> mapping)
                    throws X;
        }

        /**
         * チェック対象の変換機構を取得する.
         *
         * @return チェック対象の変換機構.
         * @since 0.1
         */
        Argument.OfObj.Mappper<T> map();
    }

    /**
     * double 引数の前提条件.
     *
     * @author Se-foo
     * @since 0.1
     */
    static interface OfDouble extends Argument {

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 指定された値 other を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の値.
         * @return チェック対象又は other.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の値が NULL の場合.
         * @see #orElseGet(Supplier)
         * @since 0.1
         */
        default double orElse(double other) {
            return this.orElseGet(() -> other);
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 戻り値生成関数 other を呼び出し, その結果を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の戻り値生成関数.
         * @return チェック対象又は戻り値生成関数 other の呼び出し結果.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の戻り値生成関数が NULL, 又は戻り値生成関数の呼び出し結果が
         *             NULL の場合.
         * @see #check()
         * @since 0.1
         */
        double orElseGet(DoubleSupplier other);

        /**
         * <p>
         * チェック対象が指定されたチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック中に発生する例外クラス.
         * @param filter
         *            チェック内容.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容が NULL の場合.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @since 0.1
         */
        @NonNullReturnValue
        <X extends Throwable> Argument.OfDouble filter(PredicateWithThrown.OfDouble<? extends X> filter) throws X;

        /**
         * <p>
         * チェック対象がチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック対象がチェック内容に沿っていない場合の例外クラス.
         * @param predicate
         *            チェック内容.
         * @param throwable
         *            チェック対象がチェック内容に沿っていない場合にスローする例外生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容及びエラー生成関数が NULL, 又はエラー生成関数の呼び出し結果が NULL の場合.
         * @throws X
         *             チェック対象がチェック内容に沿っていない場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfDouble)
         * @since 0.1
         */
        @NonNullReturnValue
        default <X extends Throwable> Argument.OfDouble require(DoublePredicate predicate,
                DoubleFunction<? extends X> throwable) throws X {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(throwable);

            return this.filter(target -> {
                if (predicate.test(target)) {
                    return true;
                } else {
                    throw Objects.requireNonNull(throwable.apply(target));
                }
            });
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #requireNonNegative(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble requireNonNegative() {
            return this.requireNonNegative(() -> null);
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param message
         *            チェック対象が NULL の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #requireNonNegative(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble requireNonNegative(CharSequence message) {
            return this.requireNonNegative(() -> message);
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param message
         *            チェック対象が負の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #require(DoublePredicate, DoubleFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble requireNonNegative(Supplier<? extends CharSequence> message) {
            Objects.requireNonNull(message);

            return this.require(target -> target >= 0, target -> new IllegalArgumentException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象がチェック対象が指定された範囲外の場合.
         * @see #requireBounds(double, double, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble requireBounds(double from, double to) {

            // throw IllegalArgumentException if from > to at #requireBounds.
            return this.requireBounds(from, to, () -> null);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象がチェック対象が指定された範囲外の場合.
         * @see #requireBounds(double, double, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble requireBounds(double from, double to, CharSequence message) {

            // throw IllegalArgumentException if from > to at #requireBounds.
            return this.requireBounds(from, to, () -> message);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象がチェック対象が指定された範囲外の場合.
         * @see #require(DoublePredicate, DoubleFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble requireBounds(double from, double to, Supplier<? extends CharSequence> message) {
            if (from > to) {
                throw new IllegalArgumentException();
            }
            Objects.requireNonNull(message);

            return this.require(target -> target >= from && target < to, target -> new IllegalArgumentException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @see #filter(sp.util.function.PredicateWithThrown.OfDouble)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble nonNegative() {
            return this.filter(target -> target >= 0);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfDouble)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfDouble isBounds(double from, double to) {
            if (from > to) {
                throw new IllegalArgumentException();
            }

            return this.filter(target -> target >= from && target < to);
        }

        // TODO : need #map()
    }

    /**
     * int 引数の前提条件.
     *
     * @author Se-foo
     * @since 0.1
     */
    static interface OfInt extends Argument {

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 指定された値 other を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の値.
         * @return チェック対象又は other.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の値が NULL の場合.
         * @see #orElseGet(Supplier)
         * @since 0.1
         */
        default int orElse(int other) {
            return this.orElseGet(() -> other);
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 戻り値生成関数 other を呼び出し, その結果を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の戻り値生成関数.
         * @return チェック対象又は戻り値生成関数 other の呼び出し結果.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の戻り値生成関数が NULL, 又は戻り値生成関数の呼び出し結果が
         *             NULL の場合.
         * @see #check()
         * @since 0.1
         */
        int orElseGet(IntSupplier other);

        /**
         * <p>
         * チェック対象が指定されたチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック中に発生する例外クラス.
         * @param filter
         *            チェック内容.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容が NULL の場合.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @since 0.1
         */
        @NonNullReturnValue
        <X extends Throwable> Argument.OfInt filter(PredicateWithThrown.OfInt<? extends X> filter) throws X;

        /**
         * <p>
         * チェック対象がチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック対象がチェック内容に沿っていない場合の例外クラス.
         * @param predicate
         *            チェック内容.
         * @param throwable
         *            チェック対象がチェック内容に沿っていない場合にスローする例外生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容及びエラー生成関数が NULL, 又はエラー生成関数の呼び出し結果が NULL の場合.
         * @throws X
         *             チェック対象がチェック内容に沿っていない場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfInt)
         * @since 0.1
         */
        @NonNullReturnValue
        default <X extends Throwable> Argument.OfInt require(IntPredicate predicate, IntFunction<? extends X> throwable)
                throws X {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(throwable);

            return this.filter(target -> {
                if (predicate.test(target)) {
                    return true;
                } else {
                    throw Objects.requireNonNull(throwable.apply(target));
                }
            });
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #requireNonNegative(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireNonNegative() {
            return this.requireNonNegative(() -> null);
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param message
         *            チェック対象が NULL の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #requireNonNegative(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireNonNegative(CharSequence message) {
            return this.requireNonNegative(() -> message);
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param message
         *            チェック対象が負の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #require(IntPredicate, IntFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireNonNegative(Supplier<? extends CharSequence> message) {
            Objects.requireNonNull(message);

            return this.require(target -> target >= 0, target -> new IllegalArgumentException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象が指定された範囲外の場合.
         * @see #requireBounds(int, int, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireBounds(int from, int to) {

            // throw IllegalArgumentException if from > to at #requireBounds.
            return this.requireBounds(from, to, () -> null);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象が指定された範囲外の場合.
         * @see #requireBounds(int, int, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireBounds(int from, int to, CharSequence message) {

            // throw IllegalArgumentException if from > to at #requireBounds.
            return this.requireBounds(from, to, () -> message);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象が指定された範囲外の場合.
         * @see #require(IntPredicate, IntFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireBounds(int from, int to, Supplier<? extends CharSequence> message) {
            if (from > to) {
                throw new IllegalArgumentException();
            }
            Objects.requireNonNull(message);

            return this.require(target -> target >= from && target < to, target -> new IllegalArgumentException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象がインデックスであることを確認する.
         * </p>
         * <p>
         * チェック対象が 0 より小さい又は指定されたサイズ以上の場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param size
         *            サイズ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定されたサイズが 0 より小さい場合.
         * @throws IndexOutOfBoundsException
         *             チェック対象がインデックスでない場合.
         * @see #requireIndex(int, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireIndex(int size) {

            // throw IllegalArgumentException if size < 0 at #requireIndex.
            return this.requireIndex(size, () -> null);
        }

        /**
         * <p>
         * チェック対象がインデックスであることを確認する.
         * </p>
         * <p>
         * チェック対象が 0 より小さい又は指定されたサイズ以上の場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param size
         *            サイズ.
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定されたサイズが 0 より小さい場合.
         * @throws IndexOutOfBoundsException
         *             チェック対象がインデックスでない場合.
         * @see #requireIndex(int, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireIndex(int size, CharSequence message) {

            // throw IllegalArgumentException if size < 0 at #requireIndex.
            return this.requireIndex(size, () -> message);
        }

        /**
         * <p>
         * チェック対象がインデックスであることを確認する.
         * </p>
         * <p>
         * チェック対象が 0 より小さい又は指定されたサイズ以上の場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param size
         *            サイズ.
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             指定されたサイズが 0 より小さい場合.
         * @throws IndexOutOfBoundsException
         *             チェック対象がインデックスでない場合.
         * @see #require(IntPredicate, IntFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt requireIndex(int size, Supplier<? extends CharSequence> message) {
            if (size < 0) {
                throw new IllegalArgumentException();
            }
            Objects.requireNonNull(message);

            return this.require(target -> target >= 0 && target < size, target -> new IndexOutOfBoundsException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @see #filter(sp.util.function.PredicateWithThrown.OfInt)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt nonNegative() {
            return this.filter(target -> target >= 0);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfInt)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt isBounds(int from, int to) {
            if (from > to) {
                throw new IllegalArgumentException();
            }

            return this.filter(target -> target >= from && target < to);
        }

        /**
         * <p>
         * チェック対象がインデックスであることを確認する.
         * </p>
         * <p>
         * チェック対象が 0 より小さい又は指定されたサイズ以上の場合, 本メソッド以降のフィルタ・マッピング処理は無視され,
         * {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param size
         *            サイズ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定されたサイズが 0 より小さい場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfInt)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfInt isIndex(int size) {
            if (size < 0) {
                throw new IllegalArgumentException();
            }

            return this.filter(target -> target >= 0 && target < size);
        }

        // TODO : need #map()
    }

    /**
     * long 引数の前提条件.
     *
     * @author Se-foo
     * @since 0.1
     */
    static interface OfLong extends Argument {

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 指定された値 other を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の値.
         * @return チェック対象又は other.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の値が NULL の場合.
         * @see #orElseGet(Supplier)
         * @since 0.1
         */
        default long orElse(long other) {
            return this.orElseGet(() -> other);
        }

        /**
         * <p>
         * チェック対象を取得する.
         * </p>
         * <p>
         * チェック対象がチェック内容に沿っていない場合, 戻り値生成関数 other を呼び出し, その結果を返す.
         * </p>
         *
         * @param other
         *            チェック対象がチェック内容に沿っていない場合の戻り値生成関数.
         * @return チェック対象又は戻り値生成関数 other の呼び出し結果.
         * @throws NullPointerException
         *             チェック対象がチェック内容に沿っていない場合の戻り値生成関数が NULL, 又は戻り値生成関数の呼び出し結果が
         *             NULL の場合.
         * @see #check()
         * @since 0.1
         */
        long orElseGet(DoubleSupplier other);

        /**
         * <p>
         * チェック対象が指定されたチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック中に発生する例外クラス.
         * @param filter
         *            チェック内容.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容が NULL の場合.
         * @throws X
         *             チェック中にエラーが発生した場合.
         * @since 0.1
         */
        @NonNullReturnValue
        <X extends Throwable> Argument.OfLong filter(PredicateWithThrown.OfLong<? extends X> filter) throws X;

        /**
         * <p>
         * チェック対象がチェック内容に沿っているか確認する.
         * </p>
         * <p>
         * チェック内容に沿っていない場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE
         * を返す.
         * </p>
         *
         * @param <X>
         *            チェック対象がチェック内容に沿っていない場合の例外クラス.
         * @param predicate
         *            チェック内容.
         * @param throwable
         *            チェック対象がチェック内容に沿っていない場合にスローする例外生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             チェック内容及びエラー生成関数が NULL, 又はエラー生成関数の呼び出し結果が NULL の場合.
         * @throws X
         *             チェック対象がチェック内容に沿っていない場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfLong)
         * @since 0.1
         */
        @NonNullReturnValue
        default <X extends Throwable> Argument.OfLong require(LongPredicate predicate,
                LongFunction<? extends X> throwable) throws X {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(throwable);

            return this.filter(target -> {
                if (predicate.test(target)) {
                    return true;
                } else {
                    throw Objects.requireNonNull(throwable.apply(target));
                }
            });
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #requireNonNegative(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong requireNonNegative() {
            return this.requireNonNegative(() -> null);
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param message
         *            チェック対象が NULL の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #requireNonNegative(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong requireNonNegative(CharSequence message) {
            return this.requireNonNegative(() -> message);
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @param message
         *            チェック対象が負の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             チェック対象が負の場合.
         * @see #require(LongPredicate, LongFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong requireNonNegative(Supplier<? extends CharSequence> message) {
            Objects.requireNonNull(message);

            return this.require(target -> target >= 0, target -> new IllegalArgumentException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象がチェック対象が指定された範囲外の場合.
         * @see #requireBounds(long, long, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong requireBounds(long from, long to) {

            // throw IllegalArgumentException if from > to at #requireBounds.
            return this.requireBounds(from, to, () -> null);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ.
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象がチェック対象が指定された範囲外の場合.
         * @see #requireBounds(double, double, Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong requireBounds(long from, long to, CharSequence message) {

            // throw IllegalArgumentException if from > to at #requireBounds.
            return this.requireBounds(from, to, () -> message);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @param message
         *            チェック対象が指定された範囲外の場合のエラーメッセージ生成関数.
         * @return 本チェックストリーム.
         * @throws NullPointerException
         *             エラーメッセージ生成関数が NULL の場合.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合, 又はチェック対象がチェック対象が指定された範囲外の場合.
         * @see #require(LongPredicate, LongFunction)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong requireBounds(long from, long to, Supplier<? extends CharSequence> message) {
            if (from > to) {
                throw new IllegalArgumentException();
            }
            Objects.requireNonNull(message);

            return this.require(target -> target >= from && target < to, target -> new IllegalArgumentException(
                    Optional.ofNullable(message.get()).map(m -> m.toString()).orElse(null)));
        }

        /**
         * <p>
         * チェック対象が負でないことを確認する.
         * </p>
         * <p>
         * チェック対象が負の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @see #filter(sp.util.function.PredicateWithThrown.OfDouble)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong nonNegative() {
            return this.filter(target -> target >= 0);
        }

        /**
         * <p>
         * チェック対象が始端 (from, これを含む) と終端 (to, これを含まない) の間であることを確認する.
         * </p>
         * <p>
         * チェック対象が指定された範囲外の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は
         * FALSE を返す.
         * </p>
         *
         * @param from
         *            始端. (これを含む)
         * @param to
         *            終端. (これを含まない)
         * @return 本チェックストリーム.
         * @throws IllegalArgumentException
         *             指定された始端が終端より大きい場合.
         * @see #filter(sp.util.function.PredicateWithThrown.OfLong)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfLong isBounds(long from, long to) {
            if (from > to) {
                throw new IllegalArgumentException();
            }

            return this.filter(target -> target >= from && target < to);
        }

        // TODO : need #map()
    }

    /**
     * チェック対象のチェック結果を取得する.
     *
     * @return チェック対象のチェック結果.
     * @since 0.1
     */
    boolean check();
}
