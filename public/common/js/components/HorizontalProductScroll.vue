<template>
  <div
    class="horizontal-product-main mb32"
    v-if="getRecommendedProducts?.length > 0"
  >
    <h3 class="card-catg-name">Recommended</h3>

    <Carousel :itemsToShow="itemsToShow" :transition="500">
      <Slide v-for="(result, i) in getRecommendedProducts" :key="i">
        <product-card
          :data-cardSource="cardSource"
          :data-cardRounded="vpsProps.cardRounded"
          :data-cardSearchable="vpsProps.cardSearchable"
          :data-result="result"
          :data-counterText="'undefined'"
          :marginGap="vpsProps.marginGap"
          :data-moduleProps="moduleProps"
          :data-type="'Recommended'"
          :data-index="i"
          :key="result.code"
        ></product-card>
      </Slide>
      <template #addons> </template>
    </Carousel>
  </div>
</template>

<script>
import ProductCard from "./ProductCard.vue";
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide } from "vue3-carousel";
import { mapGetters } from "vuex";
import {
  keyMapsAction,
  keyMapsGetter,
  keyMapsMutation,
} from "../helpers/storeKeys";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

export default {
  name: "HorizontalProductScroll",
  components: {
    ProductCard,
    Carousel,
    Slide,
  },
  props: ["moduleProps"],

  mounted() {
    this.modulePropsData =
      typeof this.moduleProps != null &&
      typeof this.moduleProps != "null" &&
      typeof this.moduleProps != "undefined"
        ? JSON.parse(this.moduleProps)
        : {};
    this.itemsToShow = this.modulePropsData?.itemsInScreen || 1.08;
  },
  updated() {
    if(!this.isUpdated && this.getRecommendedProducts?.length > 0) {
      this.eventFunction(false, { "positionWithinSection": "1", "category": this.getRecommendedProducts[0].name });
      this.isUpdated = true;
    }
  },
  data() {
    return {
      isCorrectModuleType: false,
      vpsProps: {
        cardRounded: true,
        cardSearchable: false,
        marginGap: true,
      },
      modulePropsData: {},
      itemsToShow: 1,
      // moduleProps: {
      //   title: "",
      //   type: "",
      //   cardType: "",
      //   aspectRatio: "",
      //   itemsInScreen: "",
      // },
      cardSource: "HPS",
      isUpdated: false,
    };
  },
  methods: {
    getEventObj(item) {
      let returnObj = {
        "rating": item
      };
      if(!this.recommendateItem(item)?.length > 0) returnObj["sellerType"] = "bestseller";
      if(this.recommendateItem(item)?.length > 0) returnObj["popularityType"] = "popular in " + item.categoryName;
      return returnObj;
    },
    eventFunction(isClicked=false, eObj={}) {
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      const isLogged = this.isLoggedIn ? "yes" : "no";
      let preEventObj = { ...eObj };
      const eventObj = Utility.amplitudeEventObj(userAction, "recommended", "8", preEventObj);
      setEvents("amplitude", eventType, eventObj);
    },
    recommendateItem() {
      return this.$store.state.ProductItems.recommendedItemsIds.includes(this.result.code) && (this.dataType !== "Recommended") ? "BESTSELLER" : ""
    },
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
    //     recommendedProducts() {
    //   const str = this.product
    //   // let newArr = str.map((i) => i[0].data);
    //   let finalRecommendedPro = str
    //     .map((i) => [...new Set(this.checkTheArray(i))])
    //     .reduce((r, e) => (r.push(...e), r), []);
    //   return finalRecommendedPro;
    // }
    getRecommendedProducts() {
      return this.$store.getters[keyMapsGetter.GET_RECOMMENDED_PRODUCTS]();
    },
    allItemData() {
      return this.$store.getters[keyMapsGetter.GET_PRODUCT_ITEMS_IN_ONE_ARRAY];
    },
  },
};
</script>

<style lang="scss" scoped></style>
