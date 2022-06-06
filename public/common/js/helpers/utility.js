import {
  v2PwaBasePath,
  projectBasePathWithFolder,
} from "../config/apiEndpoints";
import LocalStorageFunc from "./localStorageFunc";
import store from "../store/index";
import { keyMapsAction, keyMapsMutation } from "../helpers/storeKeys";

const randomnumber = (minimum = 100000000, maximum = 999999999) => {
  return Math.floor(Math.random() * (maximum - minimum, maximum + 1)) + minimum;
};

const pizzaCounter = () => {
  if (!!window.IntersectionObserver) {
    let scrollY = window.scrollY;
    let observer = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach((entry) => {
          if (window.scrollY <= scrollY && entry.isIntersecting) {
            store.commit(
              keyMapsMutation.SET_PRODUCT_COUNTER_STRING,
              entry.target.id
            );
          } else if (window.scrollY > scrollY && !entry.isIntersecting) {
            store.commit(
              keyMapsMutation.SET_PRODUCT_COUNTER_STRING,
              entry.target.id
            );
          }
          scrollY = window.scrollY;
        });
      },
      {
        root: null,
        threshold: 1,
      }
    );
    store.commit(keyMapsMutation.SET_PRODUCT_OBSERVER, observer);
  }
};

const tryJsonParse = (value) => {
  let _ = {};
  try {
    _ = JSON.parse(value);
  } catch (e) {
    _ = {};
  }
  return _;
};

const calculateIsTrueLocationDetectedFromLocationSource = () => {
  return LocalStorageFunc.getLocationSource() == "ip" ? false : true;
};

const takeawayDineinFolder = (isTrueLocationDetected) => {
  if (isTrueLocationDetected) {
    return "nearestStore";
  }
  const takeawayLocation = getSessionStorageValue("takeawayLocation");
  if (typeof takeawayLocation != "undefined" && takeawayLocation)
    return "nearestStore";
  return "locationSelection";
};

const deliveryFolder = (isLoggedIn) => {
  if (isLoggedIn) {
    let userAddresses = localStorage.userAddresses
      ? tryJsonParse(localStorage.userAddresses)
      : "";
    if (Array.isArray(userAddresses) && userAddresses.length > 0)
      return "address";
  }
  return "changeAddress";
};

const textLowerAndTrimSpace = (text) =>
  text.toLocaleLowerCase().replace(/\s/g, "");

const keybasedProductItems = (productItems) => {
  let response = {};
  productItems.forEach((_) => {
    let temp1 = {};
    _.data?.forEach((__) => {
      let temp2 = {};
      __.data?.forEach((___) => {
        temp2[___.code] = ___;
      });
      temp1[__.categoryName] = { ...__, data: temp2 };
    });
    response = { ...response, ...temp1 };
  });
  return response;
};

const keybasedProductItem = (_) => {
  let response = {};
  let priceHashCode = {};
  _?.forEach((__) => {
    let temp2 = {};
    __.data?.forEach((___) => {
      temp2[___.code] = ___;
      priceHashCode[___.code] = {};
      temp2[___.code]["customCategoryId"] = __.categoryId;
      temp2[___.code]["customCategoryName"] = __.categoryName;

      temp2[___.code].crust?.map((c) => {
        priceHashCode[___.code][c.crustId] = {};
        c?.sizes.map((s) => {
          priceHashCode[___.code][c.crustId][s.sizeId] = s.price;
        });
      });

      if (!temp2[___.code]["defaultPrice"]) {
        temp2[___.code]["defaultPrice"] = +temp2[___.code].crust
          .filter((_) => {
            if (_.crustId == temp2[___.code].selectedCrustId) {
              temp2[___.code]["defaultCrustName"] = _.name;
              temp2[___.code]["crustName"] = _.name;
              temp2[___.code]["crustId"] = temp2[___.code].selectedCrustId;
            }
            return _.crustId == temp2[___.code].selectedCrustId;
          })[0]
          ?.sizes.filter((_) => {
            if (_.sizeId == temp2[___.code].selectedSizeId) {
              temp2[___.code]["defaultSizeName"] = _.name;
              temp2[___.code]["sizeName"] = _.name;
              temp2[___.code]["sizeId"] = temp2[___.code].selectedSizeId;
            }
            return _.sizeId == temp2[___.code].selectedSizeId;
          })[0]?.price;
        temp2[___.code]["itemPrice"] = temp2[___.code]["defaultPrice"];
      }
      temp2[___.code]["itemPrice"] = temp2[___.code]["defaultPrice"];
      temp2[___.code]["addTopngs"] = [];

      if (Object.keys(priceHashCode[___.code]) == 0) {
        delete priceHashCode[___.code];
      }
    });
    response[__.categoryName] = { ...__, data: temp2 };
  });

  return { response: response, priceHashCode: priceHashCode };
};

