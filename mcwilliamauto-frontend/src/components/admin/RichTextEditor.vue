<template>
  <Editor
    v-model="innerValue"
    tinymce-script-src="/tinymce/tinymce.min.js"
    :init="editorInit"
  />
</template>

<script setup>
/**
 * 用 TinyMCE(自托管, 见 scripts/copy-tinymce.mjs) 替代自研的contenteditable编辑器。
 * 组件对外的props/v-model接口保持不变(modelValue是HTML字符串，uploadUrl是图片上传接口)，
 * 所以调用方(CrudTable.vue / AboutUsEditor.vue)完全不用改。
 *
 * 字号/字体/多种排版：靠TinyMCE默认工具栏(fontfamily/fontsize等)自带，不需要额外插件。
 * 图片文字环绕：选中已插入的图片后，工具栏里点左对齐/右对齐，TinyMCE会给<img>加
 * style="float:left/right"，这是原生行为——编辑器里和前台v-html渲染出来的效果完全一致。
 */
import { computed } from 'vue'
import Editor from '@tinymce/tinymce-vue'
import request from '@/api/request'

const props = defineProps({
  modelValue: { type: String, default: '' },
  uploadUrl: { type: String, default: '/admin/upload/page-content-image' }
})
const emit = defineEmits(['update:modelValue'])

const innerValue = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v)
})

const editorInit = {
  height: 560,
  menubar: 'edit view insert format table tools',
  branding: false,
  promotion: false,
  plugins: 'advlist autolink lists link image imagetools table wordcount code fullscreen searchreplace visualblocks',
  toolbar:
    'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough forecolor backcolor | ' +
    'alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | ' +
    'link image table | removeformat | code fullscreen',
  toolbar_mode: 'sliding',
  font_size_formats: '10px 12px 14px 16px 18px 20px 24px 28px 32px 36px 48px',
  // 1200px = 前台全局 .container 的 max-width(1280px) 减去左右 padding(各40px)，
  // 即真正渲染出来的正文可用宽度。编辑画布必须跟这个宽度一致，多图并排/排版才是
  // 真正的所见即所得——如果这两处宽度以后要改，要一起改，不然又会出现"后台对齐、前台错位"。
  content_style:
    'body{font-family:Manrope,Arial,sans-serif;font-size:16px;line-height:1.7;color:#333;' +
    'max-width:1200px;margin:0 auto;padding:0 4px;} img{max-width:100%;height:auto}',
  image_advtab: true,
  image_caption: true,
  automatic_uploads: true,
  /** 编辑器里"上传图片"/粘贴图片时，实际调用我们自己后端的上传接口，返回可访问URL插入到内容里 */
  images_upload_handler: (blobInfo) =>
    new Promise((resolve, reject) => {
      const formData = new FormData()
      formData.append('file', blobInfo.blob(), blobInfo.filename())
      request
        .post(props.uploadUrl, formData, { headers: { 'Content-Type': 'multipart/form-data' } })
        .then((res) => resolve(res.url))
        .catch(() => reject('Image upload failed'))
    })
}
</script>
