import Vuex from "vuex";
import { createStore } from "vuex";

import Location from "./modules/Location";
import Auth from "./modules/Auth";
import User from "./modules/User";
import Offers from "./modules/Offers";
import TrackOrder from "./modules/TrackOrder";
import ReOrder from "./modules/ReOrder";
import YourPizzaProfile from "./modules/YourPizzaProfile";
import HorizontalBannerWithText from "./modules/HorizontalBannerWithText";
import ProductItems from "./modules/ProductItems";
import Cart from "./modules/Cart";
import UniqueCartItems from "./modules/UniqueCartItems";
import ProfileDrawer from "./modules/ProfileDrawer";
import Config from "./modules/Config";
import PaymentOffers from "./modules/PaymentOffers";


export default createStore({
  state: {
    loading: false,
  },
  getters: {
    loading: (state) => state.loading,
  },
  mutations: {
    updateLoadingState(state, payload) {
      state.loading = payload;
    },
  },
  actions: {},
  modules: {
    Location: Location,
    Auth: Auth,
    User: User,
    Offers: Offers,
    TrackOrder: TrackOrder,
    ReOrder: ReOrder,
    YourPizzaProfile: YourPizzaProfile,
    HorizontalBannerWithText: HorizontalBannerWithText,
    ProductItems: ProductItems,
    ProfileDrawer: ProfileDrawer,
    Cart: Cart,
    UniqueCartItems: UniqueCartItems,
    Config: Config,
    PaymentOffers: PaymentOffers
  },
});
