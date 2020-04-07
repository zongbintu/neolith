package neolith.sensitive;

import java.util.Map;

/**
 * 脱敏工具类
 *
 * @auther Tu
 * @date 2019/4/15
 * @email enum@foxmail.com
 */
public class MaskUtils {

  private MaskUtils() {
    //no instance
  }

  public static Map<String, ?> toMask(Map<String, ?> map) {
    return MaskRule.toMask(map);
  }

  /**
   * 脱敏中文姓名
   *
   * @param value 姓名
   * @return 王思聪 王**
   */
  public static String maskName(String value) {
    return maskString(value, MaskRule.CHN_NAME);
  }

  /**
   * 脱敏身份证号
   *
   * @param value 身份证号
   */
  public static String maskIdCard(String value) {
    return maskString(value, MaskRule.ID_CARD);
  }

  /**
   * 脱敏手机号
   *
   * @param value 手机号
   */
  public static String maskMobile(String value) {
    return maskString(value, MaskRule.PHONE_MOBILE);
  }

  /**
   * 脱敏银行卡号
   *
   * @param value 银行卡号
   */
  public static String maskBankCard(String value) {
    return maskString(value, MaskRule.BANK_CARD);
  }

  /**
   * 脱敏字符串
   *
   * @param value 要脱敏的字符串
   * @param maskRule {@link MaskRule}
   */
  public static String maskString(String value, MaskRule maskRule) {
    return maskRule.toMask(value);
  }

  /**
   * 脱敏字符串
   *
   * @param value 要脱敏的字符串
   * @param leftKeep 保留左边位数
   * @param rightKeep 保留右边位数
   */
  public static String maskString(String value, int leftKeep, int rightKeep) {
    return MaskRule.toMaskString(value, leftKeep, rightKeep);
  }
}

