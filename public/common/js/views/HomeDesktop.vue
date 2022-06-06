<template>
  <div class="home-route">
    <div id="nav" class="hide">
      <router-link to="/">Home</router-link> -
      <router-link to="/cart">Cart (0)</router-link>
    </div>

    <div class="container-fluid ">
      <div class="row">
        <!-- Start HOOK_TOP -->
        <top-bar> </top-bar>
        <location-message-banner></location-message-banner>
        <login-message-banner></login-message-banner>
        <div class="col-sm-12 col-lg-6 delivery-type-tabs">delivery-type-tabs module here...</div>
        <div class="col-sm-12 col-lg-6  search-bar">search-bar  module here....</div>

        <personalized-offers></personalized-offers>
        <!-- eof HOOK_TOP -->
      </div>
      <div class="row">
        <!-- Start HOOK_MIDDLE -->


        <!-- eof HOOK_MIDDLE -->
      </div>
    </div>



    <div style="height: 800px">This is Home page view</div>
    <bottom-bar> </bottom-bar>
  </div>
</template>

<script>
import TopBar from "../components/TopBar.vue";
import LocationMessageBanner from "../components/LocationMessageBanner.vue";
import LoginMessageBanner from "../components/LoginMessageBanner.vue";
import BottomBar from "../components/BottomBar.vue";
import PersonalizedOffers from "../components/PersonalizedOffers.vue";


export default {
  components: {
    TopBar,
    LocationMessageBanner,
    LoginMessageBanner,
    BottomBar,
    PersonalizedOffers
  },
  name: 'Home',
  setup() {
    // 
  }
};
</script>

<style>
.home-route {
  /* border: 2px solid yellow; */
}
</style>
