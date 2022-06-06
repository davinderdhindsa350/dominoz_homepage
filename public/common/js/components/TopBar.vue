<template>
  <div class="col-sm-12 header-component mb16 flex-row" id="pageTop">
    <home-page-user-address></home-page-user-address>
    <reward-program></reward-program>
    <profile-icon :data-left-menu-links="dataResults"></profile-icon>
  </div>
</template>

<script>
import HomePageUserAddress from "./HomePageUserAddress.vue";
import RewardProgram from "./RewardProgram.vue";
import ProfileIcon from "./ProfileIcon.vue";

export default {
  name: "TopBar",

  components: {
    HomePageUserAddress,
    RewardProgram,
    ProfileIcon,
  },

  props: ["dataResults", "moduleProps"],

};
</script>

<style scoped></style>
