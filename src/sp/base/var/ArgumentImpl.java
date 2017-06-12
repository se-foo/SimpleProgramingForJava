/*
sp.base.var.ArgumentImpl

Copyright (c) 2017 Se-foo

This software is released under the MIT License.
http://opensource.org/licenses/mit-license.php
*/
package sp.base.var;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import sp.lang.ShallowCopyable;
import sp.util.function.FunctionWithThrown;
import sp.util.function.PredicateWithThrown;

/**
 * 引数の前提条件.
 *
 * @author Se-foo
 * @since 0.1
 */
abstract class ArgumentImpl implements Argument, ShallowCopyable<ArgumentImpl> {

    /**
     * オブジェクト引数の前提条件.
     *
     * @author Se-foo
     * @param <T>
     *            チェック対象のクラス.
     * @since 0.1
     */
    static class OfObj<T> extends ArgumentImpl implements Argument.OfObj<T> {

        /**
         * チェック対象.
         */
        T target;

        /*
         * (非 Javadoc)
         *
         * @see sp.base.var.ArgumentImpl#getTargetString()
         */
        @Override
        protected String getTargetString() {
            final String format = "%s:%s";
            return Optional.ofNullable(this.target)
                    .map(target -> String.format(format, target.getClass().getName(), target.toString()))
                    .orElse("null");
        }

        /**
         * 指定されたチェック対象から前提条件を作成する.
         *
         * @param target
         *            チェック対象.
         */
        public OfObj(T target) {
            super(true);

            // 初期化する.
            this.target = target;
        }

        /**
         * {@link #check()} == FALSE となる前提条件を作成する.
         */
        public OfObj() {
            super(false);
        }

        /**
         * チェック対象のチェック結果によって指定された関数を呼び出す.
         *
         * @param <R>
         *            関数の呼び出し結果クラス.
         * @param <X>
         *            関数の呼び出し中に発生するエラークラス.
         * @param success
         *            チェック対象のチェック結果が TRUE の場合に呼び出される関数.
         * @param fail
         *            チェック対象のチェック結果が FALSE の場合に呼び出される関数.
         * @return 関数の呼び出し結果.
         * @throws NullPointerException
         *             指定された関数が NULL の場合.
         * @throws X
         *             関数の呼び出し中にエラーが発生した場合.
         */
        <R, X extends Throwable> R ifPresentOrElse(
                FunctionWithThrown.OfObj<? super T, ? extends R, ? extends X> success, Supplier<? extends R> fail)
                throws X {
            Objects.requireNonNull(success);
            Objects.requireNonNull(fail);

            if (this.check()) {
                return success.apply(this.target);
            }
            return fail.get();
        }

        /*
         * (非 Javadoc)
         *
         * @see
         * sp.base.var.Argument.OfObj#orElseGet(java.util.function.Supplier)
         */
        @Override
        public T orElseGet(Supplier<? extends T> other) {
            Objects.requireNonNull(other);

            if (this.check()) {
                return this.target;
            }
            return other.get();
        }

        /*
         * (非 Javadoc)
         *
         * @see sp.base.var.Argument.OfObj#filter(sp.util.function.
         * PredicateWithThrown.OfObj)
         */
        @Override
        public <X extends Throwable> Argument.OfObj<T> filter(PredicateWithThrown.OfObj<? super T, ? extends X> filter)
                throws X {
            Objects.requireNonNull(filter);

            if (this.check()) {

                // throw X at filter#test.
                if (!filter.test(this.target)) {
                    this.toFail();
                }
            }
            return this;
        }

        /*
         * (非 Javadoc)
         *
         * @see sp.base.var.ArgumentImpl#clone()
         */
        @Override
        public ArgumentImpl.OfObj<T> clone() {
            @SuppressWarnings("unchecked")
            ArgumentImpl.OfObj<T> returnValue = (ArgumentImpl.OfObj<T>) super.clone();
            returnValue.target = this.target;
            return returnValue;
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            if (this.check()) {
                return Objects.hash(ArgumentImpl.OfObj.class, true, this.target);
            }
            return Objects.hash(ArgumentImpl.OfObj.class, false);
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object object) {
            boolean returnValue = false;
            if (object instanceof ArgumentImpl.OfObj) {
                ArgumentImpl.OfObj<?> instance = (ArgumentImpl.OfObj<?>) object;
                if (instance.check()) {
                    returnValue = Objects.equals(instance.target, this.target);
                } else {
                    returnValue = !this.check();
                }
            }
            return returnValue;
        }
    }

    /**
     * チェック結果.
     */
    boolean resultOfCheck;

    /*
     * (非 Javadoc)
     *
     * @see sp.base.var.Argument#check()
     */
    @Override
    public boolean check() {
        return this.resultOfCheck;
    }

    /**
     * チェック結果を失敗に変更する.
     */
    protected void toFail() {
        this.resultOfCheck = false;
    }

    /**
     * Constractor.
     *
     * @param check
     *            チェック結果.
     * @see #setResult(boolean)
     */
    protected ArgumentImpl(boolean check) {
        super();

        // 初期化する.
        this.resultOfCheck = check;
    }

    /*
     * (非 Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public ArgumentImpl clone() {
        ArgumentImpl returnValue = null;
        try {
            returnValue = (ArgumentImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            assert false;
            throw new InternalError(e);
        }
        returnValue.resultOfCheck = this.resultOfCheck;
        return returnValue;
    }

    /**
     * チェック対象の文字列変換結果を取得する.
     *
     * @return チェック対象の文字列変換結果.
     */
    protected abstract String getTargetString();

    /*
     * (非 Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.check()) {
            return new StringBuilder().append("checking=").append(this.getTargetString()).toString();
        }
        return "check=false";
    }
}
