<template>
  <div>
    <div
      class="col-sm-12 location-message-banner pd10 flex-row mb16"
      id="location-message-banner-id"
      v-if="!isTrueLocationDetected"
    >
      <span class="location-icon mr5 flex-column">
        <i class="jfi-location fs-17"></i>
      </span>
      <span class="location-nudge-message lh18"
        >Give us your exact location for seemless delivery</span
      >
      <button @click="detectGeoLocationPopup" class="line-btn sm">
        Use current location
      </button>
    </div>
  </div>
</template>

<script>
import GenericModal from "./GenericModal.vue";
import LocationAccessBlockedMessage from "./LocationAccessBlockedMessage.vue";
import { keyMapsMutation, keyMapsAction } from "../helpers/storeKeys";
export default {
  name: "LocationMessageBanner",

  components: {
    GenericModal,
    LocationAccessBlockedMessage,
  },

  data() {
    return {
      cachedComponentOffsetTop: 0,
    };
  },

  mounted() {
    window.addEventListener("scroll", () => {
      this.watchSticky();
    });
  },

  destroyed() {
    window.removeEventListener("scroll", this.watchSticky);
  },

  computed: {
    isTrueLocationDetected() {
      return this.$store.state.Location.isTrueLocationDetected;
    },
  },

  methods: {
    watchSticky() {
      let componentRef = document.getElementById("location-message-banner-id");
      if (!componentRef) return;
      if (!this.cachedComponentOffsetTop && componentRef.offsetTop > 0) {
        this.cachedComponentOffsetTop = componentRef.offsetTop;
      }
      if (window.pageYOffset >= this.cachedComponentOffsetTop) {
        componentRef.classList.add("watch-sticky");
      } else {
        componentRef.classList.remove("watch-sticky");
      }
    },
    detectGeoLocationPopup() {
      this.$store.dispatch(keyMapsAction.DETECT_GEO_LOCATION_POPUP, {
        source: "locationPopup",
        showModalM: () => this.showModalM(),
      });
    },
    showModalM() {
      this.$store.commit(
        keyMapsMutation.UPDATE_SHOW_LOCATION_ACCESS_BLOCKED_MODAL,
        true
      );
    },
  },
};
</script>

<style scoped></style>
