package learn.javax.annotation;

import learn.javax.annotation.meta.TypeQualifierNickname;
import learn.javax.annotation.meta.TypeQualifierValidator;
import learn.javax.annotation.meta.When;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This qualifier is used to denote String values that should be a Regular
 * expression.
 * 
 */
@Documented
@Syntax("RegEx")
@TypeQualifierNickname
@Retention(RetentionPolicy.RUNTIME)
public @interface RegEx {
    When when() default When.ALWAYS;

    static class Checker implements TypeQualifierValidator<RegEx> {

        public When forConstantValue(RegEx annotation, Object value) {
            if (!(value instanceof String))
                return When.NEVER;

            try {
                Pattern.compile((String) value);
            } catch (PatternSyntaxException e) {
                return When.NEVER;
            }
            return When.ALWAYS;

        }

    }

}
