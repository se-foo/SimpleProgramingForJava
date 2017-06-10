package sp.lang;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Objects;

import org.junit.Test;

import sp.base.NonNullReturnValue;

/**
 * {@link ShallowCopyable} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class ShallowCopyableTddTest {

    /**
     * Sample Class.
     *
     * @author Se-foo
     * @since 0.1
     */
    static class Sample implements ShallowCopyable<Sample> {

        /**
         * Inner Object.
         */
        Object inner;

        /**
         * Constractor.
         *
         * @param inner
         *            Inner Object.
         */
        public Sample(Object inner) {
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
        public Sample clone() {
            Sample returnValue = null;
            try {
                returnValue = (Sample) super.clone();
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
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hash(Sample.class, this.inner);
        }

        /*
         * (非 Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object object) {
            boolean returnValue = false;
            if (object instanceof Sample) {
                Sample instance = (Sample) object;
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
        Sample instance = new Sample(new Object());
        Sample result = instance.clone();

        // test @NonNullReturnValue
        assertThat(result, notNullValue());

        // test result of #clone
        assertThat(result != instance, is(true));
        assertThat(result.getClass() == instance.getClass(), is(true));
        assertThat(Objects.equals(result, instance), is(true));
    }

}
