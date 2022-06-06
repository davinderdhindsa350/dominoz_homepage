import Location from "../config/location";
import Utility from "./utility";

const getLastLocationNewOrLastLocationNewForIp = () => {
  let _ = Utility.tryJsonParse(localStorage.lastLocationNew);
  if (Object.keys(_).length > 0) return _;
  _ = Utility.tryJsonParse(localStorage.lastLocationNewForIp);
  if (Object.keys(_).length > 0) return _;
  return [];
};

const isMiniAppPaytmGpay = () => {
  let _ = localStorage.isMiniapp ? localStorage.isMiniapp : false;

  if (_ == "PAYTM" || _ == "GPAY") {
    return _;
  }

  let queryParams = Utility.getQueryFromUrl();
  if (
    queryParams["utm_source"] == "PAYTM" ||
    queryParams["utm_source"] == "GPAY"
  ) {
    localStorage.isMiniapp = queryParams["utm_source"];
    return queryParams["utm_source"];
  }
  return false;
};

const getIsLocationPermisssionGiven = () => {
  return localStorage.isLocationPermissionGiven
    ? Utility.tryJsonParse(localStorage.isLocationPermissionGiven)
    : false;
};

const setIsLocationPermissionGiven = () => {
  localStorage.isLocationPermissionGiven = JSON.stringify(true);
};

const setAskLocationLaterInSession = () => {
  sessionStorage.askLocationLater = JSON.stringify(true);
};

const setTrackerOfKeyInSession = (bol) => {
  sessionStorage.isTrackerStatusOpen = JSON.stringify(bol);
};

const getAskLocationLaterFromSession = () => {
  return sessionStorage.askLocationLater
    ? Utility.tryJsonParse(sessionStorage.askLocationLater)
    : false;
};

const setHideLocationPopupOnceInSession = () => {
  sessionStorage.hideLocationPopupOnce = JSON.stringify(true);
};

const getHideLocationPopupOnceFromSession = () => {
  return sessionStorage.hideLocationPopupOnce
    ? Utility.tryJsonParse(sessionStorage.hideLocationPopupOnce)
    : false;
};

const conditionalPurgeStorage = () => {
  let liveStorageNumber = +window._siteProps_?.props?.storageNumber;
  let localStorageNumber = localStorage.storageNumber;
  if (liveStorageNumber && liveStorageNumber > localStorageNumber) {
    localStorage.clear();
    sessionStorage.clear();
    localStorage.storageNumber = liveStorageNumber;
  } else if (liveStorageNumber) {
    localStorage.storageNumber = liveStorageNumber;
  }
};

const cartId = () => {
  return localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.cartId
    : "";
};

const setNoDeliveryZoneInSession = (value) => {
  sessionStorage.noDeliveryLocation = JSON.stringify(value);
  return;
};

const getReturnedFromMapScreen = () => {
  return localStorage.returnedFromMapScreen
    ? Utility.tryJsonParse(localStorage.returnedFromMapScreen)
    : false;
};

const setReturnedFromMapScreen = () => {
  localStorage.returnedFromMapScreen = JSON.stringify(true);
};

const getLocationSource = () => {
  return getLastLocationNewOrLastLocationNewForIp()[0]?.source || "";
};

const isLocationSelectedFromPwaV2 = () => {
  return (
    sessionStorage.lastLocationNewSource &&
    sessionStorage.lastLocationNewSource == "pwa-v2"
  );
};

const setLocationSelectedFromPwaV2 = () => {
  sessionStorage.lastLocationNewSource = "pwa-v2";
};

const unsetSessionStorageKey = (key) => {
  delete sessionStorage.removeItem(key);
};

const setLocationSource = (value) => {
  localStorage.locationSource = value;
};

const getUserData = () => {
  return localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)
    : {};
};

const getCoordsFromRecentLocation = () => {
  let value = getLastLocationNewOrLastLocationNewForIp()[0];
  if (value?.location?.latitude) {
    value = {
      coords: {
        latitude: value.location.latitude,
        longitude: value.location.longitude,
      },
    };
  } else if (value?.recent?.addressComponent?.latitude) {
    value = {
      coords: {
        latitude: value.recent.addressComponent.latitude,
        longitude: value.recent.addressComponent.longitude,
      },
    };
  } else if (value?.recent?.latitude) {
    value = {
      coords: {
        latitude: value.recent.latitude,
        longitude: value.recent.longitude,
      },
    };
  }
  return value;
};

