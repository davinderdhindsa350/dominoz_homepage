<template>
  <div class="col-sm-12">
    <div class="no-offer-applied" v-if="showDealsAndOfferModal">
      <div class="bold pTB20 plr16 no-offer-applied-msg">
        <div>
          <span class="fs-18 bold">Selected Offer</span>
          <i @click="closeModalM()" class="jfi-cross sticky-pop-cross"></i>
        </div>
        <div class="pt10 pl15"></div>
        <div class="fs-14 lh18 color-slaty">
          Add items to your cart and proceed. The offer will be applied in your
          cart.
        </div>
        <button class="btn medium fr" @click="closeModalM()">OK, GOT IT</button>
      </div>
    </div>

    <!-- <generic-modal
      v-if="showDealsAndOfferModal"
      @close="closeModalM()"
      :height="menuModalDim.height"
      :width="menuModalDim.width"
      :modalClass="menuModalDim.modalClass"
    >
      <template v-slot:header>
        <div class="bold fs-18">
            Selected Offer
        </div>
      </template>
      <template v-slot:body>
        <div class="">
          <p class="fs-14 lh20">
            Add items to your cart and proceed. The offer will be applied in your cart.
          </p>
          <button class="btn medium fr" @click="closeModalM()">OK, GOT IT</button>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal> -->
  </div>
</template>

<script>
import localStorageFunc from "../helpers/localStorageFunc";
import utility from "../helpers/utility";
import GenericModal from "./GenericModal.vue";
export default {
  components: { GenericModal },
  name: "OfferShownModal",
  data() {
    return {
      showDealsAndOfferModal: false,
    };
  },
  mounted() {
    if (
      utility.getSessionStorageValue("leftNavOfferCode") !== undefined &&
      utility.getSessionStorageValue("leftNavOfferCode") &&
      (localStorageFunc.getCouponCode() ||
      localStorageFunc.getPaymentOfferPaymentIdLS())
    ) {
      this.triggerDealsAndShowModal();
    }
  },
  computed: {},
  methods: {
    triggerDealsAndShowModal() {
      this.showDealsAndOfferModal = true;
    },
    closeModalM() {
      this.showDealsAndOfferModal = false;
      localStorageFunc.unsetSessionStorageKey("leftNavOfferCode");
    },
  },
};
</script>

<style scoped></style>
