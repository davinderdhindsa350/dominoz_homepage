<template>
<div v-if="paymentOffers?.length > 0" class="payment-offer-wrap mb32">
    <div v-if="modulePropsData?.title" class="mb12 flex-row pl12">
      <div class="lh21 fs-18 bold ">{{ modulePropsData.title }}</div>
      <span v-if="modulePropsData?.subTitle" class="lh16 fs-14 bold">{{modulePropsData?.subTitle}}</span>
    </div>    
    <Carousel :pauseAutoplayOnHover="true" :transition="500" :itemsToShow="itemsToShow">
      <Slide v-for="(item, index) in paymentOffers" :key="item?.id">
        <div class="payment-offer-item pr flex-column pl12 pb12">
          <div class="corner-image" v-if="item?.logo">
            <img :src="item?.logo" class="banners-item flex-row fs-20" alt="payment option image" />
          </div>
          <div class="flex-column upper pt16 pr12">
            <p class="title fs-18 lh25 bold">{{item?.title}}</p>
            <p class="description fs-12 lh18">
              {{item?.subTitle}}
            </p>
          </div>
          <div class="flex-row lower pr12 pt5">
            <p @click="triggerTncModal(item, index)" class="more-details fs-12 lh14 bold">More details</p>
            <button class="line-btn sm" @click="triggerPaymentOfferAppliedModal(index)">Select</button>
          </div>
        </div>
      </Slide>
      <template #addons> </template>
    </Carousel>

    <!-- offer details popup -->
    <generic-modal
      v-if="tncModalFlag"
      @close="closeTncModal()"
      :height="tncModalDim.height"
      :width="tncModalDim.width"
      :animationStyle="tncModalDim.animationStyle"
    >
      <template v-slot:header>
        <i
          @click="closeTncModal()"
          class="jfi-cross sticky-pop-cross"
        ></i>
      </template>
      <template v-slot:body>
        <div class="ms-offer-pop-content">
          <div class="logo-box-po mb10" v-if="paymentOffers[selectedOfferIndex]?.logo">
            <img :src="paymentOffers[selectedOfferIndex]?.logo" class="po-logo" alt="payment option image" />
          </div>
          <div class="fs-18 bold">{{ paymentOffers[selectedOfferIndex]?.title }}</div>
          <div class="offr-full-desc mt10 lh18">
            {{paymentOffers[selectedOfferIndex].subTitle}}
          </div>
          <div class="offer-state">
            <div class="coupan-dets color-slaty ib">
              Coupon code:
              <span class="bold italic">{{
                paymentOffers[selectedOfferIndex].paymentId
              }}</span>
            </div>
            <!-- <div v-if="checkStoreForAlreadyApplied?.paymentId || checkLSforAlreadyApplied" class="code-applied-dets disp-col-inline">
              <span>Code {{paymentOffers[selectedOfferIndex].paymentId}} selected!</span>
              <span class="bold remove-code" @click="clearPaymentOffers()">Remove</span>
            </div> -->
          </div>
          <div class="ms-offer-details" v-if="selectedPaymentOfferTnc?.tnc">
            <h3 class="bold">Offer Details:</h3>
            <ul class="disc-styled mt10 color-grey fs-11">
              <li
                class="lh16 mb5"
                v-for="tncItem in selectedPaymentOfferTnc?.tnc"
                :key="tncItem"
              >
                <span v-html="tncItem"></span>
              </li>
            </ul>
          </div>
          <div class="btn-wrap">
            <button class="btn medium bold" @click="triggerPaymentOfferAppliedModal(selectedOfferIndex)">
              Select
            </button>
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal> 

    <!-- Offer Appplied popup -->
    <generic-modal
      v-if="showOfferAppliedModal"
      @close="closeAppliedModal()"
      :height="'auto'"
      :width="msModalDim.width"
      :modalClass="msModalDim.modalClass"
    >
      <template v-slot:header>
        <p></p>
      </template>
      <template v-slot:body>
        <div class="lottie-confetti">
          <VueLottiePlayer
            name="confettiAnim"
            width="100%"
            height="100%"
            loop
            :animationData="require('../assets/confetti.json')"
          />
        </div>
        <div class="lottie-offer-icon">
          <VueLottiePlayer
            name="offerIconAnim"
            width="100%"
            height="100%"
            loop
            :animationData="require('../assets/offer-icon-animation.json')"
          />
        </div>
        <div
          class="offer-applied-popup tc flex-column"
        >
          <div class="fs-18 lh18 normal mb10">
            <!-- Coupon Code -->
            <span class="upercase bold">{{paymentOffers[selectedOfferIndex].paymentModeName}}</span>
            <span class="bold"> payment mode selected!</span>
          </div>
          <div class="mt5 lh18 fs-12">
            you can change later in the cart 
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
    <offer-shown-modal></offer-shown-modal>
  </div>
</template>

<script>
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import VueLottiePlayer from "vue-lottie-player";
import Utility from "../helpers/utility";
import GenericModal from "./GenericModal.vue";
import LocalStorageFunc from "../helpers/localStorageFunc";
import setEvents from "../helpers/events";

import {
  keyMapsAction,
  keyMapsMutation,
  keyMapsGetter,
} from "../helpers/storeKeys";
import OfferShownModal from './OfferShownModal.vue';

