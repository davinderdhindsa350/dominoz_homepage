import ApiCalls from "../../config/apiCalls";
import Location from "../../config/location";
import Headers from "../../config/headers";
import LocalStorageFunc from "../../helpers/localStorageFunc";
import Utility from "../../helpers/utility";
import { keyMapsAction } from "../../helpers/storeKeys";
import {
  projectBasePathWithFolder,
  v2PwaBasePath,
} from "../../config/apiEndpoints";

const afterStoreApiSuccess = function (stores, commit, dispatch) {
  LocalStorageFunc.updateStoreId(stores.data.data.id);
  if (
    !Utility.checkWindowLocationIncludes(
      Utility.getCurrentStoreFolder() + stores.data.data.id
    )
  ) {
    window.location.href =
      projectBasePathWithFolder +
      Utility.getCurrentStoreFolder() +
      stores.data.data.id +
      Utility.getUrlQueryString();
  } else {
    commit("updateIsLocationSettled", {});
  }

  /* stores.data.data.online = 0;
  stores.data.data.startTime = "16:00"; */
  commit("updateStoreDetails", stores.data.data);
  if (!stores.data.data.online) {
    dispatch("locatorStoreTakeawayDineInAvailabilityApi", {});
  }

  dispatch(keyMapsAction.WELCOME_OFFER_API, {}, { root: true });
  dispatch(keyMapsAction.PIZZA_TOPPING_API, {}, { root: true });
};

const updateLocalLocationAfterStoreApi = function (stores, commit, payload) {
  commit("updateLocationFromGeoCodeApiCombo", {
    address_alias:
      payload.source == "ip"
        ? stores?.data?.data?.city || stores?.data?.data?.footerAddress || ""
        : Utility.getAddressAlias(),
    complete_address:
      LocalStorageFunc.getDeliveryType() != "D"
        ? stores?.data?.data?.name || ""
        : stores?.data?.data?.displayAddress || "",
    lastLocationObjectforLocalStorage: {
      recent: stores.data.data,
      location: {
        latitude:
          stores.data.data.addressComponent?.latitude ||
          stores.data.data.latitude,
        longitude:
          stores.data.data.addressComponent?.longitude ||
          stores.data.data.longitude,
        addressRequired: true,
      },
      deliveryType: LocalStorageFunc.getDeliveryType(),
      source: payload.source,
    },
    isTrueLocationDetected: payload.source == "ip" ? false : true,
  });
};

