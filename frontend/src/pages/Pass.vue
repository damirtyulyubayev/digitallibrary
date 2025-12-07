<template>
  <div class="stack">
    <header>
      <h1>Читательский билет</h1>
      <p>Проверка и сканирование билетов</p>
    </header>

    <div class="card grid-forms">
      <div class="form">
        <h2>По пользователю</h2>
        <input v-model.number="userId" class="input" placeholder="ID пользователя" />
        <AppButton variant="primary" @click="loadUser">Показать</AppButton>
      </div>
      <div class="form">
        <h2>Скан</h2>
        <input v-model="qrToken" class="input" placeholder="QR токен" />
        <AppButton variant="ghost" @click="scan">Сканировать</AppButton>
      </div>
    </div>

    <div class="card" v-if="pass">
      <div class="row"><strong>ID:</strong> {{ pass.id }}</div>
      <div class="row"><strong>Пользователь:</strong> {{ pass.userId }}</div>
      <div class="row"><strong>QR:</strong> {{ pass.qrToken }}</div>
      <div class="row" v-if="pass.status"><strong>Статус:</strong> {{ pass.status }}</div>
    </div>
    <p v-else>Билет не найден.</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AppButton from '../components/ui/AppButton.vue';
import { fetchPassByUser, scanPass } from '../api/endpoints/passes';
import type { LibraryPass } from '../api/types';

const userId = ref<number | null>(null);
const qrToken = ref('');
const pass = ref<LibraryPass | null>(null);
const mockPass: LibraryPass = { id: 1, userId: 10, qrToken: 'demo-token', status: 'ACTIVE' };

const loadUser = async () => {
  if (!userId.value) return;
  try {
    pass.value = await fetchPassByUser(userId.value);
  } catch {
    pass.value = mockPass;
  }
};

const scan = async () => {
  if (!qrToken.value) return;
  try {
    pass.value = await scanPass(qrToken.value);
  } catch {
    pass.value = mockPass;
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
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
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

.row {
  margin-bottom: var(--space-sm);
}
</style>