const getCurrentToppingsTotalPrice = (toppingsMasterData, item) => {
  let sum = 0;
  item.addTopngs?.map((_) => {
    sum =
      sum +
        +toppingsMasterData.filter((__) => {
          return __.toppingId == _;
        })[0]?.price[item.sizeId] || 0;
  });
  return sum;
};

const sendToLoginScreen = (location) => {
  localStorage.finalRedirectUrl = location.href;
  window.location.href = v2PwaBasePath + "/login?redirectUrl=" + location.href;
  return;
};

const getAppDownloadLink = () => {
  return "https://dominos.onelink.me/uM7R/3e839899";
};

const getCurrentStoreFolder = () => {
  let _ = window.location.href.split(projectBasePathWithFolder + "/")[1];
  if (_?.startsWith("?")) _ = "store";
  else {
    _ = _?.split("/")[0]?.split("?")[0] || "store";
  }

  return `/${_}/`;
};

const scrollUptoTo = (elmId) => {
  let el = document.getElementById(elmId);
  let top = el.offsetTop;

  window.scrollTo({
    top: top - 70,
    left: 100,
    behavior: "smooth",
  });
};

const getToppingPriceForSize = (toppingsArray, toppingId, sizeId) => {
  let _ = 0;
  for (let i = 0; i < toppingsArray.length; i++) {
    if (toppingsArray[i].toppingId == toppingId) {
      _ = +toppingsArray[i].price[sizeId];
      break;
    }
  }
  return _;
};

const toppingsNameByToppingId = (mainArry, allTopngs, data) => {
  let currentToppingsName = [];
  if (mainArry?.length > 0 && data?.length > 0) {
    currentToppingsName = mainArry.filter((i) => {
      if (data.sort().toString() == i.addTopngs.sort().toString()) {
        return i;
      }
    });
  }
  let obj = currentToppingsName[0] || [];
  if (obj.addTopngs) {
    let newObj = obj.addTopngs.map((i) => Number(i));
    return allTopngs
      .filter((j) => newObj.includes(Number(j.toppingId)))
      .map((o) => o.name)
      .join();
  } else {
    return "";
  }
};

const currentCrustName = (arry, result) => {
  // arry = it is current CartItems from store
  // result = it is current selected item which will going to add or sub
  let ary = [];
  if (arry && result) {
    ary = arry.filter((i) => i.crustName == result.crustName);
  }
  return ary[0]?.crustName || "";
};

const currentSizeName = (arry, result) => {
  // arry = it is current CartItems from store
  // result = it is current selected item which will going to add or sub
  let name = [];
  if (arry && result) {
    name = arry.filter((i) => i.sizeName == result.sizeName);
  }
  return name[0]?.sizeName || "";
};

const deepCopyArry = (data) => {
  return JSON.parse(JSON.stringify(data));
};
const sortQniqueArry = (data) => {
  return Array.from(new Set(data.map(JSON.stringify))).map(JSON.parse);
};

const getWidgetWidthHeight = (
  itemsInScreen = 1.75,
  aspectRatio = 1,
  gap = 28,
  minWidth = 300
) => {
  let width = (window.innerWidth - gap) / itemsInScreen;
  let height = width / (aspectRatio + gap);
  return {
    width: width >= minWidth ? width : minWidth,
    height: height,
  };
};

