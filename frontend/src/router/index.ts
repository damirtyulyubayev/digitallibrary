import { createRouter, createWebHistory } from 'vue-router';
import AppShell from '../components/layout/AppShell.vue';
import Home from '../pages/Home.vue';
import Books from '../pages/Books.vue';
import BookDetails from '../pages/BookDetails.vue';
import Reservations from '../pages/Reservations.vue';
import Events from '../pages/Events.vue';
import ReadingList from '../pages/ReadingList.vue';
import Recommendations from '../pages/Recommendations.vue';
import Pass from '../pages/Pass.vue';
import NotFound from '../pages/NotFound.vue';

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: AppShell,
      children: [
        { path: '', component: Home },
        { path: 'books', component: Books },
        { path: 'books/:id', component: BookDetails },
        { path: 'reservations', component: Reservations },
        { path: 'events', component: Events },
        { path: 'reading-list', component: ReadingList },
        { path: 'recommendations', component: Recommendations },
        { path: 'pass', component: Pass }
      ]
    },
    { path: '/:pathMatch(.*)*', component: NotFound }
  ]
});
