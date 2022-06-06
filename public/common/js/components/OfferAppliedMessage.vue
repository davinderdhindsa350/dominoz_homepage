<template>
  <div id="offerApply" class="pr">
    <!-- <transition name="bg-change" mode="out-in"> -->
    <div
      class="offer-applied-message fs-11 lh16 flex-row"
      v-if="
        $store.state.Offers.selectedOfferIndex !== -1 &&
        offerApplyResponse?.status == 'SUCCESS'
      "
      :key="offerApplyResponse?.status == 'SUCCESS'"
    >
      <div>
        <img
          :src="staticImgBaseUrl + '/images/global/offer-percentage.svg'"
          class="pd10"
        />
      </div>
      <h4>
        <span
          >Code
          {{ welcomeOffers[selectedOfferIndex]?.promoCode }} applied!</span
        >
        <span
          v-if="
            welcomeOffers[selectedOfferIndex]?.couponDetail?.maxCap ||
            welcomeOffers[selectedOfferIndex]?.couponDetail?.discountAmount
          "
          >Add items worth ₹{{
            welcomeOffers[selectedOfferIndex]?.couponDetail?.minCartValue
          }}
          or more to get ₹{{
            welcomeOffers[selectedOfferIndex]?.couponDetail?.maxCap ||
            welcomeOffers[selectedOfferIndex]?.couponDetail?.discountAmount
          }}
          cashback</span
        >
      </h4>
      <div
        class="mr20 hand-cursor"
        @click="
          triggerTncModal($event, index, welcomeOffers[selectedOfferIndex])
        "
      >
        <i class="jfi-arrow-line ml7"></i>
      </div>
    </div>
    <!-- </transition> -->
    <!-- Offer applied message popup -->
    <generic-modal
      v-if="offerApplyResponse?.status == 'SUCCESS' && showTncModalFromStore"
      @close="closeTncModal()"
      :height="msModalDim.height"
      :width="msModalDim.width"
      :animationStyle="msModalDim.animationStyle"
    >
      <template v-slot:header>
        <i @click="closeTncModal(true)" class="jfi-cross sticky-pop-cross"></i>
      </template>
      <template v-slot:body>
        <div class="ms-offer-pop-content">
          <div class="logo-box mb10">
            <div class="logo-sm-img"></div>
          </div>
          <div class="fs-18 bold mb15">
            {{ welcomeOffers[selectedOfferIndex]?.text }}
          </div>
          <ul class="fs-14 bold disp-col-inline ms-timeline" v-if="welcomeOffers[selectedOfferIndex]?.milestonesCouponDTOList">
            <li
              v-for="(mileStoneItem, i) in welcomeOffers[selectedOfferIndex]
                ?.milestonesCouponDTOList"
              :key="i"
            >
              <div class="bold order-no color-grey">
                {{ i + 1 }}{{ nth(i + 1) }} Order
              </div>
              <div class="fs-12 bold color-green">
                {{ mileStoneItem.title }}
              </div>
              <div class="border-arrow-img-grey"></div>
            </li>
          </ul>
          <div class="offer-state">
            <!-- tobe shown when coupan code is applied -->
            <div class="code-applied-dets disp-col-inline">
              <span
                >Code
                {{ welcomeOffers[selectedOfferIndex].promoCode }} applied!</span
              >
              <span
                class="bold remove-code"
                @click="removeCouponCode($event, index)"
                >Remove</span
              >
            </div>
          </div>
          <div class="ms-offer-details">
            <h3 class="bold">Offer Details:</h3>
            <ul class="disc-styled mt10 color-grey fs-11">
              <li
                class="lh16 mb5"
                v-for="tncItem in selectedOfferTnc"
                :key="tncItem"
              >
                {{ tncItem }}
              </li>
            </ul>
          </div>
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
import {
  keyMapsAction,
  keyMapsGetter,
  keyMapsMutation,
} from "../helpers/storeKeys";
import { staticImgBaseUrl } from "../config/apiEndpoints";
import LocalStorageFunc from "../helpers/localStorageFunc";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

