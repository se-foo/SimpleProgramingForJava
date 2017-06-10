package sp.lang;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Objects;
import java.util.Optional;

import org.junit.Test;

import sp.base.NonNullReturnValue;

/**
 * {@link DeepCopyable} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class DeepCopyableTddTest {

    /**
     * Sample Class.
     *
     * @author Se-foo
     * @since 0.1
     */
    static class Sample implements DeepCopyable<Sample> {

        /**
         * Inner Object.
         */
        String inner;

        /**
         * Constractor.
         *
         * @param inner
         *            Inner String.
         */
        public Sample(String inner) {
            super();
            this.inner = inner;
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#clone()
         */
        @NonNullReturnValue
        @Override
        protected DeepCopyableTddTest.Sample clone() {
            DeepCopyableTddTest.Sample returnValue = null;
            try {
                returnValue = (DeepCopyableTddTest.Sample) super.clone();
            } catch (CloneNotSupportedException e) {
                assert false;
                throw new InternalError(e);
            }
            returnValue.inner = this.inner;
            return returnValue;
        }

        /*
         * (非 Javadoc)
         *
         * @see sp.lang.DeepCopyable#deepclone()
         */
        @NonNullReturnValue
        @Override
        public DeepCopyableTddTest.Sample deepclone() {
            DeepCopyableTddTest.Sample returnValue = this.clone();
            returnValue.inner = Optional.ofNullable(this.inner).map(target -> new String(target)).orElse(null);
            return returnValue;
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hash(DeepCopyableTddTest.Sample.class, this.inner);
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object object) {
            boolean returnValue = false;
            if (object instanceof DeepCopyableTddTest.Sample) {
                DeepCopyableTddTest.Sample instance = (DeepCopyableTddTest.Sample) object;
                returnValue = Objects.equals(instance.inner, this.inner);
            }
            return returnValue;
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @NonNullReturnValue
        @Override
        public String toString() {
            return Objects.toString(this.inner);
        }
    }

    @Test
    public void test() {

        // #clone
        DeepCopyableTddTest.Sample instance = new DeepCopyableTddTest.Sample("inner");
        DeepCopyableTddTest.Sample result = instance.deepclone();

        // test @NonNullReturnValue
        assertThat(result, notNullValue());

        // test result of #clone
        assertThat(result != instance, is(true));
        assertThat(result.getClass() == instance.getClass(), is(true));
        assertThat(Objects.equals(result, instance), is(true));
        assertThat(result.inner != instance.inner, is(true));
    }

}
