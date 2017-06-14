package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfObjToDouble} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfObjToDoubleTddTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #apply("test")
        this.thrown.expect(NumberFormatException.class);
        this.thrown.expectMessage("test");

        // #apply
        FunctionWithThrown.OfObjToDouble<String, NumberFormatException> instance = target -> Double.parseDouble(target);
        assertThat(instance.apply("100.0"), is(100.0));

        // #compose
        assertThat(instance.compose((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("100.0")), is(100.0));

        // #composeFunction
        assertThat(instance.composeFunction((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("100.0")), is(100.0));

        // andThen
        double value = instance.andThen(target -> target * -1).apply("100.0");
        assertThat(value == -100.0, is(true));

        // andThenFunction
        value = instance.andThenFunction(target -> target * -1).apply("100.0");
        assertThat(value == -100.0, is(true));

        // #toFunction
        assertThat(instance.toFunction().applyAsDouble("100.0"), is(100.0));

        // #apply
        instance.toFunction().applyAsDouble("test");
    }

}
