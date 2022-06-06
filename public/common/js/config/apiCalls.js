import HTTP from "./axios";
import {
  apiEndpoints as ApiEndpoints,
  countryCodeFromDomain as CountryCodeFromDomain,
} from "./apiEndpoints";
import Utility from "../helpers/utility";
import ApiKeys from "./apiKeys";

const GEOCODE_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.GEOCODE}?latitude=${payload.latitude}&longitude=${
      payload.longitude
    }&v=${Utility.randomnumber()}`,
    { headers: headers }
  ).catch((err) => {
    return err;
  });
};

const GOOGLE_GEOLOCATION_API = async ({ headers, payload }) => {
  return HTTP.post(
    `${ApiEndpoints.GOOGLE_GEO_LOCATION}?key=${
      ApiKeys.GOOGLE_MAPS_API_KEY_MAP[CountryCodeFromDomain]
    }&v=${Utility.randomnumber()}`,
    payload,
    { headers: headers }
  ).catch((err) => {
    return err;
  });
};

const LOCATOR_STORES_API = async ({ headers, payload }) => {
  let res;
  for (var i = 0; i < 1; i++) {
    try {
      res = await HTTP.post(
        `${ApiEndpoints.LOCATOR_STORES}?v=${Utility.randomnumber()}`,
        payload,
        {
          headers: headers,
        }
      );
      if (res?.data?.data?.addressComponent) {
        break;
      }
    } catch (e) {
      res = e;
    }
  }
  return res;
};

const NEAREST_STORE_API = async ({ headers, payload }) => {
  let res;
  for (var i = 0; i < 1; i++) {
    try {
      res = await HTTP.post(
        `${ApiEndpoints.NEAREST_STORE}?v=${Utility.randomnumber()}`,
        payload,
        {
          headers: headers,
        }
      );
      if (res?.data?.data?.addressComponent) {
        break;
      }
    } catch (e) {
      res = e;
    }
  }
  return res;
};

const LOCATOR_STORES_TAKEAWAY_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.LOCATOR_STORES_TAKEAWAY}?orderDeliveryType=${
      payload.orderType
    }&latitude=${payload.latitude}&longitude=${payload.longitude}&${
      payload.extra
    }&v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const STORE_OFFERS_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${
      ApiEndpoints.STORE_OFFERS
    }/${payload}/cart/offers?includePartnerOffer=true&v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const LEFT_MENU_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${
      ApiEndpoints.LEFT_MENU
    }/${payload}/leftMenu?new=true&v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const APPLY_WELCOME_OFFER_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.APPLY_WELCOME_OFFER}/${payload.cartId}/promo/${
      payload.promoCode
    }?${
      payload.isBinOffer ? "includePartnerOffer=true&" : ""
    }v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const OFFER_TNC_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.OFFER_TNC}/${
      payload.promoCode
    }/tnc?v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const USER_LOYALTY_API = async ({ headers, payload }) => {
  return HTTP.get(`${ApiEndpoints.USER_LOYALTY}?v=${Utility.randomnumber()}`, {
    headers: headers,
  }).catch((err) => {
    return err;
  });
};

const PERSONALIZATION_ITEMS_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.PERSONALIZATION_ITEMS}?v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const WALLET_BALANCE_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.WALLET_BALANCE}?v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const ANONYNMOUS_USER_API = async ({ headers, payload }) => {
  return HTTP.post(
    `${ApiEndpoints.ANONYMOUS_USER}?v=${Utility.randomnumber()}`,
    payload,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

const TRACK_ORDER_API = async ({ headers, payload }) => {
  const { mobile, orderId, userId } = payload;
  return await HTTP.get(
    `${ApiEndpoints.TRACK_ORDERS}?mobile=${mobile}&order=${orderId}&userId=${userId}`,
    {
      headers: headers,
    }
  );
};

const PIZZA_TOPPING_API = async ({ headers, payload }) =>
  await HTTP.get(
    `${
      ApiEndpoints.PIZZA_TOPPING
    }/${payload}/toppings/data?v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  );

const PREVIOUS_ORDER_API = async ({ headers, payload }) =>
  await HTTP.get(`${ApiEndpoints.PREVIOUS_ORDER}/orders?userId=244371299`, {
    headers: headers,
  });

const ESTIMENT_ORDER_TIME_API = async ({ headers, payload }) =>
  await HTTP.get(`${ApiEndpoints.ESTIMENT_ORDER_TIME}`, {
    headers: headers,
  });

const SEND_TRACKER_STATUS_API = async ({ headers, payload }) => {
  return HTTP.post(`${ApiEndpoints.SEND_TRACKER_STATUS}`, payload, {
    headers: headers,
  }).catch((err) => {
    return err;
  });
};

const PAYMENT_OFFER_TNC_API = async ({ headers, payload }) => {
  return HTTP.get(
    `${ApiEndpoints.PAYMENT_OFFER_TNC}?offerId=${payload.offerId}&langId=${
      payload.langId
    }&platform=${payload.platform}&v=${Utility.randomnumber()}`,
    {
      headers: headers,
    }
  ).catch((err) => {
    return err;
  });
};

export default {
  GEOCODE_API,
  GOOGLE_GEOLOCATION_API,
  LOCATOR_STORES_API,
  NEAREST_STORE_API,
  LOCATOR_STORES_TAKEAWAY_API,
  STORE_OFFERS_API,
  LEFT_MENU_API,
  APPLY_WELCOME_OFFER_API,
  OFFER_TNC_API,
  USER_LOYALTY_API,
  PERSONALIZATION_ITEMS_API,
  WALLET_BALANCE_API,
  ANONYNMOUS_USER_API,
  TRACK_ORDER_API,
  PIZZA_TOPPING_API,
  PREVIOUS_ORDER_API,
  ESTIMENT_ORDER_TIME_API,
  SEND_TRACKER_STATUS_API,
  PAYMENT_OFFER_TNC_API,
};