const amplitudeEventObj = (
  userAction,
  sectionName,
  sectionPosition,
  preObj
) => {
  return {
    userAction,
    sectionName,
    sectionPosition,
    ...(preObj.positionWithinSection && {
      positionWithinSection: preObj.positionWithinSection,
    }),
    ...(preObj.category && { category: preObj.category }),
    ...(preObj.subCategory && { subCategory: preObj.subCategory }),
    ...(preObj.orderType && { orderType: preObj.orderType }),
    ...(preObj.searchSection && { searchSection: preObj.searchSection }),
    ...(preObj.searchQuery && { searchQuery: preObj.searchQuery }),
    ...(preObj.categoryPosition && {
      categoryPosition: preObj.categoryPosition,
    }),
    ...(preObj.offerType && { offerType: preObj.offerType }),
    ...(preObj.milestoneStage && { milestoneStage: preObj.milestoneStage }),
    ...(preObj.offerStage && { offerStage: preObj.offerStage }),
    ...(preObj.impressionType && { impressionType: preObj.impressionType }),
    ...(preObj.scrollType && { scrollType: preObj.scrollType }),
    ...(preObj.enrollmentStatus && {
      enrollmentStatus: preObj.enrollmentStatus,
    }),
    ...(preObj.points && { points: preObj.points }),
    ...(preObj.loginStatus && { loginStatus: preObj.loginStatus }),
    ...(preObj.actionType && { actionType: preObj.actionType }),
    ...(preObj.addToCart && { addToCart: preObj.addToCart }),
    ...(preObj.sellerType && { sellerType: preObj.sellerType }),
    ...(preObj.popularityType && { popularityType: preObj.popularityType }),
    ...(preObj.addCounter && { addCounter: preObj.addCounter }),
    ...(preObj.addNonVegToppings && {
      addNonVegToppings: preObj.addNonVegToppings,
    }),
    ...(preObj.addVegToppings && { addVegToppings: preObj.addVegToppings }),
    ...(preObj.changeCrust && { changeCrust: preObj.changeCrust }),
    ...(preObj.changeSize && { changeSize: preObj.changeSize }),
    ...(preObj.close && { close: preObj.close }),
    ...(preObj.entry && { entry: preObj.entry }),
    ...(preObj.replaceToppings && { replaceToppings: preObj.replaceToppings }),
    ...(preObj.replaceToppingsFrom && {
      replaceToppingsFrom: preObj.replaceToppingsFrom,
    }),
    ...(preObj.replaceToppingsTo && {
      replaceToppingsTo: preObj.replaceToppingsTo,
    }),
    ...(preObj.add && { add: preObj.add }),
    ...(preObj.resetAll && { resetAll: preObj.resetAll }),
    ...(preObj.update && { update: preObj.update }),
    ...(preObj.resultCount && { resultCount: preObj.resultCount }),
    ...(preObj.topResult && { topResult: preObj.topResult }),
    screenName: preObj.screenName ? preObj.screenName : "homepage",
    previousScreenname: preObj.previousScreenname
      ? preObj.previousScreenname
      : document.referrer,
  };
};

const returnsGetItemWQuantity = (cartData, addItemToCart) => {
  let item = [];
  if (cartData) {
    item = cartData.find((i) => i.code == addItemToCart.code);
  }
  if (item) {
    return true;
  } else {
    return false;
  }
};

const jsonParser = (json, defaultVal = {}) => {
  try {
    if (typeof json === "object" || !json) return json;
    return JSON.parse(json);
  } catch (e) {
    return defaultVal;
  }
};

const getSessionStorageValue = (key) => {
  try {
    if (typeof window !== "undefined") {
      return jsonParser(sessionStorage.getItem(key));
    }
  } catch (err) {}
};

const replaceToppingText = (cartData, allTopngs, data) => {
  let replaceTopDisplay = "";
  if (cartData.length > 0 && cartData?.length !== undefined) {
    const curObj = cartData[cartData.length - 1];
    const { remTopngs, repTopngs } = curObj;
    if (remTopngs && repTopngs) {
      let removeTop = allTopngs.find(
        (i) => Number(i.toppingId) === Number(curObj["remTopngs"][0])
      );
      let addTop = allTopngs.find(
        (i) => Number(i.toppingId) === Number(curObj["repTopngs"][0])
      );
      if (removeTop && addTop) {
        replaceTopDisplay = `${removeTop.name} with ${addTop.name}` || "";
      }
    }
  }
  if (data?.repTopngs?.length !== undefined && data?.repTopngs?.length > 0) {
    return replaceTopDisplay;
  } else {
    return "";
  }
};

const defaultToppingsCustomAtrrArry = (
  currentItemDefaultTopping,
  allTopngs
) => {
  if (currentItemDefaultTopping && allTopngs) {
    return allTopngs
      .filter((item) => currentItemDefaultTopping.includes(item.toppingId))
      .map((i) => i.custAttrField);
  }
};

const getAddressAlias = (
  coords = LocalStorageFunc.getLastLocationNewOrLastLocationNewForIp()[0]
    ?.location || ""
) => {
  let footerAddress =
    LocalStorageFunc.getLastLocationNewOrLastLocationNewForIp()[0]?.recent
      ?.footerAddress ||
    LocalStorageFunc.getLastLocationNewOrLastLocationNewForIp()[0]?.recent
      ?.city ||
    "";
  if (LocalStorageFunc.getLocationSource() == "ip") {
    return (
      LocalStorageFunc.getLastLocationNewOrLastLocationNewForIp()[0]?.recent
        ?.city || ""
    );
  }
  let deliveryType = LocalStorageFunc.getDeliveryType();
  if (deliveryType == "P") {
    return "Takeaway from";
  } else if (deliveryType == "DINEIN") {
    return "Dine-in at";
  } else {
    let userAddresses = localStorage.userAddresses
      ? tryJsonParse(localStorage.userAddresses)
      : "";
    try {
      for (const item of userAddresses) {
        if (
          item.longitude == coords.longitude &&
          item.latitude == coords.latitude
        ) {
          return (
            item.customer_address_name ||
            "Home" ||
            item.footerAddress ||
            item.city
          );
        }
      }
    } catch (err) {}
    return footerAddress;
  }
};

