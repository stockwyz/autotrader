/**
 * 车身类型可选图标清单——使用 Twemoji（Twitter开源彩色表情图标，CC-BY 4.0协议）
 * 直接引用CDN上的SVG文件，不需要额外安装npm包，也不会有emoji在不同系统/浏览器下
 * 显示不一致的问题。前台首页(BodyStyleNav.vue)和后台图标选择器(DictBodyStyles.vue)
 * 共用这一份清单，保证"能选的图标"和"能显示的图标"永远是同一个集合。
 */

const TWEMOJI_BASE = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg'

export function twemojiUrl(code) {
  return `${TWEMOJI_BASE}/${code}.svg`
}

export const BODY_STYLE_ICON_OPTIONS = [
  { value: '1f697', label: 'Sedan' },
  { value: '1f699', label: 'SUV' },
  { value: '1f695', label: 'Taxi' },
  { value: '1f6fb', label: 'Pickup Truck' },
  { value: '1f69a', label: 'Delivery Truck' },
  { value: '1f69b', label: 'Articulated Truck' },
  { value: '1f690', label: 'Minibus / Van' },
  { value: '1f68c', label: 'Bus' },
  { value: '1f3ce', label: 'Racing / Convertible' },
  { value: '1f6a4', label: 'Speedboat' },
  { value: '26f5', label: 'Sailboat' },
  { value: '1f6f4', label: 'Scooter' }
]

/** 数据库没配图标、或者配了个不在清单里的值时，统一兜底显示这个 */
export const DEFAULT_BODY_STYLE_ICON_CODE = '1f697'
