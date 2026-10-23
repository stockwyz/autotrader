<template>
  <CrudTable
    title="Pro-Label"
    subtitle="Reusable promotional label graphics (e.g. Featured ribbon) with position/size/angle you can fine-tune"
    entity-name="Label"
    :api="dictApi.proLabels"
    new-route="/admin/dict/pro-labels/new"
    :edit-route-resolver="row => `/admin/dict/pro-labels/edit/${row.id}`"
    :columns="[
      { prop: 'imageUrl', label: 'Image', type: 'image', width: 100 },
      { prop: 'name', label: 'Name' },
      { prop: 'widthPercent', label: 'Width %', width: 90 },
      { prop: 'heightPercent', label: 'Height %', width: 90 },
      { prop: 'angle', label: 'Angle °', width: 90 },
      { prop: 'posX', label: 'X %', width: 80 },
      { prop: 'posY', label: 'Y %', width: 80 },
      { prop: 'sortNo', label: 'Sort Order', width: 100 },
      { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
    ]"
    :form-fields="[]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { dictApi } from '@/api/admin'
</script>
