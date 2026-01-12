export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },

  modules: ['@pinia/nuxt', '@pinia-plugin-persistedstate/nuxt'],

  css: [
    '~/assets/css/styleGuide.css'
  ],
})

