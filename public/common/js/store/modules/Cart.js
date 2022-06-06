import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";
import LocalStorageFunc from "../../helpers/localStorageFunc";
import Utility from "../../helpers/utility";
import localStorageFunc from "../../helpers/localStorageFunc";
import { keyMapsAction, keyMapsMutation } from "../../helpers/storeKeys";

export default {
  namespaced: true,
  state: {
    cartItems: [],
    crossSellWithProduct: "",
    toppingItems: [],
    // selectedToppings: [],
    isCustomizedOptionEnable: false,
    itemPriceToppingArray: [],
    selectedAndReplacedToppings: {
      addedToppings: {},
      replacedToppings: {},
    },
    replaceDisable: false,
    currentSelToppObj: [],
    isAddedItemIsNew: true,
  },
  getters: {
    getItemQuantity: (state) => (product) => {
      return state.cartItems
        .filter((_) => {
          return _.id === product.id;
        })
        .reduce((acc, cartItem) => {
          return cartItem.quantity + acc;
        }, 0);
    },
    getCartItems: (state) => {
      return state.cartItems;
    },
    getCartItemLatest: (state) => (productid) => {
      for (let i = state.cartItems.length - 1; i >= 0; i--) {
        if (state.cartItems[i].id == productid) {
          return state.cartItems[i];
        }
      }
      return;
    },
    cartTotal: (state) => {
      return state.cartItems
        .reduce((acc, cartItem) => {
          return cartItem.quantity * (cartItem.itemPrice || 0) + acc;
        }, 0)
        .toFixed(2);
    },
    cartQuantity: (state) => {
      return state.cartItems.reduce((acc, cartItem) => {
        return cartItem.quantity + acc;
      }, 0);
    },
  },
  mutations: {
    addToCart(state, product) {
      let item = state.cartItems.find((i) => {
        if (
          Number(i.id) === Number(product.id) &&
          i.crustId == product.crustId &&
          i.sizeId == product.sizeId &&
          i.addTopngs
            .map((o) => Number(o))
            .sort()
            .toString() ==
            product.addTopngs
              .map((o) => Number(o))
              .sort()
              .toString() //  compare of addTopngs with prev toppings
        ) {
          return i;
        }
      });

      if (item) {
        item.quantity++;
      } else {
        state.cartItems.push({ ...product, quantity: 1 });
      }

      LocalStorageFunc.updateCart(state.cartItems);
    },
    removeFromCart(state, product) {
      let item = state.cartItems.find(
        (i) => Number(i.id) === Number(product.id)
      );

      if (item) {
        if (item.quantity > 1) {
          item.quantity--;
        } else {
          state.cartItems = state.cartItems.filter(
            (i) => Number(i.id) !== Number(product.id)
          );
        }
      }

      LocalStorageFunc.updateCart(state.cartItems);
    },
    updateLatestInCart(state, product) {
      for (let index = state.cartItems.length - 1; index >= 0; index--) {
        if (state.cartItems[index].id == product.id) {
          let currentToppingsPrice =
            product["source"] != "TOPPING"
              ? Utility.getCurrentToppingsTotalPrice(
                  state.toppingItems,
                  state.cartItems[index]
                )
              : "";

          if (product["shouldApply"]) {
            if (product["source"] == "SIZE") {
              if (!state.cartItems[index].defaultSizeName) {
                state.cartItems[index].defaultSizeName =
                  state.cartItems[index].sizeName;
              }
              state.cartItems[index].sizeId = product.sizeId;
              state.cartItems[index].sizeName = product.sizeName;
              state.cartItems[index].itemPrice = product["itemPrice"];
            } else if (product["source"] == "CRUST") {
              if (!state.cartItems[index].defaultCrustName) {
                state.cartItems[index].defaultCrustName =
                  state.cartItems[index].crustName;
              }
              state.cartItems[index].crustId = product.crustId;
              state.cartItems[index].crustName = product.crustName;
              state.cartItems[index].itemPrice = product["itemPrice"];
            } else if (product["source"] == "TOPPING") {
              state.cartItems[index].addTopngs.push(...product.addTopngs);
              state.cartItems[index].itemPrice =
                state.cartItems[index].itemPrice + +product["itemPrice"];
            }
          } else {
            if (product["source"] == "SIZE") {
              state.cartItems[index].sizeId = product.selectedSizeId;
              state.cartItems[index].sizeName =
                state.cartItems[index].defaultSizeName;

              currentToppingsPrice = Utility.getCurrentToppingsTotalPrice(
                state.toppingItems,
                state.cartItems[index]
              );

              state.cartItems[index].itemPrice =
                (product.crust
                  .filter((_) => {
                    return _.crustId == state.cartItems[index].crustId;
                  })[0]
                  ?.sizes.filter((_) => {
                    return _.sizeId == product.selectedSizeId;
                  })[0]?.price || 0) + currentToppingsPrice;
            } else if (product["source"] == "CRUST") {
              state.cartItems[index].crustId = product.selectedCrustId;
              state.cartItems[index].crustName =
                state.cartItems[index].defaultCrustName;

              state.cartItems[index].itemPrice =
                (product.crust
                  .filter((_) => {
                    return _.crustId == product.selectedCrustId;
                  })[0]
                  ?.sizes.filter((_) => {
                    return _.sizeId == state.cartItems[index].sizeId;
                  })[0]?.price || 0) + currentToppingsPrice;
            } else if (product["source"] == "TOPPING") {
              state.cartItems[index].addTopngs = state.cartItems[
                index
              ].addTopngs.filter((value) => {
                return value != product.addTopngs[0];
              });
              state.cartItems[index].itemPrice =
                state.cartItems[index].itemPrice - +product["itemPrice"];
            }
          }
        }
      }
      LocalStorageFunc.updateCart(state.cartItems);
    },
    newFeatureRemoveCart(state, payload) {
      // this the new feature of not disable button
      // if (Number(payload.id) === Number(state.cartItems[state.cartItems.length - 1]?.id)) {
      //   let item = state.cartItems[state.cartItems.length - 1]?.quantity;
      //   if (item > 1 && item !== undefined) {
      //     state.cartItems[state.cartItems.length - 1].quantity--;
      //   } else {
      //     state.cartItems.pop();
      //   }
      // } else {
      //   let data = state.cartItems.find((i) => Number(i.id) === Number(payload.id));
      //   if (data) {
      //     if (data.quantity > 1) {
      //       data.quantity--;
      //     } else {
      //       state.cartItems = state.cartItems.filter(
      //         (i) => Number(i.id) !== Number(payload.id)
      //       );
      //     }
      //   }
      // }
      let obj = state.cartItems.filter(
        (i) => Number(i.id) == Number(payload.id)
      )[
        state.cartItems.filter((i) => Number(i.id) == Number(payload.id))
          .length - 1
      ];
      if (obj.quantity > 1) {
        obj.quantity--;
      } else {
        state.cartItems.splice(state.cartItems.indexOf(obj), 1);
      }
      LocalStorageFunc.updateCart(state.cartItems);
    },
    updateCart(state, product) {
      if (!product[1]) {
        let udpdateItem = state.cartItems.find((i) => {
          if (
            Number(i.id) === Number(product[0].id) &&
            i.crustId === product[0].crustId &&
            i.sizeId === product[0].sizeId &&
            i.addTopngs
              .map((o) => Number(o))
              .sort()
              .toString() ==
              product[0].addTopngs
                .map((o) => Number(o))
                .sort()
                .toString() //  compare of addTopngs with prev toppings
          ) {
            return i;
          }
        });
        if (udpdateItem) {
          udpdateItem.quantity++;
        } else {
          state.cartItems.push({ ...product[0], quantity: 1 });
        }
      } else {
        let obj = state.cartItems.filter(
          (i) => Number(i.id) == Number(product[0].id)
        )[
          state.cartItems.filter((i) => Number(i.id) == Number(product[0].id))
            .length - 1
        ];
        if (obj) {
          state.cartItems[state.cartItems.indexOf(obj)] = product[0];
        }
        // obj.addTopngs = product[0].addTopngs
        // obj.repTopngs = product[0].repTopngs
        // obj.sizeName = product[0].sizeName
        // obj.itemPrice = product[0].itemPrice
        // obj.isUserCustomizable = product[0].isUserCustomizable
        // obj.isReplaceable = product[0].isReplaceable
        // obj.isUserCustomizable = product[0].isUserCustomizable
        // obj.isCustomizable = product[0].isCustomizable
        // obj.crustId = product[0].crustId
        // obj.crustName = product[0].crustName
        // obj.sizeId = product[0].sizeId

        // console.log("4444444444444", state.cartItems.filter(i => Number(i.id) == Number(product[0].id)) );
        // console.log("4444444444444", [state.cartItems.filter(i => Number(i.id) == Number(product[0].id)).length - 1] );
        // console.log("4444444444444", state.cartItems.filter(i => Number(i.id) == Number(product[0].id)) );
        // console.log("4444444444444", state.cartItems.filter(i => Number(i.id) == Number(product[0].id))[state.cartItems.filter(i => Number(i.id) == Number(product[0].id)).length - 1]);
        // state.cartItems[state.cartItems.length - 1] = product[0];

        // state.cartItems.push({ ...product[0], quantity: 1 })
      }
      // } else {
      //   let setData = [...state.cartItems];
      //   delete setData[setData.length - 1];
      //   state.cartItems[state.cartItems.length - 1] = product[0];
      // }

      // state.selectedToppings = product[0].addTopngs;
      LocalStorageFunc.updateCart(state.cartItems);
    },
    updateCartFromLocalStorage(state, payload) {
      let cart = Utility.addKeyToCartWhenReloadsData(LocalStorageFunc.cart());
      if (cart?.length > 0 && state.toppingItems.length > 0) {
        let tempCartItems = cart.map(function (item, index) {
          //add missing keys when cartItems is modified by old pwa
          let _ = {};
          for (const [key, value] of Object.entries(payload)) {
            if (value?.data[item?.product?.code]) {
              _ = value?.data[item.product.code];
              break;
            }
          }

          if (Object.keys(_).length == 0) {
            return _;
          }

          _?.crust?.map((__) => {
            if (__.crustId == item.crustId) {
              _.crustName = __.name;
              __.sizes.map((___) => {
                if (___.sizeId == item.sizeId) {
                  _.sizeName = ___.name;
                  _.itemPrice = ___.price;
                }
              });
            }
          });

          item.id = _.id;
          item.itemPrice = _.itemPrice;

          _ = {
            ..._,
            ...item,
          };

          //handling cart when location is updated and updated location has different item id of same product
          _ = {
            ..._,
            product: {
              id: _.id,
              code: _.code,
            },
          };

          if (_.addTopngs?.length > 0) {
            _.addTopngs.map((toppping) => {
              _.itemPrice =
                +_.itemPrice +
                Utility.getToppingPriceForSize(
                  state.toppingItems,
                  toppping,
                  _.sizeId
                );
            });
          }
          if (_.repTopngs?.length > 0) {
            _.addTopngs.map((toppping) => {
              _.itemPrice =
                +_.itemPrice +
                Utility.getToppingPriceForSize(
                  state.toppingItems,
                  toppping,
                  _.sizeId
                );
            });
          }
          if (_.remTopngs?.length > 0) {
            _.addTopngs.map((toppping) => {
              _.itemPrice =
                +_.itemPrice -
                Utility.getToppingPriceForSize(
                  state.toppingItems,
                  toppping,
                  _.sizeId
                );
            });
          }

          return _;
        });

        //handling cart when location is updated and updated location has no product of previous code and id
        state.cartItems = tempCartItems.filter(function (item, index) {
          return item?.product?.id;
        });

        LocalStorageFunc.updateCart(state.cartItems);
      }
    },
    updateCrossSellWithProduct(state, payload) {
      if (!state.crossSellWithProduct) {
        state.crossSellWithProduct = payload;
      }
    },
    updatePizzaToppings(state, payload) {
      state.toppingItems = payload;
    },
    updateSelectedToppings(state, payload) {
      if (payload) {
        const { addedTopping = {}, replacedTopping = {} } = payload[0] || {};
        if (payload[1]) {
          state.selectedAndReplacedToppings.addedToppings[payload[1]] =
            addedTopping[payload[1]] || [];
          state.selectedAndReplacedToppings.replacedToppings[payload[1]] =
            replacedTopping[payload[1]] || [];
          LocalStorageFunc.setSelectedAndReplaceToppingsLocalStorage(
            payload[0]
          );
        }
      }
    },
    updatePreviousSelectedToppings(state, payload) {
      if (payload) {
        state.selectedAndReplacedToppings.addedToppings[payload[1]] =
          payload[0];
        LocalStorageFunc.setSelectedAndReplaceToppingsLocalStorage(payload[0]);
      }
    },
    updateIsCustomize(state, payload) {
      state.isCustomizedOptionEnable = payload;
      localStorageFunc.setCustomizedOptionKeyLocalStorage(payload || false);
    },
    updateCartItemPriceArray(state, payload) {
      state.itemPriceToppingArray = payload || [];
      LocalStorageFunc.setToppingArrayPriceKeyLocalStorage(payload || []);
    },
    updateDisableReplaceName(state, payload) {
      state.replaceDisable = payload;
      LocalStorageFunc.setReplaceDisableLocalStorage(payload);
    },
    updateCurrentSelToppingObj(state, payload) {
      state.currentSelToppObj = payload || [];
      LocalStorageFunc.setCurrentSelectedToppings(payload || []);
    },
    removeCurrentSelToppingObj(state, payload) {
      const { addTopngs } = payload;
      if (state.currentSelToppObj && addTopngs) {
        addTopngs.forEach((i) => {
          state.currentSelToppObj.splice(
            state.currentSelToppObj.findIndex(
              (a) => Number(a.toppingId) == Number(i)
            ),
            1
          );
        });
      }
      LocalStorageFunc.setCurrentSelectedToppings(
        state.currentSelToppObj || []
      );
    },
    resetCurrentSelToppingObj(state) {
      state.currentSelToppObj = [];
      LocalStorageFunc.setCurrentSelectedToppings([]);
    },
    previousOrderAddToCart(state, payload) {
      if (payload) {
        payload["product"] = { id: payload?.id, code: payload?.code };
        let item = state.cartItems.find((i) => {
          if (Number(i.id) === Number(payload.id)) {
            return i;
          }
        });
        if (item) {
          item.quantity = payload.quantity;
        } else {
          state.cartItems.push(payload);
        }
        // state.cartItems.push(payload)
        LocalStorageFunc.updateCart(state.cartItems);
      }
    },
    resetAllSelectedToppings(state, payload) {
      let item = state.cartItems.find((i) => {
        if (Number(i.id) === Number(payload.id)) {
          return i;
        }
      });
      if (item) {
        item.addTopngs = [];
      }
    },
    isItemNewOrPreviousOnCheck(state, payload) {
      state.isAddedItemIsNew = payload;
    },
  },
  actions: {
    pizzaToppingApi({ commit, rootState }, payload) {
      ApiCalls.PIZZA_TOPPING_API({
        headers: Headers(),
        payload: LocalStorageFunc.storeId(),
      })
        .then(function (response) {
          if (response?.data?.data) {
            commit("updatePizzaToppings", response.data.data);
            commit(
              "updateCartFromLocalStorage",
              rootState.ProductItems.productItems
            );
            commit(
              keyMapsMutation.LOAD_UNIQUE_CART_ITEMS,
              rootState.Cart.cartItems,
              { root: true }
            );
          }
        })
        .catch(function (error) {
          commit("updatePizzaToppings", []);
        });
    },
  },
};
