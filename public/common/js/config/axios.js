const axios = require("axios");
import store from "../store/index";
import { apiEndpoints as ApiEndpoints } from "./apiEndpoints";
import Headers from "./headers";
import { keyMapsAction, keyMapsMutation } from "../helpers/storeKeys";
import LocalStorageFunc from "../helpers/localStorageFunc";

const axiosConfig = {
  timeout: 10000,
  headers: {
    Accept: "application/json",
  },
};

const HTTP = axios.create(axiosConfig);

export default HTTP;

// separate instance for axios refreshtoken call
const refreshInstance = axios.create();

const refresh_token = async (headers, sourceUrl) => {
  try {
    if (LocalStorageFunc.isLoggedIn()) {
      return await refreshInstance.post(
        ApiEndpoints.REFRESH_TOKEN,
        {
          userId: headers.userid,
          refreshToken: headers.refreshtoken,
        },
        { headers: headers }
      );
    } else {
      return await refreshInstance.post(
        ApiEndpoints.REFRESH_TOKEN_ANONYMOUS,
        {
          userId: headers.userid,
          refreshToken: headers.refreshtoken,
        },
        { headers: headers }
      );
    }
  } catch (e) {
    store.commit(keyMapsMutation.UPDATE_LOGGIN_STATE, {
      isLoggedIn: false,
    });
    if (!sourceUrl.includes("loginhandler/anonymoususer")) {
      store.dispatch(keyMapsAction.ANONYMOUS_USER_API);
    } else {
      setTimeout(() => {
        store.dispatch(keyMapsAction.ANONYMOUS_USER_API);
      }, 5000);
    }
  }
};

let refreshing_token = null;

HTTP.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const config = error.config;
    if (
      ((error.response &&
        (error.response.status == 401 ||
          error.response.status == 500 ||
          error.response.status == 403)) ||
        !error.response) && //handled refresh for cors issue
      !config._retry
    ) {
      config._retry = true;
      try {
        refreshing_token = refreshing_token
          ? refreshing_token
          : refresh_token(Headers({ withRefreshToken: true }), config.url);
        let res = await refreshing_token;
        refreshing_token = null;
        if (res?.data?.credentials) {
          store.commit(
            "Auth/updateLocalStorageUserInfoCredential",
            res.data.credentials
          );
          config.headers = {
            ...config.headers,
            authtoken: res.data.credentials.accessToken,
          };
          return HTTP(config);
        }
      } catch (err) {
        store.commit(keyMapsMutation.UPDATE_LOGGIN_STATE, {
          isLoggedIn: false,
        });
        return Promise.reject(err);
      }
    }
    return Promise.reject(error);
  }
);
