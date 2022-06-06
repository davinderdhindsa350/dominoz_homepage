let apiBaseUrl = window._siteProps_?.props?.apibaseurl;
let apiBaseUrl2 = window._siteProps_?.props?.apibaseurl2;
let apiBaseUrl3 = window._siteProps_?.props?.apibaseurl3;
let imgBaseUrl = window._siteProps_?.props?.imgbaseurl;
let staticImgBaseUrl = window._siteProps_?.props?.staticimgbaseurl;
let projectBasePathWithFolder =
  window._siteProps_?.props?.projectbasepathwithfolder;
let v2PwaBasePath = window._siteProps_?.props?.v2pwabasepath;
let PROXY_PATH = "";

//use on local if needed
/* apiBaseUrl = "https://s1.dominosindia.in";
apiBaseUrl2 = "https://s1.dominosindia.in";
apiBaseUrl3 = "https://s3.dominosindia.in";
staticImgBaseUrl = location.origin + "/discovery-ui-web/public/dist/default";
projectBasePathWithFolder = location.origin + "/discovery-ui-web/en/pwa";
v2PwaBasePath = "https://m.dominos.co.in";
PROXY_PATH = "http://localhost:3002/"; */

let countryCodeFromDomain;
switch (window._siteProps_?.props?.build_env_var) {
  case "production":
    if (location.host.includes("dominos.co.in")) {
      countryCodeFromDomain = "IN";
    } else if (location.host.includes("dominos.com.bd")) {
      countryCodeFromDomain = "BG";
    } else {
      countryCodeFromDomain = "SL";
    }
    break;
  case "qa":
  case "dev":
  case "local":
  default:
    countryCodeFromDomain = "IN";
}

let countryFromDomain;
switch (window._siteProps_?.props?.build_env_var) {
  case "production":
    if (location.host.includes("dominos.co.in")) {
      countryFromDomain = "INDIA";
    } else if (location.host.includes("dominos.com.bd")) {
      countryFromDomain = "BANGLADESH";
    } else {
      countryFromDomain = "SRILANKA";
    }
    break;
  case "qa":
  case "dev":
  case "local":
  default:
    countryFromDomain = "INDIA";
}

let domainReferrerFromDomain;
switch (window._siteProps_?.props?.build_env_var) {
  case "production":
    domainReferrerFromDomain = "https://" + location.host + "/";
    break;
  case "qa":
  case "dev":
  case "local":
  default:
    domainReferrerFromDomain = "https://m.dominos.co.in/";
}

const urls = {
  DOMINO_API_BASE_URL: PROXY_PATH + apiBaseUrl,
  DOMINO_API_BASE_URL2: PROXY_PATH + apiBaseUrl2,
  DOMINO_API_BASE_URL3: PROXY_PATH + apiBaseUrl3,
  GOOGLE_API_BASE_URL:
    PROXY_PATH + window._siteProps_?.props?.google_api_base_url,
};

const apiEndpoints = {
  REFRESH_TOKEN: urls.DOMINO_API_BASE_URL + "/loginhandler/refreshtoken",
  REFRESH_TOKEN_ANONYMOUS: urls.DOMINO_API_BASE_URL + "/loginhandler/refreshtokenanonymous",
  GEOCODE: urls.DOMINO_API_BASE_URL + "/locator-service/ve2/geocode",
  GOOGLE_GEO_LOCATION: urls.GOOGLE_API_BASE_URL + "/geolocation/v1/geolocate",
  LOCATOR_STORES: urls.DOMINO_API_BASE_URL + "/locator-service/ve2/stores",
  NEAREST_STORE: urls.DOMINO_API_BASE_URL3 + "/locator-service/ve3/nearest-store",
  LOCATOR_STORES_TAKEAWAY:
    urls.DOMINO_API_BASE_URL + "/locator-service/ve3/stores",
  STORE_OFFERS: urls.DOMINO_API_BASE_URL + "/agg/ve1/leftnav/store",
  LEFT_MENU: urls.DOMINO_API_BASE_URL + "/catalog-service/ve1/stores",
  OFFER_TNC: urls.DOMINO_API_BASE_URL + "/promo-service/ve1/promo",
  APPLY_WELCOME_OFFER: urls.DOMINO_API_BASE_URL + "/agg/cart-service/ve1/cart",
  TRACK_ORDERS: urls.DOMINO_API_BASE_URL + "/tracker-service/ve2/orders",
  USER_LOYALTY: urls.DOMINO_API_BASE_URL + "/customer-service/ve1/loyalty/user",
  PERSONALIZATION_ITEMS:
    urls.DOMINO_API_BASE_URL2 + "/jfl-discovery-service/ve1/api/public/sites/v1/upsell-data",
  WALLET_BALANCE:
    urls.DOMINO_API_BASE_URL + "/wallet-service/ve3/wallet/balance",
  ANONYMOUS_USER: urls.DOMINO_API_BASE_URL + "/loginhandler/anonymoususer",
  PIZZA_TOPPING: urls.DOMINO_API_BASE_URL + "/prod-olo-api/v1",
  PREVIOUS_ORDER: urls.DOMINO_API_BASE_URL + "/order-service/ve1",
  ESTIMENT_ORDER_TIME:
    urls.DOMINO_API_BASE_URL + "/order-service/ve1/orders/tracker/trackOnMap",
  SEND_TRACKER_STATUS:
    urls.DOMINO_API_BASE_URL + "/tracker-service/ve2/sendtrackerstatus",
  PAYMENT_OFFER_TNC: urls.DOMINO_API_BASE_URL3 + "/jfl-discovery-service/ve1/api/public/sites/v1/tnc",
};

export {
  apiEndpoints,
  imgBaseUrl,
  staticImgBaseUrl,
  projectBasePathWithFolder,
  v2PwaBasePath,
  countryCodeFromDomain,
  domainReferrerFromDomain,
  countryFromDomain,
};
