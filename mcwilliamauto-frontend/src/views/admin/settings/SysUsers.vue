<template>
  <CrudTable
    title="Admin Users"
    subtitle="Staff accounts with access to this admin panel"
    entity-name="User"
    :api="sysUserApi"
    :columns="[
      { prop: 'username', label: 'Username' },
      { prop: 'realName', label: 'Real Name' },
      { prop: 'email', label: 'Email' },
      { prop: 'phone', label: 'Phone', width: 130 },
      { prop: 'status', label: 'Enabled', type: 'switch', width: 100 }
    ]"
    :form-fields="[
      { prop: 'username', label: 'Username', type: 'text', required: true },
      { prop: 'password', label: 'Password (leave blank to keep unchanged when editing)', type: 'text' },
      { prop: 'realName', label: 'Real Name', type: 'text' },
      { prop: 'email', label: 'Email', type: 'text' },
      { prop: 'phone', label: 'Phone', type: 'text' },
      { prop: 'status', label: 'Enabled', type: 'switch' }
    ]"
  />
</template>

<script setup>
import CrudTable from '@/components/admin/CrudTable.vue'
import { sysUserApi } from '@/api/admin'
</script>
