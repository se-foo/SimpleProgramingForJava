/*
sp.util.function.Mapper
sp.util.function.Mapper.*

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.util.function;


/**
 * マッピングによる変換機構.
 *
 * @author Se-foo
 * @param <R>
 *            変換先クラス.
 * @since 0.1
 */
public interface Mapper<R> {

    /**
     * マッピングによるオブジェクトの変換機構.
     *
     * @author Se-foo
     * @param <T>
     *            マッピングの変換元クラス.
     * @param <R>
     *            変換先クラス.
     * @since 0.1
     */
    interface FromObj<T, R> extends Mapper<R> {

        /**
         * 指定されたマッピングを用いて対象を変換する.
         *
         * @param <A>
         *            マッピングの変換先クラス.
         * @param <X>
         *            変換中に発生する例外クラス.
         * @param mapping
         *            対象の変換に用いるマッピング.
         * @return 対象の変換結果.
         * @throws NullPointerException
         *             対象の変換に用いるマッピングが NULL の場合.
         * @throws X
         *             変換中にエラーが発生した場合.
         * @since 0.1
         */
        <A, X extends Throwable> R toObj(FunctionWithThrown.OfObj<? super T, ? extends A, ? extends X> mapping)
                throws X;

        /**
         * 指定されたマッピングを用いて対象を変換する.
         *
         * @param <X>
         *            変換中に発生する例外クラス.
         * @param mapping
         *            対象の変換に用いるマッピング.
         * @return 対象の変換結果.
         * @throws NullPointerException
         *             対象の変換に用いるマッピングが NULL の場合.
         * @throws X
         *             変換中にエラーが発生した場合.
         * @since 0.1
         */
        <X extends Throwable> R toDouble(FunctionWithThrown.OfObjToDouble<? super T, ? extends X> mapping) throws X;

        /**
         * 指定されたマッピングを用いて対象を変換する.
         *
         * @param <X>
         *            変換中に発生する例外クラス.
         * @param mapping
         *            対象の変換に用いるマッピング.
         * @return 対象の変換結果.
         * @throws NullPointerException
         *             対象の変換に用いるマッピングが NULL の場合.
         * @throws X
         *             変換中にエラーが発生した場合.
         * @since 0.1
         */
        <X extends Throwable> R toInt(FunctionWithThrown.OfObjToInt<? super T, ? extends X> mapping) throws X;

        /**
         * 指定されたマッピングを用いて対象を変換する.
         *
         * @param <X>
         *            変換中に発生する例外クラス.
         * @param mapping
         *            対象の変換に用いるマッピング.
         * @return 対象の変換結果.
         * @throws NullPointerException
         *             対象の変換に用いるマッピングが NULL の場合.
         * @throws X
         *             変換中にエラーが発生した場合.
         * @since 0.1
         */
        <X extends Throwable> R toLong(FunctionWithThrown.OfObjToLong<? super T, ? extends X> mapping) throws X;
    }

}
