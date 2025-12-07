<template>
  <div class="stack">
    <header>
      <h1>Рекомендации</h1>
      <p>Персональные подборки и похожие книги</p>
    </header>

    <div class="card grid-forms">
      <div class="form">
        <h2>Для пользователя</h2>
        <input v-model.number="userId" class="input" placeholder="ID пользователя" />
        <AppButton variant="primary" @click="loadUser">Показать</AppButton>
      </div>
      <div class="form">
        <h2>Похожие на книгу</h2>
        <input v-model.number="bookId" class="input" placeholder="ID книги" />
        <AppButton variant="ghost" @click="loadSimilar">Показать</AppButton>
      </div>
    </div>

    <div class="card">
      <h2>Рекомендации пользователю</h2>
      <div v-if="userRecs.length" class="grid">
        <article v-for="rec in userRecs" :key="'u'+rec.id" class="card rec">
          <div class="title">{{ rec.title }}</div>
          <div class="author">{{ rec.author || rec.reason }}</div>
        </article>
      </div>
      <p v-else>Пусто.</p>
    </div>

    <div class="card">
      <h2>Похожие книги</h2>
      <div v-if="similar.length" class="grid">
        <article v-for="rec in similar" :key="'s'+rec.id" class="card rec">
          <div class="title">{{ rec.title }}</div>
          <div class="author">{{ rec.author || rec.reason }}</div>
        </article>
      </div>
      <p v-else>Пусто.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AppButton from '../components/ui/AppButton.vue';
import { fetchRecommendationsForUser, fetchSimilarBooks } from '../api/endpoints/recommendations';
import type { Recommendation } from '../api/types';

const userId = ref<number | null>(null);
const bookId = ref<number | null>(null);
const userRecs = ref<Recommendation[]>([]);
const similar = ref<Recommendation[]>([]);
const mockUserRecs: Recommendation[] = [
  { id: 1, title: 'Сияние', author: 'Стивен Кинг', reason: 'Истории с напряжением' },
  { id: 2, title: 'Тень горы', author: 'Грегори Дэвид Робертс', reason: 'Путешествия и драма' }
];
const mockSimilar: Recommendation[] = [
  { id: 3, title: 'Шантарам', author: 'Грегори Дэвид Робертс' },
  { id: 4, title: 'Один день', author: 'Дэвид Николлс' }
];

const loadUser = async () => {
  if (!userId.value) return;
  try {
    userRecs.value = await fetchRecommendationsForUser(userId.value);
  } catch {
    userRecs.value = mockUserRecs;
  }
};

const loadSimilar = async () => {
  if (!bookId.value) return;
  try {
    similar.value = await fetchSimilarBooks(bookId.value);
  } catch {
    similar.value = mockSimilar;
  }
};
</script>

<style scoped>
.stack {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.grid-forms {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: var(--space-md);
}

.form {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.input {
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 10px 12px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: var(--space-md);
}

.rec {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
}

.title {
  font-weight: 600;
}

.author {
  color: var(--text-muted);
}
</style>
