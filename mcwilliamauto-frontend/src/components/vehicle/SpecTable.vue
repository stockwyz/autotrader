<template>
  <el-descriptions title="Vehicle Specs" :column="2" border>
    <el-descriptions-item label="Make">{{ v.makeName }}</el-descriptions-item>
    <el-descriptions-item label="Model">{{ v.modelName }}</el-descriptions-item>
    <el-descriptions-item label="Year">{{ v.year }}</el-descriptions-item>
    <el-descriptions-item label="Body Style">{{ v.bodyStyleName }}</el-descriptions-item>
    <el-descriptions-item label="Odometer">{{ v.odometerKm?.toLocaleString() }} Km</el-descriptions-item>
    <el-descriptions-item label="Transmission">{{ v.transmissionName }}</el-descriptions-item>
    <el-descriptions-item label="Engine">{{ v.engineName }}</el-descriptions-item>
    <el-descriptions-item label="Engine Size">{{ v.engineSize }}</el-descriptions-item>
    <el-descriptions-item label="Driveline">{{ v.driveline }}</el-descriptions-item>
    <el-descriptions-item label="Exterior">{{ v.exteriorColorName }}</el-descriptions-item>
    <el-descriptions-item label="Interior">{{ v.interiorColorName }}</el-descriptions-item>
    <el-descriptions-item label="Doors">{{ v.doors }}</el-descriptions-item>
    <el-descriptions-item label="Passengers">{{ v.passengers }}</el-descriptions-item>
    <el-descriptions-item label="Fuel Type">{{ v.fuelTypeName }}</el-descriptions-item>
    <el-descriptions-item label="City Fuel">{{ v.cityFuelConsumption }}</el-descriptions-item>
    <el-descriptions-item label="Hwy Fuel">{{ v.hwyFuelConsumption }}</el-descriptions-item>
    <el-descriptions-item label="Stock Number">{{ v.stockNo }}</el-descriptions-item>
    <el-descriptions-item label="VIN">{{ v.vin }}</el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
defineProps({ v: { type: Object, required: true } })
</script>
