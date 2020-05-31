package learn.org.codehaus.mojo.animal_sniffer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Documented
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.TYPE})
public @interface IgnoreJRERequirement {}


/* Location:              /Users/wangyong14/.gradle/caches/modules-2/files-2.1/org.codehaus.mojo/animal-sniffer-annotations/1.16/2ad95a6e62dd84969dbca54382c855f17d4ee7b1/animal-sniffer-annotations-1.16.jar!/org/codehaus/mojo/animal_sniffer/IgnoreJRERequirement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */