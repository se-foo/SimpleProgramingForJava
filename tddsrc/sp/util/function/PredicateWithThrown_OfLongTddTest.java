package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link PredicateWithThrown.OfLong} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class PredicateWithThrown_OfLongTddTest {

    /**
     * long 値を int 型にキャストする.
     *
     * @param number
     *            キャスト元 long 値.
     * @return キャスト後の int 値.
     * @throws ClassCastException
     *             キャストの前後で値が異なる場合.
     */
    static int cast(long number) {
        int intNumber = (int) number;
        if ((long) intNumber == number) {
            return intNumber;
        }
        throw new ClassCastException("fail to cast " + number + ".");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #test(Long.MAX_VALUE)
        this.thrown.expect(ClassCastException.class);
        this.thrown.expectMessage(String.valueOf(Long.MAX_VALUE));

        // #test
        PredicateWithThrown.OfLong<ClassCastException> instance = target -> cast(target) > 0;
        assertThat(instance.test(1), is(true));

        // #negate
        assertThat(instance.negate().test(1), is(false));

        // #and
        assertThat(instance.and(target -> target < 0).test(1), is(false));

        // #andPredicate
        assertThat(instance.andPredicate(target -> target < 0).test(1), is(false));

        // #or
        assertThat(instance.or(target -> target < 0).test(1), is(true));

        // #orPredicate
        assertThat(instance.orPredicate(target -> target < 0).test(1), is(true));

        // #toPredicate
        assertThat(instance.toPredicate().test(1), is(true));

        // #test
        instance.toPredicate().test(Long.MAX_VALUE);
    }

}
