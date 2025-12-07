<template>
  <button class="btn" :class="variant" v-bind="$attrs">
    <slot />
  </button>
</template>

<script setup lang="ts">
defineProps<{ variant?: 'primary' | 'ghost' }>();
</script>

<style scoped>
.btn {
  border: none;
  border-radius: 10px;
  padding: 10px 18px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s ease, color 0.15s ease;
}

.primary {
  background: var(--primary);
  color: #fff;
}

.primary:hover {
  background: #1d4ed8;
}

.ghost {
  background: transparent;
  color: var(--primary);
}

.ghost:hover {
  background: var(--primary-soft);
}
</style>
