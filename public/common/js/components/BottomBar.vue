<template>
  <div class="botm-bar-wrap">
    <offer-applied-message v-if="welcomeOffers?.length > 0"></offer-applied-message>
    <!-- disabled to show the payment offer message as of now -->
    <!-- <payment-offer-message v-if="paymentOffers?.length  > 0"></payment-offer-message> --> 
    <!-- with-checkout class is added when checkout option is available -->
    <div
      class="bottom-nav active"
      :class="{ 'with-checkout': cartQuantity > 0 }"
    >
      <a href="javascript:void(0);" class="nav-item active">
        <div @click="triggerMenuModal($event)" class="nav-item-content">
          <span class="nav-item-text">Menu</span>
          <i aria-hidden="true" class="jfi-menu-full"></i>
        </div>
      </a>

      <a href="javascript:void(0);" class="nav-item">
        <div class="nav-item-content pl0 pr32" @click="handlePizza99($event)">
          <span class="nav-item-text">Pizza@99</span>
          <i aria-hidden="true" class="jfi-tag-v2"></i>
        </div>
      </a>

      <a
        href="javascript:void(0);"
        class="nav-item cart-nav"
        @click="handleCartClick($event)"
      >

        <div v-if="cartQuantity >= 0" class="btn btn-checkout disp-col-inline">
          <span class="cart-total">
            <transition name="slide-fade" mode="out-in">
              <span class="count-holder" :key="cartQuantity">{{ cartQuantity }}</span>
            </transition>
            <i aria-hidden="true" class="jfi-cart fs-18"></i>
          </span>
          <span class="ml10 tl">
            <span class="block fs-11"
              ><span class="rs">Rs.</span>
              <span> {{ cartTotal }} + taxes </span>
            </span>
            <span class="block fs-14 bold">Checkout</span>
          </span>
          <i aria-hidden="true" class="jfi-arrow-line fs-16"></i>
        </div>
      </a>
    </div>


    <!-- menu popup -->
    <transition
      name="custom-classes-transition"
      enter-active-class="modal-show"
    >
    <generic-modal
      v-if="showMenuModal"
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
        <div class="menu-pop-content">
          <ul
            class=""
            v-for="(value, propertyName, index) in getBottomBarData"
            :key="propertyName"
          >
            <li
              class="disp-col-inline mb20 cursor-pointer"
              @click="
                goto(
                  index,
                  value.categoryId,
                  propertyName,
                  Object.keys(getBottomBarData[propertyName]?.data).length || 0
                )
              "
            >
              <div class="pr tag-pos">
                <template v-if="value?.categoryImages?.length > 0">
                  <template v-for="(img, index) in value?.categoryImages">
                    <template v-if="img.mediaSize == 'MenuV1'">
                      <v-lazy-image 
                        class="img-product-menu" 
                        alt="menu image"
                        :src="prepareImgPath(img.mediaPath)"
                        :src-placeholder="staticImgBaseUrl + '/images/global/default-product-img.png'" 
                      />
                    </template>
                  </template>
                  <!-- <i class="tag-veg"></i> default-product-img.png -->
                </template>
                <template v-else>
                  <v-lazy-image class="default-product-img" alt="default image" :src="staticImgBaseUrl + '/images/global/default-product-img.png'" />
                </template>
              </div>
              <div>
                {{ propertyName }}
                <span
                  >({{
                    Object.keys(getBottomBarData[propertyName]?.data).length ||
                    0
                  }})</span
                >
              </div>
            </li>
          </ul>
        </div>

        <div class="menu-pop-close disp-col-inline" @click="closeModalM(true)">
          <span v-if="cartQuantity > 0" > 
            <i class="jfi-close-v2"></i>
          </span>
          <span v-else class="flex-row jcsb align-items-center "> 
            <i class="jfi-close-v2"></i>
            <span class="bold fs-12 ml5">Close</span>
          </span>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
    </transition>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
