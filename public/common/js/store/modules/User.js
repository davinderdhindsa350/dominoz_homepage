import WalletApiResponse from "../../assets/FallbackData/WalletApiResponse.json";
import LoyaltyApiResponse from "../../assets/FallbackData/LoyaltyApiResponse.json";
import SafeVal from "../../helpers/safeVal";
import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";
import LocalStorageFunc from "../../helpers/localStorageFunc";
import { staticImgBaseUrl } from "../../config/apiEndpoints";
import Utility from "../../helpers/utility";

export default {
  namespaced: true,
  state: {
    loyalty: {},
    walletBalance: {},
    currentOrderType: "",
  },
  getters: {
    getUserData: (state) => {
      if (Object.keys(state.loyalty).length === 0) {
        return {
          rewardType: "",
          isEnrolled: "",
          firstName: localStorage.userInfo
            ? Utility.tryJsonParse(localStorage.userInfo)?.userData?.firstName
            : "",
          lastName: localStorage.userInfo
            ? Utility.tryJsonParse(localStorage.userInfo)?.userData?.lastName
            : "",
          rewardData: {},
        };
      }
      let isEnrolled =
        state.loyalty.enrollmentStatus == "ENROLLED" ? true : false;
      let rewardType =
        state.loyalty.loyaltyCardCode == "PAYMENT"
          ? "PIZZAPAL"
          : state.loyalty.loyaltyCardCode == "POTP1" ||
            state.loyalty.loyaltyCardCode == "POTP2"
          ? "CHEESY"
          : "UNKNOWN";
      return {
        rewardType: rewardType,
        isEnrolled: isEnrolled,
        firstName: localStorage.userInfo
          ? Utility.tryJsonParse(localStorage.userInfo)?.userData?.firstName
          : "",
        lastName: localStorage.userInfo
          ? Utility.tryJsonParse(localStorage.userInfo)?.userData?.lastName
          : "",
        rewardData: {
          points:
            isEnrolled &&
            SafeVal(() => {
              if (rewardType == "PIZZAPAL") {
                let item = state.walletBalance.wallet.data.filter(
                  (_) => _.type == "LOYALTY"
                );
                return item[0].amount;
              } else if (rewardType == "CHEESY") {
                return state.walletBalance.loyaltyProgram.balance.burnable
                  .points;
              } else {
                return 0;
              }
            }, 0),
          maxPoints: SafeVal(() => {
            if (rewardType == "PIZZAPAL") {
              return 0;
            } else if (rewardType == "CHEESY") {
              return state.walletBalance.loyaltyProgram.programConfig
                .itemRedeemPoints;
            } else {
              return 0;
            }
          }, 0),
          elizibleForPizza:
            isEnrolled &&
            SafeVal(() => {
              if (rewardType == "PIZZAPAL") {
                return false;
              } else if (rewardType == "CHEESY") {
                return state.walletBalance.loyaltyProgram.balance.burnable
                  .freeItems > 0
                  ? true
                  : false;
              } else {
                return false;
              }
            }, false),
        },
      };
    },
    getRewardIconPath: (state, getters) => {
      if (
        Object.keys(state.loyalty || {}).length === 0 ||
        Object.keys(state.walletBalance || {}).length === 0
      )
        return "";
      let userData = getters.getUserData;
      if (userData.rewardType == "CHEESY") {
        if (userData.rewardData.points > 0) {
          if (userData.rewardData.points >= userData.rewardData.maxPoints) {
            return `${staticImgBaseUrl}/images/global/rewards/header-cheesy-reward.png`;
          } else {
            return `${staticImgBaseUrl}/images/global/rewards/header-cheesy-reward-${Math.floor(
              userData.rewardData.points / (userData.rewardData.maxPoints / 6)
            )}.svg`;
          }
        } else {
          return `${staticImgBaseUrl}/images/global/rewards/header-cheesy-reward-0.svg`;
        }
      } else {
        return `${staticImgBaseUrl}/images/global/rewards/pizza-pals.svg`;
      }
    },
    getCheesyRewardIconPath: (state, getters) => {
      if (
        Object.keys(state.loyalty).length === 0 ||
        Object.keys(state.walletBalance).length === 0
      )
        return "";
      let userData = getters.getUserData;
      if (!userData.isEnrolled) {
        return `${staticImgBaseUrl}/images/global/rewards/cheesy-reward-un-enrolled.png`;
      }
      if (userData.rewardData.points >= userData.rewardData.maxPoints) {
        return `${staticImgBaseUrl}/images/global/pizza.png`;
      } else {
        return `${staticImgBaseUrl}/images/global/rewards/cheesy-widget-${Math.floor(
          userData.rewardData.points / (userData.rewardData.maxPoints / 6)
        )}.svg`;
      }
    },
    getRewardIconText: (state, getters) => {
      if (
        Object.keys(state.loyalty).length === 0 ||
        Object.keys(state.walletBalance).length === 0
      )
        return "";
      let userData = getters.getUserData;
      if (userData.rewardType == "CHEESY") {
        if (userData.rewardData.points > 0) {
          if (userData.rewardData.points >= userData.rewardData.maxPoints) {
            return "Redeem \nPizza";
          } else {
            return `<span style='color:black'>${userData.rewardData.points}</span>/${userData.rewardData.maxPoints}`;
          }
        } else {
          return "CHEESY \nREWARDS";
        }
      } else {
        if (userData.rewardData.points > 0) {
          return `<span style='color:black'>${userData.rewardData.points}</span>\nPoints`;
        } else {
          return "PIZZA \nPALS";
        }
      }
    },
  },
  mutations: {
    updateLoyalty(state, payload) {
      state.loyalty = payload;
      LocalStorageFunc.updateLoyaltyInfo = payload;
    },
    updateWalletBalance(state, payload) {
      state.walletBalance = payload;
    },
    loadCurrentOrderType(state, payload) {
      let currentOrderType = LocalStorageFunc.getDeliveryType();
      if (currentOrderType) {
        state.currentOrderType = currentOrderType == "T" ? "P" : currentOrderType;
      }
    },
  },
  actions: {
    loyaltyApi({ commit, rootGetters }, payload) {
      ApiCalls.USER_LOYALTY_API({
        headers: Headers(),
        payload: {},
      })
        .then(function (response) {
          if (response?.data) {
            commit("updateLoyalty", response.data);
          }
        })
        .catch(function (error) {
          commit("updateLoyalty", {});
        });
    },
    walletBalanceApi({ commit, rootGetters }, payload) {
      ApiCalls.WALLET_BALANCE_API({
        headers: Headers(),
        payload: {},
      })
        .then(function (response) {
          commit("updateWalletBalance", response.data);
        })
        .catch(function (error) {
          /* commit("updateWalletBalance", WalletApiResponse["PIZZAPAL"]); */
        });
    },
  },
};