export default {
  name: "PaymentOfferItem",
  props: ["dataResults", "moduleProps"],
  components: {
    Carousel,
    Slide,
    GenericModal,
    VueLottiePlayer,
    OfferShownModal
  },
  data() {
    return {
      data: [],
      modulePropsData: {},
      itemsToShow: 1,
      currentSlide: 0,
      tncModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: "tnc-modal",
      },
      msModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: "modal-bg-blackout",
      },
      showOfferAppliedModal: false,
      checkLSforAlreadyApplied: false,
      isUpdated: false,
    };
  },
  mounted() {
    if (LocalStorageFunc.getPaymentOfferPaymentIdLS()) {
      this.checkLSforAlreadyApplied = LocalStorageFunc.getPaymentOfferPaymentIdLS() ? true: false;
    }
    this.data = (typeof this.dataResults != null && typeof this.dataResults != "null" && typeof this.dataResults != "undefined") ? JSON.parse(this.dataResults) : [];
    this.modulePropsData = (typeof this.moduleProps != null && typeof this.moduleProps != "null" && typeof this.moduleProps != "undefined") ? JSON.parse(this.moduleProps) : {};
    this.itemsToShow = + this.modulePropsData?.itemsInScreen;
    this.$store.commit(
      keyMapsMutation.UPDATE_PAYMENT_OFFER_ITEMS, this.data
    );
    if(LocalStorageFunc.getPaymentOfferMode()){
      this.$store.commit(keyMapsMutation.UPDATE_PAYMENT_OFFER_DETAILS, LocalStorageFunc.getPaymentOfferMode());
      this.$store.commit("PaymentOffers/updateSelectedOfferIndex", LocalStorageFunc.getPaymentOfferMode()?.index);
    }
  },
  updated() {
    if(!this.isUpdated && this.paymentOffers.length > 0) {
      this.eventFunction(false, { "positionWithinSection": "1", "category": this.paymentOffers[0].title });
      this.isUpdated = true;
    }
  },
  methods: {
    eventFunction(isClicked=false, eObj={}) {
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      const isLogged = this.isLoggedIn ? "yes" : "no";
      let preEventObj = { ...eObj };
      const eventObj = Utility.amplitudeEventObj(userAction, "PaymentOffer", "10", preEventObj);
      setEvents("amplitude", eventType, eventObj);
    },
    triggerTncModal(item, index) {
      this.$store.commit(keyMapsMutation.UPDATE_SELECTED_PAYMENT_OFFER_INDEX, index);
      this.$store.dispatch(keyMapsAction.PAYMENT_OFFER_TNC_API, {
        // promoCode: this.welcomeOffers[index].promoCode,
        offerId: this.paymentOffers[this.selectedOfferIndex].id,
        langId: 1,
        platform: (window?._siteProps_?.platform) ? ((window?._siteProps_?.platform).toLowerCase()) : 'pwa'
      });
      document.body.classList.add("body-stop-scroll-mobile");
      this.$store.commit(keyMapsMutation.UPDATE_TNC_MODAL_FLAG, true);
      this.eventFunction(true, { "positionWithinSection": index+1, "category": item?.title, "subCategory": "MoreDetails" });
    },
    closeTncModal(event) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.$store.commit(keyMapsMutation.UPDATE_TNC_MODAL_FLAG, false);
      //
      this.eventFunction(true, { "positionWithinSection": this.selectedOfferIndex+1, "category": this.paymentOffers[this.selectedOfferIndex]?.title, "subCategory": "crossTap" });
    },
    triggerPaymentOfferAppliedModal(index) {
      this.$store.commit(keyMapsMutation.UPDATE_SELECTED_PAYMENT_OFFER_INDEX, index);
      this.closeTncModal();
      let addZindex = document.getElementById("offerApply");
      addZindex && addZindex.classList.remove("z-index-offer-applied");
      let appliedOffer = {"status":"", "label": this.paymentOffers[this.selectedOfferIndex].title, "name": this.paymentOffers[this.selectedOfferIndex].paymentId,"index":1, "paymentId": this.paymentOffers[this.selectedOfferIndex].paymentId,"paymentMode": this.paymentOffers[this.selectedOfferIndex].paymentId};
      this.$store.commit(
        keyMapsMutation.UPDATE_PAYMENT_OFFER_DETAILS, appliedOffer
      );

      document.body.classList.add("body-stop-scroll-mobile");
      this.showOfferAppliedModal = true;
      setTimeout(this.closeAppliedModal, 3000);
      this.eventFunction(true, { "positionWithinSection": this.selectedOfferIndex+1, "category": this.paymentOffers[this.selectedOfferIndex]?.title, "subCategory": "Apply" });
    },
    clearPaymentOffers() {
      this.$store.commit(
        keyMapsMutation.UPDATE_PAYMENT_OFFER_DETAILS, {}
      );
      LocalStorageFunc.updatePaymentOfferpaymentId({});
      this.checkLSforAlreadyApplied = false;
    },
    closeAppliedModal(event) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showOfferAppliedModal = false;
    },
  },
  computed: {
    paymentOffers() {
      return this.$store.state.PaymentOffers.paymentOffers;
    },
    selectedOfferIndex() {
      return this.$store.state.PaymentOffers.selectedOfferIndex;
    },
    tncModalFlag() {
      return this.$store.state.PaymentOffers.tncModalFlag;
    },
    checkStoreForAlreadyApplied() {
      return this.$store.state.PaymentOffers.appliedOfferDetails;
    },
    selectedPaymentOfferTnc() {
      return this.$store.state.PaymentOffers.selectedPaymentOfferTnc;
    }
  }
};
</script>
