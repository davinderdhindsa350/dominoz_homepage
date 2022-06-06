<template>
  <div
    class="card-item options-api"
    :class="{ 'card-item-search': cardSearchable, mr12: marginGap }"
    v-if="result && Object.keys(result).length > 2"
    :dummy="productObserver"
    ref="productCard"
    :id="dataCounterText"
    v-memo="[
      itemQuantity,
      result.itemPrice,
      showCustmizationModal,
      showAddOrEditCheckModal,
      disbaleButtonModal,
      showRepeatOrAddCustomizationModal,
      recommendateItem
    ]"
  >
    <div class="card-size" :class="{ 'card-rounded': cardRounded }">
      <div class="c-img-wrap">
        <template v-if="result?.images?.length > 0">
          <template v-for="(img, index) in result?.images">
            <template
              v-if="!cardSearchable && img.mediaSize == 'HomeProductV1'"
            >
              <v-lazy-image
                class="card-img img-product-home"
                alt="default image"
                :src="prepareImgPath(img.mediaPath)"
                :src-placeholder="
                  staticImgBaseUrl + '/images/global/default-product-img-lg.png'
                "
                :intersection-options="{ rootMargin: '800px' }"
              />
            </template>
            <template
              v-if="cardSearchable && img.mediaSize == 'SearchProductV1'"
            >
              <v-lazy-image
                class="card-img img-product-search"
                alt="default image"
                :src="prepareImgPath(img.mediaPath)"
                :src-placeholder="
                  staticImgBaseUrl + '/images/global/default-product-img-lg.png'
                "
                :intersection-options="{ rootMargin: '600px' }"
              />
            </template>
          </template>
        </template>
        <template v-else>
          <v-lazy-image
            class="card-img backup-img"
            alt="product image"
            :src="imgBaseUrl + '/' + result.image"
          />
        </template>
      </div>
      <div class="card-content">
        <div class="at-top" v-if="!cardSearchable">
          <div class="tag-bg-black speciality" v-if="dataType === 'Recommended'">
            <span class="ico-trending"><i class="jfi-trending"></i></span
            >Popular in {{ result.customCategoryName}}
          </div>
          <div
            class="selling-tag"
            v-if="dataType !== 'Recommended'"
          >
            {{recommendateItem}}
          </div>
        </div>
        <div class="at-botm" :class="{ 'card-botm-gradient': !cardSearchable }">
          <!--           <div class="tag-bg-yellow p-disc hide" v-if="!cardSearchable">
            <div class="fs-12 bold lh18 disp-col-inline">
              <i class="jfi-offer-tag mr8"
                ><span class="ico-percent">%</span></i
              >
              20% OFF
            </div>
            <div class="fs-10 lh16 bold">
              Code: <span class="upercase">NONSTOP60</span>
            </div>
          </div> -->
          <!-- extra info -->
          <div class="extra-info">
            <!--             <div
              class="tag-bg-black rating-info ib mr8 hide"
              v-if="!cardSearchable"
            >
              <span class="bold">4.2</span>
              <i class="jfi-star ml5 mr5"></i>
              <span class="color-white">2k</span>
            </div> -->
            <!--             <div class="tag-bg-black ib" v-if="!cardSearchable">
              <span class="ico-red-holder"><i class="jfi-trending"></i></span>
              <span class="bold upercase fs-10">SPICY</span>
            </div> -->
            <!-- "customized" class will be added along "tag-customize", when card is customised -->
            <!-- :class="{'customized': !itemIsCustomized()}" -->
            <div
              v-if="result.isCustomizable"
              class="tag-customize ib fr"
              :class="{ customized: itemIsCustomized() }"
              @click="triggerModal('customise')"
            >
              <span class="ico-checkmark"><i class="jfi-checker"></i></span>
              <span class="mr10 ml5 bold cust-text"></span>
              <i class="jfi-arrow-line"></i>
            </div>
            <!-- modal for customization -->
            <generic-modal
              v-if="showCustmizationModal"
              @close="closeModal()"
              :width="custModalDim.width"
              :modalClass="custModalDim.modalClass"
              :height="custModalDim.height"
            >
              <template v-slot:header>
                <i
                  @click="closeModal('cross')"
                  class="jfi-close-v2 cross-holder"
                ></i>
              </template>
              <template v-slot:body>
                <div class="customization-pop-content">
                  <product-customization
                    :data-result="result"
                    @close="closeModal()"
                    @item-customised="itemIsCustomized()"
                    :last-edit="islastEditOn"
                    :new-feature="newItemRemoveFeature"
                    :data-customiseType="customiseType"
                    :data-type="dataType"
                    :data-add-counter="itemQuantity"
                    :data-index="dataIndex"
                  ></product-customization>
                </div>
              </template>
              <template v-slot:footer>
                <div></div>
              </template>
            </generic-modal>

            <!-- modal tobe shown, if quantity > 0 and then clicked on customisation btn -->
            <generic-modal
              v-if="showAddOrEditCheckModal"
              @close="closeAddOrEditCheckModal()"
              :height="addOrEditModalDim.height"
              :width="addOrEditModalDim.width"
              :animationStyle="addOrEditModalDim.animationStyle"
            >
              <template v-slot:header>
                <i
                  @click="closeAddOrEditCheckModal('cross')"
                  class="jfi-close-v2 sticky-pop-cross"
                ></i>
              </template>
              <template v-slot:body>
                <div class="ms-offer-pop-content">
                  <div class="btn-wrap mt30">
                    <button class="btn medium" @click="addANewPizza()">
                      Add A New Pizza
                    </button>
                  </div>
                  <div class="btn-wrap mb15">
                    <button
                      class="line-btn dark medium w100"
                      @click="editLastAddedPizza()"
                    >
                      Edit Last Added Pizza
                    </button>
                  </div>
                </div>
              </template>
              <template v-slot:footer>
                <div></div>
              </template>
            </generic-modal>

            <!-- customization disbale button modal -->
            <generic-modal
              v-if="disbaleButtonModal"
              @close="closeDisableButtonModal()"
              :height="addOrEditModalDim.height"
              :width="addOrEditModalDim.width"
              :animationStyle="addOrEditModalDim.animationStyle"
            >
              <template v-slot:header>
                <i
                  @click="closeDisableButtonModal()"
                  class="jfi-cross sticky-pop-cross"
                ></i>
              </template>
              <template v-slot:body>
                <div class="ms-offer-pop-content pb20 pt20 bold fs-20">
                  You can't remove the items from here after you have
                  customized. <br /><br />
                  Please go to <a href="/cart">Cart</a> for removing already
                  customised items.
                </div>
              </template>
              <template v-slot:footer>
                <div></div>
              </template>
            </generic-modal>

            <!-- modal tobe shown, if quantity > 0 and then clicked on Add/Minus Cta btn -->
            <generic-modal
              v-if="showRepeatOrAddCustomizationModal"
              @close="closeRepeatOrAddModal()"
              :height="addOrEditModalDim.height"
              :width="addOrEditModalDim.width"
              :animationStyle="addOrEditModalDim.animationStyle"
            >
              <template v-slot:header>
                <span class="fs-18 bold color-black"
                  >Repeat last customisation?</span
                >
                <i
                  @click="closeRepeatOrAddModal()"
                  class="jfi-close-v2 sticky-pop-cross"
                ></i>
              </template>
              <template v-slot:body>
                <div class="mt30">
                  <!-- last customosed crust and size -->
                  <div class="disp-col-inline jcsb">
                    <div class="fs-14">
                      <span>{{ result.sizeName }}</span>
                      <span class="separater-before">{{
                        result.crustName
                      }}</span>
                    </div>
                    <span class="fs-18 bold">
                      <span class="rs">Rs.</span>{{ result.itemPrice }}
                    </span>
                  </div>
                  <!-- last customised toppings -->
                  <div class="lh18 mt5 disp-col-inline jcsb align-items-start">
                    <div>
                      <span v-if="tping(result.addTopngs).length > 0"
                        >Added:
                        <span class="color-grey">{{
                          tping(result.addTopngs) || ""
                        }}</span
                        >.</span
                      >
                      <span v-if="replaceTopDis().length > 0"
                        >Replaced:
                        <span
                          class="color-grey"
                          v-if="replaceTopDis().length > 0"
                          >{{ replaceTopDis() }}</span
                        >.</span
                      >
                    </div>
                    <div class="base-price ml10">
                      <span class="color-grey fs-12"
                        >Including base price <span class="rs">Rs.</span
                        >{{ result.defaultPrice }}</span
                      >
                    </div>
                  </div>
                  <div class="btn-wrap mt25">
                    <button
                      class="btn medium w100"
                      @click="repeatLastCustomization()"
                    >
                      Repeat Last Customisation
                    </button>
                  </div>
                  <div class="btn-wrap mb15">
                    <button
                      class="line-btn dark medium w100"
                      @click="addedNewCustomization()"
                    >
                      Add New Customisation
                    </button>
                  </div>
                </div>
              </template>
              <template v-slot:footer>
                <div></div>
              </template>
            </generic-modal>
          </div>
          <!-- pizza title and desc -->
          <div class="pizza-info">
            <h2>
              <i
                class="mr8"
                :class="[
                  {
                    'tag-veg':
                      result.tempProductType === 0 || result.productType === 0,
                  },
                  {
                    'tag-non-veg':
                      result.tempProductType === 1 || result.productType === 1,
                  },
                ]"
              ></i>
              <span class="pizza-title bold">{{ result.name }}</span>
            </h2>
            <h3 class="pizza-desc lh16">{{ result.description }}.</h3>

            <!-- search-cards-only -->
            <div class="search-cards-only" v-if="cardSearchable">
              <!-- <div class="tag-bg-white rating-info ib">
                <span class="bold color-base">4.2</span>
                <i class="jfi-star ml5 mr5"></i>
                <span class="color-base">2k</span>
              </div>
              <div class="tag-bg-white ib ml8">
                <span class="ico-red-holder"><i class="jfi-trending"></i></span>
                <span class="bold upercase fs-10">SPICY</span>
              </div> -->
              <div class="tag-bg-white ib" :class="{ hide: !(recommendateItem.length > 0) }">
                <span class="bold upercase fs-10">Bestseller</span>
              </div>
            </div>
          </div>

          <!-- pizza price and Add cta -->
          <div
            class="selected-info"
            :class="{ 'bg-grad-black': !cardSearchable }"
          >
            <div class="selections">
              <div class="pizza-price disp-col-inline">
                <span>
                  <i class="rs">Rs.</i>
                  <span>
                    {{ result.itemPrice }}
                  </span>
                </span>
                <!-- for EVD case only -->
                <!--                 <div class="evd-price ml8">
                  <span class="striked-price ib mr8"
                    ><i class="rs">Rs.</i> 350</span
                  >
                  <div class="saving-tag bold ib">
                    Save <i class="rs">Rs.</i> 120
                    <span class="lft-arr">
                      <i class="jfi-arrow-line"></i>
                    </span>
                  </div>
                </div> -->
              </div>
              <div class="size-selected" v-if="result.crust">
                <a
                  href="javascript:void(0);"
                  class="selected-p-profile disp-col-inline"
                  :class="{ 'color-white': !cardSearchable }"
                >
                  <ul class="dashed-botm disp-col-inline three-dot">
                    <li @click="triggerModal('upsell')" class="cursor-pointer">
                      <span class="p-size">
                        {{ currentSize() || result.sizeName }}
                      </span>
                      <span class="p-crust">{{
                        currentCrust() || result.crustName
                      }}</span>
                    </li>
                    <!-- <li>
                        <span class="p-size"> </span>
                        <span class="p-crust">{{
                          getCrust(result.crust, result.crustId)
                        }}</span>
                      </li> -->
                  </ul>

                  <i class="jfi-arrow-line"></i>
                </a>
              </div>
            </div>
            <div class="main-cta">
              <div
                v-if="itemQuantity > 0"
                class="cta-edit"
                :class="{ dark: cardSearchable }"
              >
                <!-- <span
                  class="anim-ico-minus item-status-anim"
                  :class="{ disabled: itemIsCustomized() }"
                  @click="removeFromCart()"
                ></span> -->
                <span
                  @click="
                    itemIsCustomized() && !newItemRemoveFeature
                      ? triggerDisableButtonModal()
                      : removeFromCart()
                  "
                  class="anim-ico-minus item-status-anim"
                  :class="{
                    'disable-with-event':
                      itemIsCustomized() && !newItemRemoveFeature,
                  }"
                >
                </span>
                <span>{{ itemQuantity }}</span>
                <span
                  class="anim-ico-plus tr item-status-anim"
                  @click="addToCart()"
                ></span>
              </div>

              <button
                v-if="itemQuantity === 0"
                class="btn bold hand cta-add"
                @click.prevent="addToCart()"
              >
                Add <span class="ico-plus">&#43;</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="customizations"
      v-if="
        hideToppingInRecommendedItems != 'undefined' &&
        hideToppingInRecommendedItems &&
        (tping(result.addTopngs).length > 0 || replaceTopDis().length > 0)
      "
    >
      <span class="color-base" v-if="tping(result.addTopngs).length > 0"
        >Added:&nbsp;</span
      >
      <span class="color-grey" v-if="tping(result.addTopngs).length > 0">
        {{ tping(result.addTopngs) || "" }}.
      </span>
      <span class="color-base" v-if="replaceTopDis().length > 0"
        >Replaced:&nbsp;</span
      >
      <span class="color-grey" v-if="replaceTopDis().length > 0"
        >{{ replaceTopDis() || "" }}
      </span>
    </div>

    <transition name="upsell-transition">
      <up-sell
        :data-item="result"
        v-if="!dataCardSearchable && cardSource !== 'HPS' && itemQuantity == 1"
      ></up-sell>
    </transition>
    <cross-sell
      :data-productId="result.code"
      :data-categoryId="dataCategoryId"
      v-if="
        !dataCardSearchable &&
        cardSource !== 'HPS' &&
        ((!crossSellWithProduct && itemQuantity == 1) ||
          (crossSellWithProduct.productId == result.code &&
            crossSellWithProduct.categoryId == dataCategoryId))
      "
    ></cross-sell>
  </div>
