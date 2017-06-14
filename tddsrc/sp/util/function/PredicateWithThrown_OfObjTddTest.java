package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link PredicateWithThrown.OfObj} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class PredicateWithThrown_OfObjTddTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #test("test")
        this.thrown.expect(NumberFormatException.class);
        this.thrown.expectMessage("test");

        // #test
        PredicateWithThrown.OfObj<String, NumberFormatException> instance = target -> Integer.parseInt(target) > 0;
        assertThat(instance.test("1"), is(true));
        assertThat(instance.test("0"), is(false));

        // #negate
        assertThat(instance.negate().test("1"), is(false));
        assertThat(instance.negate().test("0"), is(true));

        // #and
        assertThat(instance.and(target -> Double.parseDouble(target) > 0.0).test("1"), is(true));
        assertThat(instance.and(target -> Double.parseDouble(target) > 0.0).test("0"), is(false));

        // #andPredicate
        assertThat(instance.andPredicate(target -> !target.isEmpty()).test("1"), is(true));
        assertThat(instance.andPredicate(target -> !target.isEmpty()).test("0"), is(false));

        // #or
        assertThat(instance.or(target -> Double.parseDouble(target) > 0.0).test("1"), is(true));
        assertThat(instance.or(target -> Double.parseDouble(target) > 0.0).test("0"), is(false));

        // #orPredicate
        assertThat(instance.orPredicate(target -> !target.isEmpty()).test("1"), is(true));
        assertThat(instance.orPredicate(target -> !target.isEmpty()).test("0"), is(true));

        // #toPredicate
        assertThat(instance.toPredicate().test("1"), is(true));
        assertThat(instance.toPredicate().test("0"), is(false));

        // #test
        instance.toPredicate().test("test");
    }

}
