import apiCalls from "../../config/apiCalls";
import Headers from "../../config/headers";
import LocalStorageFunc from "../../helpers/localStorageFunc";

export default {
  namespaced: true,
  state: {
    reOrderData: [
      {
        id: 1,
        pizza: "The Farmhouse | Large | New hand tossed",
        added: "Panner, Extra cheese, Mushroom, onion",
      },
      {
        id: 2,
        pizza: "The Unthinkable Pizza | Large | New hand tossed",
        added: "Panner, Extra cheese, Mushroom, onion",
      },
      {
        id: 3,
        pizza: "The Farmhouse | Large | New hand tossed",
        added: "Panner, Extra cheese, Mushroom, onion",
      },
      {
        id: 4,
        pizza: "The Farmhouse | Large | New hand tossed",
        added:
          "Panner, Extra cheese, Mushroom, onion, garlic, olive, pine",
      },
      {
        id: 5,
        pizza: "The Farmhouse | Large | New hand tossed",
        added: "Panner, Extra cheese, Mushroom, onion",
      },
    ],
    previousOrderData: [],
    editPreviousOrderData: {
      netPrice:"",
      totalItems:"",
      orderTime:"",
      currentCartData: []
    }
  },
  getters: {
    currentCartTotal: (state) => {
      return state.editPreviousOrderData.currentCartData
        .reduce((acc, cartItem) => {
          return cartItem.quantity * cartItem.itemPrice + acc;
        }, 0)
        .toFixed(2);
    },
  },
  mutations: {
    updateReOrderData(state, payload) {
      state.previousOrderData = payload;
    },
    addPreviousOrderData(state, payload){
      // const {} = payload;
      // debugger
      let item = state.editPreviousOrderData.currentCartData.find((i) => {
        if (
          i.id === payload.id
          // i.crustId === payload.crustId &&
          // i.sizeId === payload.sizeId &&
          // i.addTopngs.sort().toString() == payload.addTopngs.sort().toString() //  compare of addTopngs with prev toppings
        ) {
          return i;
        }
      });

      if (item) {
        item.quantity++;
      } else {
        state.editPreviousOrderData.currentCartData.push({ ...payload, quantity: 1 });
        state.editPreviousOrderData.currentCartData = Array.from(new Set(state.editPreviousOrderData.currentCartData.map(JSON.stringify))).map(JSON.parse)
      }
    },
    removePreviousOrderData(state, payload){
      let item = state.editPreviousOrderData.currentCartData.find((i) => i.id === payload.id);

      if (item) {
        if (item.quantity > 1) {
          item.quantity--;
        } else {
          state.editPreviousOrderData.currentCartData = state.editPreviousOrderData.currentCartData.filter((i) => i.id !== payload.id);
        }
      }
    },
    resetCartItemData(state){
      state.editPreviousOrderData.currentCartData = [];
    },
    updateCurrentItemDetails(state, payload){
      state.editPreviousOrderData.netPrice = payload.netPrice;
      state.editPreviousOrderData.totalItems = payload.totalItems;
      state.editPreviousOrderData.orderTime = payload.orderTime;
    },
    resetCurrentItemDetails(state){
      state.editPreviousOrderData.netPrice = ""
      state.editPreviousOrderData.totalItems = ""
      state.editPreviousOrderData.orderTime = ""
    },
    updateAndCheckCurrent(state, payload){
      state.editPreviousOrderData.currentCartData = payload;
    },
  },
  actions: {
    getReOrderData({ commit }, payload) {
      apiCalls.PREVIOUS_ORDER_API({
        headers: Headers(),
        payload: LocalStorageFunc.storeId(),
      })
        .then(function (response) {
          if (response?.data?.orders) {
            // console.log("asdfasdfsadf", response?.data?.orders)

            commit("updateReOrderData", response?.data?.orders);
            // commit("updatePizzaToppings", response.data.data);
          }
        })
        .catch(function (error) {
          // commit("updateReOrderData", response?.data);
          /* console.log("There is some network error", error); */
        });
    },
  },
};
