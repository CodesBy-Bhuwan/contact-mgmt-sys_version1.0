import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:5454',
      '/authenticate': 'http://localhost:5454',
      '/do-logout': 'http://localhost:5454',
      '/oauth2': 'http://localhost:5454',
      '/login/oauth2': 'http://localhost:5454',  // OAuth callback path
    }
  }
})
