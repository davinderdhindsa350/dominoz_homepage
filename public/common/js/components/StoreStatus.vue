<template>
  <div class="col-sm-12  flex-column gap10">
    <div
      class="store-status pt16 pb16 flex-row mb16"
      v-if="!storeOnline && !currentLocationUnservicable"
    >
      <div class="icon-container">
        <i class="jfi-info-line fs-22 lh22"></i>
      </div>
      <div>
        <p class="bold fs-14 lh20">{{ storeOfflineTitle }}</p>
        <p class="fs-12 lh18" v-html="storeOfflineDetails"></p>
      </div>
    </div>
    <div
      class="store-status pt16 pb16 flex-row mb16"
      v-if="!storeOnline && currentLocationUnservicable"
    >
      <div class="icon-container">
        <i class="jfi-info-line fs-22 lh22"></i>
      </div>
      <div>
        <p class="bold fs-14 lh20">Sorry! We do not deliver to your location</p>
      </div>
    </div>
    <div
      class="current-location-status pt16 pb16 flex-row mb16"
      v-if="
        currentLocationUnservicable &&
        storeOnline &&
        !hideCurrentLocationMessage
      "
    >
      <i
        class="jfi-cross"
        @click="hideCurrentLcationMessageDisplay($event)"
      ></i>
      <div class="icon-container">
        <i class="jfi-info-line fs-22 lh22"></i>
      </div>
      <div>
        <p class="bold fs-14 lh20">
          Your Location is set to "{{
            currentOrderType == "D"
              ? locationData.address_alias
              : locationData.complete_address
          }}"
        </p>
        <p
          class="fs-12 lh18"
          v-html="'Your current location is unservicable'"
        ></p>
        <p
          class="current-location-map-link mt12 bold fs-12 lh18"
          @click="gotoMapScreen($event)"
        >
          Use current location anyway
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { staticImgBaseUrl, v2PwaBasePath } from "../config/apiEndpoints";
import Utility from "../helpers/utility";
import LocalStorageFunc from "../helpers/localStorageFunc";

export default {
  name: "StoreStatus",

  components: {},

  data() {
    return {
      staticImgBaseUrl: staticImgBaseUrl,
      hideCurrentLocationMessage: false,
    };
  },

  mounted() {},

  methods: {
    hideCurrentLcationMessageDisplay() {
      this.hideCurrentLocationMessage = true;
    },
    gotoMapScreen(event) {
      LocalStorageFunc.setNoDeliveryZoneInSession(
        this.$store.state.Location.noDeliveryZoneData
      );
      window.location.href = `${v2PwaBasePath}/deliveryMapScreen?deliveryType=D&storeSelected=false&noDeliveryZone=true`;
      return;
    },
  },
  computed: {
    currentOrderType() {
      return this.$store.state.User.currentOrderType;
    },
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
    storeOnline() {
      let _ = this.$store.state.Location.storeDetails.online;
      return _;
    },
    currentLocationUnservicable() {
      return this.$store.state.Location.currentLocationUnservicable;
    },
    locationData() {
      return this.$store.state.Location.locationData;
    },
    storeOfflineDetails() {
      let __ = this.$store.state.Location.storeDetails.online;
      let _ = this.$store.state.Location.takeawayDineAvailability;

      let redirectUrl = Utility.takeawayDineinFolder(
        this.isTrueLocationDetected
      );
      if (LocalStorageFunc.getLocationSource() != "ip") {
        redirectUrl = "nearestStore";
      }

      let takeawayHref = `${v2PwaBasePath}/${redirectUrl}?deliveryType=P`;
      let dineHref = `${v2PwaBasePath}/${redirectUrl}?deliveryType=DINEIN`;
      let appDownloadLink = Utility.getAppDownloadLink();
      let advanceOrderApplicable =
        this.$store.state.Location.storeDetails.startTime
          .advanceOrderApplicable;

      if (LocalStorageFunc.getDeliveryType() != "D") {
        return `Please try again after sometime`;
      }

      if (_.TAKEAWAY && _.DINEIN) {
        if (advanceOrderApplicable)
          return `To place an advance order <a href='${appDownloadLink}'>Download App</a> or choose <span><a href='${takeawayHref}'>Takeaway</a></span> or <span><a href='${dineHref}'>Dine-in</a></span>`;
        else
          return `Please try again after sometime or choose <span><a href='${takeawayHref}'>Takeaway</a></span> or <span><a href='${dineHref}'>Dine-in</a></span>`;
      } else if (_.TAKEAWAY) {
        if (advanceOrderApplicable)
          return `To place an advance order <a href='${appDownloadLink}'>Download App</a> or choose <span><a href='${takeawayHref}'>Takeaway</a></span>`;
        else
          return `Please try again after sometime or choose <span><a href='${takeawayHref}'>Takeaway</a></span>`;
      } else if (_.DINEIN) {
        if (advanceOrderApplicable)
          return `To place an advance order <a href='${appDownloadLink}'>Download App</a> or choose <span><a href='${dineHref}'>Dine-in</a></span>`;
        else
          return `Please try again after sometime or choose <span><a href='${dineHref}'>Dine-in</a></span>`;
      } else {
        if (advanceOrderApplicable)
          return `To place an advance order <a href='${appDownloadLink}'>Download App</a>`;
        else return `Please try again after sometime`;
      }
    },
    isTrueLocationDetected() {
      return this.$store.state.Location.isTrueLocationDetected;
    },
    storeOfflineTitle() {
      let advanceOrderApplicable =
        this.$store.state.Location.storeDetails.startTime
          .advanceOrderApplicable;

      if (advanceOrderApplicable) {
        return `Store opens at ${
          (this.$store.state.Location.storeDetails.startTime.hour || "00") +
          ":" +
          (this.$store.state.Location.storeDetails.startTime.minute || "00")
        } ${this.$store.state.Location.storeDetails.startTime.amOrPm}`;
      } else {
        return "Sorry, Store is currently Offline";
      }
    },
  },
};
</script>

<style scoped></style>
