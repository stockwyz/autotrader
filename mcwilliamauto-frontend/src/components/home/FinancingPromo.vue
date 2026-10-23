<template>
  <div class="container promo-wrap">
    <div class="promo">
      <div class="promo-text">
        <div class="eyebrow-label">Financing · Limited-time approvals</div>
        <h2>Get the payments you can afford.</h2>
        <p>Tell us a little about yourself and we will match you with a bi-weekly payment that fits your budget.</p>
        <router-link to="/car-loan-application"><el-button type="primary" size="large">Apply for credit</el-button></router-link>
      </div>
      <div class="steps">
        <div class="step"><b>01</b><span>Choose</span></div>
        <div class="step"><b>02</b><span>Apply</span></div>
        <div class="step"><b>03</b><span>Drive</span></div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.promo-wrap.container { max-width: 1680px; padding: 0 24px; }
.promo { background: linear-gradient(120deg, var(--s-soft) 0%, var(--s-soft2) 100%); border: 1px solid var(--s-line); border-radius: 16px; padding: 44px 48px; display: flex; justify-content: space-between; align-items: center; gap: 40px; }
.promo h2 { font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 42px; line-height: 1.05; margin: 0 0 14px; max-width: 560px; color: var(--s-ink); }
.promo p { color: var(--s-text2); font-size: 16px; line-height: 1.55; max-width: 520px; margin: 0 0 26px; }
.steps { display: flex; gap: 12px; }
.step { width: 124px; background: #fff; border: 1px solid var(--s-line2); border-radius: 10px; padding: 18px 0; text-align: center; }
.step b { display: block; font-family: 'Archivo', sans-serif; font-weight: 900; font-size: 32px; color: var(--s-ink); }
.step span { font: 800 9px 'Archivo', sans-serif; letter-spacing: .2em; text-transform: uppercase; color: var(--s-muted); }
@media (max-width: 900px) { .promo { flex-direction: column; align-items: flex-start; padding: 28px; } .promo h2 { font-size: 32px; } }
</style>
