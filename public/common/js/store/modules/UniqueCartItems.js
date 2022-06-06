import LocalStorageFunc from "../../helpers/localStorageFunc";

export default {
  namespaced: true,
  state: {
    uniqueProductItems: [],
  },
  getters: {
    getProductQuantity: (state) => (product) => {
      const productItem = state.uniqueProductItems.find(
        (i) => i.id === product.id
      );

      if (productItem) {
        return productItem.quantity;
      } else {
        return 0;
      }
    },
  },
  mutations: {
    addToUniqueCartItems(state, product) {
      let productItems = state.uniqueProductItems.find(
        (i) => i.id === product.id
      );
      if (productItems) {
        productItems.quantity++;
      } else {
        state.uniqueProductItems.push({ ...product, quantity: 1 });
      }
    },
    removeFromUniqueCartItems(state, product) {
      let productItems = state.uniqueProductItems.find(
        (i) => i.id === product.id
      );
      if (productItems) {
        if (productItems.quantity > 1) {
          productItems.quantity--;
        } else {
          state.uniqueProductItems = state.uniqueProductItems.filter(
            (i) => i.id !== product.id
          );
        }
      }
    },
    updateUniqueCartItems(state, product) {
      let productItems = state.uniqueProductItems.find(
        (i) => i.id === product.id
      );
      if (productItems) {
        productItems.quantity++;
      } else {
        state.uniqueProductItems.push({ ...product, quantity: 1 });
      }
    },
    loadUniqueCartItems(state, payload) {
      let _ = {};
      payload.map((item) => {
        if (_[item.id]) {
          _[item.id] = +_[item.id] + +item.quantity;
        } else {
          _[item.id] = item.quantity;
        }
      });
      let __ = [];
      Object.keys(_).forEach(function (key) {
        __.push({ id: key, quantity: _[key] });
      });
      state.uniqueProductItems = __;
    },
  },
  actions: {},
};
