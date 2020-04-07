package neolith.sensitive;

/**
 * 脱敏类型
 * @auther Tu
 * @date 2019/4/15
 * @email enum@foxmail.com
 */
public enum MaskType {
  DEFAULT(MaskRule.DEFAULT),
  HIDDEN(MaskRule.HIDDEN),
  CHN_NAME(MaskRule.CHN_NAME),
  PASSWORD(MaskRule.PASSWORD),
  PHONE_MOBILE(MaskRule.PHONE_MOBILE),
  PHONE_FIXED(MaskRule.PHONE_FIXED),
  BANK_CARD(MaskRule.BANK_CARD),
  ID_CARD(MaskRule.ID_CARD),
  EMAIL(MaskRule.EMAIL),
  ADDRESS(MaskRule.ADDRESS);

  private MaskRule maskRule;

  MaskType(MaskRule maskRule) {
    this.maskRule = maskRule;
  }

  public MaskRule getRule() {
    return this.maskRule;
  }

  public String toMask(String value, Mask mask) {
    return this.maskRule.toMask(value, mask);
  }
}