export default {
  namespaced: true,
  state: {
    isTrueLocationDetected: false,
    locationData: {},
    storeStartTime: "00:00",
    takeawayStoreList: [],
    takeawayDineAvailability: {
      TAKEAWAY: false,
      DINEIN: false,
    },
    storeNotFound: false,
    currentLocationUnservicable: false,
    noDeliveryZoneData: {},
    isLocationSettled: false,
    storeDetails: {
      online: 1,
    },
    showLocationAccessBlockedModal: false,
  },
  getters: {},
  mutations: {
    updateLocationFromGeoCodeApiCombo(state, payload) {
      state.isTrueLocationDetected = payload.isTrueLocationDetected;
      state.locationData = {
        address_alias: payload.address_alias,
        complete_address: payload.complete_address,
      };
      LocalStorageFunc.updateLastLocation(
        payload.lastLocationObjectforLocalStorage
      );
    },
    updateLocationFromLocalStorage(state, payload) {
      state.isTrueLocationDetected = payload.isTrueLocationDetected;
      state.locationData = payload.data;
    },
    resetLocationtate(state, payload) {
      state.isTrueLocationDetected = false;
      state.locationData = {};
      state.isLocationSettled = true;
    },
    updateTakeAwayStoresList(state, payload) {
      state.takeawayStoreList = payload;
    },
    updateStoreNotFound(state, payload) {
      state.storeNotFound = true;
      state.isLocationSettled = true;
    },
    updateTakeawayDineAvailability(state, payload) {
      state.takeawayDineAvailability.TAKEAWAY = payload.TAKEAWAY || false;
      state.takeawayDineAvailability.DINEIN = payload.DINEIN || false;
    },
    updateCurrentLocationUnservicable(state, payload) {
      state.currentLocationUnservicable = payload;
    },
    updateNoDeliveryZoneData(state, payload) {
      state.noDeliveryZoneData = payload;
    },
    updateIsLocationSettled(state, payload) {
      state.isLocationSettled = true;
    },
    resetIsLocationSettled(state, payload) {
      state.isLocationSettled = false;
    },
    updateStoreDetails(state, payload) {
      if (payload.startTime) {
        let hour = +payload.startTime.split(":")[0] || 0;
        let minute = +payload.startTime.split(":")[1];
        payload.startTime = {
          hour: hour > 12 ? hour - 12 : hour,
          minute: minute,
          amOrPm: hour > 12 ? "pm" : "am",
          advanceOrderApplicable:
            new Date().getHours() < hour ||
            (new Date().getHours() == hour &&
              new Date().getMinutes() == minute),
        };
      } else {
        payload.startTime = {
          advanceOrderApplicable: false,
        };
      }
      state.storeDetails = payload;
    },
    updateShowLocationAccessBlockedModal(state, payload) {
      state.showLocationAccessBlockedModal = payload;
    },
  },
  actions: {
    detectGeoLocationPopup({ commit, dispatch }, payload) {
      commit("resetIsLocationSettled", {});
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (data) => {
            LocalStorageFunc.setIsLocationPermissionGiven();
            dispatch("geoCodeApiCombo", data);
          },
          (error) => {
            LocalStorageFunc.unsetKey("isLocationPermissionGiven");
            LocalStorageFunc.setAskLocationLaterInSession();
            if (payload.source == "locationPopup") {
              payload.showModalM();
            } else {
              dispatch("googleGeoLocationApi", error);
            }
          },
          Location.geoLocationOptions
        );
      } else {
        commit("resetLocationtate", {});
      }
    },
    googleGeoLocationApi({ commit, dispatch }, payload) {
      ApiCalls.GOOGLE_GEOLOCATION_API({
        headers: {},
        payload: {
          considerIp: true,
        },
      })
        .then(function (response) {
          if (response?.data?.location?.lat) {
            dispatch("nearestStoreApi", {
              coords: {
                latitude: response.data.location.lat,
                longitude: response.data.location.lng,
              },
              source: "ip",
            });
          } else {
            dispatch("loadRecentLocationFromLocalStorage");
          }
        })
        .catch(function (error) {
          dispatch("loadRecentLocationFromLocalStorage");
        });
    },

    nearestStoreApi({ commit, dispatch }, payload) {
      Promise.all([
        ApiCalls.NEAREST_STORE_API({
          headers: Headers(),
          payload: {
            latitude: payload.coords.latitude,
            longitude: payload.coords.longitude,
            addressRequired: true,
          },
        }),
        ApiCalls.GEOCODE_API({
          headers: Headers(),
          payload: {
            latitude: payload.coords.latitude,
            longitude: payload.coords.longitude,
          },
        }),
      ])
        .then(async ([stores, geocode]) => {
          if (stores?.data?.data?.id) {
            payload.source = "ip";
            updateLocalLocationAfterStoreApi(stores, commit, payload);
            afterStoreApiSuccess(stores, commit, dispatch);
          } else {
            if (
              !Utility.checkWindowLocationIncludes(
                Utility.getCurrentStoreFolder() + Location.defaultStoreId
              )
            ) {
              LocalStorageFunc.unsetSessionStorageKey("noDeliveryLocation");
              window.location.href =
                projectBasePathWithFolder +
                Utility.getCurrentStoreFolder() +
                Location.defaultStoreId +
                Utility.getUrlQueryString();
            }
            commit("updateStoreNotFound", {});
          }
        })
        .catch((errors) => {
          dispatch("loadRecentLocationFromLocalStorage");
        });
    },

    geoCodeApiCombo({ commit, dispatch, rootGetters }, payload) {
      Promise.all([
        ApiCalls.LOCATOR_STORES_API({
          headers: Headers(),
          payload: {
            latitude: payload.coords.latitude,
            longitude: payload.coords.longitude,
            addressRequired: true,
          },
        }),
        ApiCalls.GEOCODE_API({
          headers: Headers(),
          payload: {
            latitude: payload.coords.latitude,
            longitude: payload.coords.longitude,
          },
        }),
      ])
        .then(async ([stores, geocode]) => {
          let noDeliveryZoneD = {
            location: {
              latitude: payload.coords.latitude,
              longitude: payload.coords.longitude,
            },
            displayText: geocode.data?.results && geocode.data?.results[0]?.displayAddress || "",
          };
          if (!stores?.data?.data?.id) {
            commit("updateNoDeliveryZoneData", noDeliveryZoneD);
          }
          if (stores?.data?.data?.id) {
            if (
              payload.source == "ip" &&
              LocalStorageFunc.storeId() == Location.defaultStoreId
            ) {
              LocalStorageFunc.setHideLocationPopupOnceInSession();
            }
            updateLocalLocationAfterStoreApi(stores, commit, payload);
            afterStoreApiSuccess(stores, commit, dispatch);
          } else if (
            LocalStorageFunc.storeId() == Location.defaultStoreId &&
            !LocalStorageFunc.getReturnedFromMapScreen()
          ) {
            LocalStorageFunc.setNoDeliveryZoneInSession(noDeliveryZoneD);
            LocalStorageFunc.setReturnedFromMapScreen();
            window.location.href = `${v2PwaBasePath}/deliveryMapScreen?deliveryType=D&storeSelected=false&noDeliveryZone=true`;
            return;
          } else {
            let payload2 = LocalStorageFunc.getCoordsFromRecentLocation();
            if (payload2) {
              await ApiCalls.LOCATOR_STORES_API({
                headers: Headers(),
                payload: {
                  latitude: payload2.coords.latitude,
                  longitude: payload2.coords.longitude,
                  addressRequired: true,
                },
              })
                .then(async function (response) {
                  if (response?.data?.data?.id) {
                    commit("updateCurrentLocationUnservicable", true);
                    if (LocalStorageFunc.isLocationSelectedFromPwaV2) {
                      LocalStorageFunc.unsetSessionStorageKey(
                        "lastLocationNewSource"
                      );
                    } else {
                      LocalStorageFunc.setLocationSelectedFromPwaV2();
                    }
                    if (
                      !Utility.checkWindowLocationIncludes(
                        Utility.getCurrentStoreFolder() + response.data.data.id
                      )
                    ) {
                      window.location.href =
                        projectBasePathWithFolder +
                        Utility.getCurrentStoreFolder() +
                        response.data.data.id +
                        Utility.getUrlQueryString();
                    } else {
                      updateLocalLocationAfterStoreApi(
                        response,
                        commit,
                        payload
                      );
                      afterStoreApiSuccess(response, commit, dispatch);
                    }
                  } else {
                    dispatch("nearestStoreApi", payload);
                  }
                })
                .catch(async function (error) {
                  dispatch("nearestStoreApi", payload);
                });
            } else {
              dispatch("nearestStoreApi", payload);
            }
          }
        })
        .catch((errors) => {
          dispatch("loadRecentLocationFromLocalStorage");
        });
    },

    loadRecentLocationFromLocalStorage({ commit, dispatch }, payload) {
      let value = localStorage.lastLocationNew
        ? Utility.tryJsonParse(localStorage.lastLocationNew)[0]?.recent
            ?.displayAddress
        : "";

      if (value) {
        commit("updateLocationFromLocalStorage", {
          isTrueLocationDetected:
            Utility.calculateIsTrueLocationDetectedFromLocationSource(),
          data: {
            address_alias: Utility.getAddressAlias(),
            complete_address: value,
          },
        });
        commit("updateIsLocationSettled", {});
      } else {
        if (
          !Utility.checkWindowLocationIncludes(
            Utility.getCurrentStoreFolder() + Location.defaultStoreId
          )
        ) {
          window.location.href =
            projectBasePathWithFolder +
            Utility.getCurrentStoreFolder() +
            Location.defaultStoreId +
            Utility.getUrlQueryString();
        }
        commit("resetLocationtate", {});
      }
    },

    locatorStoreTakeawayApi({ commit, dispatch }, payload) {
      let value = LocalStorageFunc.getCoordsFromRecentLocation();
      ApiCalls.LOCATOR_STORES_TAKEAWAY_API({
        headers: Headers(),
        payload: {
          latitude: value?.coords?.latitude,
          longitude: value?.coords?.longitude,
          orderType: payload || "P",
        },
      })
        .then(function (response) {
          if (
            response?.data?.status == "SUCCESS" &&
            response?.data?.storeStationDetails.length > 0
          ) {
            commit(
              "updateTakeAwayStoresList",
              response.data.storeStationDetails
            );
          } else {
            commit("updateTakeAwayStoresList", []);
          }
        })
        .catch(function (error) {
          commit("updateTakeAwayStoresList", []);
        });
    },

    locatorStoreTakeawayDineInAvailabilityApi({ commit, dispatch }, payload) {
      let value = LocalStorageFunc.getCoordsFromRecentLocation();
      ApiCalls.LOCATOR_STORES_TAKEAWAY_API({
        headers: Headers(),
        payload: {
          latitude: value?.coords?.latitude,
          longitude: value?.coords?.longitude,
          orderType: "P%2CCURB%2CDINEIN",
          extra: "distanceRequired=false",
        },
      })
        .then(function (response) {
          if (
            response?.data?.status == "SUCCESS" &&
            response?.data?.storeStationDetails.length > 0
          ) {
            let TAKEAWAY = false,
              DINEIN = false;
            response.data.storeStationDetails.map((_) => {
              _.store.stations.map((__) => {
                if (
                  __.status &&
                  __.operational &&
                  __.deliveryTypeCode == "DINEIN"
                ) {
                  DINEIN = true;
                }
                if (
                  __.status &&
                  __.operational &&
                  (__.deliveryTypeCode == "P" || __.deliveryTypeCode == "CURB")
                ) {
                  TAKEAWAY = true;
                }
              });
            });
            commit("updateTakeawayDineAvailability", {
              TAKEAWAY: TAKEAWAY,
              DINEIN: DINEIN,
            });
          } else {
            commit("updateTakeawayDineAvailability", {});
          }
        })
        .catch(function (error) {
          commit("updateTakeawayDineAvailability", {});
        });
    },
  },
};
