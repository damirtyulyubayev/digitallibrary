<template>
  <div class="stack">
    <header class="head">
      <div>
        <h1>Книги</h1>
        <p>Каталог библиотеки</p>
      </div>
      <div class="actions">
        <input v-model="query" class="input" placeholder="Найдите книгу..." @keyup.enter="search" />
        <AppButton variant="primary" @click="search">Искать</AppButton>
      </div>
    </header>

    <div class="grid">
      <article v-for="book in books" :key="book.id" class="card book">
        <div class="cover" />
        <div class="meta">
          <div class="title">{{ book.title }}</div>
          <div class="author">{{ book.author }}</div>
        </div>
        <RouterLink :to="`/books/${book.id}`" class="link">Открыть</RouterLink>
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { RouterLink } from 'vue-router';
import AppButton from '../components/ui/AppButton.vue';
import { fetchBooks } from '../api/endpoints/books';
import type { Book } from '../api/types';

const books = ref<Book[]>([]);
const query = ref('');

const mockBooks: Book[] = [
  { id: 1, title: 'Гарри Поттер и философский камень', author: 'Дж. К. Роулинг' },
  { id: 2, title: 'Мастер и Маргарита', author: 'Михаил Булгаков' },
  { id: 3, title: 'Три товарища', author: 'Эрих Мария Ремарк' },
  { id: 4, title: 'Мёртвые души', author: 'Николай Гоголь' }
];

const load = async () => {
  try {
    books.value = await fetchBooks(query.value);
  } catch {
    books.value = mockBooks;
  }
};

const search = () => load();

onMounted(load);
</script>

<style scoped>
.stack {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: var(--space-md);
  flex-wrap: wrap;
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
  min-width: 240px;
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

.link {
  color: var(--primary);
  text-decoration: none;
  font-weight: 600;
}
</style>