const getCoordsForTakeaway = () => {
  let _ = sessionStorage.takeawayLocation
    ? Utility.tryJsonParse(sessionStorage.takeawayLocation)
    : "";
  if (_?.latitude) {
    return {
      coords: {
        latitude: _.latitude,
        longitude: _.longitude,
      },
    };
  } else {
    return "";
  }
};

const storeId = () => {
  if (getDeliveryType() != "D") {
    return sessionStorage.takeawayLocation?.storeId || Location.defaultStoreId;
  }
  let value =
    getLastLocationNewOrLastLocationNewForIp()[0]?.recent
      ?.advanceOrderStoreId || Location.defaultStoreId;
  if (!value) {
    value = Location.defaultStoreId;
  }
  return value;
};

const isLoggedIn = () => {
  return localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.isLoggedIn
    : "";
};

const couponCode = () => {
  return localStorage.couponCode
    ? Utility.tryJsonParse(localStorage.couponCode)?.code
    : "";
};

const updateCouponCode = (payload) => {
  try {
    if (payload) {
      localStorage.couponCode = JSON.stringify({ code: payload });
    } else {
      delete localStorage.couponCode;
    }
  } catch (e) {}
};

const updateStoreId = (payload) => {
  let value = getLastLocationNewOrLastLocationNewForIp();
  try {
    if (value[0]?.recent?.advanceOrderStoreId) {
      localStorage.storeId = value[0].recent.advanceOrderStoreId;
      value[0].recent.advanceOrderStoreId = payload.toString();
      value[0].recent.id = payload.toString();
      localStorage.storeId = value[0].recent.advanceOrderStoreId;
    }
    if (getLocationSource() == "ip") {
      localStorage.lastLocationNewForIp = JSON.stringify(value);
    } else if (localStorage.lastLocationNew) {
      localStorage.lastLocationNew = JSON.stringify(value);
    }
  } catch (e) {}
};

const updateLastLocation = (payload) => {
  let _ = payload.source == "ip" ? "lastLocationNewForIp" : "lastLocationNew";
  let value = Utility.tryJsonParse(localStorage[_]);
  try {
    if (value[0]) {
      value[0] = payload;
    } else {
      value = [payload];
    }
    localStorage[_] = JSON.stringify(value);
    if (_ == "lastLocationNewForIp") {
      localStorage.removeItem("lastLocationNew");
    }
    if (payload?.recent?.advanceOrderStoreId) {
      localStorage.storeId = payload.recent.advanceOrderStoreId;
    }
  } catch (e) {}
};

const updateLoyaltyInfo = (payload) => {
  try {
    localStorage.loyaltyInfo = JSON.stringify(payload);
  } catch (e) {}
};

const updateIsLoggedIn = (payload) => {
  let value = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)
    : "";
  try {
    if (value?.headers?.isLoggedIn) {
      value.headers.isLoggedIn = payload.isLoggedIn;
    }
    localStorage.userInfo = JSON.stringify(value);
  } catch (e) {}
};

const getDeliveryType = () => {
  let value = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)
    : "";
  return value?.headers?.deliveryType || "D";
};

const updateCart = (payload) => {
  try {
    localStorage.cartItems = JSON.stringify(payload);
  } catch (e) {}
};

const cart = () => {
  return localStorage.cartItems
    ? Utility.tryJsonParse(localStorage.cartItems)
    : "";
};

const unsetKey = (key) => {
  localStorage.removeItem(key);
};

const setCustomizedOptionKeyLocalStorage = (data) => {
  return localStorage.setItem("isCustomizedOptionEnable", JSON.stringify(data));
};

const getCustomizedOptionKeyLocalStorage = () => {
  return Utility.tryJsonParse(localStorage.getItem("isCustomizedOptionEnable"));
};
const setToppingArrayPriceKeyLocalStorage = (data) => {
  return localStorage.setItem("itemPriceToppingArray", JSON.stringify(data));
};

const getToppingArrayPriceKeyLocalStorage = () => {
  return Utility.tryJsonParse(localStorage.getItem("itemPriceToppingArray"));
};

const setSelectedAndReplaceToppingsLocalStorage = (data) => {
  return localStorage.setItem(
    "selectedAndReplacedToppings",
    JSON.stringify(data)
  );
};

const getSelectedAndReplaceToppingsLocalStorage = () => {
  return Utility.tryJsonParse(
    localStorage.getItem("selectedAndReplacedToppings")
  );
};
const setReplaceDisableLocalStorage = (data) => {
  return localStorage.setItem("replaceDisable", JSON.stringify(data));
};

