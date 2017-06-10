package sp.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 対象クラスのインスタンスが不変であることを示す.
 *
 * @author Se-foo
 * @since 0.1
 */
@Documented
@Target(ElementType.TYPE)
public @interface Immutable {
}
