import Headers from "../../config/headers";
import ApiCalls from "../../config/apiCalls";
import LocalStorageFunc from "../../helpers/localStorageFunc";

export default {
  namespaced: true,
  state: {
    leftMenuLinks: [],
  },
  getters: {},
  mutations: {
    updateLeftMenuLinks(state, payload) {
      state.leftMenuLinks = payload;
    },
  },
  actions: {
    leftMenuApi({ commit, dispatch }, payload) {
      ApiCalls.LEFT_MENU_API({
        headers: Headers(),
        payload: LocalStorageFunc.storeId(),
      })
        .then(function (response) {
          if(response?.data?.data){
            commit("updateLeftMenuLinks", response.data.data);
          }
        })
        .catch(function (error) {

        });
    },
  },
};
