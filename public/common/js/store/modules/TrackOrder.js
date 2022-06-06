import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";
import utility from "../../helpers/utility";

export default {
  namespaced: true,
  state: {
    iconStatus: 1, // confirm = 1, prepared = 2, orderWay = 4, delivered = 9, cancelled = 5
    orderStatus: "",
    orderTime: null,
    orderAmount: "",
    isModalOpen: false,
    orderItemCount: "",
    estimateTime: "",
    trackOnMapKey: "",
    deliverType: "",
    sendTrackStatus:{
      orderId: "",
      storeId: "",
      orderStatus: ""
    }
  },
  getters: {},
  mutations: {
    udpateOrderStatus(state, payload) {
      const { tracker, orderSummary } = payload;
      if (tracker || orderSummary) {
        state.orderStatus = tracker?.msg || "";
        state.iconStatus = tracker?.stage || 1;
        state.orderTime = tracker?.orderTime || null;
        state.orderAmount = orderSummary?.netPrice || "";
        state.orderItemCount = orderSummary?.items?.length || 0;
        state.trackOnMapKey = tracker.trackOnMap;
        state.sendTrackStatus["orderId"] = orderSummary?.orderId;
        state.sendTrackStatus["orderStatus"] = orderSummary?.orderStatus;
        state.sendTrackStatus["storeId"] = orderSummary?.store?.id;
        state.deliverType = orderSummary?.deliveryType;
      } else {
        state.orderStatus = "";
        state.iconStatus = 1;
      }
    },
    updateTrackOrderModal(state, payload) {
      state.isModalOpen = payload;
    },
    updateEstimateTime(state, payload){
      if(payload){
        let { eta } = payload;
        if(eta){
          let currentTime = Number(new Date().getMinutes())
          let estTime = Number(new Date(eta).getMinutes())
          state.estimateTime = estTime - currentTime
        } else {
          state.estimateTime = ""
        }
      }
    }
  },
  actions: {
    getOrderStatusOfUser({ commit, rootGetters }, payload) {
      ApiCalls
        .TRACK_ORDER_API({
          headers: Headers(),
          payload: {userId:payload.userId, mobile: payload.mobile, orderId: payload.orderId},
        })
        .then((res) => {
          commit("udpateOrderStatus", res?.data || {});
          if(res?.data?.tracker) {
            if(utility?.getSessionStorageValue("isTrackerStatusOpen")) {
              commit("updateTrackOrderModal", true)
            }
          }
        })
        .catch((error) => commit("updateTrackOrderModal", false));
    },
    getEstimentTimeOfOrder({commit}, payload){
      ApiCalls.ESTIMENT_ORDER_TIME_API({
        headers: Headers()
      })
      .then(res => commit("updateEstimateTime", res?.data))
    },
    getSendTrackerStatus({commit}, payload){
      ApiCalls.SEND_TRACKER_STATUS_API({
        headers: Headers(),
        payload: payload
      })
      .then(res => console.log("---- send tracker status", res))
    }
  },
};
