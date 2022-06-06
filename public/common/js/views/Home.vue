<template>
  <div class="home-route">
    <div id="nav" class="hide">
      <router-link to="/">Home</router-link> -
      <router-link to="/cart">Cart (0)</router-link>
    </div>

    <div class="container-fluid">
      <div class="row">
        <!-- Start HOOK_TOP -->
        <top-bar> </top-bar>
        <location-message-banner></location-message-banner>
        <login-message-banner></login-message-banner>
        <order-type-tabs></order-type-tabs>
        <search-products></search-products>
        <re-order></re-order>
        <track-order></track-order>
        <personalized-offers></personalized-offers>
        <horizontal-banner-with-text></horizontal-banner-with-text>
        <order-filters
          :data="OrderFiltersData.data"
          :moduleProps="OrderFiltersData.moduleProps"
        ></order-filters>
        <!-- <vertical-product-scroll></vertical-product-scroll> -->
        <horizontal-product-scroll></horizontal-product-scroll>
        <!-- <vertical-product-scroll-new></vertical-product-scroll-new> -->
        <vertical-product-scroll
          :data-results="BestSellersData.data"
          :moduleProps="BestSellersData.moduleProps"
        ></vertical-product-scroll>
        <vertical-product-scroll
          :data-results="VegPizzaData.data"
          :moduleProps="VegPizzaData.moduleProps"
        ></vertical-product-scroll>
        <vertical-product-scroll
          :data-results="NonVegPizzaData.data"
          :moduleProps="NonVegPizzaData.moduleProps"
        ></vertical-product-scroll>
        <vertical-product-scroll
          :data-results="BeveragesData.data"
          :moduleProps="BeveragesData.moduleProps"
        ></vertical-product-scroll>
        <vertical-product-scroll
          :data-results="DessertsData.data"
          :moduleProps="DessertsData.moduleProps"
        ></vertical-product-scroll>
        <vertical-product-scroll
          :data-results="SidesData.data"
          :moduleProps="SidesData.moduleProps"
        ></vertical-product-scroll>
        <up-sell></up-sell>
        <your-pizza-profile></your-pizza-profile>
        <horizontal-banners
          :data="PaymentOffersData.data"
          :moduleProps="PaymentOffersData.moduleProps"
        ></horizontal-banners>
        <horizontal-banners
          :data="EDVOfferData.data"
          :moduleProps="EDVOfferData.moduleProps"
        ></horizontal-banners>
        <cheesy-rewards></cheesy-rewards>
        <!-- <profile-drawer></profile-drawer> -->

        <div
          style="height: 800px; border: 1px solid yellow; padding-top: 50px"
          class="col-sm-12"
        >
          This is Home page view
        </div>

        <bottom-bar> </bottom-bar>
        <!-- eof HOOK_TOP -->
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted } from "vue";
import { useStore } from "vuex";

import TopBar from "../components/TopBar.vue";
import LocationMessageBanner from "../components/LocationMessageBanner.vue";
import LoginMessageBanner from "../components/LoginMessageBanner.vue";
import BottomBar from "../components/BottomBar.vue";
import OrderTypeTabs from "../components/OrderTypeTabs.vue";
import SearchProducts from "../components/SearchProducts.vue";
import PersonalizedOffers from "../components/PersonalizedOffers.vue";
import TrackOrder from "../components/TrackOrder.vue";
import ReOrder from "../components/ReOrder.vue";
import CheesyRewards from "../components/CheesyRewards.vue";
import OrderFilters from "../components/OrderFilters.vue";
import YourPizzaProfile from "../components/YourPizzaProfile.vue";
import HorizontalBannerWithText from "../components/HorizontalBannerWithText.vue";
import HorizontalBanners from "../components/HorizontalBanners.vue";
import PaymentOffersData from "../assets/FallbackData/PaymentOffersData";
import EDVOfferData from "../assets/FallbackData/EDVOfferData";
import OrderFiltersData from "../assets/FallbackData/OrderFiltersData";
import BestSellersData from "../assets/FallbackData/BestSellersData";
import VegPizzaData from "../assets/FallbackData/VegPizzaData";
import NonVegPizzaData from "../assets/FallbackData/NonVegPizzaData";
import BeveragesData from "../assets/FallbackData/BeveragesData";
import DessertsData from "../assets/FallbackData/DessertsData";
import SidesData from "../assets/FallbackData/SidesData";
import UpSell from "../components/UpSell.vue";
import HorizontalProductScroll from "../components/HorizontalProductScroll.vue";

export default {
  components: {
    TopBar,
    LocationMessageBanner,
    LoginMessageBanner,
    BottomBar,
    OrderTypeTabs,
    SearchProducts,
    PersonalizedOffers,
    TrackOrder,
    CheesyRewards,
    ReOrder,
    CheesyRewards,
    OrderFilters,
    YourPizzaProfile,
    HorizontalBannerWithText,
    HorizontalBanners,
    UpSell,
    HorizontalProductScroll,
  },
  name: "Home",
  setup() {
    const store = useStore();
    onMounted(() => {
      // store.commit("Cart/updateCartFromLocalStorage");
    });

    return {
      PaymentOffersData,
      EDVOfferData,
      OrderFiltersData,
      BestSellersData,
      VegPizzaData,
      NonVegPizzaData,
      BeveragesData,
      DessertsData,
      SidesData,
    };
  },
};
</script>

<style>
.home-route {
  /* border: 2px solid yellow; */
}
</style>