export default {
  name: "OfferAppliedMessage",
  props: [],

  components: {
    GenericModal,
  },

  data() {
    return {
      showTncModal: false,
      msModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: "modal-bg-blackout",
      },
      staticImgBaseUrl: staticImgBaseUrl,
    };
  },
  mounted() {
    let addZindex = document.getElementById("offerApply");
    addZindex && addZindex.classList.add("z-index-offer-applied");
  },
  watch: {
    offerApplyResponse: function (value) {
      if (value?.status == "SUCCESS") {
        //Amplitude impression event load
        this.milestoneEventFunction(this.selectedOfferIndex, false, {
          offerStage: "Applied",
          impressionType: "CTA",
        });
      }
    },
  },
  methods: {
    milestoneStage(offer) {
      if (
        offer?.milestonesCouponDTOList &&
        offer.milestonesCouponDTOList.length > 0
      ) {
        for (let i = 0; i < offer.milestonesCouponDTOList.length; i++) {
          let ele = offer.milestonesCouponDTOList[i];
          if (!ele.redeemed) return i + 1;
        }
      } else {
        return 1;
      }
    },
    milestoneEventFunction(index, isClicked=false, eObj) {
      let preEventObj = {};
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      if(this.welcomeOffers[index]?.type && this.welcomeOffers[index]?.type == "MILESTONE") {
        let mStage = 1;
        mStage = this.milestoneStage(this.welcomeOffers[index]);
        preEventObj = { "positionWithinSection": index + 1, "category": this.welcomeOffers[index]?.title, "offerType": "milestone", "milestoneStage": mStage, ...eObj };
      } else {
        preEventObj = { "positionWithinSection": index + 1, "category": this.welcomeOffers[index]?.title, "offerType": "percentageDiscount", ...eObj };
      } 
      const eventObj = Utility.amplitudeEventObj(userAction, "Offer", "6", preEventObj);
      setEvents("amplitude", eventType, eventObj);
    },
    nth(n) {
      return ["st", "nd", "rd"][((((n + 90) % 100) - 10) % 10) - 1] || "th";
    },
    removeCouponCode() {
      //Amplitude Click event load
      this.milestoneEventFunction(this.selectedOfferIndex, true, {
        offerStage: "Applied",
        subCategory: "remove",
      });

      if (
        this.welcomeOffers[this.selectedOfferIndex]?.paymentOptions?.filter(
          (_) => _.paymentId == "CC"
        )?.length > 0 &&
        LocalStorageFunc.getPaymentOfferPaymentIdLS() == "CC"
      ) {
        LocalStorageFunc.updatePaymentOfferpaymentId({});
      }

      this.$store.commit(keyMapsMutation.UPDATE_SELECTED_OFFER_INDEX, -1);
      this.$store.commit(keyMapsMutation.UPDATE_OFFER_APPLY_RESPONSE, {});
      LocalStorageFunc.updateCouponCode();

      this.closeTncModal();
      this.$store.commit("Offers/updateOfferApplyModalKey", false);
      this.$store.commit("Offers/updateShowTncModal", false);
      let addZindex = document.getElementById("offerApply");
      addZindex.classList.remove("z-index-offer-applied");
    },
    triggerTncModal(event, index, data) {
      //Amplitude Click event load
      this.milestoneEventFunction(this.selectedOfferIndex, true, {
        offerStage: "Applied",
      });
      document.body.classList.add("body-stop-scroll-mobile");
      this.showTncModal = true;
      this.$store.commit("Offers/updateShowTncModal", true);
      const { promoCode } = data;
      this.$store.commit("Offers/updateSelectedOfferTnc", []);
      promoCode &&
        this.$store.dispatch("Offers/offerTncApi", { promoCode: promoCode });
      //Amplitude impression event load
      this.milestoneEventFunction(this.selectedOfferIndex, false, {
        offerStage: "Applied",
        impressionType: "Applied Pop Up",
      });
    },
    closeTncModal(crossClick = false) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showTncModal = false;
      this.$store.commit("Offers/updateShowTncModal", false);
      if (crossClick) {
        //Amplitude Click event load
        this.milestoneEventFunction(this.selectedOfferIndex, true, {
          offerStage: "Applied",
          subCategory: "crosstap",
        });
      }
    },
  },
  computed: {
    welcomeOffers() {
      return this.$store.state.Offers.welcomeOffers;
    },
    selectedOfferIndex() {
      return this.$store.state.Offers.selectedOfferIndex;
    },
    offerApplyResponse() {
      return this.$store.state.Offers.offerApplyResponse;
    },
    selectedOfferTnc() {
      return this.$store.state.Offers.selectedOfferTnc;
    },
    showTncModalFromStore() {
      return this.$store.state.Offers.showTncModal;
    },
  },
};
</script>
