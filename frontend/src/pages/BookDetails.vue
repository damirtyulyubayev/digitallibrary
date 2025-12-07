<template>
  <div class="stack">
    <header class="head">
      <div>
        <h1>{{ book?.title || 'Загрузка...' }}</h1>
        <p v-if="book">Автор: {{ book.author }}</p>
      </div>
      <AppButton variant="ghost" @click="goBack">Назад</AppButton>
    </header>

    <div class="card detail" v-if="book">
      <div class="cover" />
      <div class="meta">
        <div class="label">Название</div>
        <div class="value">{{ book.title }}</div>
        <div class="label">Автор</div>
        <div class="value">{{ book.author }}</div>
      </div>
    </div>

    <div class="card reviews">
      <div class="reviews-head">
        <h2>Отзывы</h2>
      </div>
      <div class="list" v-if="reviews.length">
        <div v-for="rev in reviews" :key="rev.id" class="review">
          <div class="rating">Оценка: {{ rev.rating }}</div>
          <div class="comment">{{ rev.comment }}</div>
        </div>
      </div>
      <p v-else>Отзывов пока нет.</p>

      <div class="form">
        <input v-model.number="newRating" class="input" placeholder="Оценка (1-5)" type="number" min="1" max="5" />
        <input v-model="newComment" class="input" placeholder="Комментарий" />
        <AppButton variant="primary" @click="submitReview">Добавить отзыв</AppButton>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import AppButton from '../components/ui/AppButton.vue';
import { fetchBook } from '../api/endpoints/books';
import { fetchReviewsByBook, postReview } from '../api/endpoints/reviews';
import type { Book } from '../api/types';
import type { Review } from '../api/types';

const route = useRoute();
const router = useRouter();
const book = ref<Book | null>(null);
const reviews = ref<Review[]>([]);
const newRating = ref<number | null>(null);
const newComment = ref('');

const mockBook: Book = { id: Number(route.params.id) || 1, title: 'Демонстрационная книга', author: 'Автор' };
const mockReviews: Review[] = [
  { id: 1, bookId: mockBook.id, rating: 5, comment: 'Отличная книга!' },
  { id: 2, bookId: mockBook.id, rating: 4, comment: 'Полезно и интересно.' }
];

const load = async () => {
  try {
    book.value = await fetchBook(route.params.id as string);
  } catch {
    book.value = mockBook;
  }
  try {
    reviews.value = await fetchReviewsByBook(route.params.id as string);
  } catch {
    reviews.value = mockReviews;
  }
};

const goBack = () => router.back();

const submitReview = async () => {
  if (!book.value || !newRating.value) return;
  try {
    await postReview({ bookId: book.value.id, rating: newRating.value, comment: newComment.value || '' });
    reviews.value = await fetchReviewsByBook(book.value.id);
  } catch {
    // имитация добавления отзыва в моках
    const newId = reviews.value.length + 1;
    reviews.value = [
      ...reviews.value,
      { id: newId, bookId: book.value.id, rating: newRating.value, comment: newComment.value || '' }
    ];
  }
  newRating.value = null;
  newComment.value = '';
};

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
}

.detail {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: var(--space-lg);
}

.cover {
  background: linear-gradient(135deg, var(--primary-soft), #fefefe);
  border-radius: 12px;
  height: 360px;
}

.label {
  color: var(--text-muted);
  font-size: 13px;
  margin-top: var(--space-sm);
}

.value {
  font-weight: 600;
  color: var(--text-main);
}

.reviews {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
}

.reviews-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.list {
  display: grid;
  gap: var(--space-sm);
}

.review {
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
}

.rating {
  font-weight: 600;
  margin-bottom: 4px;
}

.comment {
  color: var(--text-muted);
}

.form {
  display: flex;
  gap: var(--space-sm);
  align-items: center;
  flex-wrap: wrap;
}

.input {
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 10px 12px;
}
</style>
