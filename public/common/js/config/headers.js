import Location from "./location";
import ApiKeys from "./apiKeys";
import Utility from "../helpers/utility";

const headers = (payload) => {
  let headers = {
    api_key: ApiKeys.DOMINO_API_KEY,
    client_type: "web app-chrome", // @todo: value to be calculated
  };

  let credentials = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.credentials
    : "";
  let value = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers
    : "";
  if (value && !value.isLoggedIn) {
    if (credentials) {
      for (const [key, val] of Object.entries(credentials)) {
        headers[key.toLowerCase()] = val;
      }
      if (credentials?.accessKeyId) {
        headers["authtoken"] = credentials.accessKeyId;
      }
    }
  } else {
    if (credentials?.accessKeyId) {
      headers["authtoken"] = credentials.accessKeyId;
    }
  }

  value = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.userId
    : "";
  if (value) {
    headers["userid"] = value;
  }

  value = localStorage.lastLocationNew
    ? Utility.tryJsonParse(localStorage.lastLocationNew)[0]?.recent?.advanceOrderStoreId
    : "";
  if (value) {
    headers["storeid"] = value;
  } else {
    let __ = localStorage.storeId || "";
    if (__) {
      headers["storeid"] = __;
    } else {
      headers["storeid"] = Location.defaultStoreId;
      localStorage.storeId = Location.defaultStoreId;
    }
  }
  value = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.cartId
    : "";
  if (value) {
    headers["cartid"] = value;
  }

  value = localStorage.userInfo
    ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.deliveryType
    : "";
  if (value) {
    headers["deliverytype"] = value;
  }

  if (payload && payload.withRefreshToken) {
    value = localStorage.userInfo
      ? Utility.tryJsonParse(localStorage.userInfo)?.headers?.credentials?.refreshToken
      : "";
    if (value) {
      headers["refreshtoken"] = value;
    }
  }

  return headers;
};

export default headers;
