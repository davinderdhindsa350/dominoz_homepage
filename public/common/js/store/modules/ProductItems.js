import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";
import PersonalizationItemsApiResponse from "../../assets/FallbackData/PersonalizationItemsApiResponse.json";
import LocalStorageFunc from "../../helpers/localStorageFunc";
import Utility from "../../helpers/utility";
export default {
  namespaced: true,
  state: {
    productItems: {},
    unfilteredProductItems: {},
    personalizationItems: {},
    allProductItemInOne: [],
    clearFilterRef: 0,
    productObserver: "",
    productCounterString: "",
    allCategoryId: [],
    recommendedItemsIds: [],
    sizeCrustPriceHashMap: {},
    openAllCards: false,
  },
  getters: {
    getProductItemForBottomBar: (state) => {
      return state.unfilteredProductItems;
    },
    getProductItems: (state) => {
      let _ = [];
      Object.entries(state.productItems).forEach(([key, value]) => {
        let __ = [];
        Object.entries(value.data).forEach(([key2, value2]) => {
          __.push(value2);
        });
        _.push(__);
      });
      return _;
    },
    getBestSellerItemsId(state) {
      let _ = [];
      Object.entries(state.productItems).forEach(([key, value]) => {
        let __ = [];
        Object.entries(value.data).forEach(([key2, value2]) => {
          __.push(value2);
        });
        _.push(__);
      });
      return _[0].map((i) => i.id);
    },
    getPopularTagItems(state) {
      let _ = [];
      Object.entries(state.productItems).forEach(([key, value]) => {
        let __ = [];
        Object.entries(value.data).forEach(([key2, value2]) => {
          __.push(value2);
        });
        _.push(__);
      });
      return _.slice(1, _.length - 1)
        .flat()
        .map((i) => {
          let obj = {};
          obj["id"] = i.id;
          obj["customCategoryName"] = i.customCategoryName;
          return obj;
        });
    },
    getProductItemsInOneArray(state, getters) {
      state.allProductItemInOne = [].concat
        .apply([], getters.getProductItems)
        .map((i) => {
          if (i["sortOrder"] !== undefined) {
            delete i["sortOrder"];
          }
          return i;
        });
      return state.allProductItemInOne;
    },
    getSingleProductItemCategory: (state) => (categoryName) => {
      return state.productItems[categoryName];
    },
    getRecommendedProducts: (state, getters) => () => {
      let recommendedProducts =
        state.personalizationItems.recommendedItems?.data?.filter(
          (item) => item.type == "PIZZA"
        );
      state.recommendedItemsIds = state.personalizationItems.recommendedItems?.data?.map(i => i.menuCode) || []
      return recommendedProducts
        ?.map((_) => {
          for (const [key, value] of Object.entries(state.productItems)) {
            if (value?.data[_.menuCode]) {
              return {
                ...value.data[_.menuCode],
                categoryName: value?.categoryName,
              };
            }
          }
        })
        .filter((value) => value && Object.keys(value).length !== 0);
    },
    getCrossSellProducts: (state, getters) => () => {
      let crossSellProducts =
        state.personalizationItems.recommendedItems?.data?.filter(
          (item) => item.type != "PIZZA"
        );
      return crossSellProducts
        ?.map((_) => {
          for (const [key, value] of Object.entries(state.productItems)) {
            if (value?.data[_.menuCode]) {
              return {
                ...value.data[_.menuCode],
                categoryName: value?.categoryName,
                categoryId: value?.categoryId,
              };
            }
          }
        })
        .filter((value) => value && Object.keys(value).length !== 0);
    },
    getUpSellOptions: (state) => (itemFromCart) => {
      if (!state.personalizationItems.upsell || !itemFromCart) {
        return [];
      }
      let res =
        state.personalizationItems.upsell.data
          .filter((_) => {
            return (
              _.crustId == itemFromCart.selectedCrustId &&
              _.sizeId == itemFromCart.selectedSizeId
            );
          })
          .map((_) => {
            return _;
          })[0] || {};

      res =
        res.suggestions?.map((_) => {
          let __ = {};
          if (_.type == "TOPPINGS") {
            for (let i = 0; i < itemFromCart.toppingsMasterData.length; i++) {
              if (
                itemFromCart.toppingsMasterData[i].toppingId == _.itemId &&
                itemFromCart.allowedToppings.includes(+_.itemId)
              ) {
                __["name"] = itemFromCart.toppingsMasterData[i].name;
                __["price"] =
                  +itemFromCart.toppingsMasterData[i].price[
                    itemFromCart.sizeId
                  ];
                __["toppingId"] = _.itemId;
                break;
              }
            }
          } else if (_.type == "CRUST") {
            for (let i = 0; i < itemFromCart.crust.length; i++) {
              if (itemFromCart.crust[i].crustId == _.itemId) {
                __["name"] = itemFromCart.crust[i].name;
                for (let j = 0; j < itemFromCart.crust[i].sizes.length; j++) {
                  if (
                    itemFromCart.crust[i].sizes[j].sizeId == itemFromCart.sizeId
                  ) {
                    __["price"] = +itemFromCart.crust[i].sizes[j].price;
                    let sum = 0;
                    itemFromCart.addTopngs?.map((_) => {
                      sum =
                        sum +
                          +itemFromCart.toppingsMasterData.filter((__) => {
                            return __.toppingId == _;
                          })[0]?.price[itemFromCart.sizeId] || 0;
                    });

                    __["price"] = __["price"] + sum;
                    if (
                      itemFromCart.crust[i].sizes[j].price <
                      itemFromCart.defaultPrice
                    ) {
                      __["name"] = "";
                    }
                    __["selectedCrustId"] = _.itemId;
                    break;
                  }
                }
                break;
              }
            }
          } else if (_.type == "SIZE") {
            for (let i = 0; i < itemFromCart.crust.length; i++) {
              for (let j = 0; j < itemFromCart.crust[i].sizes.length; j++) {
                if (itemFromCart.crust[i].sizes[j].sizeId == _.itemId) {
                  __["name"] = itemFromCart.crust[i].sizes[j].name;
                  __["selectedSizeId"] = _.itemId;
                }
                if (
                  itemFromCart.crust[i].crustId == itemFromCart.crustId &&
                  itemFromCart.crust[i].sizes[j].sizeId == _.itemId
                ) {
                  __["price"] = +itemFromCart.crust[i].sizes[j].price;

                  let sum = 0;
                  itemFromCart.addTopngs?.map((_) => {
                    sum =
                      sum +
                        +itemFromCart.toppingsMasterData.filter((__) => {
                          return __.toppingId == _;
                        })[0]?.price[__["selectedSizeId"]] || 0;
                  });

                  __["price"] = __["price"] + sum;

                  if (
                    itemFromCart.crust[i].sizes[j].price <
                    itemFromCart.defaultPrice
                  ) {
                    __["name"] = "";
                  }
                }
              }
            }
          }
          return __;
        }) || [];

      return res;
    },
  },
  mutations: {
    setProductObserver(state, payload) {
      state.productObserver = payload;
    },
    setProductCounterString(state, payload) {
      state.productCounterString = payload;
    },
    triggerClearFilterRef(state, payload) {
      state.clearFilterRef = Utility.randomnumber(100000000, 999999999);
    },
    resetOpenAllCards(state, payload) {
      state.openAllCards = payload;
    },
    updatePersonalizationItems(state, payload) {
      state.personalizationItems = payload;
    },
    updateProductItems(state, payload) {
      let { response, priceHashCode } = Utility.keybasedProductItem(payload);
      for (const [key, value] of Object.entries(response)) {
        state.productItems[key] = value;
        state.unfilteredProductItems[key] = value;
      }
      state.sizeCrustPriceHashMap = {
        ...state.sizeCrustPriceHashMap,
        ...priceHashCode,
      };
      state.allCategoryId.push(payload[0]);
    },
    filterProductItems(state, payload) {
      const { cartData, cartQuantitiy, filterProductItem } = payload;
      let res = JSON.parse(JSON.stringify(state.unfilteredProductItems));
      filterProductItem?.map((_) => {
        if (_.group == "FILTER") {
          if (_.type == "VEG") {
            for (const [key, value] of Object.entries(res)) {
              for (const [key2, value2] of Object.entries(value.data)) {
                if (
                  value2.productType != 0 &&
                  !Utility.returnsGetItemWQuantity(cartData, value2)
                ) {
                  delete res[key].data[key2];
                }
              }
            }
          } else if ((_.type = "NONVEG")) {
            for (const [key, value] of Object.entries(res)) {
              if (key == "Beverages" || key == "Dessert") continue;
              for (const [key2, value2] of Object.entries(value.data)) {
                if (
                  value2.productType != 1 &&
                  !Utility.returnsGetItemWQuantity(cartData, value2)
                ) {
                  delete res[key].data[key2];
                }
              }
            }
          }
        } else if (_.group == "SORT") {
          if (_.type == "HIGH_TO_LOW") {
            for (const [key, value] of Object.entries(res)) {
              value.data = Object.keys(value.data)
                .sort((a, b) => {
                  let aPrice =
                    value.data[a].defaultPrice ||
                    value.data[a].defaultCrustPrice;
                  let bPrice =
                    value.data[b].defaultPrice ||
                    value.data[b].defaultCrustPrice;
                  return bPrice - aPrice;
                })
                .reduce(function (result, key) {
                  result[key] = value.data[key];
                  return result;
                }, {});
            }
          } else if (_.type == "LOW_TO_HIGH") {
            for (const [key, value] of Object.entries(res)) {
              value.data = Object.keys(value.data)
                .sort((a, b) => {
                  let aPrice =
                    value.data[a].defaultPrice ||
                    value.data[a].defaultCrustPrice;
                  let bPrice =
                    value.data[b].defaultPrice ||
                    value.data[b].defaultCrustPrice;
                  return aPrice - bPrice;
                })
                .reduce(function (result, key) {
                  result[key] = value.data[key];
                  return result;
                }, {});
            }
          }
        }
      });
      state.productItems = res;
    },
  },
  actions: {
    personalizationApi({ commit }, payload) {
      ApiCalls.PERSONALIZATION_ITEMS_API({
        headers: Headers(),
        payload: { cartId: LocalStorageFunc.cartId() },
      })
        .then(function (response) {
          if (response?.data) {
            commit("updatePersonalizationItems", response.data);
          }
        })
        .catch(function (error) {
          commit("updatePersonalizationItems", {});
        });
    },
  },
};
