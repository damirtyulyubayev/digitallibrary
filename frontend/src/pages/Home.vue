<template>
  <div class="stack">
    <section class="hero card">
      <div>
        <h1>Обзор</h1>
        <p>Книги, события и рекомендации в одном месте.</p>
      </div>
      <div class="actions">
        <input v-model="query" class="input" placeholder="Найдите книгу..." @keyup.enter="search" />
        <AppButton variant="primary" @click="search">Искать</AppButton>
      </div>
    </section>

    <section class="section card">
      <div class="section-head">
        <h2>Рекомендации книг</h2>
      </div>
      <div class="grid">
        <article v-for="book in featured" :key="book.id" class="book card">
          <div class="cover" />
          <div class="meta">
            <div class="title">{{ book.title }}</div>
            <div class="author">{{ book.author }}</div>
          </div>
        </article>
      </div>
    </section>

    <section class="card ping">
      <div class="row">
        <div>
          <h2>Статус API</h2>
          <p>Проверка доступности бекенда.</p>
        </div>
        <AppButton variant="ghost" @click="checkPing">Проверить</AppButton>
      </div>
      <div class="status" :data-status="pingStatus">{{ pingStatus }}</div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import AppButton from '../components/ui/AppButton.vue';
import { fetchBooks } from '../api/endpoints/books';
import { ping } from '../api/endpoints/ping';
import type { Book } from '../api/types';

const featured = ref<Book[]>([]);
const query = ref('');
const pingStatus = ref('не проверено');

const mockBooks: Book[] = [
  { id: 1, title: 'Дорога', author: 'Кормак Маккарти' },
  { id: 2, title: '451° по Фаренгейту', author: 'Рэй Брэдбери' },
  { id: 3, title: 'Маленькая жизнь', author: 'Ханья Янагихара' },
  { id: 4, title: 'Пикник на обочине', author: 'Стругацкие' }
];

const load = async () => {
  try {
    featured.value = await fetchBooks();
  } catch {
    featured.value = mockBooks;
  }
};

const search = async () => {
  try {
    featured.value = await fetchBooks(query.value);
  } catch {
    featured.value = mockBooks.filter(b => b.title.toLowerCase().includes(query.value.toLowerCase()));
  }
};

const checkPing = async () => {
  try {
    const res = await ping();
    pingStatus.value = res || 'ok';
  } catch (e) {
    pingStatus.value = 'недоступно';
  }
};

onMounted(() => {
  load();
  checkPing();
});
</script>

<style scoped>
.stack {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-lg);
}

.actions {
  display: flex;
  gap: var(--space-sm);
  align-items: center;
}

.input {
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 10px 12px;
  min-width: 260px;
}

.section {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.section-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: var(--space-md);
}

.book {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.cover {
  background: linear-gradient(135deg, var(--primary-soft), #fefefe);
  border-radius: 10px;
  height: 200px;
}

.title {
  font-weight: 600;
  color: var(--text-main);
}

.author {
  color: var(--text-muted);
  font-size: 13px;
}

.ping {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status {
  font-weight: 600;
}
</style>
