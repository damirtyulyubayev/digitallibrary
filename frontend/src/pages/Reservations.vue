<template>
  <div class="stack">
    <header>
      <h1>Бронирования</h1>
      <p>Создавайте, выдавайте и возвращайте книги</p>
    </header>

    <div class="card grid-forms">
      <div class="form">
        <h2>Создать</h2>
        <input v-model.number="createUserId" class="input" placeholder="ID пользователя" />
        <input v-model.number="createBookId" class="input" placeholder="ID книги" />
        <AppButton variant="primary" @click="create">Создать бронь</AppButton>
      </div>
      <div class="form">
        <h2>Выдать</h2>
        <input v-model.number="actionId" class="input" placeholder="ID брони" />
        <AppButton variant="primary" @click="issue">Выдать</AppButton>
      </div>
      <div class="form">
        <h2>Вернуть</h2>
        <input v-model.number="actionIdReturn" class="input" placeholder="ID брони" />
        <AppButton variant="primary" @click="returnRes">Вернуть</AppButton>
      </div>
      <div class="form">
        <h2>По пользователю</h2>
        <input v-model.number="userFilter" class="input" placeholder="ID пользователя" />
        <AppButton variant="ghost" @click="loadUser">Показать</AppButton>
      </div>
    </div>

    <div class="card">
      <table class="table" v-if="reservations.length">
        <thead>
          <tr>
            <th>ID</th>
            <th>Книга</th>
            <th>yernaarj / damir</th>
            <th>Статус</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in reservations" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.bookId }}</td>
            <td>{{ r.userId }}</td>
            <td>{{ r.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>Пока нет бронирований.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AppButton from '../components/ui/AppButton.vue';
import { createReservation, fetchReservationsByUser, issueReservation, returnReservation } from '../api/endpoints/reservations';
import type { Reservation } from '../api/types';

const reservations = ref<Reservation[]>([]);

const createUserId = ref<number | null>(null);
const createBookId = ref<number | null>(null);
const actionId = ref<number | null>(null);
const actionIdReturn = ref<number | null>(null);
const userFilter = ref<number | null>(null);

const mockReservations: Reservation[] = [
  { id: 101, bookId: 1, userId: 10, status: 'CREATED' },
  { id: 102, bookId: 2, userId: 10, status: 'ISSUED' }
];

const create = async () => {
  if (!createUserId.value || !createBookId.value) return;
  try {
    await createReservation({ userId: createUserId.value, bookId: createBookId.value });
    if (userFilter.value) await loadUser();
  } catch {
    reservations.value = mockReservations;
  }
};

const issue = async () => {
  if (!actionId.value) return;
  try {
    await issueReservation(actionId.value);
    if (userFilter.value) await loadUser();
  } catch {
    reservations.value = mockReservations.map(r => r.id === actionId.value ? { ...r, status: 'ISSUED' } : r);
  }
};

const returnRes = async () => {
  if (!actionIdReturn.value) return;
  try {
    await returnReservation(actionIdReturn.value);
    if (userFilter.value) await loadUser();
  } catch {
    reservations.value = mockReservations.map(r => r.id === actionIdReturn.value ? { ...r, status: 'RETURNED' } : r);
  }
};

const loadUser = async () => {
  if (!userFilter.value) return;
  try {
    reservations.value = await fetchReservationsByUser(userFilter.value);
  } catch {
    reservations.value = mockReservations;
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

.table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  text-align: left;
  padding: 10px 12px;
  border-bottom: 1px solid var(--border);
}

th {
  color: var(--text-muted);
  font-weight: 600;
}
</style>
