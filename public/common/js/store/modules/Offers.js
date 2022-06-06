import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";
import LocalStorageFunc from "../../helpers/localStorageFunc";
import { keyMapsAction } from "../../helpers/storeKeys";

export default {
  namespaced: true,
  state: {
    welcomeOffers: [],
    selectedOfferIndex: -1,
    selectedOfferTnc: [],
    offerApplyResponse: {},
    paymentOffers: ["80 Cashback", "70 Cashback", "20 Cashback"],
    isOpenOfferAppliedModal: false,
    showTncModal: false,
    offerFailedMsg: ""
  },
  getters: {},
  mutations: {
    updateWelcomeOffer(state, payload) {
      state.welcomeOffers = payload;
    },
    updateOfferApplyResponse(state, payload) {
      state.offerApplyResponse = payload;
    },
    updateSelectedOfferIndex(state, payload) {
      state.selectedOfferIndex = payload;
    },
    updateSelectedOfferTnc(state, payload) {
      state.selectedOfferTnc = payload;
    },
    updateOfferApplyModalKey(state, payload) {
      state.isOpenOfferAppliedModal = payload;
    },
    updateShowTncModal(state, payload) {
      state.showTncModal = payload;
    },
    updateOfferFailedMsg(state, payload) {
      state.offerFailedMsg = payload;
    },
  },
  actions: {
    welcomeOfferApi({ commit, state }, payload) {
      ApiCalls.STORE_OFFERS_API({
        headers: Headers(),
        payload: LocalStorageFunc.storeId(),
      })
        .then(function (response) {
          // response.data = StoreOfferApiResponse;
          if (response?.data?.applicable?.data?.length > 0) {
            if (response?.data?.milestoneOffers?.data?.length > 0) {
              response.data.applicable.data.splice(
                1,
                0,
                response.data.milestoneOffers.data.map((_) => {
                  return { ..._, type: "MILESTONE" };
                })[0]
              );
            }
            commit("updateWelcomeOffer", response.data.applicable.data);
            if (
              response.data.applicable.data.length > 0 &&
              response.data.applicable.data?.length !== undefined
            ) {
              if (LocalStorageFunc.getCouponCode()?.code !== undefined) {
                let currentCoupneCodeIndex =
                  response.data.applicable.data.findIndex(
                    (i) => i.promoCode == LocalStorageFunc.getCouponCode().code
                  );
                commit("updateSelectedOfferIndex", currentCoupneCodeIndex);
              }
              commit("updateOfferApplyModalKey", true);
              commit("updateOfferApplyResponse", { status: "SUCCESS" });
              LocalStorageFunc.setApplyOfferSuccess("SUCCESS");
            }
          } else if (response?.data?.milestoneOffers?.data) {
            commit(
              "updateWelcomeOffer",
              ...response.data.milestoneOffers.data.map((_) => {
                return { ..._, type: "MILESTONE" };
              })
            );
          }
        })
        .catch(function (error) {
          commit("updateWelcomeOffer", {});
          commit("updateOfferApplyResponse", { status: "" });
          LocalStorageFunc.setApplyOfferSuccess("");
        });
    },
    applyOfferApi({ commit, dispatch, rootState }, payload) {
      commit("updateOfferApplyResponse", {});
      ApiCalls.APPLY_WELCOME_OFFER_API({
        headers: Headers(),
        payload: {
          promoCode: payload.promoCode,
          cartId: LocalStorageFunc.cartId(),
          isBinOffer:
            payload.paymentOptions?.filter((_) => _.paymentId == "CC")?.length >
            0,
        },
      })
        .then(function (response) {
          commit("updateOfferApplyResponse", response.data);
          if (response.data.status == "SUCCESS") {
            commit("updateOfferFailedMsg", "")
            if (rootState.Config.copyOfferCodeOnApply) {
              navigator?.clipboard?.writeText(payload.promoCode);
            }
            payload.showOfferAppliedModal();
            LocalStorageFunc.updateCouponCode(payload.promoCode);
            if (payload.paymentOptions) {
              let _ = payload.paymentOptions.filter((_) => _.paymentId == "CC");
              if (_.length > 0) {
                let appliedOffer = {
                  status: "",
                  label: _[0].label,
                  name: _[0].paymentId,
                  index: 1,
                  paymentId: _[0].paymentId,
                  paymentMode: _[0].paymentId,
                };
                LocalStorageFunc.updatePaymentOfferpaymentId(appliedOffer);
              }
            }
            dispatch(keyMapsAction.OFFER_TNC_API, payload, { root: true });
          } else {
            response.data.errors[0].displayMsg && commit("updateOfferFailedMsg", response.data.errors[0]?.displayMsg || "")
          }
        })
        .catch(function (error) {
          commit("updateOfferApplyResponse", {});
          /* payload.showOfferAppliedModal(); */
          commit("updateOfferFailedMsg", "")
        });
    },
    offerTncApi({ commit }, payload) {
      ApiCalls.OFFER_TNC_API({
        headers: Headers(),
        payload: {
          promoCode: payload.promoCode,
        },
      })
        .then(function (response) {
          if (response?.data?.tnc) {
            commit("updateSelectedOfferTnc", response.data.tnc);
          } else {
            commit("updateSelectedOfferTnc", []);
          }
        })
        .catch(function (error) {
          commit("updateSelectedOfferTnc", []);
        });
    },
  },
};
