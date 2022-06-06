import Headers from "../../config/headers";
import LocalStorageFunc from "../../helpers/localStorageFunc";
import ApiCalls from "../../config/apiCalls";
import Utility from "../../helpers/utility";

export default {
  namespaced: true,
  state: {
    isLoggedIn: false,
    loginMessageDynamicText: [
      "Exclusive Offers",
      "Saved addresses",
      "Free Pizza rewards",
      "Best experience",
    ],
    isSessionPresent: LocalStorageFunc.cartId() ? true : false,
  },
  getters: {},
  mutations: {
    updateLoginMessageDynamicText(state, payload) {
      if (payload) {
        state.loginMessageDynamicText = payload;
      }
    },
    updateLogginState(state, payload) {
      LocalStorageFunc.updateIsLoggedIn(payload);
      state.isLoggedIn = payload.isLoggedIn;
    },
    createLocalStorageUserInfo(state, payload) {
      let value = {
        headers: {
          isLoggedIn: false,
          deliveryType: "D",
          ...payload,
        },
      };
      localStorage.userInfo = JSON.stringify(value);
      state.isLoggedIn = false;
    },
    updateLocalStorageUserInfoCredential(state, payload) {
      let value = localStorage.userInfo
        ? Utility.tryJsonParse(localStorage.userInfo)
        : "";
      try {
        value.headers.credentials = {
          accessKeyId: payload.accessToken,
          refreshToken: payload.refreshToken,
        };
        localStorage.userInfo = JSON.stringify(value);
      } catch (e) {
        state.isLoggedIn = false;
      }
    },
    updateIsSessionPresent(state, payload) {
      state.isSessionPresent = payload;
    },
  },
  actions: {
    anonymousUserApi({ commit }, payload) {
      ApiCalls.ANONYNMOUS_USER_API({
        headers: Headers(),
        payload: {},
      })
        .then(function (response) {
          if (response?.data?.cartId) {
            commit("createLocalStorageUserInfo", response.data);
            LocalStorageFunc.unsetKey("userAddresses");
            if (typeof payload.startupAppiCalls === "function") {
              payload.startupAppiCalls();
            } else {
              location.reload();
            }
          }
          commit("updateIsSessionPresent", true);
        })
        .catch(function (error) {
          commit("updateIsSessionPresent", true);
        });
    },
    loadLoginStatus({ commit, dispatch }, payload) {
      commit("updateLogginState", {
        isLoggedIn: LocalStorageFunc.isLoggedIn() || false,
      });
    },
  },
};
