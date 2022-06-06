<template>
  <div>
    <div
      class="user-address-header"
      @click="handleAddressEdit($event)"
      :class="{ hide: !isOnStorePage }"
    >
      <i class="jfi-location fs-13"></i>
      <span class="address-alias lh18 bold ml5 mr8 fs-14">{{
        locationData.address_alias || "No Location"
      }}</span>
      <i class="jfi-arrow-line"></i>
      <span class="complete-address lh18">{{
        isTrueLocationDetected
          ? locationData.complete_address
          : "Exact location required!"
      }}</span>
    </div>

    <!-- Modal fetching location animation -->
    <generic-modal
      v-if="showLocationLoadingPopup && !isLocationSettled"
      :height="locationLoaderModalDim.height"
      :width="locationLoaderModalDim.width"
      :modalClass="locationLoaderModalDim.modalClass"
    >
      <template v-slot:header>
        <div></div>
      </template>
      <template v-slot:body>
        <div class="setting-location">
          <i class="jfi-location fs-52 bounce-effect"></i>
          <p class="fs-18 bold color-base lh25 block">
            {{ "Setting up location" }}
          </p>
          <!--           <p class="fs-18 bold lh20 block">
            {{
              locationData.address_alias
                ? isTrueLocationDetected && locationData.complete_address
                  ? locationData.complete_address
                  : "Exact location required!"
                : ""
            }}
          </p> -->
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>

    <!-- Modal while intial session is created -->
    <generic-modal
      v-if="!isSessionPresent"
      :height="locationLoaderModalDim.height"
      :width="locationLoaderModalDim.width"
      :modalClass="locationLoaderModalDim.modalClass"
    >
      <template v-slot:header>
        <div></div>
      </template>
      <template v-slot:body>

      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>

    <!-- Location permission popup -->
    <generic-modal
      v-if="showLocationPermissionModal"
      :height="menuModalDim.height"
      :width="menuModalDim.width"
      :modalClass="menuModalDim.modalClass"
    >
      <template v-slot:header>
        <div></div>
      </template>
      <template v-slot:body>
        <div class="flex-column location-permission-div">
          <i class="jfi-location fs-14 color-dark-green"></i>
          <p class="fs-14 tc">
            Allow <b class="bold">Domino's</b> to access this <br />
            device's location ?
          </p>
          <div class="disp-col-inline jcsb full mt20">
            <span
              class="text-cta"
              @click="handleLocationPermissionModal(false)"
            >
              Ask Later
            </span>
            <button
              class="btn medium"
              @click="handleLocationPermissionModal(true)"
            >
              Allow
            </button>
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>

    <!--  Modal Location fetched animation -->
    <!--     <generic-modal
      v-if="showLocationFetchedPopup"
      :height="locationLoaderModalDim.height"
      :width="locationLoaderModalDim.width"
      :modalClass="locationLoaderModalDim.modalClass"
    >
      <template v-slot:header>
        <div></div>
      </template>
      <template v-slot:body>
        <div class="setting-location move-effect lft-pos">
          <div>
            <i class="jfi-location fs-52 loc-ico-effect"></i>
            <div
              class="addr-alias bold fs-18 color-primary font-dec-effect-14 tl pl15"
            >
              {{ isTrueLocationDetected ? locationData.address_alias : "..." }}
            </div>
          </div>
          <div class="addr-complete fs-14 color-base font-dec-effect-12">
            {{
              isTrueLocationDetected
                ? locationData.complete_address
                : "Exact location required!"
            }}
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal> -->

    <location-access-blocked-message></location-access-blocked-message>

    <!-- Login modal -->
    <generic-modal
      v-if="showLoginModal"
      @close="closeLoginModal()"
      :height="loginModalDim.height"
      :width="loginModalDim.width"
      :modalClass="loginModalDim.modalClass"
    >
      <template v-slot:header>
        <!-- <i @click="closeSearchModal()" class="jfi-cross sticky-pop-cross"></i> -->
      </template>
      <template v-slot:body>
        <div class="login-modal">
          <div class="login-top">
            <Carousel
              :autoplay="3000"
              :wrap-around="true"
              :pauseAutoplayOnHover="true"
              :mouseDrag="false"
              :touchDrag="false"
              :transition="900"
              :items-to-show="1"
            >
              <Slide v-for="(data, index) in loginModalData" :key="index">
                <div class="login-nudge-img pr">
                  <template v-if="data.imageUrl">
                    <v-lazy-image
                      :src="staticImgBaseUrl + '/images/global' + data.imageUrl"
                      alt="Login Nudge image"
                    />
                  </template>
                  <div class="dot-flashing-container">
                    <div
                      class="dot-flashing"
                      :class="{ 'active-slide-dot': i == index }"
                      v-for="(m, i) in loginModalData"
                      :key="i"
                    ></div>
                  </div>
                  <div class="domino-logo-img">
                    <v-lazy-image
                      :src="
                        staticImgBaseUrl + '/images/global' + loginModuleIcon
                      "
                      alt="Domino logo image"
                    />
                  </div>
                </div>
                <div class="login-nudge-desc bold" v-html="data.desc"></div>
              </Slide>
              <template #addons> </template>
            </Carousel>
          </div>
          <div class="login-bottom tc">
            <button class="btn login-cta" @click="gotoLogin()">Login</button
            ><br />
            <span class="color-grey fs-12 mt5" @click="closeLoginModal()"
              >Skip Login</span
            >
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
  </div>
