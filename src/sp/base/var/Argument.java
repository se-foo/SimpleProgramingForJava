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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import sp.base.NonNullReturnValue;
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
         * @see #requireNotNull(Supplier)
         * @since 0.1
         */
        @NonNullReturnValue
        default Argument.OfObj<T> requireNotNull() {
            return this.requireNotNull(() -> null);
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
         * @see #requireNotNull(Supplier)
         * @since 0.1
         */
        default Argument.OfObj<T> requireNotNull(CharSequence message) {
            return this.requireNotNull(() -> message);
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
        default Argument.OfObj<T> requireNotNull(Supplier<? extends CharSequence> message) {
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
         * チェック対象が NULL の場合, 本メソッド以降のフィルタ・マッピング処理は無視され, {@link #check()} は FALSE を返す.
         * </p>
         *
         * @return 本チェックストリーム.
         * @see #filter(sp.util.function.PredicateWithThrown.OfObj)
         * @since 0.1
         */
        default Argument.OfObj<T> notNull() {
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
        default Argument.OfObj<T> instanceOf(Class<?> classObject) {
            Objects.requireNonNull(classObject);

            return this.filter(target -> classObject.isInstance(target));
        }
    }

    /**
     * チェック対象のチェック結果を取得する.
     *
     * @return チェック対象のチェック結果.
     * @since 0.1
     */
    boolean check();
}
