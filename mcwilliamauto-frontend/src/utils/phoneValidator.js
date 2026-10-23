/**
 * 电话号码校验：兼容北美(美国/加拿大 NANP，10位)和中国大陆手机号(11位，1开头)两种格式。
 * 允许常见的分隔符/空格/括号/国码前缀，校验时先去除这些再匹配纯数字规则。
 *
 * 美加(NANP): 可选 +1/1 前缀，区号首位2-9，共10位数字
 *   例: 4165551234 / (416) 555-1234 / 416-555-1234 / +1 416 555 1234
 * 中国大陆手机号: 1开头，第二位3-9，共11位数字
 *   例: 13812345678 / +86 138 1234 5678 / 138-1234-5678
 */

const NA_PHONE_REGEX = /^1?[2-9]\d{2}[2-9]\d{6}$/
const CN_MOBILE_REGEX = /^(86)?1[3-9]\d{9}$/

export function isValidPhone(value) {
  if (!value) return false
  const digits = String(value).replace(/[\s\-().+]/g, '')
  if (!/^\d+$/.test(digits)) return false
  return NA_PHONE_REGEX.test(digits) || CN_MOBILE_REGEX.test(digits)
}

/** Element Plus el-form rules 用的 validator 函数 */
export function phoneValidator(rule, value, callback) {
  if (!value) {
    callback(new Error('Phone is required'))
  } else if (!isValidPhone(value)) {
    callback(new Error('Please enter a valid North American or China phone number'))
  } else {
    callback()
  }
}

/** 标准 el-form rules 数组，直接展开到 rules.phone 上使用 */
export const phoneRules = [
  { required: true, validator: phoneValidator, trigger: 'blur' }
]
