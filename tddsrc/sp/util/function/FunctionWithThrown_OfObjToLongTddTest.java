package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfObjToLong} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfObjToLongTddTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #apply("test")
        this.thrown.expect(NumberFormatException.class);
        this.thrown.expectMessage("test");

        // #apply
        FunctionWithThrown.OfObjToLong<String, NumberFormatException> instance = target -> Long.parseLong(target);
        assertThat(instance.apply("100"), is(100L));

        // #compose
        assertThat(instance.compose((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("100")), is(100L));

        // #composeFunction
        assertThat(instance.composeFunction((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("100")), is(100L));

        // andThen
        double value = instance.andThen(target -> target * -1).apply("100");
        assertThat(value == -100L, is(true));

        // andThenFunction
        value = instance.andThenFunction(target -> target * -1).apply("100");
        assertThat(value == -100L, is(true));

        // #toFunction
        assertThat(instance.toFunction().applyAsLong("100"), is(100L));

        // #apply
        instance.toFunction().applyAsLong("test");
    }

}
