/**
 * TinyMCE 官方推荐的"自托管"接入方式：把 node_modules/tinymce 整个目录复制到 public/tinymce，
 * 由 Vite 原样静态托管，编辑器组件通过 tinymce-script-src="/tinymce/tinymce.min.js" 加载。
 * 这样完全不需要在代码里用 import 去引用 tinymce 内部的插件/皮肤文件路径，
 * 避免因为具体版本内部目录结构差异导致打包报错。
 * npm install 后会自动执行一次(见 package.json 的 postinstall)；也可以手动重跑: node scripts/copy-tinymce.mjs
 */
import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url'

const __dirname = path.dirname(fileURLToPath(import.meta.url))
const src = path.resolve(__dirname, '../node_modules/tinymce')
const dest = path.resolve(__dirname, '../public/tinymce')

function copyDir(srcDir, destDir) {
  fs.mkdirSync(destDir, { recursive: true })
  for (const entry of fs.readdirSync(srcDir, { withFileTypes: true })) {
    const s = path.join(srcDir, entry.name)
    const d = path.join(destDir, entry.name)
    if (entry.isDirectory()) copyDir(s, d)
    else fs.copyFileSync(s, d)
  }
}

if (!fs.existsSync(src)) {
  console.warn('[copy-tinymce] node_modules/tinymce not found — run `npm install` first.')
  process.exit(0)
}

copyDir(src, dest)
console.log('[copy-tinymce] copied tinymce assets to public/tinymce')
