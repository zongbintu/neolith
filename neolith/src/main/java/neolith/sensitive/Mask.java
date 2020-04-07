package neolith.sensitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏注解
 * @auther Tu
 * @date 2019/4/15
 * @email enum@foxmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD})
public @interface Mask {
  MaskType value() default MaskType.DEFAULT;

  int leftKeep() default 0;

  int rightKeep() default 0;

  char maskChar() default '*';
}