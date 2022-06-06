<template>
  <div
    class="col-sm-12 order-type-wrap line-btn sm mb16"
    v-if="orderTypes.length > 0"
  >
    <div
      class="order-type-btn btn-1 color-grey"
      v-for="(item, i) in orderTypes"
      @click="handleOrderTypeClick($event, item, i)"
      :class="[currentOrderType == item.orderType ? 'active' : '', item.orderType === 'IRCTC' && 'hide' ]"
      :key="i"
    >
      <div class="fs-12 lh16">{{ item.title }}</div>
      <div class="fs-10 lh14 normal">{{ getSubTitle(item) }}</div>
    </div>
  </div>
</template>

<script>
import { imgBaseUrl, v2PwaBasePath } from "../config/apiEndpoints";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";
import localStorageFunc from "../helpers/localStorageFunc";

export default {
  name: "OrderTypeTabs",
  components: {},
  props: ["dataResults", "moduleProps"],
  data() {
    return {
      imgBaseUrl: imgBaseUrl,
      orderTypes: [],
      timeServiceGuarantee: "",
    };
  },
  mounted() {
    this.orderTypes = JSON.parse(this.dataResults);
    //Amplitude impression event load
    const eventObj = Utility.amplitudeEventObj(
      "impression",
      "Order type section",
      "3",
      {}
    );
    setEvents("amplitude", "impression", eventObj);
  },
  methods: {
    handleOrderTypeClick(event, item, index) {
      //Amplitude click event load
      const category = {
        D: "delivery",
        P: "takeaway",
        DINEIN: "dine-in",
        IRCTC: "delivery to train",
      }[item.orderType];
      const preEventObj = { positionWithinSection: index + 1, category };
      const eventObj = Utility.amplitudeEventObj(
        "click",
        "Order type section",
        "3",
        preEventObj
      );
      setEvents("amplitude", "Click", eventObj);

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
      }[item.orderType];
    },
    getSubTitle(item) {
      if (item.orderType == "D" && this.currentOrderType == "D") {
        return (this.isTrueLocationDetected && this.getStoreDetails?.timeServiceGuarantee?.type !== undefined && this.getStoreDetails?.timeServiceGuarantee?.type !== "")
          ? `${this.getStoreDetails?.timeServiceGuarantee?.type} MIN`
          : "___";
      } else if (
        item.orderType == "DINEIN" &&
        this.currentOrderType == "DINEIN"
      ) {
        return (
          this.isTrueLocationDetected ? this.setOrderTypeDetails() || "___" :
          item.defaultSubtitle
        );
      } else if (item.orderType == "P" && this.currentOrderType == "P") {
        return (
          this.isTrueLocationDetected ? this.setOrderTypeDetails() || "___" :
          item.defaultSubtitle
        );
      } else {
        if(item.orderType == "D"){
          return this.currentOrderType == "D" ? "___" : item.defaultSubtitle;
        } else {
          return item.defaultSubtitle;
        }
      }
    },
    setOrderTypeDetails() {
      let msg = "";
      let cond_1 = (this.getOrderDetailsFromLoc?.driveDistanceInKm !== undefined &&
          this.getMaxDistanceShown !== undefined &&
          this.getOrderDetailsFromLoc?.driveDistanceInKm &&
          this.getMaxDistanceShown) ? true : false;
      let cond_2 = (this.getOrderDetailsFromLoc?.aerialDistanceInKm !== undefined &&
          this.getMaxDistanceShown !== undefined &&
          this.getOrderDetailsFromLoc?.aerialDistanceInKm &&
          this.getMaxDistanceShown) ? true : false;
      if (
        this.getStoreDetails?.latitude !== undefined &&
        this.getStoreDetails?.longitude !== undefined
      ) {
        if (
          cond_1 &&
          Math.abs(this.getOrderDetailsFromLoc?.driveDistanceInKm) > 0 &&
          Math.round(this.getMaxDistanceShown) >
            Math.abs(this.getOrderDetailsFromLoc?.driveDistanceInKm)
        ) {
          msg = `${Math.abs(
            this.getOrderDetailsFromLoc?.driveDistanceInKm
          )} KM`;
        } else if (
          cond_1 &&
          Math.abs(this.getOrderDetailsFromLoc?.driveDistanceInKm) > 0 &&
          Math.round(this.getMaxDistanceShown) <
            Math.abs(this.getOrderDetailsFromLoc?.driveDistanceInKm)
        ) {
          msg = `${Math.round(
            this.getMaxDistanceShown
          )} + KM`;
        } else if (
          cond_1 &&
          Math.abs(this.getOrderDetailsFromLoc?.driveDistanceInKm) > 0 && Math.abs(this.getOrderDetailsFromLoc?.driveDistanceInKm) < 1 &&
          this.getOrderDetailsFromLoc?.driveDistanceInKm !== ""
        ) {
          msg = `~ 100 meter`;
        } else {
          if (
            cond_2 &&
            Math.abs(this.getOrderDetailsFromLoc?.aerialDistanceInKm) > 0 &&
            Math.round(this.getMaxDistanceShown) >
              Math.abs(this.getOrderDetailsFromLoc?.aerialDistanceInKm)
          ) {
            msg = `${Math.abs(
              this.getOrderDetailsFromLoc?.aerialDistanceInKm
            )} KM`;
          } else if (
            cond_2 &&
            Math.abs(this.getOrderDetailsFromLoc?.aerialDistanceInKm) > 0 &&
            Math.round(this.getMaxDistanceShown) <
              Math.abs(this.getOrderDetailsFromLoc?.aerialDistanceInKm)
          ) {
            msg = `${Math.round(
              this.getMaxDistanceShown
            )} + KM`;
          } else if (
            cond_2 &&
            Math.abs(this.getOrderDetailsFromLoc?.aerialDistanceInKm) > 0 && Math.abs(this.getOrderDetailsFromLoc?.aerialDistanceInKm) < 1 &&
            this.getOrderDetailsFromLoc?.aerialDistanceInKm !== ""
          ) {
            msg = `~ 100 meter`;
          }
        }
      }
      return msg;
    },
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
    currentOrderType() {
      return this.$store.state.User.currentOrderType;
    },
    isTrueLocationDetected() {
      return this.$store.state.Location.isTrueLocationDetected;
    },
    getStoreDetails() {
      return (
        this.$store.state.Location?.storeDetails !== undefined &&
        this.$store.state.Location.storeDetails
      );
    },
    getOrderDetailsFromLoc() {
      return (
        localStorageFunc.getDeliveryTypeStationData() ||
        {}
      );
    },
    getMaxDistanceShown(){
      return window?._siteProps_?.props?.maxShownDistance || 0;
    }
  },
};
</script>
