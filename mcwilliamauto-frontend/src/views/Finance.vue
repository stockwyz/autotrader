<template>
  <div class="container page">
    <h1>Finance Department</h1>
    <p>
      We work with multiple lenders to get you the best rate possible, regardless of your credit history.
      Whether you have good credit, bad credit, or no credit, our finance team is here to help.
    </p>
    <el-row :gutter="24" style="margin-top:24px">
      <el-col :span="12">
        <router-link to="/car-loan-calculator"><el-button type="primary">Try Our Loan Calculator</el-button></router-link>
      </el-col>
      <el-col :span="12">
        <router-link to="/car-loan-application"><el-button type="warning">Apply For Credit</el-button></router-link>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.page { padding: 24px 16px; }
</style>
