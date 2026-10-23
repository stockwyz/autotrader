/**
 * v-reveal: 元素滚动进入视口时，从透明渐显到完全不透明（配合小幅度上移，视觉上更自然）。
 * 用IntersectionObserver实现，只在元素第一次进入视口时触发一次，避免反复滚动来回触发造成闪烁。
 * 用法：直接在模板里 <VehicleCard v-reveal ... />，不需要额外传参。
 */
export const vReveal = {
  mounted(el) {
    el.classList.add('reveal-init')
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            el.classList.add('reveal-visible')
            observer.unobserve(el)
          }
        })
      },
      { threshold: 0.12, rootMargin: '0px 0px -40px 0px' }
    )
    observer.observe(el)
  }
}
