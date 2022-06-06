<template>
  <div
    class="col-sm-12 pers-offers-wrap pl0"
    :class="{ pr0: welcomeOffers.length > 1 }"
    v-if="welcomeOffers.length > 0"
  >
    <h3 class="fs-14 bold lh20 mb8 pl12">
      {{ welcomeOfferModuleTitle }}
    </h3>
    <Carousel
      :items-to-show="1.05"
      :pauseAutoplayOnHover="true"
      :transition="500"
      :itemsToShow="itemsToShow"
      v-model="currentSlide"
      v-on:update:modelValue="updateCurrentSlide"
    >
      <Slide v-for="(item, index) in welcomeOffers" :key="index">
        <div
          class="pers-offer flex-row"
          v-if="item.type != 'MILESTONE'"
          :style="{ width: widgetWidthHeight.width + 'px' }"
          @click="
            !$store.state.Offers.isOpenOfferAppliedModal ||
            $store.state.Offers.selectedOfferIndex !== index
              ? triggerMilestoneModal($event, index)
              : setTncModalTrue(item)
          "
        >
          <div>
            <div class="fs-14 lh-20 bold offer-title disp-col-inline">
              <i class="jfi-offer-tag mr8 jfi-offers-v2"
                ></i
              >
              <span class="three-dot">{{ item.title }}</span>
            </div>
            <div class="fs-12 lh-18 offer-desc three-dot">
              {{ item.description }}
            </div>
          </div>
          <button
            class="line-btn sm"
            @click="triggerOfferAppliedModal($event, index)"
          >
            Apply
          </button>
        </div>
        <div
          class="milestone-offer flex-row"
          v-if="item.type == 'MILESTONE'"
          :style="{ width: widgetWidthHeight.width + 'px' }"
          @click="
            !$store.state.Offers.isOpenOfferAppliedModal ||
            $store.state.Offers.selectedOfferIndex !== index
              ? triggerMilestoneModal($event, index)
              : setTncModalTrue(index)
          "
        >
          <div>
            <ul class="fs-14 bold disp-col-inline ms-timeline">
              <li
                class=""
                v-for="(mileStoneItem, i) in item.milestonesCouponDTOList"
                :key="i"
              >
                <div class="bold order-no">
                  {{ i + 1 }}{{ nth(i + 1) }} Order
                </div>
                <div class="fs-10 bold three-dot ms-title">
                  {{ mileStoneItem.title }}
                </div>
                <div class="border-arrow-img"></div>
              </li>
            </ul>
          </div>
          <button
            class="line-btn sm"
            @click="triggerOfferAppliedModal($event, index)"
          >
            Apply
          </button>
        </div>
      </Slide>
    </Carousel>

    <!-- Milestone popup -->
    <generic-modal
      v-if="showMilestoneModal"
      @close="closeMilestoneModal()"
      :height="msModalDim.height"
      :width="msModalDim.width"
      :animationStyle="msModalDim.animationStyle"
    >
      <template v-slot:header>
        <i
          @click="closeMilestoneModal()"
          class="jfi-cross sticky-pop-cross"
        ></i>
      </template>
      <template v-slot:body>
        <div class="ms-offer-pop-content">
          <div class="logo-box mb10">
            <div class="logo-sm-img"></div>
          </div>
          <div class="fs-18 bold mb15">
            {{
              welcomeOffers[currentMileStoneIndex].type === "MILESTONE"
                ? "Milestone Offer"
                : welcomeOffers[currentMileStoneIndex]?.title
            }}
          </div>
          <ul class="fs-14 bold disp-col-inline ms-timeline">
            <li
              v-for="(mileStoneItem, i) in welcomeOffers[currentMileStoneIndex]
                ?.milestonesCouponDTOList"
              :key="i"
            >
              <div class="bold order-no color-grey">
                {{ i + 1 }}{{ nth(i + 1) }} Order
              </div>
              <div class="fs-12 bold color-green pl10">
                {{ mileStoneItem.title }}
              </div>
              <div class="border-arrow-img-grey"></div>
            </li>
          </ul>
          <div class="offer-state">
            <div class="coupan-dets color-slaty ib">
              Coupon code:
              <span class="bold italic">{{
                welcomeOffers[currentMileStoneIndex]?.promoCode
              }}</span>
            </div>
            <!-- tobe shown when coupan code is applied -->
            <div class="code-applied-dets disp-col-inline hide">
              <span>Code NONSTOP60 applied!</span>
              <span class="bold remove-code">Remove</span>
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
          <div class="btn-wrap">
            <button
              class="btn medium bold"
              @click="triggerOfferAppliedModal($event, currentMileStoneIndex)"
            >
              Apply Offer
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
          v-if="offerApplyResponse?.status == 'SUCCESS'"
        >
          <div class="fs-12 lh18 normal mb10">
            Coupon Code
            <span class="upercase bold">{{
              welcomeOffers[selectedOfferIndex]?.promoCode
            }}</span>
            applied!
          </div>
          <div
            class="fs-18 lh25 bold"
            v-if="welcomeOffers[selectedOfferIndex].couponDetail?.maxCap"
          >
            You will save ₹{{
              welcomeOffers[selectedOfferIndex].couponDetail?.maxCap
            }}!
          </div>
        </div>
        <div
          class="offer-applied-popup tc"
          v-if="!(offerApplyResponse?.status == 'SUCCESS')"
        >
          <div class="">
            {{ offerApplyResponse?.errors?.[0]?.displayMsg || "" }}
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>

    <!-- offer not applied msg modal -->
    <generic-modal
      v-if="showOfferNotApplied"
      :height="msModalDim.height"
      :width="msModalDim.width"
      :animationStyle="msModalDim.animationStyle"
    >
      <template v-slot:header>
        <div class="bold fs-18">
        </div>
      </template>
      <template v-slot:body>
        <div class="">
          <p class="fs-16 lh20 error-msg pTB24 mb20">
            {{ offerFailedMsg }}
          </p>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
  </div>
