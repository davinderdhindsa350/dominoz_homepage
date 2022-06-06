<template>
  <div :class="{ hide: !isOnStorePage }">
    <div
      class="profile-icon flex-column"
      @click="handleProfileIconClick($event)"
    >
      <a href="javascript:void(0);"><i class="jfi-user fs-18"></i></a>
    </div>
    <transition
      name="profile-icon-classes-transition"
      enter-active-class="modal-show-slide-out"
      leave-active-class="modal-hiding-slide-in"
    >
      <generic-modal
        v-if="showAddressEditModal"
        @close="closeModalA()"
        :width="addressEditModal.width"
        :height="addressEditModal.height"
        :modalClass="addressEditModal.modalClass"
      >
        <template v-slot:header>
          <div class="profile-drawer flex-row" v-if="!isLoggedIn">
            <div class="user-cover"><i class="jfi-user fs-18"></i></div>
            <Carousel
              :autoplay="4000"
              :wrap-around="true"
              :pauseAutoplayOnHover="true"
              :mouseDrag="false"
              :touchDrag="false"
              :transition="1200"
            >
              <template #addons> 
                <div class="login-cars-dots">Login for &nbsp;&nbsp; <Pagination /> </div>
              </template>
              <Slide
                v-for="(message, index) in loginMessageDynamicText"
                :key="index"
              >
                <div class="pl20 flex-column login-text">
                  <div class="fs-14">
                    {{ message }}
                  </div>
                </div>
              </Slide>
              
            </Carousel>
            <button
              class="btn profile-drawer-btn fs-14 lh20 bold"
              @click="handleLoginLink($event)"
            >
              Login
            </button>
          </div>
          <div
            class="profile-drawer flex-row light-blue-background jcfs"
            v-if="isLoggedIn"
          >
            <div class="user-cover light-blue">
              <i class="jfi-user fs-18"></i>
            </div>
            <div class="user-detail">
              <div>{{ getUserData.firstName }} {{ getUserData.lastName }}</div>
              <div class="fs-12 lh18 bold">{{ mobile }}</div>
            </div>
            <div
              class="edit-btn fs-12 lh18 bold hand"
              @click="handleProfileEdit($event)"
            >
              Edit
            </div>
          </div>
        </template>
        <template v-slot:body>
          <div class="mt14">
            <div
              v-for="(item, index) in links"
              class="fs-14 mb30 color-black"
              :key="index"
              @click="handleSidebarLinkClick($event, item)"
            >
              {{ item.name }}
            </div>
          </div>
        </template>
        <template v-slot:footer>
          <div class="text-align-right">{{ APP_VERSION }}</div>
        </template>
      </generic-modal>
    </transition>
  </div>
</template>

<script>
import { Carousel, Slide, Pagination } from "vue3-carousel";

import "vue3-carousel/dist/carousel.css";
import GenericModal from "./GenericModal.vue";
import ApiKeys from "../config/apiKeys";
import Utility from "../helpers/utility";
import { keyMapsGetter } from "../helpers/storeKeys";
import { v2PwaBasePath } from "../config/apiEndpoints";
import setEvents from "../helpers/events";

export default {
  components: { GenericModal, Carousel, Slide, Pagination },
  name: "ProfileIcon",

  props: ["dataLeftMenuLinks"],

  data() {
    return {
      APP_VERSION: ApiKeys.APP_VERSION,
      addressEditModal: {
        width: "312px",
        height: "100vh",
        modalClass: "profile-Modal",
      },
      isOnStorePage: window.location.href.includes(
        Utility.getCurrentStoreFolder()
      ),
      showAddressEditModal: false,
      links: [],
    };
  },

  mounted() {
    this.links = JSON.parse(this.dataLeftMenuLinks);
  },

  methods: {
    eventFunction(isClicked=false, eObj={}) {
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      const isLogged = this.isLoggedIn ? "yes" : "no";
      let preEventObj = { "category": "profile", "loginStatus": isLogged, ...eObj };
      const eventObj = Utility.amplitudeEventObj(userAction, "topbar", "1", preEventObj);
      setEvents("amplitude", eventType, eventObj);
    },
    handleProfileIconClick() {
      //Amplitude click event load
      this.eventFunction(true);
      this.showAddressEditModal = true;
      //Amplitude impression event load
      this.eventFunction(false, { "actionType": "hamburgerMenu" });
    },
    closeModalA() {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showAddressEditModal = false;
    },
    handleLoginLink(event) {
      this.eventFunction(true, { "subCategory": "login" });
      Utility.sendToLoginScreen(window.location);
    },
    handleSidebarLinkClick(event, item) {
      this.eventFunction(true, { "subCategory": item?.name });
      if (item.link_rewrite) {
        window.location.href = item.link_rewrite;
      }
      return;
    },
    handleProfileEdit(event) {
      this.eventFunction(true, { "subCategory": "edit" });
      window.location.href = `${v2PwaBasePath}/profile`;
    },
  },

  computed: {
    getUserData() {
      return this.$store.getters[keyMapsGetter.GET_USER_DATA];
    },
    isLoggedIn() {
      let _ = this.$store.state.Auth.isLoggedIn;
      this.links = JSON.parse(this.dataLeftMenuLinks)?.filter((__) => {
        return !(!_ && __.name == "My Wallet");
      });
      return _;
    },
    loginMessageDynamicText() {
      return this.$store.state.Auth.loginMessageDynamicText;
    },
    mobile() {
      return this.$store.state.User.loyalty.mobile;
    },
  },
};
</script>

<style scoped></style>
