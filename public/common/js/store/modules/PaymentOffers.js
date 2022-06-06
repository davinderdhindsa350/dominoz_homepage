import LocalStorageFunc from "../../helpers/localStorageFunc";
import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";

export default {
  namespaced: true,
  state: {
    paymentOffers: [],
    selectedOfferIndex: -1,
    appliedOfferDetails: {},
    selectedPaymentOfferTnc: {},
    tncModalFlag: false
  },
  getters: {},
  mutations: {
    updateOfferItems(state, payload) {
      state.paymentOffers = payload;
    },
    updateSelectedOfferIndex(state, payload) {
      state.selectedOfferIndex = payload;
    },
    updatePmOfferDetails(state, payload) {
      state.appliedOfferDetails = payload;
      LocalStorageFunc.updatePaymentOfferpaymentId(state.appliedOfferDetails);
    },
    updateSelectedPaymentOfferTnc(state, payload) {
      state.selectedPaymentOfferTnc = payload;
    },
    updateTncModalFlag(state, payload) {
      state.tncModalFlag = payload;
    },
  },
  actions: {
    paymentOfferTncApi({ commit }, payload) {
      ApiCalls.PAYMENT_OFFER_TNC_API({
        headers: Headers(),
        payload: {
          offerId: payload.offerId,
          langId: payload.langId,
          platform: payload.platform
        },
      })
        .then(function (response) {
          if (response?.data?.tnc) {
            commit("updateSelectedPaymentOfferTnc", response.data);
          } else {
            commit("updateSelectedPaymentOfferTnc", []);
          }
        })
        .catch(function (error) {
          commit("updateSelectedPaymentOfferTnc", []);
        });
    },
  },
};
