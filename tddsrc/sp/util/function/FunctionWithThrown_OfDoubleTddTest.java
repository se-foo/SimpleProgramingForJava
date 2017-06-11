package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfDouble} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfDoubleTddTest {

    /**
     * double 値が 0 以上であることを確認する.
     *
     * @param number
     *            確認対象.
     * @return 確認対象.
     * @throws IllegalArgumentException
     *             確認対象が 0 未満の場合.
     */
    static double notNegative(double number) {
        double abs = Math.abs(number);
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
        this.thrown.expectMessage("-100.0");

        // #apply
        FunctionWithThrown.OfDouble<IllegalArgumentException> instance = target -> notNegative(target);
        assertThat(instance.apply(100.0), is(100.0));

        // #compose
        assertThat(instance.compose(target -> target * -1).apply(-100.0), is(100.0));

        // #composeFunction
        assertThat(instance.composeFunction(target -> target * -1).apply(-100.0), is(100.0));

        // #andThen
        assertThat(instance.andThen(target -> target * -1).apply(100.0), is(-100.0));

        // #andThenFunction
        assertThat(instance.andThenFunction(target -> target * -1).apply(100.0), is(-100.0));

        // #toFunction
        assertThat(instance.toFunction().applyAsDouble(100.0), is(100.0));

        // #apply
        instance.toFunction().applyAsDouble(-100.0);
    }

}