const findOrdinalsSufixInDate = (day) => {
  // it will set the appropriate string according to day
  let first = Number(day) % 10;
  let second = Number(day) % 100;
  if (first == 1 && second != 11) {
    return "st";
  }
  if (first == 2 && second != 12) {
    return "nd";
  }
  if (first == 3 && second != 13) {
    return "rd";
  }
  return "th";
};

const getNotAvailablePizzaInStore = (storeItem, currentPizza) => {
  let isAva = [];
  if (currentPizza !== undefined && storeItem?.length !== undefined) {
    isAva = storeItem.find((i) => i.code === currentPizza.menuCode);
  }
  if (isAva && Object.keys(isAva)?.length > 0) {
    return false;
  } else {
    return true;
  }
};

const getDomain = () => {
  let domain = window.location.host.toString();
  let index = domain.indexOf(".");
  domain = domain.substr(index, domain.length - 1);
  return domain;
};

const setCookie = (name, value, days, domain) => {
  var expires = "";
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie =
    name +
    "=" +
    (value || "") +
    ";domain=" +
    (domain || ".dominos.co.in") +
    expires +
    "; path=/";
};

const getCookie = (c_name) => {
  var i,
    x,
    y,
    ARRcookies = document.cookie.split(";");
  for (i = 0; i < ARRcookies.length; i++) {
    x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
    y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
    x = x.replace(/^\s+|\s+$/g, "");
    if (x == c_name) {
      return unescape(y);
    }
  }
};

const deleteCookie = (name, domain) => {
  if (name) {
    document.cookie =
      name +
      "=;expires=Thu,01 Jan 1970 00:00:01 UTC;path=/;domain=" +
      (domain || ".dominos.co.in");
  }
};

const addKeyToCartWhenReloadsData = (arr) => {
  if (arr) {
    return arr.map((i) => {
      if (
        (i?.addTopngs?.length !== undefined && i?.addTopngs?.length > 0) ||
        i.defaultSizeName !== i.sizeName ||
        i.defaultCrustName !== i.crustName
      ) {
        return { ...i, isUserCustomizable: true };
      } else {
        return { ...i, isUserCustomizable: false };
      }
    });
  } else {
    return [];
  }
};

const getUrlQueryString = () => {
  let _ = "";
  try {
    var url = window.location.href,
      retObject = {},
      parameters;
    if (url.indexOf("?") === -1) {
      return "";
    }
    url = url.split("?")[1];
    parameters = url.split("&");
    for (var i = 0; i < parameters.length; i++) {
      retObject[parameters[i].split("=")[0]] = parameters[i].split("=")[1];
    }

    _ = "?";
    for (const [key, value] of Object.entries(retObject)) {
      if (key) {
        _ = _ + "&" + key + "=" + value;
      }
    }
  } catch (e) {
    _ = "";
  }

  return _;
};

const getQueryFromUrl = () => {
  var retObject = {};
  try {
    var url = window.location.href,
      parameters;
    if (url.indexOf("?") === -1) {
      return {};
    }
    url = url.split("?")[1];
    parameters = url.split("&");
    for (var i = 0; i < parameters.length; i++) {
      retObject[parameters[i].split("=")[0]] = parameters[i].split("=")[1];
    }
  } catch (e) {
    return {};
  }

  return retObject;
};

const checkWindowLocationIncludes = (partial) => {
  return (
    window.location.href.includes(partial) &&
    (window.location.href.includes(partial + "/") ||
      window.location.href.includes(partial + "?") ||
      !window.location.href.split(partial)[1])
  );
};

export default {
  pizzaCounter,
  randomnumber,
  tryJsonParse,
  calculateIsTrueLocationDetectedFromLocationSource,
  takeawayDineinFolder,
  deliveryFolder,
  keybasedProductItems,
  textLowerAndTrimSpace,
  keybasedProductItem,
  getCurrentToppingsTotalPrice,
  sendToLoginScreen,
  getAppDownloadLink,
  getCurrentStoreFolder,
  scrollUptoTo,
  deepCopyArry,
  sortQniqueArry,
  getToppingPriceForSize,
  getWidgetWidthHeight,
  toppingsNameByToppingId,
  amplitudeEventObj,
  currentCrustName,
  currentSizeName,
  returnsGetItemWQuantity,
  jsonParser,
  getSessionStorageValue,
  replaceToppingText,
  defaultToppingsCustomAtrrArry,
  getAddressAlias,
  findOrdinalsSufixInDate,
  getNotAvailablePizzaInStore,
  getDomain,
  setCookie,
  getCookie,
  deleteCookie,
  addKeyToCartWhenReloadsData,
  getUrlQueryString,
  getQueryFromUrl,
  checkWindowLocationIncludes,
};
