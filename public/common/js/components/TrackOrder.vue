<template>
  <div class="col-sm-12 col-md-12 col-lg-12" v-if="modalClose">
    <div class="main-confirm-order mb16">
      <div class="confirm-order">
        <div class="label-icon-parent flex-row justify-content-between">
          <div
            class="label-icon d-flex justify-content-center"
            :class="[
              iconData === 1 || 2 ? 'align-items-center' : 'align-items-start',
            ]"
          >
            <span :class="['star-icon flex-row']">
              <i
                v-if="Number(iconData) === 9 || Number(iconData) === 5"
                :class="['mrl8 fs-md', iconShow(iconData)]"
              ></i>
              <track-order-icons
                v-if="Number(iconData) !== 9 && Number(iconData) !== 5"
                :iconJson="iconShow(iconData)"
              ></track-order-icons>
            </span>
            <div class="parent-base">
              <div class="label-track col-sm-12 pl0">
                <div class="bold fs-14 label-track-child">
                  {{ showItemStatus(iconData) || "Track Your Order" }}
                </div>
                <div class="mt2 price-label">
                  <!-- <small class="fs-11">{{ timeOrder(timeOfOrder) }}</small> -->
                  <small class="fs-11" v-if="totalOrderCount">
                    {{ totalOrderCount }} items</small
                  >
                  <small class="fs-11" v-if="netPrice">
                    | Paid <i class="rs">Rs.</i> {{ netPrice }}</small
                  >
                </div>
                <div v-if="(Number(iconData) === 4) && trackDeliveryType === 'D' " class="pt6">
                  <i class="jfi-clock fs-10"></i>
                  <span class="fs-11"> Reaching in {{ estimateTime || 5 }} minutes </span>
                </div>
              </div>
              <div class="need-help-text" v-if="Number(iconData) === 9">
                <span
                  class="underline-help bold fs-12"
                  @click="needHelpHandler()"
                  >Need Help ?</span
                >
                <span
                  v-if="trackDeliveryType === 'D'"
                  class="underline-help bold fs-12 pl16"
                  @click="notDelivered()"
                  >Not delivered?</span
                >
                <span
                  v-if="trackDeliveryType !== 'D'"
                  class="underline-help bold fs-12 pl16"
                  @click="notDelivered()"
                  >Not Recieved?</span
                >
              </div>
            </div>
          </div>
          <!-- <i
          class="jfi-cross cursor-pointer fs-12"
          v-if="Number(iconData) === 1"
          @click="isTrackOrderModalOpen(false)"
        ></i> -->
        </div>
      </div>
      <div class="pt15 pr15" v-if="Number(iconData) === 9">
        <button
          class="line-btn dark pTB4 fs-12 sm"
          @click="trackOrderHandler()"
        >
          Okay
        </button>
      </div>
      <div
        class="arrow-icon border-left d-flex justify-content-center align-items-center cursor-pointer"
        v-if="arrowNotShow(Number(iconData))"
        @click="checkTheTrackOrderStatus()"
      >
        <i class="jfi-arrow-line fs-14"></i>
      </div>
    </div>
  </div>
</template>

<script>
import LocalStorageFunc from "../helpers/localStorageFunc";
import { keyMapsAction, keyMapsMutation } from "../helpers/storeKeys";
import TrackOrderIcons from "./TrackOrderIcons.vue";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";
import { v2PwaBasePath } from '../config/apiEndpoints';

