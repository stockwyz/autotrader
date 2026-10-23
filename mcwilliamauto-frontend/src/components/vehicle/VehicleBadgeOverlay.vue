<template>
  <div class="vb-overlay">
    <div class="vb-badges">
      <img v-if="vehicle.isFeatured" :src="featuredImg" class="vb-badge vb-badge-featured" alt="Featured" />
      <img v-if="vehicle.isOneOwner" :src="oneOwnerImg" class="vb-badge vb-badge-owner" alt="One Owner" />
      <img v-if="vehicle.isLowKm" :src="lowKmImg" class="vb-badge vb-badge-lowkm" alt="Low KM" />
    </div>
    <img v-if="vehicle.isSold" :src="soldImg" class="vb-sold" alt="Sold" />
  </div>
</template>

<script setup>
/**
 * 车辆促销徽章(Featured/One Owner/Low KM/Sold)叠加层，首页卡片、搜索结果卡片、详情页大图统一用这一个组件，
 * 保证不管图片实际渲染多大，徽章跟图片的相对比例都完全一致——所有尺寸都用相对于这个组件自身(铺满图片容器)
 * 宽度的百分比，不用固定像素。
 */
import soldImg from '@/assets/badges/sold.png'
import featuredImg from '@/assets/badges/featured.png'
import oneOwnerImg from '@/assets/badges/one-owner.png'
import lowKmImg from '@/assets/badges/low-km.png'

defineProps({ vehicle: { type: Object, required: true } })
</script>

<style scoped>
.vb-overlay { position: absolute; inset: 0; pointer-events: none; }

.vb-badges {
  position: absolute; top: 3.6%; left: 3.6%;
  display: flex; flex-direction: column; gap: 2%;
  width: 30%; /* 适当缩小左侧三个徽章的显示大小 */
  z-index: 2;
}
.vb-badge { width: 100%; height: auto; display: block; filter: drop-shadow(0 3px 8px rgba(23,34,59,.35)); }
.vb-badge-owner { width: 66.6%; }
.vb-badge-lowkm { width: 80%; }

.vb-sold {
  position: absolute; top: 50%; left: 50%;
  width: 39%; height: auto;
  transform: translate(-50%, -50%) rotate(-15deg);
  filter: drop-shadow(0 6px 16px rgba(0,0,0,.4));
  z-index: 3;
}
</style>
