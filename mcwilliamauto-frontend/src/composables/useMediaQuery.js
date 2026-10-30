import { onMounted, onUnmounted, ref } from 'vue'

// Match the CSS breakpoint and release the listener when the view is removed.
export function useMediaQuery(query) {
  const media = window.matchMedia(query)
  const matches = ref(media.matches)
  const update = (event) => { matches.value = event.matches }
  onMounted(() => media.addEventListener('change', update))
  onUnmounted(() => media.removeEventListener('change', update))
  return matches
}