</script>
<script>
import GenericModal from "./GenericModal.vue";
import OfferAppliedMessage from "./OfferAppliedMessage.vue";
import PaymentOfferMessage from "./PaymentOfferMessage.vue";

import {
  keyMapsAction,
  keyMapsGetter,
  keyMapsMutation,
} from "../helpers/storeKeys";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";
import { imgBaseUrl, staticImgBaseUrl, v2PwaBasePath } from "../config/apiEndpoints";
import localStorageFunc from '../helpers/localStorageFunc';

export default {
  name: "BottomBar",
  components: {
    GenericModal,
    OfferAppliedMessage,
    PaymentOfferMessage
  },
  props: ["modalProps"],
  data() {
    return {
      showMenuModal: false,
      menuModalDim: { width: "290px", height: "fit-content", modalClass: "menu-modal" },
      staticImgBaseUrl: staticImgBaseUrl,
      imgBaseUrl: imgBaseUrl
    };
  },
  mounted() {
    if (!this.moduleProps) {
      this.moduleProps = {
        title: "Non Veg Pizza",
        type: "VERTICAL_PRODUCT_SCROLL",
        cardType: "video/static",
        aspectRatio: 1,
        itemsInScreen: 1.75,
      };
    }
  },
  methods: {
    prepareImgPath(url) {
      if (url.indexOf("http") != -1) {
        return url;
      } else {
        return (this.imgBaseUrl + '/' + url);
      }
    },
    eventFunction(isClicked=false, preEventObj={}) {
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      const eventObj = Utility.amplitudeEventObj(userAction, "bottomMenu", "-1", preEventObj);
      setEvents("amplitude", eventType, eventObj);
    },
    triggerMenuModal(e) {
      //Amplitude click event load
      this.eventFunction(true, { "category": "menu" });
      document.body.classList.add("body-stop-scroll-mobile");
      this.showMenuModal = true;
      //Amplitude impression event load
      this.eventFunction(false, { "category": "menu" });
      this.$store.commit(keyMapsMutation.OPEN_ALL_CARDS, true);
    },
    closeModalM(crossClicked=false) {
      if(crossClicked) {
        //Amplitude click event load
        this.eventFunction(true, { "category": "menu", "subCategory": "close" });
      }
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showMenuModal = false;
    },
    handlePizza99(event, item) {
      //Amplitude click event load
      this.eventFunction(true, { "category": "pizza@99" });
      window.location.href = `${v2PwaBasePath}/edv`;
    },
    handleCartClick(event, item) {
      //Amplitude click event load
      localStorageFunc.setTrackerOfKeyInSession(true)
      this.eventFunction(true, { "category": "checkout" });
      window.location.href = `${v2PwaBasePath}/cart`;
    },
    goto(index, itmId, categoryName, count) {
      this.$store.commit(keyMapsMutation.TRIGGER_FILTER_REF, {});
      //Amplitude click event load
      this.eventFunction(true, { "positionWithinSection": index+1, "category": "menu", "subCategory": categoryName });
      if (itmId) {
        setTimeout(() => {
          this.$store.commit(
            keyMapsMutation.SET_PRODUCT_COUNTER_STRING,
            categoryName + " ("+count+")"
          );
        }, 0);
        setTimeout(() => {
          Utility.scrollUptoTo("element_catg_" + itmId);
          this.closeModalM();
        }, 0);
      }
    },
  },
  computed: {
    getBottomBarData() {
      return this.$store.getters[keyMapsGetter.GET_PRODUCT_ITEM_FOR_BOTTOMBAR];
    },
    cartQuantity() {
      return this.$store.getters[keyMapsGetter.CART_QUANTITY];
    },
    cartTotal() {
      return this.$store.getters[keyMapsGetter.CART_TOTAL];
    },
    welcomeOffers() {
      return this.$store.state.Offers.welcomeOffers;
    },
    paymentOffers() {
      return this.$store.state.PaymentOffers.paymentOffers;
    },
  },
};
</script>
