<template>
  <div v-if="HorizontalBannerData?.length > 0" class="horizontal-menu mb16 pr0 col-sm-12 pl0">
    <Carousel :pauseAutoplayOnHover="true" :transition="500" :itemsToShow="itemsToShow">
      <Slide v-for="(offer, index) in HorizontalBannerData" :key="index">
        <div class="flex-column flex-wrap cursor-pointer" @click="goto(offer, index)">
          <div class="menu-img-wrap"> 
            <template v-if="offer?.categoryImages?.length > 0">
              <template v-for="(img, index) in offer?.categoryImages">
                <template v-if="img.mediaSize == 'CategoriesV1'">
                  <v-lazy-image 
                    class="horizontal-item flex-row img-category-menu" 
                    alt="default image"
                    :src="prepareImgPath(img.mediaPath)"
                    :src-placeholder="staticImgBaseUrl + '/images/global/default-product-img-lg.png'" 
                  />
                </template>
              </template>
            </template>
            <template v-else>
              <v-lazy-image 
                class="horizontal-item flex-row img-category-menu-fallback" 
                alt="offer image"
                :src="offer?.imageUrl" 
                :src-placeholder="staticImgBaseUrl + '/images/global/default-product-img-lg.png'" 
              />
            </template>
            <!-- <span class="horizontal-item-tag" v-if="offer.id == '1' || offer.id == '2'">
              <i :class="[{'tag-veg': offer.id == '1'}, {'tag-non-veg': offer.id == '2'}]" >
              </i>
            </span> -->
          </div>
          <span class="fs-11 color-slaty bold mt10">{{ offer?.title }}</span>
        </div>
      </Slide>
      <template #addons> </template>
    </Carousel>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
</script>
<script>
import { Carousel, Slide } from "vue3-carousel";
import "vue3-carousel/dist/carousel.css";
import { staticImgBaseUrl } from "../config/apiEndpoints";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";
import { keyMapsMutation } from '../helpers/storeKeys';
import { imgBaseUrl } from "../config/apiEndpoints";

export default {
  name: "HorizontalBannerWithText",
  components: {
    Carousel,
    Slide,
  },
  props: ["dataResults", "moduleProps"],
  data() {
    return {
      HorizontalBannerData: [],
      modulePropsData: {},
      itemsToShow: 3,
      staticImgBaseUrl: staticImgBaseUrl,
      imgBaseUrl: imgBaseUrl
    };
  },
  mounted() {
    this.HorizontalBannerData = (typeof this.dataResults != null && typeof this.dataResults != "null" && typeof this.dataResults != "undefined") ? JSON.parse(this.dataResults) : [];
    this.modulePropsData = (typeof this.moduleProps != null && typeof this.moduleProps != "null" && typeof this.moduleProps != "undefined") ? JSON.parse(this.moduleProps) : {};
    this.itemsToShow = + this.modulePropsData?.itemsInScreen;
    //Amplitude impression event load
    const eventObj = Utility.amplitudeEventObj("impression", "Explore Menu Section", "6", {});
    setEvents("amplitude", "Impression", eventObj);
  },
  methods: {
    prepareImgPath(url) {
      if (url.indexOf("http") != -1) {
        return url;
      } else {
        return (this.imgBaseUrl + '/' + url);
      }
    },
    goto(item, index) {
      this.$store.commit(keyMapsMutation.TRIGGER_FILTER_REF, {});
      let itmId = item?.links[0].href;
      if (itmId) {
        setTimeout(() => {
          Utility.scrollUptoTo("element_catg_" + itmId);
          this.$store.commit(keyMapsMutation.OPEN_ALL_CARDS, true);
        }, 0);
      }
      //Amplitude click event load
      const preEventObj = {
        "positionWithinSection": index + 1,
        "category" : item.title,
      };
      const eventObj = Utility.amplitudeEventObj("click", "Explore Menu Section", "6", preEventObj);
      setEvents("amplitude", "Click", eventObj);
    }
  },
};
</script>

<style lang="scss" scoped></style>
