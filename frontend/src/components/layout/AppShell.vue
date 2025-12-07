<template>
  <div class="shell">
    <aside class="sidebar card">
      <div class="logo">Цифровая библиотека</div>
      <nav>
        <RouterLink v-for="item in nav" :key="item.to" :to="item.to" class="nav-item">
          {{ item.label }}
        </RouterLink>
      </nav>
    </aside>
    <main class="main">
      <header class="topbar card">
        <div class="title">Обзор</div>
        <div class="user">Пользователь</div>
      </header>
      <section class="content card">
        <RouterView />
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
const nav = [
  { to: '/', label: 'Обзор' },
  { to: '/books', label: 'Книги' },
  { to: '/reservations', label: 'Бронирования' },
  { to: '/events', label: 'События' },
  { to: '/reading-list', label: 'Список чтения' },
  { to: '/recommendations', label: 'Рекомендации' },
  { to: '/pass', label: 'Читательский билет' }
];
</script>

<style scoped>
.shell {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: var(--space-lg);
  min-height: 100vh;
  padding: var(--space-lg);
  background: radial-gradient(circle at 20% 20%, #f0f0f0 0, #f7f7f8 45%);
}

.sidebar {
  padding: var(--space-lg);
}

.logo {
  font-weight: 700;
  font-size: 16px;
  margin-bottom: var(--space-lg);
}

.nav-item {
  display: block;
  padding: 10px 12px;
  margin-bottom: var(--space-sm);
  border-radius: 10px;
  color: var(--text-main);
  text-decoration: none;
}

.nav-item.router-link-active {
  background: var(--primary-soft);
  color: var(--primary);
}

.main {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-md);
}

.title {
  font-weight: 600;
  font-size: 18px;
}

.content {
  padding: var(--space-lg);
}
</style>
