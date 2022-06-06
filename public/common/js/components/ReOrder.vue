<template>
  <div
    class="col-sm-12 col-md-12 col-lg-12 pr0"
    v-if="previousOrderPizza.length > 0"
  >
    <div class="d-flex justify-content-between align-items-center previous-order-header pTB16 pr16"
    >
      <div class="main-header">
        <span class="fs-18 bold "> Quick Order </span>
      </div>
      <div class="bold color-secondary" @click="viewAllHandler()">
        View All Orders
      </div>
    </div>
    <div class="reorder-carousel mb16">
      <Carousel :items-to-show="1.5" :transition="500">
        <Slide v-for="(offer, ind) in previousOrderPizza" :key="ind">
          <div class="pizza-items mr5">
            <div class="pizza-header plr12">
              <span class="bold fs-14">{{
                dateCoversionFun(offer?.orderTimeStamp)
              }}</span>
              <button
                class="plr12 bold color-secondary fs-10 ptb1 previous-btn"
              >
                PREVIOUS ORDER
              </button>
            </div>
            <div class="main-pizza-body">
              <ol class="pizza-body">
                <li
                  class="list-dec plr12"
                  v-for="(item, index) in reOrderDisplayFun(offer.items) || []"
                  :key="index"
                >
                  <div :class="['fs-12 tag-veg-icon tl', checkCurrentItemPresentOrNot(item?.product) && 'disabled']" v-if="!(index >= 4)" >
                    <i
                      class="mr5"
                      :class="[
                        {
                          'tag-veg': item.product.foodType === 'VEG',
                        },
                        {
                          'tag-non-veg': item.product.foodType === 'NON_VEG',
                        },
                      ]"
                    ></i>
                    <span class="lh18 three-dot">
                      <span v-if="item.product">{{ item.product.name }}</span>
                      <span v-if="item.size">| {{ item.size.name }}</span>
                      <span v-if="item.crust">| {{ item.crust.name }}</span>
                    </span>
                  </div>
                  <div class="prev-order-cust" v-if="!(index >= 4)">
                    <div
                      v-if="
                        convertAllToppingIntoSingleString(item.addTopngs || [])
                          .length > 37
                      "
                      class="fs-11 topping-item-data lh16"
                    >
                      <div class="added-color-with-ellipsis">
                        Added:
                        {{
                          convertAllToppingIntoSingleString(
                            item.addTopngs || []
                          )
                        }}
                      </div>
                      <span v-if="Number(addedCount(
                          convertAllToppingIntoSingleString(
                            item.addTopngs || []
                          )
                        )) > 0">
                      {{
                        "+" +
                        addedCount(
                          convertAllToppingIntoSingleString(
                            item.addTopngs || []
                          )
                        )
                      }}
                      </span>
                    </div>
                    <div v-else class="fs-11 added-color lh16">
                      <div
                        v-if="item.addTopngs !== undefined"
                        class="three-dot"
                      >
                        Added:
                        {{
                          convertAllToppingIntoSingleString(
                            item.addTopngs || []
                          )
                        }}
                      </div>
                    </div>
                  </div>
                  
                </li>
                <hr v-if="itemCount(offer.items)" class="ml0 mr0" />
              </ol>
              <do-you-know
                v-if="itemCount(offer.items)"
                :itemSize="(offer.items || []).length"
              ></do-you-know>
              <div
                v-if="offer?.items?.length > 4"
                class="bold color-secondary fs-12 col-sm-12 tl mb5 ro-more-item"
              >
                +{{ offer?.items?.length > 4 ? offer?.items.length - 4 : "" }}
                items
              </div>
            </div>
            <div class="pizza-footer plr12">
              <span class="fs-14"
                ><span class="rs">Rs.</span>{{ offer?.netPrice || "" }} |
                {{ Math.round(totalReOrderCount(offer?.items)) || offers?.items?.length || "" }} items</span
              >
              <div class="button-group">
                <!-- <button // hide for temporary bcz it is in next sprint
                  class="line-btn dark sm mr10"
                  @click="editPreviousOrder(offer)"
                >
                  Edit
                </button> -->
                <button
                  class="bold"
                  :class="['btn', checkReorderButtonDisable(offer.items) && 'disabled']"
                  @click="addAllOrderIntoCart(offer.items, false)"
                >
                  Reorder
                </button>
              </div>
            </div>
          </div>
        </Slide>
        <template #addons> </template>
      </Carousel>
    </div>

    <generic-modal
      v-if="showEditPreviousOrderModal"
      @close="closeModal($event)"
      :height="msModalDim.height"
      :width="msModalDim.width"
      :animationStyle="msModalDim.animationStyle"
      :padding="msModalDim.padding"
      :modalClass="msModalDim.modalClass"
    >
      <template v-slot:header>
        <!-- <p class="modal-title fs-18 lh25 bold mb20 ml10">Sort by</p>
        <i @click="closeModal($event)" class="jfi-cross sticky-pop-cross"></i> -->
        <div
          class="d-flex justify-content-between align-items-center previous-order-header pTB16 plr16"
        >
          <div class="main-header">
            <span @click="closeModal(true, getPreviousOrder?.currentCartData)">
              <i class="jfi-back-v2 fs-14"></i>
            </span>
            <span class="fs-18 bold ml19"> Quick Order </span>
          </div>
          <div class="bold color-secondary" @click="viewAllHandler()">
            View All Orders
          </div>
        </div>
        <div
          class="pt12 plr16 previous-order-status d-flex justify-content-between align-items-center"
        >
          <span class="fs-12 color-base bold">
            {{ getPreviousOrder?.orderTime || "" }}
          </span>
          <span>
            <span class="rs">Rs.</span>{{ getPreviousOrder?.netPrice || "" }} |
            <span>{{ getPreviousOrder?.totalItems || "" }} items</span>
          </span>
        </div>
      </template>
      <template v-slot:body>
        <div
          class="pb16 pt8 plr16 reorder-edit-modal-body"
          v-if="getPreviousOrder.currentCartData.length > 0"
        >
          <div
            v-for="(data, ind) in getPreviousOrder.currentCartData || []"
            :key="ind"
          >
            <quick-order-items :propsData="data"></quick-order-items>
          </div>
        </div>
        <!-- <div
          class="d-flex justify-content-center align-items-center previous-order-footer pTB16 plr16"
        >
          <button
            class="fs-14 btn w100 bold"
            @click="addAllOrderIntoCart(getPreviousOrder.currentCartData, true)"
          >
            <span v-if="Number(cartQuantity) !== 0"
              ><i class="rs">Rs.</i>{{ Number(cartQuantity) }} |
            </span>
            Add all
          </button>
        </div> -->
        <!-- <ul class="list-wrap">
          <li
            class="list-item fs-14 lh20 pl10 pt10 pb10 flex-row"
            v-for="(item, index) in sortOptions"
            :key="index"
            @click="applyFilter($event, item)"
          >
            <span
              :class="{ dot: appliedSortFilters[0]?.title == item.title }"
              >{{ item.title }}</span
            >
            <span><i class="jfi-arrow-line ml7"></i></span>
          </li>
        </ul> -->
        <!-- <p
          class="clear-sort-cta fs-12 lh18 pl10 pt10 pb10 bold flex-row"
          @click="clearSortFilter($event)"
        >    max-height: 517px;
    overflow-y: auto;
          Reset
        </p> -->
      </template>
      <template v-slot:footer>
        <div
          class="d-flex justify-content-center align-items-center previous-order-footer pTB16 plr16"
        >
          <button
            class="fs-14 btn w100 bold"
            @click="addAllOrderIntoCart(getPreviousOrder.currentCartData, true)"
          >
            <span v-if="Number(cartQuantity) !== 0"
              ><i class="rs">Rs.</i>{{ Number(cartQuantity) }} |
            </span>
            Add all
          </button>
        </div>
        <div></div>
      </template>
    </generic-modal>
  </div>
