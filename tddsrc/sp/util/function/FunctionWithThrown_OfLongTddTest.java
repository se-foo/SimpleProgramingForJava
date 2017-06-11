package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfLong} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfLongTddTest {

    /**
     * long 値が 0 以上であることを確認する.
     *
     * @param number
     *            確認対象.
     * @return 確認対象.
     * @throws IllegalArgumentException
     *             確認対象が 0 未満の場合.
     */
    static long notNegative(long number) {
        long abs = Math.abs(number);
        if (abs == number) {
            return number;
        }
        throw new IllegalArgumentException("fail to abs " + number + ".");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #apply("test")
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("-100");

        // #apply
        FunctionWithThrown.OfLong<IllegalArgumentException> instance = target -> notNegative(target);
        assertThat(instance.apply(100L), is(100L));

        // #compose
        assertThat(instance.compose(target -> target * -1).apply(-100L), is(100L));

        // #composeFunction
        assertThat(instance.composeFunction(target -> target * -1).apply(-100L), is(100L));

        // #andThen
        assertThat(instance.andThen(target -> target * -1).apply(100L), is(-100L));

        // #andThenFunction
        assertThat(instance.andThenFunction(target -> target * -1).apply(100L), is(-100L));

        // #toFunction
        assertThat(instance.toFunction().applyAsLong(100L), is(100L));

        // #apply
        instance.toFunction().applyAsLong(-100L);
    }

}
