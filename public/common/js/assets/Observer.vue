<!-- Observer.vue -->
<template>
  <div class="observer"/>
</template>

<script>
export default {
  props: ['options'],
  data: () => ({
    observer: null,
  }),
  mounted() {
    const options = this.options || {rootMargin: '200px'};
    this.observer = new IntersectionObserver(([entry]) => {
      if (entry && entry.isIntersecting) {
        this.$emit("intersect");
        this.observer.disconnect();
      }
    }, options);
    this.observer.observe(this.$el);
  },
  destroyed() {
    this.observer.disconnect();
  },
};
</script>