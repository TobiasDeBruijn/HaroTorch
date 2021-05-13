package nl.thedutchmc.harotorch.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation indicates that the return value may be null. You should probably perform a null check on the returned value.
 * @author Tobias de Bruijn
 * @since 2.2.2
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {}
