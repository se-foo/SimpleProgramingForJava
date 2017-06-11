/**
 *
 */
package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link PredicateWithThrown.OfInt} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class PredicateWithThrown_OfIntTddTest {

    /**
     * int 値が 0 以上であることを確認する.
     *
     * @param number
     *            確認対象.
     * @return 確認対象.
     * @throws IllegalArgumentException
     *             確認対象が 0 未満の場合.
     */
    static int notNegative(int number) {
        int abs = Math.abs(number);
        if (abs == number) {
            return number;
        }
        throw new IllegalArgumentException("fail to abs " + number + ".");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #test(-1)
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("-1");

        // #test
        PredicateWithThrown.OfInt<IllegalArgumentException> instance = target -> notNegative(target) < 10;
        assertThat(instance.test(1), is(true));

        // #negate
        assertThat(instance.negate().test(1), is(false));

        // #and
        assertThat(instance.and(target -> target == 5).test(1), is(false));

        // #andPredicate
        assertThat(instance.andPredicate(target -> target == 5).test(1), is(false));

        // #or
        assertThat(instance.or(target -> target == 5).test(1), is(true));

        // #orPredicate
        assertThat(instance.orPredicate(target -> target == 5).test(1), is(true));

        // #toPredicate
        assertThat(instance.toPredicate().test(1), is(true));

        // #test
        instance.toPredicate().test(-1);
    }

}