</template>

<script setup>
import VLazyImage from "v-lazy-image";
</script>

<script>
import GenericModal from "./GenericModal.vue";
import LocationAccessBlockedMessage from "./LocationAccessBlockedMessage.vue";
import LocalStorageFunc from "../helpers/localStorageFunc";
import Location from "../config/location";
import { keyMapsMutation, keyMapsAction } from "../helpers/storeKeys";
import {
  staticImgBaseUrl,
  projectBasePathWithFolder,
  v2PwaBasePath,
} from "../config/apiEndpoints";
import localStorageFunc from "../helpers/localStorageFunc";
import Utility from "../helpers/utility";
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import LoginNudgeModalData from "../assets/FallbackData/LoginNudgeModalData.json";

export default {
  name: "HomePageUserAddress",

  components: {
    GenericModal,
    LocationAccessBlockedMessage,
    Carousel,
    Slide,
  },

  data() {
    return {
      showMenuModal: false,
      menuModalDim: {
        width: "270px",
        height: "fit-content",
        modalClass: "top-40per",
      },
      staticImgBaseUrl: staticImgBaseUrl,
      showLocationLoadingPopup: false,
      /* showLocationFetchedPopup: false, */
      showLocationPermissionModal: false,
      isOnStorePage: window.location.href.includes(
        Utility.getCurrentStoreFolder()
      ),
      locationLoaderModalDim: {
        width: "100vw",
        height: "100vh",
        modalClass: "full-screen-popup search-modal-wrap",
      },
      showLoginModal: false,
      loginModalDim: {
        width: "100vw",
        height: "100vh",
        modalClass: "full-screen-popup login-modal-wrap",
      },
      loginModalData: [],
      loginModuleIcon: "",
    };
  },

  mounted() {
    this.hidePageLoader();
    this.checkShowLoginModal();
/*     if (LocalStorageFunc.isMiniAppPaytmGpay()) {
      LocalStorageFunc.unsetKey("nextgenhomepage");
      window.location.href =
        v2PwaBasePath + "?utm_source=" + LocalStorageFunc.isMiniAppPaytmGpay();
    } else {
      localStorageFunc.setNextGenKey();
    } */

    localStorageFunc.setNextGenKey();
    /* this.locationAnimation(); */
    this.loginModalData = LoginNudgeModalData?.data
      ? LoginNudgeModalData?.data
      : [];
    this.loginModuleIcon = LoginNudgeModalData?.logoUrl
      ? LoginNudgeModalData?.logoUrl
      : "";
    window.addEventListener("online", function () {
      // console.log("online");
    });
    window.addEventListener("offline", function () {
      // console.log("offline");
    });
    if (!LocalStorageFunc.cartId()) {
      this.$store.dispatch(keyMapsAction.ANONYMOUS_USER_API, {
        startupAppiCalls: () => {
          this.startupAppiCalls();
        },
      });
    } else {
      this.startupAppiCalls();
    }

    Utility.pizzaCounter();
  },

  methods: {
    hidePageLoader() {
      document.getElementById("pageLoader").style.display = "none";
    },
    checkShowLoginModal() {
      var loginSkippedStatus = Utility.getCookie("Login_skipped");
      LocalStorageFunc.isLoggedIn()
        ? (this.showLoginModal = false)
        : loginSkippedStatus == "true"
        ? (this.showLoginModal = false)
        : (this.showLoginModal = true);
      return this.showLoginModal;
    },
    /*     locationAnimation() {
      if (
        window.location.href.includes(Utility.getCurrentStoreFolder()) &&
        !window.location.href.includes(
          Utility.getCurrentStoreFolder() + Location.defaultStoreId
        ) &&
        !localStorageFunc.getLocationAnimationKey()
      ) {
        localStorageFunc.setLocationAnimationKey(true);
        this.showLocationFetchedPopup = true;
      }
      setTimeout(this.closeLocationFetchedPopup, 1600);
    }, */
    /*     closeLocationFetchedPopup() {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showLocationFetchedPopup = false;
    }, */
    handleAddressEdit() {
      let redirectUrl = Utility.takeawayDineinFolder(
        this.isTrueLocationDetected
      );
      window.location.href = {
        D: `${v2PwaBasePath}/${Utility.deliveryFolder(
          this.isLoggedIn
        )}?deliveryType=D`,
        P: `${v2PwaBasePath}/${redirectUrl}?deliveryType=P`,
        DINEIN: `${v2PwaBasePath}/${redirectUrl}?deliveryType=DINEIN`,
        IRCTC: `${v2PwaBasePath}/deliverOnTrain`,
      }[this.currentOrderType];
    },
    startupAppiCalls() {
      if (this.checkShowLoginModal()) {
        return;
      }
      this.$store.dispatch(keyMapsAction.LOAD_LOGIN_STATUS, {});
      this.$store.commit(keyMapsMutation.LOAD_CURRENT_ORDER_TYPE, {});
      if (window.location.href.includes(Utility.getCurrentStoreFolder())) {
        let payload = LocalStorageFunc.getCoordsFromRecentLocation();
        if (payload) {
          payload.source = LocalStorageFunc.getLocationSource();
        }
        if (
          LocalStorageFunc.getDeliveryType() != "D" &&
          LocalStorageFunc.getCoordsForTakeaway()
        ) {
          this.$store.dispatch(
            keyMapsAction.GEO_CODE_API_COMBO,
            LocalStorageFunc.getCoordsForTakeaway()
          );
        } else if (payload?.source == "ip") {
          if (LocalStorageFunc.getHideLocationPopupOnceFromSession()) {
            this.$store.dispatch(keyMapsAction.GOOGLE_GEO_LOCATION_API, {});
            LocalStorageFunc.unsetSessionStorageKey("hideLocationPopupOnce");
          } else {
            this.showLocationPermissionModal = true;
            if (LocalStorageFunc.getIsLocationPermisssionGiven()) {
              this.handleLocationPermissionModal(true);
            } else if (LocalStorageFunc.getAskLocationLaterFromSession()) {
              this.handleLocationPermissionModal(false);
            }
          }
        } else if (
          payload?.coords?.latitude &&
          LocalStorageFunc.isLocationSelectedFromPwaV2()
        ) {
          this.$store.dispatch(keyMapsAction.GEO_CODE_API_COMBO, payload);
        } else {
          this.showLocationPermissionModal = true;
          if (LocalStorageFunc.getIsLocationPermisssionGiven()) {
            this.handleLocationPermissionModal(true);
          } else if (LocalStorageFunc.getAskLocationLaterFromSession()) {
            this.handleLocationPermissionModal(false);
          }
        }
      } else {
        window.location.href =
          projectBasePathWithFolder +
          Utility.getCurrentStoreFolder() +
          LocalStorageFunc.storeId() +
          Utility.getUrlQueryString();
      }
      LocalStorageFunc.isLoggedIn() &&
        this.$store.dispatch(keyMapsAction.LOYALITY_API);
      LocalStorageFunc.isLoggedIn() &&
        this.$store.dispatch(keyMapsAction.WALLET_BALANCE_API);
      this.$store.dispatch(keyMapsAction.PIZZA_TOPPING_API);
      this.$store.dispatch(keyMapsAction.WELCOME_OFFER_API, {});
      this.$store.dispatch(keyMapsAction.PERSONALIZATION_API, {});
      /* this.$store.dispatch(keyMapsAction.LEFT_MENU_API, {}); */
      LocalStorageFunc.getApplyOfferSuccess()?.welcomeOfferApplyStatus !==
        undefined &&
        this.$store.commit("Offers/updateOfferApplyResponse", {
          welcomeOfferApplyStatus:
            LocalStorageFunc.getApplyOfferSuccess().welcomeOfferApplyStatus,
        });
    },
    handleLocationPermissionModal(value) {
      this.showLocationLoadingPopup = true;
      if (value) {
        this.$store.dispatch(keyMapsAction.DETECT_GEO_LOCATION_POPUP, {
          source: "locationPopup",
          showModalM: () => this.showModalM(),
        });
      } else {
        LocalStorageFunc.setAskLocationLaterInSession();
        this.$store.dispatch(keyMapsAction.GOOGLE_GEO_LOCATION_API, {});
      }
      this.showLocationPermissionModal = false;
    },
    showModalM() {
      this.$store.commit(
        keyMapsMutation.UPDATE_SHOW_LOCATION_ACCESS_BLOCKED_MODAL,
        true
      );
    },
    closeLoginModal() {
      let domain = Utility.getDomain();
      Utility.setCookie("Login_skipped", true, 1, domain);
      if (window.location.href.includes(Utility.getCurrentStoreFolder())) {
        window.location.reload();
      } else {
        window.location.href =
          projectBasePathWithFolder +
          Utility.getCurrentStoreFolder() +
          LocalStorageFunc.storeId() +
          Utility.getUrlQueryString();
      }
    },
    gotoLogin() {
      window.location.href = `${v2PwaBasePath}/login`;
    },
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
    isSessionPresent() {
      let _ =
        this.$store.state.Auth.isLoggedIn ||
        this.$store.state.Auth.isSessionPresent;
      return _;
    },
    locationData() {
      return this.$store.state.Location.locationData;
    },
    isTrueLocationDetected() {
      return this.$store.state.Location.isTrueLocationDetected;
    },
    currentOrderType() {
      return this.$store.state.User.currentOrderType;
    },
    storeNotFound() {
      return this.$store.state.Location.storeNotFound;
    },
    isLocationSettled() {
      return this.$store.state.Location.isLocationSettled;
    },
  },
};
</script>

<style scoped></style>
