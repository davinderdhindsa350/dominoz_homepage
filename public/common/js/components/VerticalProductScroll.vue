<template>
  <div>
    <p :id="'element_catg_' + getSingleProductItemCategory?.categoryId"></p>
    <div
      v-if="
        moduleProps &&
        isCorrectModuleType &&
        Object.keys(getSingleProductItemCategory?.data || {})?.length > 0
      "
      class="cards-wrap mb16 clearfix options-api"
      :style="{ minHeight: minHeightCal + 'px' }"
    >
      <Observer @intersect="itisvisible"/>
      <h3 class="card-catg-name">
        {{ getSingleProductItemCategory?.categoryName }}
      </h3>

      <div class="cards-holder" v-if="showList || openAllCards">
        <product-card
          v-for="(value, key, index) in getSingleProductItemCategory?.data"
          :key="key"
          :data-cardRounded="vpsProps.cardRounded"
          :data-cardSearchable="vpsProps.cardSearchable"
          :data-result="value"
          :data-moduleProps="moduleProps"
          :data-counterText="
            'Showing ' +
            categoryName +
            ' | ' +
            (index + 1) +
            '/' +
            Object.keys(getSingleProductItemCategory?.data)?.length
          "
          :data-categoryId="getSingleProductItemCategory?.categoryId || '-1'"
          :data-type="getSingleProductItemCategory?.categoryName"
        ></product-card>
      </div>
    </div>
  </div>
</template>

<script>
import {
  keyMapsAction,
  keyMapsMutation,
  keyMapsGetter,
} from "../helpers/storeKeys";
import ProductCard from "./ProductCard.vue";
import Observer from "../assets/Observer.vue";

export default {
  name: "VerticalProductScroll",
  props: ["dataResults", "moduleProps"],
  components: {
    ProductCard,
    Observer,
  },
  data() {
    return {
      isCorrectModuleType: false,
      vpsProps: {
        cardRounded: false,
        cardSearchable: false,
      },
      categoryName: "",
      defaultModuleProperties: {
        cardType: "video/static",
        itemsInScreen: "1.75",
        aspectRatio: "1",
        title: "Best Sellers",
        type: "VERTICAL_PRODUCT_SCROLL",
      },
      showList: false
    };
  },
  mounted() {
    if (JSON.parse(this.moduleProps)) {
      this.defaultModuleProperties = JSON.parse(this.moduleProps);
    }
    this.categoryName = JSON.parse(this.dataResults)?.[0].categoryName;
    this.$store.commit(
      keyMapsMutation.UPDATE_PRODUCT_ITEMS,
      (this.dataResults && JSON.parse(this.dataResults)) || []
    );
    this.checkModuleTypeToRender();
    this.$store.commit(
      keyMapsMutation.UPDATE_CART_FROM_LOCAL_STORAGE,
      this.$store.state.ProductItems.productItems
    );
    this.$store.commit(
      keyMapsMutation.LOAD_UNIQUE_CART_ITEMS,
      this.$store.state.Cart.cartItems
    );
  },
  methods: {
    checkModuleTypeToRender() {
      if (this.defaultModuleProperties?.type === "VERTICAL_PRODUCT_SCROLL") {
        this.isCorrectModuleType = true;
      }
    },
    itisvisible() {
      this.showList = true;
    },
  },
  computed: {
    getSingleProductItemCategory() {
      return this.$store.getters[
        keyMapsGetter.GET_SINGLE_PRODUCT_ITEM_CATEGORY
      ](this.categoryName);
    },
    openAllCards() {
      return this.$store.state.ProductItems.openAllCards;
    },
    minHeightCal() {
      let count = Object.keys(this.getSingleProductItemCategory?.data || {})?.length;
      // console.log('count all...', this.getSingleProductItemCategory?.categoryName, count, 366*count+37);
      return 366*count+37;
    },
  },
};
</script>
