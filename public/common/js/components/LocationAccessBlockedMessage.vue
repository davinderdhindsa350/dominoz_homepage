<template>
  <div>
    <generic-modal
      v-if="showLocationAccessBlockedModal"
      @close="closeModalM()"
      :height="menuModalDim.height"
      :width="menuModalDim.width"
      :modalClass="menuModalDim.modalClass"
    >
      <template v-slot:header>
        <div></div>
        <!-- <i @click="closeModalM()" class="jfi-cross"></i> -->
      </template>
      <template v-slot:body>
        <div class="">
          <p class="fs-14 lh20">
            Sorry! You have blocked location access. Please type and search your
            location.
          </p>
          <br /><br />
          <p class="fs-14 lh20">
            To allow location access please go to your browser settings
          </p>
          <button class="btn medium fr" @click="closeModalM(false)">OK</button>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
  </div>
</template>

<script>
import GenericModal from "./GenericModal.vue";
import { keyMapsMutation, keyMapsAction } from "../helpers/storeKeys";
export default {
  name: "LocationAccessBlockedMessage",

  components: {
    GenericModal,
  },

  data() {
    return {
      menuModalDim: {
        width: "270px",
        height: "fit-content",
        modalClass: "top-40per",
      },
    };
  },

  mounted() {},

  computed: {
    showLocationAccessBlockedModal() {
      let _ = this.$store.state.Location.showLocationAccessBlockedModal;
      if (_) {
        document.body.classList.add("body-stop-scroll-mobile");
      }
      return _;
    },
  },

  methods: {
    closeModalM(choice) {
      this.$store.commit(
        keyMapsMutation.UPDATE_SHOW_LOCATION_ACCESS_BLOCKED_MODAL,
        false
      );
      this.$store.dispatch(keyMapsAction.GOOGLE_GEO_LOCATION_API, {});
      document.body.classList.remove("body-stop-scroll-mobile");
    },
  },
};
</script>

<style scoped></style>
