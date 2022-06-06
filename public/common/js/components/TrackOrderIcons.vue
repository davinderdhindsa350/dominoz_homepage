<template>
  <div class="col-sm-12 pl0 pr0">
    <div class="track-order-icon" :key="iconJson">
      <VueLottiePlayer
        name="offerIconAnim"
        width="100%"
        height="100%"
        loop
        :animationData="require(`../assets/AnimationIcon/${iconJsonData}.json`)"
      />
    </div>
  </div>
</template>

<script>
import VueLottiePlayer from "vue-lottie-player";

export default {
  name: "TrackOrderIcons",
  props: ["iconJson"],
  components: { VueLottiePlayer },
  data() {
    return {};
  },
  computed: {
    iconJsonData(){
      return this.iconJson && this.iconJson
    }
  },
};
</script>

<style scoped></style>