</template>

<script>
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide } from "vue3-carousel";
import LocalStorageFunc from "../helpers/localStorageFunc";
import DoYouKnow from "./DoYouKnow.vue";
import {
  keyMapsGetter,
  keyMapsAction,
  keyMapsMutation,
} from "../helpers/storeKeys";
import GenericModal from "./GenericModal.vue";
import QuickOrderItems from "./QuickOrderItems.vue";
import utility from "../helpers/utility.js";
import { v2PwaBasePath } from "../config/apiEndpoints";
import setEvents from "../helpers/events";

export default {
  name: "ReOrder",
  components: {
    Carousel,
    Slide,
    DoYouKnow,
    GenericModal,
    QuickOrderItems,
  },
  data() {
    return {
      allSetting: [],
      showEditPreviousOrderModal: false,
      msModalDim: {
        width: "100vw",
        height: "100vh",
        animationStyle: "full-screen-popup",
        modalClass: "re-order-modal",
        padding: "0px",
      },
      isUpdated: false,
    };
  },
  mounted() {
    LocalStorageFunc.isLoggedIn() &&
      this.$store.dispatch(keyMapsAction.GET_REORDER_DATA, true);
  },
  updated() {
    if(!this.isUpdated && this.previousOrderPizza.length > 0) {
      if(this.previousOrderPizza[0]?.items && this.previousOrderPizza[0].items.length > 0) {
        this.eventFunction(false, this.previousOrderPizza[0].items, {});
        this.isUpdated = true;
      }
    }
  },
  methods: {
    getOrderItemArray(itemArr) {
      let itemList = [];
      for(let i=0; i< itemArr.length; i++) {
        itemList.push(itemArr[i].product ? itemArr[i].product.name : itemArr[i].name);
      }
      return itemList;
    },
    eventFunction(isClicked=false, itemArr=[], eObj={}) {
      let eventType = isClicked ? "Click" : "Impression";
      let userAction = isClicked ? "click" : "impression";
      let categoryArr = this.getOrderItemArray(itemArr);
      let preEventObj = { "category": categoryArr, ...eObj };
      const eventObj = utility.amplitudeEventObj(userAction, "quickOrder", "5", preEventObj);
      setEvents("amplitude", eventType, eventObj);
    },
    itemCount(data) {
      //  show the Do you know UI if item's count is less than 3
      let doYouKnowShow = false;
      if (data?.length >= 3 && data?.length !== undefined) {
        doYouKnowShow = false;
      } else {
        doYouKnowShow = true;
      }
      return doYouKnowShow;
    },
    addedCount(data) {
      // count the toppings
      let count = 0;
      let items = data.split(",");
      if (items.length > 4) {
        count = items.length - 4;
      } else {
        count = 0;
      }
      return count;
    },
    convertAllToppingIntoSingleString(data) {
      let toppngArry = data.map((i) => i.name); //  mapping all the topping items name
      return [...new Set(toppngArry)].join();
    },
    getItemData(data) {
      // find the obj which is matched according to menuCode
      let ary = [];
      if (data !== undefined) {
        ary = this.getProductItemsInOneArray.filter((i) => {
          if (data.map((j) => j.product.menuCode).includes(i.code)) {
            let qu = data?.find(k => k.product.menuCode === i.code)?.quantity
            if (i["sortOrder"]) {
              delete i["sortOrder"];
            }
            if (i["customCategoryId"]) {
              delete i["customCategoryId"]; //  for remove duplicay while edit the previous order data
            }
            if (i["customCategoryName"]) {
              delete i["customCategoryName"]; //  for remove duplicay while edit the previous order data
            }
            if(qu){
              i["quantity"] = qu
            }
            return i;
          }
        });
      }
      return utility.sortQniqueArry(ary);
    },
    passDataIntoCart(item) {
      // passed the selected items data into reOrder store
      utility.sortQniqueArry(this.getItemData(item)).forEach((i) => {
        this.setEditPreviousDataIntoStore({ ...i, quantity: 1 }); // it will push the current selected obj
      });
      this.displayDataInModal(this.getItemData(utility.sortQniqueArry(item)));
    },
    addAllOrderIntoCart(item, isModalOpen) {
      // add the all the previous order items into cart
      if (isModalOpen) {
        this.getPreviousOrder.currentCartData.forEach((i) => {
          this.$store.commit(keyMapsMutation.PREVIOUS_ORDER_ADD_TO_CART, i);
        });
        this.closeModal();
        this.eventFunction(true, item, {"actionType": "editScreen", "subCategory": "viewAdd+", "addToCart": 1});
      } else {
        utility.sortQniqueArry(this.getItemData(item)).forEach((i) => {
          this.$store.commit(keyMapsMutation.PREVIOUS_ORDER_ADD_TO_CART, i);
        });
        this.eventFunction(true, item, {"subCategory": "Add all", "addToCart": 1});
      }
    },
    setEditPreviousDataIntoStore(data) {
      this.$store.commit(keyMapsMutation.ADD_PREVIOUS_ORDER_DATA, data);
    },
    editPreviousOrder(data) {
      let obj = {};
      this.eventFunction(true, data.items, {"subCategory": "edit"});
      obj["netPrice"] = data?.netPrice || "";
      obj["totalItems"] = data?.items.length || "";
      obj["orderTime"] = this.dateCoversionFun(data?.orderTimeStamp);
      this.passDataIntoCart(utility.sortQniqueArry(data?.items) || []); // set the cartItems data into editPreviousOrderData
      this.$store.commit(keyMapsMutation.UPDATE_CURRENT_ITEM_DETAILS, obj);
      this.triggerModal();
      this.eventFunction(false, data.items, {"actionType": "editScreen"});
    },
    triggerModal(e) {
      document.body.classList.add("body-stop-scroll-mobile");
      this.showEditPreviousOrderModal = true;
    },
    closeModal(backButton=false, itemArr=[]) {
      this.$store.commit(keyMapsMutation.RESET_CART_ITEM_DATA); // reset the data while close the currentCartData
      this.$store.commit(keyMapsMutation.RESET_CURRENT_ITEM_DETAILS);
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showEditPreviousOrderModal = false;
      if(backButton) {
        this.eventFunction(true, itemArr, {"actionType": "editScreen", "subCategory": "backTap"});
      }
    },
    dateCoversionFun(data) {
      let date = String(new Date(data)).split(" ");
      return `Ordered on ${
        date[2] + utility.findOrdinalsSufixInDate(date[2])
      } ${date[1]} ${date[3]}`;
    },
    addQuuantityKey(data) {
      let str = [];
      data.forEach((k) => {
        str.push({ ...k, quantity: 1 });
      });
      return str;
    },
    displayDataInModal(data) {
      if (data?.length > 0 && this.cartItemData.length > 0) {
        let arr = utility.deepCopyArry(
          this.cartItemData.filter((i) => data.map((j) => j.id).includes(i.id))
        );
        this.$store.commit(
          keyMapsMutation.UPDATE_AND_CHECK_CURRENT,
          utility.sortQniqueArry(arr)
        );
        // return this.cartItemData.filter(i => this.getPreviousOrder.currentCartData.map(j => j.id).includes(i.id))
      } else if (data?.length > 0 && this.cartItemData?.length === undefined) {
        let obj = utility.sortQniqueArry(this.getProductItemsInOneArray);
        let arr1 = obj.filter((i) => data.map((j) => j.id).includes(i.id));
        this.$store.commit(
          keyMapsMutation.UPDATE_AND_CHECK_CURRENT,
          utility.deepCopyArry(this.addQuuantityKey(arr1))
        );
      }
    },
    viewAllHandler() {
      this.eventFunction(true, this.getPreviousOrder?.currentCartData, {"actionType": "editScreen", "subCategory": "viewAllOrders"});
      window.location.href = `${v2PwaBasePath}/orderHistory`;
    },
    reOrderDisplayFun(data) {
      // when we have more than 4 items in order so we have to slice to display less
      if (data?.length > 4 && data?.length !== undefined) {
        return data.slice(0, 4);
      } else {
        return data;
      }
    },
    totalReOrderCount(data){
      return data
        .reduce((acc, cartItem) => {
          return cartItem.quantity + acc;
        }, 0)
        .toFixed(2) || 0;
    },
    checkCurrentItemPresentOrNot(data){ // for identify the item is present in reorder data or not
      if(this.getProductItemsInOneArray && data) {
        return utility.getNotAvailablePizzaInStore(this.getProductItemsInOneArray, data)
      }
    },
    checkReorderButtonDisable(data){ // for disable the button if there are no items present in current store
      let isAva = []
      if (data[0]?.product !== undefined && this.getProductItemsInOneArray?.length !== undefined) {
        isAva = this.getProductItemsInOneArray.find((i) => data.map(j => j.product.menuCode).includes(i.code));
      }
      if (isAva && Object.keys(isAva)?.length > 0) {
        return false;
      } else {
        return true;
      }
    }
  },
  computed: {
    reOrderPizza() {
      return this.$store.state.ReOrder.reOrderData;
    },
    previousOrderPizza() {
      return this.$store.state.ReOrder.previousOrderData;
    },
    productItemsData() {
      return this.$store.state.ProductItems.productItems;
    },
    cartItemData() {
      return this.$store.state.Cart.cartItems;
    },
    productItemGettersData() {
      return this.$store.getters[keyMapsGetter.GET_PRODUCT_ITEMS];
    },
    getProductItemsInOneArray() {
      return this.$store.getters[keyMapsGetter.GET_PRODUCT_ITEMS_IN_ONE_ARRAY];
    },
    getPreviousOrder() {
      return this.$store.state.ReOrder.editPreviousOrderData;
    },
    cartQuantity() {
      return this.$store.getters[keyMapsGetter.CURRENT_CART_TOTAL];
    },
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
  },
};
</script>

<style scoped></style>
