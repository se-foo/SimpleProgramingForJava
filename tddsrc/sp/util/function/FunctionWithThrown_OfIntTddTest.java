package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfInt} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfIntTddTest {

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

        // Check exception from #apply("test")
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("-100");

        // #apply
        FunctionWithThrown.OfInt<IllegalArgumentException> instance = target -> notNegative(target);
        assertThat(instance.apply(100), is(100));

        // #compose
        assertThat(instance.compose(target -> target * -1).apply(-100), is(100));

        // #composeFunction
        assertThat(instance.composeFunction(target -> target * -1).apply(-100), is(100));

        // #andThen
        assertThat(instance.andThen(target -> target * -1).apply(100), is(-100));

        // #andThenFunction
        assertThat(instance.andThenFunction(target -> target * -1).apply(100), is(-100));

        // #toFunction
        assertThat(instance.toFunction().applyAsInt(100), is(100));

        // #apply
        instance.toFunction().applyAsInt(-100);
    }

}
