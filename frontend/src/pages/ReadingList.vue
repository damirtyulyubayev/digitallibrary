<template>
  <div class="stack">
    <header>
      <h1>Список чтения</h1>
      <p>Сохранённые книги по пользователю</p>
    </header>

    <div class="card grid-forms">
      <div class="form">
        <h2>Загрузить список</h2>
        <input v-model.number="userId" class="input" placeholder="ID пользователя" />
        <AppButton variant="primary" @click="load">Показать</AppButton>
      </div>
      <div class="form">
        <h2>Добавить книгу</h2>
        <input v-model.number="userId" class="input" placeholder="ID пользователя" />
        <input v-model.number="bookId" class="input" placeholder="ID книги" />
        <AppButton variant="ghost" @click="add">Добавить</AppButton>
      </div>
    </div>

    <div class="card">
      <div v-if="items.length" class="list">
        <div v-for="item in items" :key="item.id" class="row">
          <div>Книга: {{ item.bookId }}</div>
          <div>Пользователь: {{ item.userId }}</div>
        </div>
      </div>
      <p v-else>Нет сохранённых книг.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AppButton from '../components/ui/AppButton.vue';
import { addToReadingList, fetchReadingList } from '../api/endpoints/readingList';
import type { ReadingListItem } from '../api/types';

const userId = ref<number | null>(null);
const bookId = ref<number | null>(null);
const items = ref<ReadingListItem[]>([]);
const mockItems: ReadingListItem[] = [
  { id: 1, userId: 10, bookId: 3 },
  { id: 2, userId: 10, bookId: 4 }
];

const load = async () => {
  if (!userId.value) return;
  try {
    items.value = await fetchReadingList(userId.value);
  } catch {
    items.value = mockItems;
  }
};

const add = async () => {
  if (!userId.value || !bookId.value) return;
  try {
    await addToReadingList(userId.value, { bookId: bookId.value });
    await load();
  } catch {
    items.value = [...mockItems, { id: mockItems.length + 1, userId: userId.value, bookId: bookId.value }];
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

.list {
  display: grid;
  gap: var(--space-sm);
}

.row {
  display: flex;
  justify-content: space-between;
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
}
</style>
