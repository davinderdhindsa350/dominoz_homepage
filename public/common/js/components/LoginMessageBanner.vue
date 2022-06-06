<template>
  <div class="login-message-banner col-sm-12" v-if="isTrueLocationDetected && !isLoggedIn">
    <div class="login-message-item pr20 pl12 flex-row mb16">
      <span class="user-icon flex-column">
        <i class="jfi-user fs-18"></i>
      </span>

      <Carousel
        :autoplay="4000"
        :wrap-around="true"
        :pauseAutoplayOnHover="true"
        :mouseDrag="false"
        :touchDrag="false"
        :transition="1200"
      >
        <template #addons> 
          <div class="login-cars-dots">Login for &nbsp;&nbsp; <Pagination /> </div>
        </template>
        <Slide v-for="(message, index) in loginMessageDynamicText" :key="index">
          <div class="login-text flex-column pl10">
            <div class="location-dynamic-message lh20 bold fs-14">{{ message }}</div>
          </div>
        </Slide>

      </Carousel>

      <button @click="handleLoginLink($event)" class="line-btn sm">
        Login
      </button>
    </div>
  </div>
</template>

<script>
import { Carousel, Slide, Pagination } from "vue3-carousel";

import "vue3-carousel/dist/carousel.css";
import utility from "../helpers/utility";
import {
  keyMapsMutation,
  keyMapsGetter,
  keyMapsAction,
} from "../helpers/storeKeys";

export default {
  name: "LoginMessageBanner",

  props: ["moduleProps"],

  components: {
    Carousel,
    Slide,
    Pagination
  },

  data() {
    return {};
  },
  mounted(){
    this.$store.commit(keyMapsMutation.UPDATE_LOGGIN_MESSAGE_DYNAMIC_TEXT, (this.moduleProps && JSON.parse(this.moduleProps))?.loginMessageDynamicText?.split(','));
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
    loginMessageDynamicText() {
      return this.$store.state.Auth.loginMessageDynamicText;
    },
    isTrueLocationDetected() {
      return this.$store.state.Location.isTrueLocationDetected;
    },
  },

  methods: {
    handleLoginLink(event) {
      utility.sendToLoginScreen(window.location);
    },
  },
};
</script>

<style scoped></style>
