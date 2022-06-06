<template>
  <div>
    <!-- <transition name="bg-change" mode="out-in"> -->
    <div
      class="offer-applied-message fs-11 lh16 flex-row"
      v-if="checkStoreForAlreadyApplied?.paymentId" 
      :key="checkStoreForAlreadyApplied?.paymentId"
    >
      <div>
        <img
          :src="staticImgBaseUrl + '/images/global/offer-percentage.svg'"
          class="pd10"
        />
      </div>
      <h4>
        <span>Code {{paymentOffers[selectedOfferIndex]?.paymentId}} selected!</span>
      </h4>
      <div class="mr20 hand-cursor" @click="triggerTncModal()">
        <i class="jfi-arrow-line ml7"></i>
      </div>
    </div>
    <!-- </transition> -->

  </div>
</template>

<script>
import GenericModal from "./GenericModal.vue";
import {
  keyMapsAction,
  keyMapsGetter,
  keyMapsMutation,
} from "../helpers/storeKeys";
import { staticImgBaseUrl } from "../config/apiEndpoints";
import LocalStorageFunc from "../helpers/localStorageFunc";

export default {
  name: "PaymentOfferMessage",
  props: [],

  components: {
    GenericModal,
  },

  data() {
    return {
      staticImgBaseUrl: staticImgBaseUrl
    };
  },
  mounted() {
    // 
  },
  created() {},
  methods: {
    triggerTncModal() {
      this.$store.dispatch(keyMapsAction.PAYMENT_OFFER_TNC_API, {
        offerId: this.paymentOffers[this.selectedOfferIndex].id,
        langId: 1,
        platform: (window?._siteProps_?.platform) ? ((window?._siteProps_?.platform).toLowerCase()) : 'pwa'
      });
      document.body.classList.add("body-stop-scroll-mobile");
      this.$store.commit(keyMapsMutation.UPDATE_TNC_MODAL_FLAG, true);
    }
  },
  computed: {
    checkStoreForAlreadyApplied() {
      return this.$store.state.PaymentOffers.appliedOfferDetails;
    },
    paymentOffers() {
      return this.$store.state.PaymentOffers.paymentOffers;
    },
    selectedOfferIndex() {
      return this.$store.state.PaymentOffers.selectedOfferIndex;
    }
  },
};
</script>
