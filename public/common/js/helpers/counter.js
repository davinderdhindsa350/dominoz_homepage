import { computed, readonly, ref } from 'vue';

export function useCounter() {
  const count = ref(0);
  const next = computed(() => count.value + 1);
  const plusOne = () => { count.value += 1 };
  
  return {
    count: readonly(count),
    next,
    plusOne,
  };
}