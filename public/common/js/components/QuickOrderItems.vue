<template>
  <div>
    <div
      class="quick-order-main pt20 pb16 d-flex justify-content-between align-items-start"
    >
      <div class="quick-order-item">
        <img class="flex-row" :src="imgBaseUrl + '/' + propsData?.image" />
        <span
          class="quick-order-item-tag"
        >
          <i
            :class="[
              { 'tag-veg': checkItemVegOrNonVeg(propsData.name) == 'Veg' },
              { 'tag-non-veg': checkItemVegOrNonVeg(propsData.name) == 'Non-Veg' },
            ]"
          >
          </i>
        </span>
      </div>
      <div class="w100">
        <div class="d-flex justify-content-between align-items-center">
          <span>{{ propsData?.name || "" }}</span>
          <span class="bold"
            ><i class="rs">Rs.</i
            >{{ propsData?.itemPrice * propsData?.quantity || 1 || "" }}</span
          >
        </div>
        <div class="d-flex justify-content-between align-items-center">
          <span><i class="rs">Rs.</i>{{ propsData?.itemPrice || "" }}</span>
          <span>
            <div class="line-btn dark sm add-minus-button fs-12 mt5">
              <i class="jfi-minus-line mr10 cursor-pointer" @click="removeItemToCart()"></i>
              {{ propsData?.quantity || 1 }}
              <i
                class="jfi-plus-line ml10 cursor-pointer"
                @click="addItemToCart()"
              ></i></div
          ></span>
        </div>
        <div class="ib">
          <span class="color-grey">{{ propsData.sizeName || "" }} </span>
           <span class="color-grey" v-if="propsData?.sizeName && propsData?.crustName">&nbsp;|&nbsp;</span>
            <span class="color-grey"> {{ propsData?.crustName || "" }}</span>
        </div>
      </div>
    </div>
    <div class="color-secondary d-flex border-bottom-light">
        <span class="mb16"  v-if="currentToppingsName?.length > 0">
            <span v-if="currentToppingsName?.length > 0">
                Added: {{currentToppingsName?.join() || ""}}.
            </span>
            <span v-if="currentReplaceTopping">
                Replaced: {{currentReplaceTopping || ""}}
            </span>
        </span>
    </div>
  </div>
</template>

<script>
import { imgBaseUrl } from "../config/apiEndpoints";
import { keyMapsMutation } from '../helpers/storeKeys';
import __ from "../helpers/utility"
export default {
  name: "QuickOrderItems",
  props: ["propsData"],
  data(props) {
    return {
        currentToppingsName: [],
        currentReplaceTopping: "",
        imgBaseUrl: imgBaseUrl,
    };
  },
  mounted() {
    this.toppingFormStore.length > 0 && this.namesOfDefaultToppings();
    console.log("=== ++ == ++ ==", this.propsData);
  },
  methods: {
    addItemToCart() {
      this.$store.commit(keyMapsMutation.ADD_PREVIOUS_ORDER_DATA, this.propsData);
    },
    removeItemToCart() {
      this.$store.commit(keyMapsMutation.REMOVE_PREVIOUS_ORDER_DATA, this.propsData);
    },
    namesOfDefaultToppings() { // get the toppings name through topping id
      this.currentToppingsName = this.toppingFormStore.filter((i) =>
        this.propsData.addTopngs?.includes(i.toppingId)
      ).map(j => j.name)
    },
    checkItemVegOrNonVeg(data){
        return data?.split(" ").includes("chicken") ? "Non-Veg": "Veg"
    },
  },
  computed: {
    toppingFormStore() {
      return this.$store.state.Cart.toppingItems;
    },
  },
};
</script>

<style scoped></style>