</template>

<script>
import { Carousel, Slide } from "vue3-carousel";
import VueLottiePlayer from "vue-lottie-player";
import GenericModal from "./GenericModal.vue";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

import "vue3-carousel/dist/carousel.css";
import {
  keyMapsAction,
  keyMapsGetter,
  keyMapsMutation,
} from "../helpers/storeKeys";

export default {
  name: "PersonalizedOffers",
  props: ["moduleProps"],

  components: {
    VueLottiePlayer,
    Carousel,
    Slide,
    GenericModal,
  },

  data() {
    return {
      moduleProperty: {},
      showMilestoneModal: false,
      showOfferAppliedModal: false,
      msModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: "modal-bg-blackout",
      },
      currentMileStoneIndex: -1,
      widgetWidthHeight: {},
      isInit: true,
      currentSlide: 0,
      itemsToShow: 1,
      showOfferNotApplied: false
    };
  },
  mounted() {
    this.moduleProperty = (this.moduleProps &&
      JSON.parse(this.moduleProps)) || {
      title: "Hi ${NAME}, we have an offer for you",
      type: "",
      cardType: "",
      aspectRatio: "",
      itemsInScreen: "",
    };
    this.itemsToShow = +this.moduleProperty?.itemsInScreen;
    this.widgetWidthHeight = Utility.getWidgetWidthHeight(
      this.moduleProperty?.itemsInScreen,
      this.moduleProperty?.aspectRatio
    );
  },
  updated() {
    if (this.isInit && this.welcomeOffers?.length > 0) {
      this.isInit = false;
      //Amplitude impression event load
      this.eventFunction(0);
    }
  },
  created() {},
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
    eventFunction(index, isClicked = false, subCat = "") {
      let preEventObj = {};
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      let mStage = 1;
      if (
        this.welcomeOffers[index]?.type &&
        this.welcomeOffers[index]?.type == "MILESTONE"
      ) {
        // console.log('MILESTONE type...', index, this.welcomeOffers[index]);
        mStage = this.milestoneStage(this.welcomeOffers[index]);
        preEventObj = {
          positionWithinSection: index + 1,
          category: this.welcomeOffers[index]?.title,
          offerType: "milestone",
          milestoneStage: mStage,
        };
      } else {
        // console.log('NONN  MILESTONE type...', index, this.welcomeOffers[index]);
        preEventObj = {
          positionWithinSection: index + 1,
          category: this.welcomeOffers[index]?.title,
          offerType: "percentageDiscount",
        };
        if (isClicked) preEventObj.subCategory = subCat;
      }
      const eventObj = Utility.amplitudeEventObj(
        userAction,
        "Offer",
        "6",
        preEventObj
      );
      setEvents("amplitude", eventType, eventObj);
    },
    updateCurrentSlide(slideVal) {
      //Amplitude impression event load
      this.eventFunction(slideVal);
    },
    nth(n) {
      return ["st", "nd", "rd"][((((n + 90) % 100) - 10) % 10) - 1] || "th";
    },
    milestoneEventFunction(index, isClicked = false, eObj) {
      if (
        this.welcomeOffers[index]?.type &&
        this.welcomeOffers[index]?.type == "MILESTONE"
      ) {
        let preEventObj = {};
        let eventType = isClicked ? "Click" : "Impression";
        let userAction = isClicked ? "click" : "impression";
        let mStage = 1;
        // console.log('MILESTONE type...', index, this.welcomeOffers[index]);
        mStage = this.milestoneStage(this.welcomeOffers[index]);
        preEventObj = {
          positionWithinSection: index + 1,
          category: this.welcomeOffers[index]?.title,
          offerType: "milestone",
          milestoneStage: mStage,
          ...eObj,
        };
        const eventObj = Utility.amplitudeEventObj(
          userAction,
          "Offer",
          "6",
          preEventObj
        );
        setEvents("amplitude", eventType, eventObj);
      }
    },
    triggerMilestoneModal(event, index) {
      this.currentMileStoneIndex = index;
      this.$store.dispatch(keyMapsAction.OFFER_TNC_API, {
        promoCode: this.welcomeOffers[index].promoCode,
      });
      document.body.classList.add("body-stop-scroll-mobile");
      this.showMilestoneModal = true;
      //Amplitude click event load
      this.eventFunction(index, true, "BannerClick");
      //Amplitude impression event load
      this.milestoneEventFunction(index, false, {
        offerStage: "Apply",
        impressionType: "Apply Pop Up",
      });
    },
    setTncModalTrue(data) {
      const { promoCode } = data;
      this.$store.commit("Offers/updateSelectedOfferTnc", []);
      promoCode &&
        this.$store.dispatch("Offers/offerTncApi", { promoCode: promoCode });
      this.$store.commit("Offers/updateShowTncModal", true);
    },
    closeMilestoneModal(event) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showMilestoneModal = false;
    },
    triggerOfferAppliedModal(event, index) {
      event.stopPropagation();
      this.$store.commit(keyMapsMutation.UPDATE_SELECTED_OFFER_INDEX, index);
      this.closeMilestoneModal();
      this.$store.dispatch(keyMapsAction.APPLY_OFFER_API, {
        promoCode: this.$store.state.Offers.welcomeOffers[index].promoCode,
        paymentOptions:
          this.$store.state.Offers.welcomeOffers[index].paymentOptions,
        showOfferAppliedModal: () => {
          document.body.classList.add("body-stop-scroll-mobile");
          this.showOfferAppliedModal = true;
          setTimeout(this.closeAppliedModal, 3000);
        },
      });
      //Amplitude click event load
      this.eventFunction(index, true, "Apply");

      this.$store.commit("Offers/updateOfferApplyModalKey", true);
      let addZindex = document.getElementById("offerApply");
      addZindex && addZindex.classList.add("z-index-offer-applied");

      if(this.offerFailedMsg){
        console.log("----", this.offerFailedMsg);
        this.showOfferNotApplied = true;
        setTimeout(() => {
          this.showOfferNotApplied = false;
        }, 4000);
      }
    },
    closeAppliedModal(event) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showOfferAppliedModal = false;
    },
  },
  computed: {
    welcomeOffers() {
      return this.$store.state.Offers.welcomeOffers;
    },
    selectedOfferIndex() {
      return this.$store.state.Offers.selectedOfferIndex;
    },
    welcomeOfferModuleTitle() {
      return this.moduleProperty.title.replace(
        "${NAME}",
        this.$store.getters[keyMapsGetter.GET_USER_DATA]?.firstName ||
          "PIZZA Lover"
      );
    },
    offerApplyResponse() {
      return this.$store.state.Offers.offerApplyResponse;
    },
    selectedOfferTnc() {
      return this.$store.state.Offers.selectedOfferTnc;
    },
    offerFailedMsg() {
      return this.$store.state.Offers.offerFailedMsg;
    },
  },
};
</script>
