<template>
  <div class="horizontal-banners mb32" v-if="data?.length > 0">
    <div v-if="modulePropsData?.title" class="banners-title mb10 pl12 bold flex-row">
      <span class="lh21 fs-18">{{ modulePropsData.title }}</span>
      <!-- <span v-if="modulePropsData.subTitle" class="lh16 fs-14">{{modulePropsData.subTitle}}</span> -->
    </div>
    <Carousel
      :pauseAutoplayOnHover="true"
      :transition="500"
      :itemsToShow="itemsToShow"
      v-model="currentSlide"
      v-on:update:modelValue="updateCurrentSlide"
    >
      <Slide v-for="(item, index) in data" :key="index">
        <div @click="goto(item, index)" class="horz-img-wrap">
          <template v-if="queryData2[index]?.logo || item.imageUrl">
            <v-lazy-image
              class="banners-item flex-row fs-20"
              alt="edv image"
              :src="
                dataType == 'EDV'
                  ? queryData2[index]?.logo || item.imageUrl
                  : item.imageUrl
              "
            />
          </template>
          <template v-else>
            <v-lazy-image
              alt="default image"
              class="banners-item flex-row fs-20"
              :src="staticImgBaseUrl + '/images/global/default-product-img.png'"
            />
          </template>
        </div>

        <!-- <payment-offer-item v-if="modulePropsData.title=='Payment Offers'" :data="item"></payment-offer-item> -->
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
import PaymentOfferItem from "./PaymentOfferItem.vue";
import "vue3-carousel/dist/carousel.css";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";
import { v2PwaBasePath, staticImgBaseUrl } from "../config/apiEndpoints";

export default {
  name: "HorizontalBanners",
  props: ["dataResults", "moduleProps", "dataType", "queryData"],

  components: {
    Carousel,
    Slide,
    PaymentOfferItem,
  },
  data() {
    return {
      data: [],
      modulePropsData: {},
      itemsToShow: 1,
      currentSlide: 0,
      queryData2: [],
      staticImgBaseUrl: staticImgBaseUrl,
    };
  },
  mounted() {
    this.data =
      typeof this.dataResults != null &&
      typeof this.dataResults != "null" &&
      typeof this.dataResults != "undefined"
        ? JSON.parse(this.dataResults)
        : [];
    this.queryData2 =
      typeof this.queryData != null &&
      typeof this.queryData != "null" &&
      typeof this.queryData != "undefined"
        ? JSON.parse(this.queryData)
        : [];

    this.modulePropsData =
      typeof this.moduleProps != null &&
      typeof this.moduleProps != "null" &&
      typeof this.moduleProps != "undefined"
        ? JSON.parse(this.moduleProps)
        : {};
    this.itemsToShow = +this.modulePropsData?.itemsInScreen;
    if (this.data && this.data?.length > 0) {
      //Amplitude impression event load
      let sectionName = "marketing banner";
      if (this.dataType && this.dataType == "EDV") sectionName = "EDV";
      const preEventObj = {
        positionWithinSection: this.currentSlide + 1,
        category: this.data[this.currentSlide]?.imageUrl,
      };
      const eventObj = Utility.amplitudeEventObj(
        "impression",
        sectionName,
        "7",
        preEventObj
      );
      setEvents("amplitude", "Impression", eventObj);
    }
  },
  methods: {
    updateCurrentSlide(slideVal) {
      this.currentSlide = slideVal;
      //Amplitude impression event load
      let sectionName = "marketing banner";
      if (this.dataType && this.dataType == "EDV") sectionName = "EDV";
      const preEventObj = {
        positionWithinSection: this.currentSlide + 1,
        category: this.data[this.currentSlide]?.imageUrl,
      };
      const eventObj = Utility.amplitudeEventObj(
        "impression",
        sectionName,
        "7",
        preEventObj
      );
      setEvents("amplitude", "Impression", eventObj);
    },
    scrollOrRedirectCategory(data) {
      const { links } = data;
      let currentElem = this.currentElementIdInStore.map((i) => i.categoryId);
      let curElementId = currentElem.includes(Number(links[0].href));
      if (curElementId) {
        console.log("categoryId is present in store");
        switch (links[0].action) {
          case "menu":
            Utility.scrollUptoTo("element_catg_" + links[0].href);
            break;
          case "MENU_LIST":
            Utility.scrollUptoTo("element_catg_" + links[0].href);
            break;
          case "PRODUCT_LIST":
            Utility.scrollUptoTo("element_catg_" + links[0].href);
            break;
          default: // set the default scroll to home
            Utility.scrollUptoTo("pageTop");
            break;
        }
      } else {
        if (links[0].action === "edv") {
          window.location.href = `${v2PwaBasePath}/${links[0].action}`;
        } else if (links[0].action === "edvitems") {
          window.location.href = `${v2PwaBasePath}/${links[0].action}`;
        } else if (links[0].action === "edvList") {
          window.location.href = `${v2PwaBasePath}/${links[0].action}`;
        } else if (links[0].action === "offers") {
          window.location.href = `${v2PwaBasePath}/${links[0].action}`;
        } else {
          Utility.scrollUptoTo("pageTop");
        }
        console.log("categoryId is not present in store");
      }
    },
    goto(item, index) {
      let sectionName = "marketing banner";
      const preEventObj = {
        positionWithinSection: index + 1,
        category: this.data[index]?.imageUrl,
      };
      if (this.dataType && this.dataType == "EDV") {
        sectionName = "EDV";
        preEventObj["subCategory"] = "BannerTap";
      }
      const eventObj = Utility.amplitudeEventObj(
        "click",
        sectionName,
        "7",
        preEventObj
      );
      setEvents("amplitude", "Click", eventObj);
      this.currentElementIdInStore && this.scrollOrRedirectCategory(item);
    },
  },
  computed: {
    currentElementIdInStore() {
      return (
        this.$store.state.ProductItems.allCategoryId?.length !== undefined &&
        this.$store.state.ProductItems.allCategoryId
      );
    },
  },
};
</script>

<style scoped></style>
