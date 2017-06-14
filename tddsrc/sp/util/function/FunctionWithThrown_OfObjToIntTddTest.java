package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfObjToInt} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfObjToIntTddTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #apply("test")
        this.thrown.expect(NumberFormatException.class);
        this.thrown.expectMessage("test");

        // #apply
        FunctionWithThrown.OfObjToInt<String, NumberFormatException> instance = target -> Integer.parseInt(target);
        assertThat(instance.apply("100"), is(100));

        // #compose
        assertThat(
                instance.compose((StringBuilder target) -> new String(target)).apply(new StringBuilder().append("100")),
                is(100));

        // #composeFunction
        assertThat(instance.composeFunction((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("100")), is(100));

        // andThen
        double value = instance.andThen(target -> target * -1).apply("100");
        assertThat(value == -100, is(true));

        // andThenFunction
        value = instance.andThenFunction(target -> target * -1).apply("100");
        assertThat(value == -100, is(true));

        // #toFunction
        assertThat(instance.toFunction().applyAsInt("100"), is(100));

        // #apply
        instance.toFunction().applyAsInt("test");
    }

}