export default {
  components: { TrackOrderIcons },
  name: "TrackOrder",
  props: ["data"],
  data() {
    return {
      trackOrderMsg: {
        D: {
          // delivery
          1: "Your Order is confirmed!",
          2: "Your food is being baked!",
          4: "Your order has dispatched!",
          5: "Your order is canceled",
          9: " Your order is delivered!",
        },
        DINEIN: {
          // dine-in
          1: "Your Order is confirmed!",
          2: "Your food is being baked!",
          4: "Your order is ready!",
          5: "Your order is canceled",
          9: "Order picked up! Enjoy!",
        },
        P: {
          // takeaway
          1: "Your Order is confirmed!",
          2: "Your food is being baked!",
          4: "Your order is ready for drive-n-pick",
          5: "Your order is canceled",
          9: " Order picked up! Enjoy!",
        },
        CURB: {
          // dine-in-pickup
          1: "Your Order is confirmed!",
          2: "Your food is being baked!",
          4: "Your order is ready for drive-n-pick !",
          5: "Your order is canceled",
          9: " Order picked up! Enjoy!",
        },
      },
      orderStatusPayload: {
        store_id: "",
        order_id: "",
        order_status: "",
      },
      trackOrderStatuscount: 0,
      trackIntervalFun:() => {}
    };
  },
  mounted() {
    // if(this.previousReorderData.length > 0){
    console.log("previous order data on mounted in tracker module", this.previousReorderData);
    setTimeout(() => {
      LocalStorageFunc.isLoggedIn() && this.updateIconStatus();
    }, 2000);
    // }
    this.isTrackOrderModalOpen(false);
    this.trackOrderStatuscount = 0
  },
  computed: {
    iconData() {
      return this.$store.state.TrackOrder.iconStatus;
    },
    orderStatusData() {
      return this.$store.state.TrackOrder.orderStatus;
    },
    timeOfOrder() {
      return this.$store.state.TrackOrder.orderTime;
    },
    netPrice() {
      return this.$store.state.TrackOrder.orderAmount;
    },
    modalClose() {
      return this.$store.state.TrackOrder.isModalOpen;
    },
    totalOrderCount() {
      return this.$store.state.TrackOrder.orderItemCount;
    },
    trackOnMapKey() {
      return this.$store.state.TrackOrder.trackOnMapKey;
    },
    estimateTime() {
      return this.$store.state.TrackOrder.estimateTime;
    },
    sendTrackStatus() {
      return this.$store.state.TrackOrder.sendTrackStatus;
    },
    previousReorderData() {
      return this.$store.state.ReOrder?.previousOrderData || [];
    },
    orderTrackMobileNumber(){
      return LocalStorageFunc.getUserData()?.userData !== undefined && 
      Number(LocalStorageFunc.getUserData()?.userData?.mobile) || ""
    },
    trackDeliveryType(){
      return this.$store.state.TrackOrder.deliverType && this.$store.state.TrackOrder.deliverType || "D"
    }
  },
  methods: {
    addPrefixZero(n) {
      return n > 9 ? "" + n : "0" + n;
    },
    iconShow(icon) {
      // set the image along with status
      switch (Number(icon)) {
        case 1:
          return "track-order-confirm(D)"; // 1 sending json file to component
        case 2:
          return "track-order-being-baked(D)"; // 2 sending json file to component
        case 4:
          this.updateOrderTime()
          // later use when we have all the icons
          return this.trackDeliveryType === "D"
            ? "track-order-dispatch(D)"
            : "track-order-ready(D)"; // 4 for dispatch
        case 5:
          return "tag-track-order-canceled"
        case 9:
          return this.trackDeliveryType === "D"
            ? "tag-track-order-delivered"
            : "tag-track-order-pickedup";
        default:
          return "track-order-confirm(D)";
      }
    },
    statusMsg(index) {
      switch (Number(index)) {
          case 1:
            return this.trackOrderMsg[this.trackDeliveryType][1]; // 1 for confirmed
          case 2:
            return this.trackOrderMsg[this.trackDeliveryType][2]; // 2 for being baked
          case 4:
            return this.trackOrderMsg[this.trackDeliveryType][4]; // 4 for dispatch
          case 5:
            return this.trackOrderMsg[this.trackDeliveryType][5]; // 5 for canceled
          case 9:
            return this.trackOrderMsg[this.trackDeliveryType][9]; // 9 for delivered
          default:
            return this.trackOrderMsg[this.trackDeliveryType][1]; //  for default i have set as confirmed data;
        }
    },
    categoryType() {
      if (this.trackDeliveryType) {
        return { D: "delivery", P: "takeaway", DINEIN: "dine-in", CURB: "dine-in-pickup", IRCTC: "delivery to train" }[this.trackDeliveryType];
      }
    },
    showItemStatus(data) {
      // set the text status
      if (this.trackDeliveryType) {
        let msg = this.statusMsg(data);
        //Amplitude impression event load
        const category = this.categoryType();
        const preEventObj = { "positionWithinSection": data, "category": msg, "orderType": category };
        const eventObj = Utility.amplitudeEventObj("impression", "track order", "3", preEventObj);
        setEvents("amplitude", "Impression", eventObj);
        return msg;
      }
    },
    callGetOrderStatus(){
      console.log("previous order data in callGetOrderStatus", this.previousReorderData);
      if(this.previousReorderData[0]?.dominosOrderId !== undefined && this.previousReorderData[0].dominosOrderId){
        this.$store.dispatch(keyMapsAction.GET_ORDER_STATUS_OF_USER, {
        userId: Number(LocalStorageFunc.getUserData()?.userData?.userId),
        mobile: Number(LocalStorageFunc.getUserData()?.userData?.mobile),
        orderId: Number(this.previousReorderData[0].dominosOrderId) || ""
        });
      }
    },
    updateIconStatus() {
        if(this.trackOrderStatuscount === 0){
          this.callGetOrderStatus();
          console.log("first time call", this.trackOrderStatuscount);
        }
        this.trackIntervalFun = setInterval(() => {
          this.callGetOrderStatus();
          this.trackOrderStatuscount += 1
          this.trackOrderStatuscount > 9 && this.clearTimeTrackStatus()
          console.log("after first time call", this.trackOrderStatuscount);
        }, 60000);
    },
    clearTimeTrackStatus(){
      clearInterval(this.trackIntervalFun)
    },
    isTrackOrderModalOpen(open) {
      this.$store.commit(keyMapsMutation.UPDATE_TRACK_ORDER_MODAL, open);
    },
    eventsForPickedup(subCat) {
      if(this.trackDeliveryType && this.iconData) {
        //Amplitude Click event load
        let msg = "Order picked up! Enjoy!";
        const category = this.categoryType();
        const preEventObj = { "positionWithinSection": this.iconData, "category": msg, "subCategory": subCat, "orderType": category };
        const eventObj = Utility.amplitudeEventObj("click", "track order", "3", preEventObj);
        setEvents("amplitude", "Click", eventObj);
      }
    },
    trackOrderHandler() {
      let obj = { ...this.orderStatusPayload };
      if (this.sendTrackStatus) {
        obj["store_id"] = this.sendTrackStatus?.storeId,
        obj["order_id"] = this.sendTrackStatus?.orderId,
        obj["order_status"] = this.sendTrackStatus?.orderStatus && "delivered";
      }
      this.$store.dispatch(keyMapsAction.GET_SEND_TRACKER_STATUS, obj)
      LocalStorageFunc.setTrackerOfKeyInSession(false)
      this.isTrackOrderModalOpen(false)
      this.eventsForPickedup("Okay");
    },
    needHelpHandler() {
      if((this.trackDeliveryType == "D" || this.trackDeliveryType == "P" || this.trackDeliveryType == "DINEIN") && this.orderTrackMobileNumber){
        window.location.href = "tel:555-666-7777"
      } else {
        window.location.href = `${v2PwaBasePath}/chatBot`;
      }
      this.eventsForPickedup("Need Help");
    },
    notDelivered() {
      let data = { ...this.orderStatusPayload };
      if (this.sendTrackStatus) {
        data["store_id"] = this.sendTrackStatus?.storeId,
        data["order_id"] = this.sendTrackStatus?.orderId,
        data["order_status"] = this.sendTrackStatus?.orderStatus && "not_delivered";
      }
      this.$store.dispatch(keyMapsAction.GET_SEND_TRACKER_STATUS, data)
      if((this.trackDeliveryType == "D" || this.trackDeliveryType == "P" || this.trackDeliveryType == "DINEIN") && this.orderTrackMobileNumber){
        window.location.href = "tel:555-666-7777"
      } else {
        window.location.href = `${v2PwaBasePath}/chatBot`;
      }
      this.eventsForPickedup("Not delivered");
    },
    checkTheTrackOrderStatus() {
      window.location.href = `${v2PwaBasePath}/thankYouAndTracking`;
      if(this.trackDeliveryType && this.iconData) {
        //Amplitude Click event load
        let msg = this.statusMsg(this.iconData);
        const category = this.categoryType();
        const preEventObj = { "positionWithinSection": this.iconData, "category": msg, "orderType": category };
        const eventObj = Utility.amplitudeEventObj("click", "track order", "3", preEventObj);
        setEvents("amplitude", "Click", eventObj);
      }
    },
    updateOrderTime(){
      if (this.trackOnMapKey && this.iconData == 4) {
        this.$store.dispatch(keyMapsAction.GET_ESTIMET_TIME_ORDER);
      }
    },
    arrowNotShow(data){
      switch (data) {
        case 9:
          return false
          break;
        case 5:
          return false
          break;
        default:
          return true
          break;
      }
    }
  },
};
</script>
