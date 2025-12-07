<template>
  <div class="stack">
    <header>
      <h1>События</h1>
      <p>Ближайшие мероприятия библиотеки</p>
    </header>

    <div class="grid">
      <article v-for="event in events" :key="event.id" class="card event">
        <div class="title">{{ event.title }}</div>
        <p>{{ event.description || 'Без описания' }}</p>
        <div class="form">
          <input v-model.number="userId" class="input" placeholder="ID пользователя для записи" />
          <AppButton class="full" variant="primary" @click="register(event.id)">Записаться</AppButton>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import AppButton from '../components/ui/AppButton.vue';
import { fetchEvents, registerForEvent } from '../api/endpoints/events';
import type { EventItem } from '../api/types';

const events = ref<EventItem[]>([]);
const userId = ref<number | null>(null);

const mockEvents: EventItem[] = [
  { id: 1, title: 'Ночь в библиотеке', description: 'Квест и открытые лекции' },
  { id: 2, title: 'Литературный клуб', description: 'Обсуждаем классику' }
];

const load = async () => {
  try {
    events.value = await fetchEvents();
  } catch {
    events.value = mockEvents;
  }
};

const register = async (id: number) => {
  if (!userId.value) return;
  try {
    await registerForEvent(id, { userId: userId.value });
  } catch {
    /* no-op for mock */
  }
};

onMounted(load);
</script>

<style scoped>
.stack {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: var(--space-md);
}

.event {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.title {
  font-weight: 600;
  color: var(--text-main);
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
  width: 100%;
}

.full {
  width: 100%;
  text-align: center;
}
</style>
