<template>
  <div id="nav">
    <router-link to="/">Home</router-link> -
    <router-link to="/cart">Cart (0)</router-link>
  </div>
  <div class="cart-route">This is Cart page view....</div>
</template>

<script>

export default {
  name: 'Cart',
  setup() {
    //
  }
};


</script>

<style scoped>
.cart-route {
  border: 2px solid red;
}
</style>
