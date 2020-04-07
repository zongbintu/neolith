package neolith.sensitive;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskRule {
  public static final char MASK_CHR = '*';
  public static final String MASK_HIDE = "****";
  public static final String MASK_PASSWORD = "********";
  public static MaskRule DEFAULT = new MaskRule() {
    public String toMask(String value, Mask mask) {
      return toMaskString(value, mask.maskChar(), mask.leftKeep(), mask.rightKeep());
    }

    public String toMask(String value) {
      throw new UnsupportedOperationException("MaskRule DEFAULT");
    }
  };
  public static MaskRule HIDDEN = new MaskRule() {
    public String toMask(String value, Mask mask) {
      return value == null ? null : "****";
    }

    public String toMask(String value) {
      return value == null ? null : "****";
    }
  };
  public static MaskRule PASSWORD = new MaskRule() {
    public String toMask(String value, Mask mask) {
      return value == null ? null : "********";
    }

    public String toMask(String value) {
      return value == null ? null : "********";
    }
  };
  public static MaskRule CHN_NAME = new MaskRule(1, 0) {
    public String toMask(String value, Mask mask) {
      return doMask(value, mask.maskChar());
    }

    public String toMask(String value) {
      return doMask(value, this.maskChar);
    }

    private String doMask(String value, char maskChar) {
      if (value == null) {
        return null;
      }
      if (value.length() <= 3) {
        return toMaskString(value, maskChar, 1, 0);
      }
      return toMaskString(value, maskChar, 2, 0);
    }
  };
  public static MaskRule EMAIL = new MaskRule(1, 0,
      Pattern.compile("(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)")) {
    public String toMask(String value, Mask mask) {
      return doMask(value, mask.maskChar());
    }

    public String toMask(String value) {
      return doMask(value, this.maskChar);
    }

    private String doMask(String value, char maskChar) {
      if (value == null) {
        return null;
      }
      int index = value.indexOf("@");
      if (index == -1) {
        return value;
      }
      int start = 2;
      if (index >= 5) {
        start = 3;
      }
      return toMaskString(value, maskChar, start, value.length() - index);
    }
  };
  public static MaskRule ID_CARD = new MaskRule(1, 1,
      Pattern.compile(
          "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}([0-9]|X|x)|[1-9]\\d{7}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}"));
  public static MaskRule PHONE_FIXED = new MaskRule(3, 3,
      Pattern.compile("(0[0-9]{2,3}\\-?)[0-9]{7,8}")) {
    private static final String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";
    private Pattern PATTERN_ZIPCODE = Pattern.compile("^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$");

    public String toMask(String value, Mask mask) {
      return doMask(value, mask.maskChar());
    }

    public String toMask(String value) {
      return doMask(value, this.maskChar);
    }

    private String doMask(String value, char maskChar) {
      if (value == null) {
        return null;
      }
      String zone = getZoneNumber(value);
      int start = 3;
      if (zone.length() > 3) {
        start = 4;
      }
      return toMaskString(value, maskChar, start, this.rightKeep);
    }

    private String getZoneNumber(String value) {
      try {
        Matcher matcher = this.PATTERN_ZIPCODE.matcher(value);
        if (matcher.find()) {
          return matcher.group(1);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return value;
    }
  };
  public static MaskRule PHONE_MOBILE = new MaskRule(0, 3,
      Pattern.compile("(\\+?86)?(1[3578][0-9]{9})"));
  public static MaskRule BANK_CARD = new MaskRule(1, 1,
      Pattern.compile("[0-9]{19}|[0-9]{16}"));
  public static MaskRule ADDRESS = new MaskRule(6, 0,
      Pattern.compile(
          "(北京|天津|上海|重庆)市?[a-zA-Z0-9\\u4e00-\\u9fa5]*|(黑龙江|吉林|辽宁|河南|河北|山东|山西|陕西|甘肃|青海|四川|湖北|湖南|安徽|江苏|浙江|江西|贵州|云南|广东|福建|海南|台湾)省?[a-zA-Z0-9\\u4e00-\\u9fa5]*|(新疆|西藏|宁夏|内蒙古|广西)(自治区)?[a-zA-Z0-9\\u4e00-\\u9fa5]*")) {
    public String toMask(String value, Mask mask) {
      return doMask(value, mask.maskChar());
    }

    public String toMask(String value) {
      return doMask(value, this.maskChar);
    }

    private String doMask(String value, char maskChar) {
      if (value == null) {
        return null;
      }
      int start = this.leftKeep;
      int end = this.rightKeep;
      if (value.length() <= 5) {
        start = 1;
        end = 1;
      }
      if (value.length() == 6) {
        start = 2;
        end = 2;
      }
      if (value.length() >= 7) {
        start = 6;
        end = 0;
      }
      return toMaskString(value, maskChar, start, end);
    }
  };
  protected char maskChar = '*';
  protected int leftKeep = 0;
  protected int rightKeep = 0;
  protected Pattern pattern;

  public MaskRule(int leftKeep, int rightKeep) {
    this.leftKeep = leftKeep;
    this.rightKeep = rightKeep;
  }

  public MaskRule(int leftKeep, int rightKeep, Pattern pattern) {
    this('*', leftKeep, rightKeep, pattern);
  }

  public MaskRule(char maskChar, int leftKeep, int rightKeep, Pattern pattern) {
    this.maskChar = maskChar;
    this.leftKeep = leftKeep;
    this.rightKeep = rightKeep;
    this.pattern = pattern;
  }

  public char getMaskChar() {
    return this.maskChar;
  }

  public void setMaskChar(char maskChar) {
    this.maskChar = maskChar;
  }

  public int getLeftKeep() {
    return this.leftKeep;
  }

  public void setLeftKeep(int leftKeep) {
    this.leftKeep = leftKeep;
  }

  public int getRightKeep() {
    return this.rightKeep;
  }

  public void setRightKeep(int rightKeep) {
    this.rightKeep = rightKeep;
  }

  public Pattern getPattern() {
    return this.pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public String toMask(String value) {
    return toMaskString(value, this.maskChar, this.leftKeep, this.rightKeep);
  }

  public String toMask(String value, Mask mask) {
    return toMaskString(value, mask.maskChar(), this.leftKeep, this.rightKeep);
  }

  /**
   * @deprecated
   */
  public static String toMaskString(String value, MaskRule... MaskRules) {
    if (value == null) {
      return null;
    }
    if (MaskRules.length > 0) {
      for (MaskRule MaskRule : MaskRules) {
        if (MaskRule.getPattern() != null) {
          int idx = 0;
          StringBuilder sb = new StringBuilder(value.length());
          Matcher matcher = MaskRule.getPattern().matcher(value);
          while (matcher.find()) {
            sb.append(value.substring(idx, matcher.start()))
                .append(MaskRule.toMask(matcher.group()));
            idx = matcher.end();
          }
          if (idx > 0) {
            sb.append(value.substring(idx)).toString();
            value = sb.toString();
          }
        }
      }
    }
    return value;
  }

  public static String toMaskString(String value, int leftKeep, int rightKeep) {
    return toMaskString(value, '*', leftKeep, rightKeep);
  }

  public static String toMaskString(String value, char maskChar, int leftKeep, int rightKeep) {
    if (value == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();

    int length = value.length();
    int totalKeep = leftKeep + rightKeep;
    if (totalKeep == 0) {
      for (int i = 0; i < length; i++) {
        sb.append(maskChar);
      }
      return sb.toString();
    }
    if (totalKeep < length) {
      for (int j = 0; j < length; j++) {
        if (j < leftKeep) {
          sb.append(value.charAt(j));
        } else if (j > length - rightKeep - 1) {
          sb.append(value.charAt(j));
        } else {
          sb.append(maskChar);
        }
      }
      return sb.toString();
    }
    return value;
  }

  private static final MaskRule MASK_RULE = new MaskRule();
  private static Map<String, MaskRule> maskRules = new ConcurrentHashMap(16);

  static {
    addRule("name", CHN_NAME);
    addRule("password", PASSWORD);
    addRule("idCard", ID_CARD);
    addRule("bankCard", BANK_CARD);
    addRule("mobile", PHONE_MOBILE);
    addRule("phone", PHONE_FIXED);
    addRule("address", ADDRESS);
    addRule("email", EMAIL);
  }

  public static MaskRule addRule(String key, MaskRule MaskRule) {
    maskRules.put(key, MaskRule);
    return MASK_RULE;
  }

  public static MaskRule clearRules() {
    maskRules.clear();
    return MASK_RULE;
  }

  public static MaskRule removeRule(String key) {
    maskRules.remove(key);
    return MASK_RULE;
  }

  public static Map<String, ?> toMask(Map<String, ?> map) {
    Map<String, Object> clone = new HashMap(map);
    for (Entry<String, MaskRule> entry : maskRules.entrySet()) {
      if (clone.containsKey(entry.getKey())) {
        Object value = clone.get(entry.getKey());
        if ((value instanceof String)) {
          clone.put(entry.getKey(), ((MaskRule) entry.getValue()).toMask((String) value));
        }
      }
    }
    return clone;
  }

  public MaskRule() {
  }
}
