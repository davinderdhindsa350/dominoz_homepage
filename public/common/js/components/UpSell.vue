<template>
  <div
    class="main-upsell mt5"
    v-if="
      dataItem &&
      upsellItems(getCartItemLatest(dataItem.id)).length > 0 &&
      isAnyUpsellItemVisible(upsellItems(getCartItemLatest(dataItem.id)))
    "
  >
    <div class="upsell-body pt8 pb8 plr13">
      <div class="fs-14 bold pr12">Pizza Added! make it more delicious</div>
      <div class="upsell-btn-wrap mt5">
        <div
          v-for="item in upsellItems(getCartItemLatest(dataItem.id))"
          :key="item"
          class="ib"
        >
          <div
            v-if="isUpsellItemVisible(item)"
            @click="updateCart($event, getCartItemLatest(dataItem.id), item)"
            :class="
              item.price
                ? upsellOptionClasses[item.name]
                : 'upsell-btn disabled'
            "
          >
            <span> {{ item.name }}</span>
            <span
              v-if="
                item.price &&
                (item.toppingId
                  ? item.price
                  : item.price - dataItem.itemPrice) > 0
              "
              class="selected-upsell-item-no-display"
              ><span>&nbsp; | &nbsp;+ </span><span class="rs">Rs.</span>
              <span>{{
                item.toppingId ? item.price : item.price - dataItem.itemPrice
              }}</span></span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { keyMapsMutation, keyMapsGetter } from "../helpers/storeKeys";

export default {
  name: "UpSell",
  props: ["dataItem"],
  data() {
    return {
      productResult: {},
      upsellOptionClasses: {},
      appliedUpsellOptions: new Set(),
    };
  },
  mounted() {},
  methods: {
    isAlreadyApplied(upsellItem, itemFromCart) {
      if (upsellItem.toppingId) {
        return (
          itemFromCart.addTopngs.includes(upsellItem.toppingId) ||
          itemFromCart.addTopngs.includes(+upsellItem.toppingId)
        );
      } else if (upsellItem.selectedCrustId) {
        return +itemFromCart.crustId == +upsellItem.selectedCrustId;
      } else if (upsellItem.selectedSizeId) {
        return +itemFromCart.sizeId == +upsellItem.selectedSizeId;
      }
    },
    updateCart(e, dItem, upsellItem) {
      if (!upsellItem.price) return;

      let obj = {};

      if (this.appliedUpsellOptions.has(upsellItem.name)) {
        this.upsellOptionClasses[upsellItem.name] = "upsell-btn";
        this.appliedUpsellOptions.delete(upsellItem.name);
      } else {
        this.upsellOptionClasses[upsellItem.name] =
          "upsell-btn selected-upsell-item";
        this.appliedUpsellOptions.add(upsellItem.name);
        obj["shouldApply"] = true;
      }

      if (upsellItem.toppingId) {
        obj["addTopngs"] = [upsellItem.toppingId];
        obj["source"] = "TOPPING";
      } else if (upsellItem.selectedCrustId) {
        obj["crustId"] = upsellItem.selectedCrustId;
        obj["crustName"] = upsellItem.name;
        obj["source"] = "CRUST";
      } else if (upsellItem.selectedSizeId) {
        obj["sizeId"] = upsellItem.selectedSizeId;
        obj["sizeName"] = upsellItem.name;
        obj["source"] = "SIZE";
      }

      obj["itemPrice"] = upsellItem["price"];

      this.$store.commit(keyMapsMutation.UPDATE_LATEST_IN_CART, {
        ...dItem,
        ...obj,
      });
    },
    isUpsellItemVisible(item) {
      return (
        item.name &&
        (item.toppingId ? item.price : item.price - this.dataItem.itemPrice) >=
          0
      );
    },
  },
  computed: {
    isAnyUpsellItemVisible(_) {
      return (_) => {
        return _?.some((item) => {
          return this.isUpsellItemVisible(item);
        });
      };
    },
    upsellItems() {
      return (itemFromCart) => {
        let _ = this.$store.getters[keyMapsGetter.GET_UPSELL_ITEMS]({
          ...itemFromCart,
          toppingsMasterData: this.$store.state.Cart.toppingItems,
        });

        _.map((__) => {
          if (this.isAlreadyApplied(__, itemFromCart)) {
            this.appliedUpsellOptions.add(__.name);
            this.upsellOptionClasses[__.name] =
              "upsell-btn selected-upsell-item";
          } else {
            this.upsellOptionClasses[__.name] = "upsell-btn";
          }
        });

        return _;
      };
    },
    getCurrentToppingsTotalPrice() {
      return (item) => {
        let sum = 0;
        item.addTopngs?.map((_) => {
          sum =
            sum +
              +this.$store.state.Cart.toppingItems.filter((__) => {
                return __.toppingId == _;
              })[0]?.price[item.sizeId] || 0;
        });
        return sum;
      };
    },
    getCartItemLatest() {
      return (id) => {
        return this.$store.getters[keyMapsGetter.GET_CART_ITEM_LATEST](id);
      };
    },
  },
};
</script>

<style lang="scss" scoped></style>
