<template>
  <div class="pr0" v-if="getCrossSellProducts?.length > 0">
    <div
      class="cross-sell-main mt5 mb5 pr0"
      :class="{ pr16: getCrossSellProducts?.length == 2 }"
    >
      <div class="fs-14 bold tl mb8 pl16">People also bought</div>
      <Carousel :items-to-show="1.5" :transition="500">
        <Slide v-for="(item, index) in getCrossSellProducts" :key="index">
          <div class="cross-item flex-column justify-content-between">
            <div class="cross-item-img"> 
              <!-- <img
                class="card-img"
                :src="imgBaseUrl + '/' + item.image"
                alt=""
              /> -->

              <template v-if="item?.images?.length > 0">
                <template v-for="(img, index) in item?.images">
                  <template v-if="img.mediaSize == 'CrosssellV1'">
                    <v-lazy-image 
                      class="card-img img-product-crossell" 
                      alt="crossell image"
                      :src="prepareImgPath(img.mediaPath)"
                      :src-placeholder="staticImgBaseUrl + '/images/global/default-product-img-lg.png'" 
                    />
                  </template>
                </template>
              </template>
              <template v-else>
                <v-lazy-image 
                  class="card-img backup-img" 
                  alt="default image"
                  :src="imgBaseUrl + '/' + item.image" 
                  :src-placeholder="staticImgBaseUrl + '/images/global/default-product-img-lg.png'" 
                />
              </template>

              <span class="cross-item-tag"
                ><i
                  class="mr8"
                  :class="[
                    {
                      'tag-veg': item.productType === 0,
                    },
                    {
                      'tag-non-veg': item.productType === 1,
                    },
                  ]"
                ></i
              ></span>
            </div>
            <div class="cross-item-body pt12 pl12 pr12 pb12">
              <div class="fs-12 bold tl mb9 multi-line-ellipses-v2 mh-28">{{ item.name }}</div>
              <div class="flex-row justify-content-between align-items-center">
                <div class="price">
                  <span class="rs">Rs.</span>
                  <span>{{ item.defaultPrice }}</span>
                </div>

                <div class="main-cta">
                  <div v-if="quantity(item) > 0" class="cta-edit">
                    <span
                      class="anim-ico-minus item-status-anim"
                      @click="removeFromCart($event, item)"
                    ></span>

                    <span class="fs-14">{{ quantity(item) }}</span>

                    <span
                      class="anim-ico-plus tr item-status-anim"
                      @click="addToCart($event, item)"
                    ></span>
                  </div>

                  <button
                    v-if="quantity(item) === 0"
                    class="btn bold hand cta-add"
                    @click.prevent="addToCart($event, item)"
                  >
                    Add <span class="ico-plus">&#43;</span>
                  </button>
                </div>

                <!-- <div class="btn cross-plus-btn" @click="addToCart($event, item)">Add +</div> -->
              </div>
            </div>
            <div
              @click="goto(item.categoryId)"
              class="cross-item-footer flex-row justify-content-between align-item-center pt12 pl12 pr12 bold"
            >
              <div class="fs-12">All {{ item.categoryName }}</div>

              <div>
                <i class="jfi-arrow-double fs-12 cursor-pointer"></i>
              </div>
            </div>
          </div>
        </Slide>
        <template #addons> </template>
      </Carousel>
    </div>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
</script>
<script>
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide } from "vue3-carousel";
import { imgBaseUrl, staticImgBaseUrl } from "../config/apiEndpoints";
import {
  keyMapsMutation,
  keyMapsAction,
  keyMapsGetter,
} from "../helpers/storeKeys";
import Utility from "../helpers/utility";

export default {
  name: "CrossSell",

  props: ["dataProductId", "dataCategoryId"],

  components: {
    Carousel,
    Slide,
  },

  mounted() {
    this.$store.commit(keyMapsMutation.UPDATE_CROSS_SELL_WITH_PRODUCT, {
      productId: this.dataProductId,
      categoryId: this.dataCategoryId,
    });
  },

  data() {
    return {
      imgBaseUrl: imgBaseUrl,
      staticImgBaseUrl: staticImgBaseUrl
    };
  },

  methods: {
    prepareImgPath(url) {
      if (url.indexOf("http") != -1) {
        return url;
      } else {
        return (this.imgBaseUrl + '/' + url);
      }
    },
    addToCart(e, item) {
      item = {
        ...item,
        addTopngs: [],
        repTopngs: [],
        remTopngs: [],
      };
      this.$store.commit(keyMapsMutation.ADD_TO_CART, item);
      this.$store.commit(keyMapsMutation.ADD_TO_UNIQUE_CART_ITEMS, {
        id: item.id,
      });
    },
    removeFromCart(e, item) {
      this.$store.commit(keyMapsMutation.REMOVE_FROM_CART, item);
      this.$store.commit(keyMapsMutation.REMOVE_FROM_UNIQUE_CART_ITEMS, {
        id: item.id,
      });
    },
    goto(itmId) {
      if (itmId) {
        Utility.scrollUptoTo("element_catg_" + itmId);
      }
    },
  },

  computed: {
    getCrossSellProducts() {
      return this.$store.getters[keyMapsGetter.GET_CROSS_SELL_PRODUCTS]();
    },
    quantity() {
      return (item) => {
        return this.$store.getters[keyMapsGetter.GET_ITEM_QUANTITY](item);
      };
    },
  },
};
</script>

<style lang="scss" scoped></style>