</template>

<script setup>
import VLazyImage from "v-lazy-image";
</script>

<script>
import { defineAsyncComponent } from "vue";
import GenericModal from "./GenericModal.vue";
import { imgBaseUrl, staticImgBaseUrl } from "../config/apiEndpoints";
// import ProductCustomization from "./ProductCustomization.vue";
// import CrossSell from "./CrossSell.vue";
// import UpSell from "./UpSell.vue";
import __ from "../helpers/utility.js";

import {
  keyMapsMutation,
  keyMapsGetter,
  keyMapsAction,
} from "../helpers/storeKeys";
import LocalStorageFunc from "../helpers/localStorageFunc";
import utility from "../helpers/utility.js";
import setEvents from "../helpers/events";

export default {
  name: "ProductCard",
  props: [
    "dataCardRounded",
    "dataCardSearchable",
    "dataCardSource",
    "dataResult",
    "dataModuleProps",
    "dataCounterText",
    "dataCategoryId",
    "marginGap",
    "dataType",
    "dataIndex",
  ],
  emits: ["getItem"],
  components: {
    GenericModal,
    // ProductCustomization,
    ProductCustomization: defineAsyncComponent(() =>
      import("./ProductCustomization.vue")
    ),
    CrossSell: defineAsyncComponent(() => import("./CrossSell.vue")),
    UpSell: defineAsyncComponent(() => import("./UpSell.vue")),
  },

  data() {
    return {
      cardRounded:
        this.dataCardRounded != "null" &&
        typeof this.dataCardRounded != "undefined"
          ? this.dataCardRounded
          : false,
      cardSearchable:
        this.dataCardSearchable != "null" &&
        typeof this.dataCardSearchable != "undefined"
          ? this.dataCardSearchable
          : false,
      cardSource: "",
      imgBaseUrl: imgBaseUrl,
      staticImgBaseUrl: staticImgBaseUrl,
      showCustmizationModal: false,
      custModalDim: {
        width: "100vw",
        height: "100%",
        modalClass: "full-screen-popup cust-modal-wrap",
      },
      productResult: {},
      showAddOrEditCheckModal: false,
      addOrEditModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: "modal-bg-blackout",
      },
      showRepeatOrAddCustomizationModal: false,
      isUserCustomizable: false,
      // isToppingAvalInCartItems: false,
      islastEditOn: false,
      disbaleButtonModal: false,
      newItemRemoveFeature: true, // property base feature
      // aryForPopularTag: [],
      // addTo
      customiseType: "customise",
    };
  },
  created() {},
  emits: ["add-to-cart", "open-customise", "open-upsell"],
  mounted() {
    this.cardSource = this.dataCardSource || "";
    this.$store.commit(
      keyMapsMutation.UPDATE_CART_ITEM_PRICE_ARRAY,
      LocalStorageFunc.getToppingArrayPriceKeyLocalStorage(
        "itemPriceToppingArray"
      )
    );
    if (this.totalCartData[this.totalCartData.length - 1]) {
      this.$store.commit(keyMapsMutation.UPDATE_SELECTED_TOPPINGS, [
        LocalStorageFunc.getSelectedAndReplaceToppingsLocalStorage(
          "selectedAndReplacedToppings"
        ),
        this.totalCartData[this.totalCartData.length - 1].id,
      ]);
    } else {
      this.$store.commit(
        keyMapsMutation.UPDATE_SELECTED_TOPPINGS,
        LocalStorageFunc.getSelectedAndReplaceToppingsLocalStorage(
          "selectedAndReplacedToppings"
        )
      );
    }
    // this.$store.commit(
    //   keyMapsMutation.UPDATE_CART_ITEM_PRICE_ARRAY,
    //   LocalStorageFunc.getReplaceDisableLocalStorage("replaceDisable")
    // );
    this.$store.commit(
      keyMapsMutation.UPDATE_CURRENT_SEL_TOPPING_OBJ,
      LocalStorageFunc.getCurrentSelectedToppings("currentSelToppObj")
    );

    if (
      this.productObserver &&
      this.dataCounterText &&
      document.getElementById(this.dataCounterText)
    ) {
      this.productObserver.observe(
        document.getElementById(this.dataCounterText)
      );
    }
    this.aryForPopularTag = __.deepCopyArry(this.allItemData);
    
  },
  destroyed() {
    if (this.observer) {
      this.observer.unobserve(document.getElementById(this.dataCounterText));
    }
  },
  methods: {
    prepareImgPath(url) {
      if (url.indexOf("https:") != -1) {
        return url;
      } else {
        return this.imgBaseUrl + "/" + url;
      }
    },
    getEventObj(item) {
      let returnObj = {};
      if(this.recommendateItem.length > 0) returnObj["sellerType"] = "bestseller";
      if(this.dataType === 'Recommended') returnObj["popularityType"] = "popular in " + item.customCategoryName;
      returnObj["category"] = item.name;
      return returnObj;
    },
    eventFunction(isClicked = false, eObj = {}) {
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      let preEventObj = { ...eObj };
      const eventObj = utility.amplitudeEventObj(
        userAction,
        this.dataType + " card",
        "8",
        preEventObj
      );
      setEvents("amplitude", eventType, eventObj);
    },
    checkItemCustomizedChanges() {
      if (
        this.result.defaultSizeName !== this.result.sizeName ||
        this.result.defaultCrustName !== this.result.crustName
      ) {
        return true;
      } else {
        return false;
      }
    },
    itemIsCustomized() {
      if (
        this.checkItemCustomizedChanges() ||
        this.result.addTopngs.length > 0
      ) {
        return true;
      } else {
        return false;
      }
    },
    repeatLastCustomization() {
      this.islastEditOn = false;
      this.$store.commit(keyMapsMutation.UPDATE_CART, [
        this.result,
        this.islastEditOn,
      ]);
      this.$store.commit(keyMapsMutation.ADD_TO_UNIQUE_CART_ITEMS, {
        id: this.result.id,
      });
      this.closeRepeatOrAddModal();
      if (this.replacedToppingText[this.result.id]?.length > 0) {
        this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, true);
      } else {
        this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
      }
      this.$store.commit(
        keyMapsMutation.IS_ITEM_NEW_OR_PREVIOUS_ON_CHECK,
        false
      ); // for checking the added item in new one or previous one
    },
    addedNewCustomization() {
      // this.result["addTopngs"] = []; // =+++++++++++++++++++
      this.showCustmizationModal = true;
      this.islastEditOn = false;
      this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
      this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false); // this one added for reset the previous toppings
      this.result.addTopngs = []; // this one added for reset the previous toppings
      this.$store.commit(
        keyMapsMutation.IS_ITEM_NEW_OR_PREVIOUS_ON_CHECK,
        true
      ); // for checking the added item in new one or previous one
      this.closeRepeatOrAddModal();
    },
    addToCart() {
      if (this.itemIsCustomized()) {
        this.triggerRepeatOrAddModal();
      } else {
        this.$store.commit(keyMapsMutation.ADD_TO_CART, this.result);
        this.$store.commit(keyMapsMutation.ADD_TO_UNIQUE_CART_ITEMS, {
          id: this.result.id,
        });
        // this.$store.commit("UniqueCartItems/addToCart", this.productResult);
      }
      if (this.dataType) {
        let eObj = {
          subCategory: "add+",
          addCounter: JSON.stringify(this.itemQuantity),
          positionWithinSection: this.dataIndex + 1,
          ...this.getEventObj(this.result),
        };
        this.eventFunction(true, eObj);
      }
      this.$emit("add-to-cart", this.result);
    },
    removeFromCart() {
      if (this.newItemRemoveFeature) {
        let obj = utility.deepCopyArry(this.result);
        this.$store.commit(keyMapsMutation.NEW_FEATURE_REMOVE_CART, {
          id: this.result.id,
        });
        this.$store.commit(keyMapsMutation.REMOVE_CURRENT_SEL_TOPPING_OBJ, obj);
        this.$store.commit(keyMapsMutation.REMOVE_FROM_UNIQUE_CART_ITEMS, {
          id: this.result.id,
        });
        if (
          this.result.addTopngs?.length !== undefined &&
          this.result.addTopngs.length > 0
        ) {
          let data = __.toppingsNameByToppingId(
            this.allCartToppingItem,
            this.toppingFormStore,
            this.result.addTopngs
          ).split(",");
          data &&
            this.$store.commit(
              keyMapsMutation.UPDATE_PREVIOUS_SELECTED_TOPPINGS,
              [data, this.result.id]
            );
        }
        if (
          this.toppingFormStore &&
          this.toppingFormStore.length > 0 &&
          this.allCartToppingItem &&
          this.allCartToppingItem.length > 0
        ) {
          let selec = this.toppingFormStore.filter((i) =>
            this.allCartToppingItem[
              this.allCartToppingItem.length - 1
            ].addTopngs.includes(i.toppingId)
          );
          selec &&
            this.$store.commit(
              keyMapsMutation.UPDATE_CURRENT_SEL_TOPPING_OBJ,
              selec
            );
        }
      } else {
        if (!this.itemIsCustomized()) {
          this.$store.commit(keyMapsMutation.REMOVE_FROM_CART, this.result);
          this.$store.commit(keyMapsMutation.REMOVE_FROM_UNIQUE_CART_ITEMS, {
            id: this.result.id,
          });
        }
      }
      if (this.allCartToppingItem) {
        // this condition is for check if there
        this.islastEditOn = false;
      }
      //
      if (this.dataType) {
        let eObj = {
          subCategory: "add-",
          addCounter: JSON.stringify(this.itemQuantity),
          positionWithinSection: this.dataIndex + 1,
          ...this.getEventObj(this.result),
        };
        this.eventFunction(true, eObj);
      }
    },
    getDataFromSelectedSize(data) {
      let sizeArr = data[0].sizes ? data[0].sizes : [];
      let selectedSizeArr = sizeArr.filter((s) => {
        return s.sizeId == this.result.sizeId;
      });
      return {
        size: selectedSizeArr[0].name,
        price: selectedSizeArr[0].price,
        sizeId: selectedSizeArr[0].sizeId,
        code: selectedSizeArr[0].code,
      };
    },
    openCustomizedPopUp() {
      //  open popup customized modal
      if (this.itemQuantity > 0 && this.itemIsCustomized()) {
        this.triggerAddOrEditCheckModal();
      }
    },
    getNewPizzaEventObj() {
      let returnObj = {
        actionType: "upsellCustomise",
        positionWithinSection: this.dataIndex + 1,
        category: this.result?.name,
        addCounter: this.itemQuantity,
      };
      return returnObj;
    },
    addANewPizza() {
      this.closeAddOrEditCheckModal();
      this.showCustmizationModal = true;
      this.islastEditOn = false;
      this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
      this.$store.commit(
        keyMapsMutation.IS_ITEM_NEW_OR_PREVIOUS_ON_CHECK,
        true
      ); // for checking the added item in new one or previous one

      // this.result.addTopngs = []; /####################################################################

      //add new pizza event
      if (this.dataType) {
        let preEventObj = this.getNewPizzaEventObj();
        preEventObj["subCategory"] = "add a new pizza";
        const eventObj = utility.amplitudeEventObj(
          "click",
          this.dataType + " card",
          "0",
          preEventObj
        );
        setEvents("amplitude", "Click", eventObj);
      }
    },
    editLastAddedPizza() {
      /* this.result = this.totalCartData[this.totalCartData.length - 1]; */
      this.closeAddOrEditCheckModal();
      this.showCustmizationModal = true;
      this.islastEditOn = true;
      if (this.replacedToppingText[this.result.id]?.length > 0) {
        this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, true);
      } else {
        this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
      }
      this.$store.commit(
        keyMapsMutation.IS_ITEM_NEW_OR_PREVIOUS_ON_CHECK,
        false
      ); // for checking the added item in new one or previous one
      //add new pizza event
      if (this.dataType) {
        let preEventObj = this.getNewPizzaEventObj();
        preEventObj["subCategory"] = "edit a new pizza";
        const eventObj = utility.amplitudeEventObj(
          "click",
          this.dataType + " card",
          "0",
          preEventObj
        );
        setEvents("amplitude", "Click", eventObj);
      }
    },
    triggerModal(subCategory) {
      document.body.classList.add("body-stop-scroll-mobile");
      if (this.itemIsCustomized() && this.itemQuantity > 0) {
        this.showCustmizationModal = false;
      } else {
        this.showCustmizationModal = true;
      }
      this.openCustomizedPopUp();
      if (this.dataType) {
        this.eventFunction(true, {
          subCategory: subCategory,
          positionWithinSection: this.dataIndex + 1,
          ...this.getEventObj(this.result),
        });
      }
      if (this.replaceTopDis().length > 0) {
        this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, true);
      } else {
        this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
      }
      let eventName = "open-customise";
      if (subCategory == "upsell") eventName = "open-upsell";
      this.$emit(eventName, this.result);
    },
    closeModal(type) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showCustmizationModal = false;
      if (this.dataType) {
        if (type && type == "cross") {
          let preEventObj = {
            entry: this.customiseType,
            category: this.result?.name,
            addCounter: JSON.stringify(this.itemQuantity),
            changeSize: this.result?.sizeName,
            changeCrust: this.result?.crustName,
            addVegToppings: "none",
            addNonVegToppings: "none",
            replaceToppings: "0",
            replaceToppingsFrom: "none",
            replaceToppingsTo: "none",
            close: 1,
          };
          if (this.islastEditOn) {
            preEventObj["actionType"] = "upsellCustomise";
          }
          if (this.result.addTopngs?.length > 0) {
            let vegToppingName = [];
            let nonVegToppingName = [];
            this.toppingFormStore.forEach((i) => {
              if (i.type === 0) {
                if (this.result.addTopngs.includes(i.toppingId)) {
                  vegToppingName.push(i.name);
                }
                return this.result.addTopngs.includes(i.toppingId);
              } else if (i.type === 1) {
                if (this.result.addTopngs.includes(i.toppingId)) {
                  nonVegToppingName.push(i.name);
                }
              }
            });
            preEventObj["addVegToppings"] = vegToppingName.join(",");
            preEventObj["addNonVegToppings"] = nonVegToppingName.join(",");
          }
          if (this.result.remTopngs?.length > 0) {
            preEventObj["replaceToppings"] = this.result.remTopngs.length;
            let replaceToppingsFrom = [];
            let replaceToppingsTo = [];
            this.toppingFormStore.forEach((i) => {
              if (this.result.remTopngs.includes(i.toppingId)) {
                replaceToppingsFrom.push(i.name);
              }
              if (this.result.repTopngs.includes(i.toppingId)) {
                replaceToppingsTo.push(i.name);
              }
            });
            preEventObj["replaceToppingsFrom"] = replaceToppingsFrom.join(",");
            preEventObj["replaceToppingsTo"] = replaceToppingsTo.join(",");
          }
          const eventObj = utility.amplitudeEventObj(
            "click",
            this.dataType + " card",
            "1",
            preEventObj
          );
          setEvents("amplitude", "Click", eventObj);
        }
      }
    },
    triggerAddOrEditCheckModal() {
      document.body.classList.add("body-stop-scroll-mobile");
      this.showAddOrEditCheckModal = true;
      //add new pizza event
      if (this.dataType) {
        let preEventObj = this.getNewPizzaEventObj();
        if(this.recommendateItem.length > 0) preEventObj["sellerType"] = "bestseller";
        if(this.dataType === "Recommended") preEventObj["popularityType"] = "popular in " + this.result.customCategoryName;
        const eventObj = utility.amplitudeEventObj("impression", this.dataType + " card", "0", preEventObj);
        setEvents("amplitude", "Impression", eventObj);
      }
    },
    closeAddOrEditCheckModal(type) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showAddOrEditCheckModal = false;
      if (type && type == "cross") {
        //add new pizza event
        if (this.dataType) {
          let preEventObj = this.getNewPizzaEventObj();
          preEventObj["subCategory"] = "cross tap";
          const eventObj = utility.amplitudeEventObj(
            "click",
            this.dataType + " card",
            "0",
            preEventObj
          );
          setEvents("amplitude", "Click", eventObj);
        }
      }
    },
    closeDisableButtonModal() {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.disbaleButtonModal = false;
    },
    triggerDisableButtonModal() {
      document.body.classList.add("body-stop-scroll-mobile");
      this.disbaleButtonModal = true;
    },
    triggerRepeatOrAddModal() {
      document.body.classList.add("body-stop-scroll-mobile");
      this.showRepeatOrAddCustomizationModal = true;
    },
    closeRepeatOrAddModal() {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showRepeatOrAddCustomizationModal = false;
    },
    tping(item) {
      // to get the current toppings name
      if (this.allCartToppingItem?.length !== undefined) {
        return __.toppingsNameByToppingId(
          this.allCartToppingItem,
          this.toppingFormStore,
          item
        );
      }
    },
    currentCrust() {
      // current selected crust which will display on main Pizza Cart
      if (this.totalCartData.length > 0 && this.result) {
        return __.currentCrustName(this.totalCartData, this.result);
      }
    },
    currentSize() {
      // current selected size which will display on main Pizza Cart
      if (this.totalCartData.length > 0 && this.result) {
        return __.currentSizeName(this.totalCartData, this.result);
      }
    },
    replaceTopDis() {
      return (
        __.replaceToppingText(
          this.allCartToppingItem,
          this.toppingFormStore,
          this.result
        ) || ""
      );
    },
  },
  computed: {
    uniqueQuantity() {
      return this.$store.getters[keyMapsGetter.GET_UNIQUE_CART_ITEMS](
        this.result
      );
    },
    itemQuantity() {
      return this.$store.getters[keyMapsGetter.GET_ITEM_QUANTITY](this.result);
    },
    allItemData() {
      return this.$store.getters[keyMapsGetter.GET_PRODUCT_ITEMS_IN_ONE_ARRAY];
    },
    allItemRecommentation() {
      return this.$store.getters[keyMapsGetter.GET_RECOMMENDED_PRODUCTS]();
    },
    recommendateItem() {
      return this.$store.state.ProductItems.recommendedItemsIds.includes(this.result.code) && (this.dataType !== "Recommended") ? "BESTSELLER" : ""
    },
    getBestSellerIds() {
      return this.$store.getters[keyMapsGetter.GET_BEST_SELLER_ITEMS_ID];
    },
    allPopularTagItems() {
      return this.$store.getters[keyMapsGetter.GET_POPULAR_TAG_ITEMS];
    },
    crossSellWithProduct() {
      return this.$store.state.Cart.crossSellWithProduct;
    },
    selectedStoreToppings() {
      return (
        this.$store.state.Cart.selectedAndReplacedToppings.addedToppings || {}
      );
    },
    replacedToppingText() {
      return (
        this.$store.state.Cart.selectedAndReplacedToppings.replacedToppings ||
        {}
      );
    },
    totalCartData() {
      return this.$store.state.Cart.cartItems || [];
    },
    ifAddedTextShow() {
      return this.totalCartData[this.totalCartData.length - 1]?.addTopngs !==
        undefined &&
        this.totalCartData[this.totalCartData.length - 1]?.addTopngs.length > 0
        ? true
        : false;
      // @todo
      // let text = false;
      //   if(this.totalCartData[this.totalCartData.length - 1]?.addTopngs !==
      //   undefined){
      //     this.totalCartData.map(i => {
      //       if([i.addTopngs].sort().toString() == [this.result.addTopngs].sort().toString()){
      //         text = true;
      //       } else {
      //         text = false;
      //       }
      //       })
      //   }
      //   return text;
    },
    ifReplaceTextShow() {
      return this.totalCartData[this.totalCartData.length - 1]?.repTopngs !==
        undefined &&
        this.totalCartData[this.totalCartData.length - 1]?.repTopngs.length > 0
        ? true
        : false;
    },
    result() {
      let _ =
        this.$store.getters[keyMapsGetter.GET_CART_ITEM_LATEST](
          this.dataResult?.id
        ) ||
        this.dataResult ||
        {};

      /*       let __ = {
        ..._,
        ...{
          product: {
            id: _.id,
            code: _.code,
          },
          // isUserCustomizable: this.showCustmizationModal ? true : false,
          isUserCustomizable: this.dataResult?.addTopngs?.length > 0 ? true : false
        },
      }; */

      // Updating result obj with extra keys required tobe sent to cart, which will be consumed by old flow cart page as it is
      _["product"] = {
        id: _.id,
        code: _.code,
      };
      _["isUserCustomizable"] =
        this.dataResult?.addTopngs?.length > 0 ? true : false;

      return _;
    },
    productObserver() {
      let observer = this.$store.state.ProductItems.productObserver;
      if (
        observer &&
        this.dataCounterText &&
        document.getElementById(this.dataCounterText)
      ) {
        observer.observe(document.getElementById(this.dataCounterText));
      }
      return observer;
    },
    allCartToppingItem() {
      return this.$store.state.Cart.cartItems || [];
    },
    // topng(){
    //   return __.toppingsNameByToppingId(this.allCartToppingItem, this.result.addTopngs)
    // }
    allProductItem(){
      return this.$store.getters["ProductItems/getProductItemsInOneArray"]
    },
    toppingFormStore() {
      return this.$store.state.Cart.toppingItems;
    },
    hideToppingInRecommendedItems(){
      return this.dataType !== "Recommended" ? true : false;
    }
  },
};
</script>

<style scoped>
.v-lazy-image {
  filter: blur(10px);
  transition: filter 0.7s;
}
.v-lazy-image-loaded {
  filter: blur(0);
}

</style>
