package net.devkhan.opencrm.module;


import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Configuration
public @interface OCPlugin {
	String value() default "";
	String loadMethod() default "load";
	String unLoadMethod() default "unload";
}
