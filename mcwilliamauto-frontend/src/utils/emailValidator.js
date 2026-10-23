/** 简单但足够严谨的邮箱格式校验，跟 phoneValidator.js 保持同样的使用方式 */

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function isValidEmail(value) {
  if (!value) return false
  return EMAIL_REGEX.test(String(value).trim())
}

/** Element Plus el-form rules 用的 validator 函数：必填 + 格式一起校验 */
export function emailValidator(rule, value, callback) {
  if (!value) {
    callback(new Error('Email is required'))
  } else if (!isValidEmail(value)) {
    callback(new Error('Please enter a valid email address'))
  } else {
    callback()
  }
}

/** 标准 el-form rules 数组，直接展开到 rules.email 上使用（必填+格式合并成同一条规则，
 *  避免跟el-form-item的required属性叠加出两条规则、互相打架，参考phoneValidator.js的教训） */
export const emailRules = [
  { required: true, validator: emailValidator, trigger: 'blur' }
]