const getReplaceDisableLocalStorage = () => {
  return Utility.tryJsonParse(localStorage.getItem("replaceDisable"));
};

const setCurrentSelectedToppings = (data) => {
  return localStorage.setItem("currentSelToppObj", JSON.stringify(data));
};

const getCurrentSelectedToppings = () => {
  return Utility.tryJsonParse(localStorage.getItem("currentSelToppObj"));
};

const setNextGenKey = (payload = window.location.href) => {
  try {
    localStorage.nextgenhomepage = payload;
  } catch (e) {}
};

const getLocationAnimationKey = () => {
  return Utility.tryJsonParse(localStorage.getItem("showLocationAnimation"));
};
const setLocationAnimationKey = (val) => {
  return localStorage.setItem("showLocationAnimation", val);
};

const updatePaymentOfferpaymentId = (payload) => {
  try {
    if (payload) {
      localStorage.paymentMode = JSON.stringify(payload);
    } else {
      delete localStorage.paymentMode;
    }
  } catch (e) {}
};

const getPaymentOfferPaymentIdLS = () => {
  return localStorage.paymentMode
    ? Utility.tryJsonParse(localStorage.paymentMode)?.paymentId
    : "";
};

const getPaymentOfferMode = () => {
  return localStorage.paymentMode
    ? Utility.tryJsonParse(localStorage.paymentMode)
    : "";
};

const setApplyOfferSuccess = (val) => {
  return (sessionStorage.welcomeOfferApplyStatus = JSON.stringify(val));
};
const getApplyOfferSuccess = () => {
  return (
    sessionStorage?.welcomeOfferApplyStatus !== undefined &&
    Utility.tryJsonParse(sessionStorage.welcomeOfferApplyStatus)
  );
};

const getCouponCode = () => {
  return Utility.tryJsonParse(localStorage.getItem("couponCode"));
};

const getTrackerIdData = () => {
  return localStorage.tracker_id_mobile
    ? Utility.tryJsonParse(localStorage.tracker_id_mobile)
    : {};
};

const getDeliveryTypeStationData = () => {
  return Utility.tryJsonParse(localStorage.getItem("deliveryTypeStationData"));
};
const setDeliveryTypeStationData = (val) => {
  return localStorage.setItem("deliveryTypeStationData", val);
};

export default {
  getLastLocationNewOrLastLocationNewForIp,
  isMiniAppPaytmGpay,
  getIsLocationPermisssionGiven,
  setIsLocationPermissionGiven,
  setAskLocationLaterInSession,
  getAskLocationLaterFromSession,
  setHideLocationPopupOnceInSession,
  getHideLocationPopupOnceFromSession,
  conditionalPurgeStorage,
  cartId,
  getReturnedFromMapScreen,
  setReturnedFromMapScreen,
  setNoDeliveryZoneInSession,
  getLocationSource,
  isLocationSelectedFromPwaV2,
  setLocationSelectedFromPwaV2,
  unsetSessionStorageKey,
  setLocationSource,
  getUserData,
  getCoordsFromRecentLocation,
  getCoordsForTakeaway,
  storeId,
  updateStoreId,
  updateLastLocation,
  updateLoyaltyInfo,
  updateIsLoggedIn,
  getDeliveryType,
  isLoggedIn,
  updateCart,
  cart,
  unsetKey,
  setCustomizedOptionKeyLocalStorage,
  getCustomizedOptionKeyLocalStorage,
  setToppingArrayPriceKeyLocalStorage,
  getToppingArrayPriceKeyLocalStorage,
  setSelectedAndReplaceToppingsLocalStorage,
  getSelectedAndReplaceToppingsLocalStorage,
  setReplaceDisableLocalStorage,
  getReplaceDisableLocalStorage,
  setCurrentSelectedToppings,
  getCurrentSelectedToppings,
  couponCode,
  updateCouponCode,
  setNextGenKey,
  setLocationAnimationKey,
  getLocationAnimationKey,
  updatePaymentOfferpaymentId,
  getPaymentOfferPaymentIdLS,
  setApplyOfferSuccess,
  getApplyOfferSuccess,
  getCouponCode,
  getTrackerIdData,
  getPaymentOfferMode,
  getDeliveryTypeStationData,
  setDeliveryTypeStationData,
  setTrackerOfKeyInSession
};
