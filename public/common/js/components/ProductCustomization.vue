<template>
  <div
    class="product-customization"
    v-if="result && Object.keys(result).length > 0"
  >
    <div class="fixed-top">
      <div class="c-img-wrap">
        <!-- <img class="card-img" :src="imgBaseUrl + '/' + result.image" alt="" /> -->


        <template v-if="result?.images?.length > 0">
          <template v-for="(img, index) in result?.images">
            <template v-if="img.mediaSize == 'HomeProductV1'">
              <v-lazy-image 
                class="card-img img-product-home" 
                alt="default image"
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
            :src="imgBaseUrl + '/' + result.image" 
            :src-placeholder="staticImgBaseUrl + '/images/global/default-product-img-lg.png'" 
          />
        </template>

      </div>
    </div>
    <div class="botm-pushed-content">
      <!-- product name and description -->
      <div
        class="name-desc plr16"
        id="setItemOnTop"
        ref="jsNameDesc"
        :class="{ 'sticky-top': isPizzaInfoSticky }"
        @scroll="test()"
      >
        <div class="card-botm-gradient"></div>
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
          <h3 class="pizza-desc lh16">
            {{ result.description }}
          </h3>
        </div>
      </div>
      <!-- product customizations -->
      <div class="bg-white pt24">
        <!-- choose size among crust -->
        <div class="crustwise-size-list mb30 plr16">
          <h3 class="fs-14 bold mb15">Change Size</h3>

          <ul>
            <li
              v-for="size in crustSizes"
              :key="size.sizeId"
              :class="['disp-col-inline jcsb mb20', size.isDisable]"
            >
              <span>
                <div class="radio-wrapper">
                  <label class="radio">
                    <input
                      type="radio"
                      :value="size.name"
                      :checked="Number(size.sizeId) === Number(result.sizeId)"
                      @change="totalAmountDisplay(size.sizeId, size.name)"
                    />
                    <!-- @click="takeCrustSizePrice(size.price)" -->

                    <i><span></span></i>
                    <span class="labl-txt"
                      >{{ size.name }}
                      <span v-if="size.description" class="fs-12"
                        >({{ size.description || "" }})</span
                      ></span
                    >
                  </label>
                </div>
              </span>

              <span class="fs-14"
                ><span v-if="size.price" class="rs">Rs.</span
                >{{ size.price }}</span
              >
            </li>
          </ul>
          <!-- fake sizes -->
        </div>

        <!-- choose crust  -->
        <div class="crust-list mb30">
          <h3 class="fs-14 bold mb15 plr16">Change Crust</h3>

          <ul class="plr16">
            <li
              class="disp-col-inline jcsb mb20"
              v-for="crust in crusts"
              :key="crust.crustId"
              :class="{ disabled: !disabledCrust(crust.sizes) }"
            >
              <span>
                <div class="radio-wrapper">
                  <label class="radio">
                    <input
                      type="radio"
                      :value="crust.name"
                      :checked="crust.name === result.crustName"
                      @change="updateCrustAndSize(crust, crust.name)"
                    />
                    <i><span></span></i>
                    <span class="labl-txt">{{ crust.name }}</span>
                  </label>
                </div>
              </span>
              <!-- <span>+<span class="rs">Rs.</span>{{defaultCrustPrice}} </span> -->
              <span class="fs-14"
                ><span v-if="takeCrustSize(crust.sizes)" class="rs">Rs.</span
                >{{ takeCrustSize(crust.sizes) || "" }}
              </span>
            </li>
          </ul>
          <ul>
            <li
              class="extra-cheese-burst disp-col-inline jcsb pt10 pb10 plr16 fs-14 color-slaty"
            >
              <span>
                <div class="checkbox-wrapper">
                  <label class="checkbox">
                    <input
                      type="checkbox"
                      id="cheese-burst"
                      :checked="toppingCheck(51)"
                      @click="addToppingToCart($event, cheeseTopping)"
                      :value="showToppingPrice(cheeseTopping.price)"
                    />
                    <!-- :checked="item.toppingId === result?.addTopngs[0]?.toppingId ? true : false"  -->
                    <!-- :checked="isChecked(item.toppingId)" -->
                    <i></i>
                  </label>
                  <label for="cheese-burst" class="vam">
                    <span>Add Extra cheese to crust</span>
                  </label>
                </div>
              </span>
              <span>
                <label>
                  <span>+&nbsp;<span class="rs">Rs.</span>{{ showToppingPrice(cheeseTopping.price) }}</span>
                </label>
              </span>

              <!-- <div class="checkbox-wrapper">
              <label class="ml16 checkbox">
                <input
                  type="checkbox"
                  id="cheese-burst"
                  :value="51"
                  :checked="toppingCheck(51)"
                  @click="addExtraCheeseBurst($event, 95)"
                />
                <i></i>
                Add Extra cheese to crust
              </label>
              <label for="cheese-burst">
                <span class="labl-txt"
                  >+<span class="rs">Rs.</span> 95.00
                </span>
              </label>
            </div> -->
            </li>
          </ul>
        </div>

        <!-- choose Veg Toppings  -->
        <div
          class="veg-toppings-list mb30 plr16"
          v-if="finalVegToppings.length > 0"
        >
          <h3 class="fs-14 bold mb15 disp-col-inline">
            Add veg toppings <i class="ml8 tag-veg"></i>
          </h3>
          <ul v-for="(item, index) in finalVegToppings || []" :key="index">
            <li
              class="disp-col-inline jcsb mb20"
              @click="
                disableCheckboxWithEventFire(item) && thisCombinationNotAllow()
              "
            >
              <span
                :class="{
                  disabled: disableCheckboxWithEventFire(item),
                }"
              >
                <div class="checkbox-wrapper">
                  <label class="checkbox">
                    <input
                      type="checkbox"
                      :id="'test' + index"
                      :value="showToppingPrice(item.price)"
                      :checked="toppingCheck(item.toppingId)"
                      @click="
                        !disableCheckboxWithEventFire(item) &&
                          addToppingToCart($event, item)
                      "
                    />
                    <!-- :checked="item.toppingId === result?.addTopngs[0]?.toppingId ? true : false"  -->
                    <!-- :checked="isChecked(item.toppingId)" -->
                    <i></i>
                  </label>
                  <label :for="'test' + index" class="vam">
                    <span class="labl-txt">{{ item.name }} </span>
                  </label>
                </div>
              </span>

              <span class="fs-14"
                >+&nbsp;<span class="rs">Rs.</span
                >{{ showToppingPrice(item.price) }}</span
              >
            </li>
          </ul>
        </div>

        <!-- choose Non Veg Toppings  -->
        <div
          class="nonveg-toppings-list mb30 plr16"
          v-if="finalNonVegToppings.length > 0"
        >
          <h3 class="fs-14 bold mb15 disp-col-inline">
            Add non-veg toppings <i class="ml8 tag-non-veg"></i>
          </h3>
          <ul v-for="(item, index) in finalNonVegToppings || []" :key="index">
            <li
              class="disp-col-inline jcsb mb20"
              @click="
                disableCheckboxWithEventFire(item) && thisCombinationNotAllow()
              "
            >
              <span
                :class="{
                  disabled: disableCheckboxWithEventFire(item),
                }"
              >
                <div class="checkbox-wrapper">
                  <label class="checkbox">
                    <input
                      type="checkbox"
                      :id="'nv' + index"
                      :value="showToppingPrice(item.price)"
                      :checked="
                        toppingCheck(item.toppingId) &&
                        !(
                          replacedSelectedToppingCustomAttri ===
                          item.custAttrField
                        )
                      "
                      @click="
                        !disableCheckboxWithEventFire(item) &&
                          addToppingToCart($event, item)
                      "
                    />
                    <i></i>
                  </label>
                  <label :for="'nv' + index" class="vam">
                    <span class="labl-txt">{{ item.name }}</span>
                  </label>
                </div>
              </span>

              <span class="fs-14"
                >+&nbsp;<span class="rs">Rs.</span
                >{{ showToppingPrice(item.price) }}</span
              >
            </li>
          </ul>
          <!-- {{toppingFormStore}} -->
        </div>

        <div
          class="nonveg-toppings-list mb30 plr16"
          style="padding-bottom: 300px"
          v-if="finalVegToppings.length > 0 || finalNonVegToppings.length > 0"
        >
          <div
            class="disp-col-inline jcsb"
            v-if="replaceToppingDisplay.length > 0"
          >
            <div class="">
              <h3 class="fs-14 bold mb3">Replace existing topping</h3>
              <div class="fs-11 mb17 color-slaty">Repalce only 1 topping</div>
            </div>
            <a @click="resetReplaceTopping()" class="bold cursor-pointer" v-if="replacedToppingName"
              >Reset</a
            >
          </div>
          <ul v-for="(data, index) in replaceToppingDisplay" :key="index">
            <li
              :class="[
                'disp-col-inline jcsb mb20 cursor-pointer',
                Number(data.toppingId) !== Number(replaceToppingDisableKey) &&
                  replaceDisable &&
                  'disabled',
              ]"
            >
              <span>
                <div class="checkbox-wrapper">
                  <label @click="replaceToppingFun(data)">
                    <i class="mr8 jfi-arrow-switch"></i>
                    <span class="labl-txt">{{ data.name }}</span>
                    <span
                      class="labl-txt"
                      v-if="
                        Number(data.toppingId) == Number(replaceToppingDisableKey) &&
                        replacedToppingName &&
                        replaceDisable
                      "
                    >
                      with {{ replacedToppingName }}</span
                    >
                  </label>
                </div>
              </span>
            </li>
          </ul>
          <!-- {{toppingFormStore}} -->
        </div>
        <div v-else style="padding-bottom: 200px"></div>
      </div>

      <!-- bottom fixed -->
      <div class="customised-details" ref="custDetailsElem">
        <div class="disp-col-inline jcsb">
          <div class="fs-14">
            <span>{{ msize }}</span>
            <span class="separater-before">{{ mcrust }}</span>
          </div>
          <a @click="resetAll()" class="bold" v-if="result.addTopngs?.length > 0">Reset all</a>
        </div>

        <!-- toppings updated -->
        <div class="lh18 mt5">
          <span v-if="result.addTopngs.length > 0"
            >Added:
            <span class="color-grey">{{
              tping(result.addTopngs) || ""
            }}</span
            >.</span
          >
          <span
            v-if="repToppings?.length > 0 && remToppings?.length > 0"
            >Replaced:
            <span class="color-grey">{{ replaceTopDis(repToppings, remToppings) || "" }}.</span></span
          >
        </div>

        <div class="btn mt12 bold custm-cta" @click="addCustomization()">
          <span
            ><i class="rs">Rs.</i>
            {{
              Number(totalCartPrice || 0) +
                Number(
                  (totalToppingPriceWhichIsAdded.length > 0
                    ? totalToppingPriceWhichIsAdded
                    : [0]
                  )?.reduce((a, b) => Number(a) + Number(b), 0)
                ) || 0
            }}</span
          >
          <span class="separater-before" v-if="!lastEdit"
            >Add <span class="ico-plus">&#43;</span>
          </span>
          <span v-else class="separater-before">Update </span>
        </div>
      </div>
    </div>
    <!-- Not allow this combination UI -->

    <!-- Replace Topping Ui -->
    <generic-modal
      v-if="showReplaceToppingModal"
      @close="closeReplaceToppingModal()"
      :height="addOrEditModalDim.height"
      :width="addOrEditModalDim.width"
      :animationStyle="addOrEditModalDim.animationStyle"
      :modalClass="addOrEditModalDim.modalClass"
    >
      <template v-slot:header>
        <div class="pt16 pl15">
          <span class="fs-18 bold"
            >Replace {{ selectForReplaceTopping }} with</span
          >
          <i
            @click="closeReplaceToppingModal()"
            class="jfi-close-v2 sticky-pop-cross"
          ></i>
        </div>
      </template>
      <template v-slot:body>
        <div class="mt30 pl15 pr15">
          <!-- last customosed crust and size -->
          <div class="veg-toppings-list mb30">
            <h3 class="fs-14 bold mb18 disp-col-inline">
              Veg toppings <i class="ml8 tag-veg"></i>
            </h3>
            <ul v-for="(item, index) in finalVegToppings || []" :key="index">
              <li
                :class="[
                  'disp-col-inline jcsb mb12',
                  finalVegToppings.length - 1 !== index && 'border-bottom-light',
                  Number(item.toppingId) == Number(replaceToppingDisableKey) && 'disabled',
                ]"
                @click="saveReplaceToppingName(item)"
              >
                <span>
                  <div class="checkbox-wrapper">
                    <div class="pb10 cursor-pointer fs-14">
                      {{ item.name }}
                    </div>
                  </div>
                </span>
                <span class="fs-14 replace-topping-arrow cursor-pointer pb10"
                  ><i class="jfi-arrow-line"></i
                ></span>
              </li>
            </ul>
          </div>

          <div class="nonveg-toppings-list mb30">
            <h3 class="fs-14 bold mb18 disp-col-inline">
              Non-veg toppings <i class="ml8 tag-non-veg"></i>
            </h3>
            <ul v-for="(item, index) in finalNonVegToppings || []" :key="index">
              <li
                :class="[
                  'disp-col-inline jcsb mb20',
                  finalNonVegToppings.length - 1 !== index && 'border-bottom',
                  Number(item.toppingId) == Number(replaceToppingDisableKey) && 'disabled',
                ]"
                @click="saveReplaceToppingName(item)"
              >
                <span>
                  <div class="checkbox-wrapper">
                    <div class="pb10 cursor-pointer fs-14">
                      {{ item.name }}
                    </div>
                  </div>
                </span>
                <span class="fs-14 replace-topping-arrow pb10"
                  ><i class="jfi-arrow-line"></i
                ></span>
              </li>
            </ul>
            <!-- {{toppingFormStore}} -->
          </div>
        </div>
        <div class="no-combination" v-if="combinationNotAllowModal">
          <div class="bold pTB20 plr16 no-combination-msg">
            <div>
              <span class="fs-18 bold">Not Allowed!!!</span>
              <i
                @click="combinationNotAllowcloseModalM()"
                class="jfi-close-v2 sticky-pop-cross"
              ></i>
            </div>
            <div class="pt10 pl15">
            </div>
            <div class="fs-14 lh18 color-slaty">
            Sorry this combination of toppings is not allowed. Please try a
            different combination!
            </div>
          </div>
        </div>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
</script>
<script>
import { imgBaseUrl, staticImgBaseUrl } from "../config/apiEndpoints";
import { keyMapsMutation } from "../helpers/storeKeys";
import GenericModal from "./GenericModal.vue";
import __ from "../helpers/utility.js";
import utility from "../helpers/utility.js";
import setEvents from "../helpers/events";

export default {
  name: "ProductCustomization",
  props: ["dataResult", "lastEdit", "newFeature", "dataCustomiseType", "dataType", "dataAddCounter", "dataIndex"],
  emits: ["close"],
  components: {
    GenericModal,
  },
  data() {
    return {
      imgBaseUrl: imgBaseUrl,
      staticImgBaseUrl: staticImgBaseUrl,
      isPizzaInfoSticky: false,
      result: {},
      crusts: [],
      mcrust: "",
      crustSizes: [],
      msize: "",
      msizeId: 0,
      currentCustomisation: {},
      elemHeight: 0,
      productResult: {},
      defaultCrustPrice: 0,
      defaultCrustSizes: [
        { name: "Regular (Serves 1)", isDisable: "disabled", sizeId: 6 },
        { name: "Medium (Serves 2)", isDisable: "disabled", sizeId: 7 },
        { name: "Large (Serves 4)", isDisable: "disabled", sizeId: 8 },
      ],
      totalCartPrice: 0,
      finalVegToppings: [], //type = 0 is for veg toppings
      finalNonVegToppings: [], // type == 1  is for non-veg toppings
      allToppingDisplay: [],
      replaceToppingDisplay: [],
      addOrEditModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: ``,
      },
      showReplaceToppingModal: false,
      replaceToppingDisableKey: 0, // key which will disable the key we have seleected
      // replaceDisable: false, //  for the first not disabled the replace key that why we have to set temp key for that
      replacedToppingName: "", //  after replaced the topping
      selectForReplaceTopping: "",
      remToppings: [],
      repToppings: [],
      currentSelectedTopping: [],
      selectedToppingCustomAttribute: "",
      replacedSelectedToppingCustomAttri: "",
      totalToppingPriceWhichIsAdded: [],
      addAndReplaceToppingStore: {
        addedTopping: {},
        replacedTopping: {},
      },
      allRecentToppingCustomAtrr: [],
      combinationNotAllowModal: false,
      resetData: false,
      cheeseTopping: {price:{"6":"50.00","7":"75.00","8":"95.00"}, toppingId:51, description:"", name:"Extra Cheese", images:"extraCheese.jpg", type:2, custAttrField:"C."}
    };
  },
  mounted() {
    this.result = __.deepCopyArry(this.dataResult);
    this.crusts = this.result.crust;
    this.crustSizes = [];
    this.initialSelectedCrust(this.result);
    this.getDistinctSizes();
    this.getHeight();
    this.totalAmountDisplay();
    this.namesOfDefaultToppings();
    this.finalVegToppings = this.toppingFormStore.filter((i) => {
      if (i.type === 0) {
        return this.result.allowedToppings.includes(i.toppingId);
      }
    });
    this.finalNonVegToppings = this.toppingFormStore.filter((i) => {
      if (i.type === 1) {
        return this.result.allowedToppings.includes(i.toppingId);
      }
    });


    if (!this.lastEdit) {
      //  if we click add new then previous toppings should not be displayed
      this.allToppingDisplay = [];
      this.result.addTopngs = [];
      this.result.repTopngs = [];
      this.result.remTopngs = [];
      this.result.crustName = this.result.defaultCrustName || "";
      this.result.sizeName = this.result.defaultSizeName || "";
      this.result.sizeId = this.result.selectedSizeId || "";
    } else {
      if (this.result.addTopngs?.length > 0) {
        this.allToppingDisplay =  this.result.addTopngs
      }
    }
    // set the default crust price of pizza
    this.defaultCrustPrice = this.result?.defaultCrustPrice || 0;

    // for customization popup
    let _this = this;
    window.addEventListener("scroll", _this.handleScroll());
    if (this.result.remTopngs) {
      this.replaceToppingDisableKey = this.result.remTopngs[0] || 0;
      this.replacedToppingName =
        this.toppingFormStore.find(
          (i) => i.toppingId == this.result.repTopngs[0]
        )?.name || "";
    }

    this.getTheCurrentPriceOfItem() //  for getting current updated price
    // if (this) {
    //   this.totalToppingPriceWhichIsAdded.push();
    // }

    // if (this.$store.state.Cart.isAddedItemIsNew) {
    //   this.allToppingDisplay = [];
    //   this.result.addTopngs = [];
    // }

    // let ary = this.allCartToppingItem.filter(i => i.id == this.result.id) || []
    // if(ary.length > 0){
    //   this.result.addTopngs = ary[ary.length - 1].addTopngs.map(i => Number(i))
    // } else {
    //   this.result.addTopngs = [];
    // }

    if(this.result.repTopngs && this.result.remTopngs){
      this.repToppings = this.result.repTopngs
      this.remToppings = this.result.remTopngs
    }
  },
  destroyed() {
    // for customization popup
    let _this = this;
    // window.removeEventListener("scroll", _this.handleScroll);
  },
  methods: {
    prepareImgPath(url) {
      if (url.indexOf("https:") != -1) {
        return url;
      } else {
        return this.imgBaseUrl + "/" + url;
      }
    },
    getEventObj() {
      let returnObj = {
        "entry": this.dataCustomiseType ? this.dataCustomiseType : "Recommended",
        "category": this.result?.name,
        "addCounter": this.dataAddCounter? this.dataAddCounter : "1",
        "changeSize": this.result?.sizeName,
        "changeCrust": this.result?.crustName,
        "addVegToppings": "none",
        "addNonVegToppings": "none",
        "replaceToppings": '0',
        "replaceToppingsFrom": "none",
        "replaceToppingsTo": "none",
      };
      if(this.result.addTopngs?.length > 0) {
            let vegToppingName = [];
            let nonVegToppingName = [];
            this.toppingFormStore.forEach((i) => {
              if (i.type === 0) {
                if(this.result.addTopngs.includes(i.toppingId)) {
                  vegToppingName.push(i.name);
                }
                return this.result.addTopngs.includes(i.toppingId);
              } else if (i.type === 1) {
                if(this.result.addTopngs.includes(i.toppingId)) {
                  nonVegToppingName.push(i.name);
                }
              }
            });
            returnObj["addVegToppings"] = vegToppingName.join(',');
            returnObj["addNonVegToppings"] = nonVegToppingName.join(',');
          }
          if(this.result.remTopngs?.length > 0) {
            returnObj["replaceToppings"] = this.result.remTopngs.length;
            let replaceToppingsFrom = [];
            let replaceToppingsTo = [];
            this.toppingFormStore.forEach((i) => {
              if(this.result.remTopngs.includes(i.toppingId)) {
                replaceToppingsFrom.push(i.name);
              }
              if(this.result.repTopngs.includes(i.toppingId)) {
                replaceToppingsTo.push(i.name);
              }
            });
            returnObj["replaceToppingsFrom"] = replaceToppingsFrom.join(',');
            returnObj["replaceToppingsTo"] = replaceToppingsTo.join(',');
          }
      return returnObj;
    },
    test(){
      console.log('scroll', this.$refs.motherFather.jsNameDesc);
    },
    getDistinctSizes() {
      //
    },
    disabledCrust(data) {
      //  disable the crust according to size
      return data.map((j) => j.name).includes(this.msize);
    },
    checkCrustData(data) {
      let validate = false;
      this.crustSizes.map((i) => {
        if (Number(i.sizeId) === Number(data.sizeId)) {
          validate = true;
        }
      });
      return validate;
    },
    getSizesFromSelectedCrust(selCrustObj) {
      // get the available sizes according to selected crust
      let selCrustSizes = [];
      selCrustSizes = selCrustObj.sizes;
      this.crustSizes = selCrustSizes;
      let newArr1 = this.defaultCrustSizes.filter(
        (i) => !this.checkCrustData(i)
      );
      newArr1.forEach((j) => {
        let crustSel = [...this.crustSizes, j];
        this.crustSizes = crustSel.sort((a, b) => a.sizeId - b.sizeId); // for sorting the pizza sizes
      });
    },
    updateCrustAndSize(selCrustObj, crustNamePrm) {
      if (crustNamePrm) {
        this.mcrust = crustNamePrm;
      }
      //  update the current selected crust and sizes
      this.result.crustId = selCrustObj.crustId;
      this.result.crustName = selCrustObj.name;
      this.mcrust = this.result.crustName;
      this.msize = this.result.sizeName;
      this.getSizesFromSelectedCrust(selCrustObj);
      this.totalAmountDisplay();

      //@todo - update result object now...
    },
    takeCrustSize(arr) {
      // set the price according to crust
      let crustPr = arr.filter((i) => i.name === this.msize)[0]?.price || 0;
      return crustPr;
    },
    totalAmountDisplay(id, sizeNamePrm) {
      if (sizeNamePrm) {
        this.msize = sizeNamePrm;
      }
      // display the amount of sizes when we change the sizes
      this.crusts.forEach((i) => {
        if (i.name && i.name === this.mcrust && this.mcrust) {
          this.totalCartPrice =
            i.sizes.filter((j) => j.name === this.msize)[0]?.price || 0;
        }
      });
      if (id) {
        this.msizeId = id; // sizename is not updated when we select last edit pizza for edit that's why we update it forcely
        this.result.sizeName = this.msize;
        this.result.crustName = this.mcrust;
        this.result.sizeId = id;
      }
    },
    showToppingPrice(data) {
      // created a custom function to show the price according to sizes
      switch (this.msize) {
        case "Regular":
          return data[6];
          break;
        case "Medium":
          return data[7];
          break;
        case "Large":
          return data[8];
          break;
        default:
          return data[6];
          break;
      }
    },
    initialSelectedCrust(result) {
      let crustArr = result.crust ? result.crust : [];
      let initialSelectedCrustId = result.crustId;
      let selectedCrustArr = crustArr.filter(function (c) {
        return c.crustId == initialSelectedCrustId;
      });
      //pass selected crust obj to updateCrustAndSize
      this.updateCrustAndSize(selectedCrustArr[0]);
    },
    changeTheProductType(product) {
      let isTag = [];
      if (product && this.toppingFormStore && this.result.productType === 0) {
        this.toppingFormStore.forEach((i) => {
          if (product.includes(i.toppingId)) {
            isTag.push(i.type);
          }
        });
      }
      return isTag.find((i) => i === 1) ? 1 : 0;
    },
    addCustomization() {
      //@todo - preparing customization object
      this.currentCustomisation[this.result.id] = {
        ...this.currentCustomisation,
        ...{
          selectedSizeName: this.result.sizeName,
          selectedSizeId: this.result.sizeId,
          selectedSizeCode: this.result.sizeCode,
          selectedCrustName: this.result.crustName,
          selectedCrustId: this.result.crustId,
          addTopngs: [...new Set(this.result.addTopngs)],
          extraCheeseAdded: false,
          keepToppings: true,
          remTopngs: this.remToppings,
          repTopngs: this.repToppings,
          tempProductType:
            this.changeTheProductType(this.result.addTopngs || []) || 0,
          // productType: this.changeTheProductType(this.result.addTopngs || []) || this.result.productType, // for change the product type if we added a non-veg topping in veg pizza
          itemPrice:
            Number(
              this.result.crust
                .filter((_) => {
                  return _.crustId == this.result.crustId;
                })[0]
                ?.sizes.filter((_) => {
                  return _.sizeId == this.result.sizeId;
                })[0]?.price
            ) +
            Number(
              (this.totalToppingPriceWhichIsAdded.length > 0
                ? this.totalToppingPriceWhichIsAdded
                : [0]
              )?.reduce((a, b) => Number(a) + Number(b), 0)
            ), //  for topping price into itemPrice
        },
      };

      // let newData = {...this.addAndReplaceToppingStore}
      if (this.selectForReplaceTopping && this.replacedToppingName) {
        this.addAndReplaceToppingStore.replacedTopping[this.result.id] = [
          `${this.selectForReplaceTopping} ${
            this.selectForReplaceTopping && "with"
          } ${this.replacedToppingName}`,
        ];
      } else {
        this.addAndReplaceToppingStore.replacedTopping[this.result.id] = [];
      }

      if (this.allCartToppingItem.length > 0) {
        let arr = [];
        this.toppingFormStore?.forEach((i) => {
          if (this.result.addTopngs.includes(i.toppingId)) {
            arr.push(i.name);
          }
        }) || [];
        this.addAndReplaceToppingStore.addedTopping[this.result.id] = arr;
      } else {
        this.addAndReplaceToppingStore.addedTopping[this.result.id] =
          this.allToppingDisplay || [];
      }

      //@todo - updating result object with the parameters customised(size,crust,toppings...etc)
      let {
        selectedSizeName,
        selectedSizeId,
        selectedSizeCode,
        selectedCrustId,
        selectedCrustName,
        addTopngs,
        remTopngs,
        repTopngs,
        itemPrice,
        tempProductType,
      } = this.currentCustomisation[this.result.id];
      let updatedResultObj = {
        sizeName: selectedSizeName,
        sizeId: selectedSizeId,
        sizeCode: selectedSizeCode,
        crustId: selectedCrustId,
        crustName: selectedCrustName,
        addTopngs: addTopngs,
        remTopngs: remTopngs,
        repTopngs: repTopngs,
        itemPrice: itemPrice,
        isUserCustomizable: true,
        tempProductType: tempProductType, // for change the product type if we added a non-veg topping in veg pizza
      };
      this.updateResultObj(updatedResultObj);

      this.$emit("close");
      this.$store.commit(keyMapsMutation.UPDATE_IS_CUSTOMIZE, true); //  for customization modal close and open
      if(this.dataType) {
        let preEventObj = this.getEventObj();
        if(this.lastEdit) {
          preEventObj["update"] = 1;
          preEventObj["actionType"] = "upsellCustomise"
        } else {
          preEventObj["add"] = 1;
        }
        const eventObj = utility.amplitudeEventObj("click", this.dataType + " card", "0", preEventObj);
        setEvents("amplitude", "Click", eventObj);
        //pizza added event
        let pizzaObj = {
          "positionWithinSection": this.dataIndex ? this.dataIndex+1 : 1, 
          "category": this.result?.name, 
          "addCounter": this.dataAddCounter? this.dataAddCounter : "1",
        }
        const sendObj = utility.amplitudeEventObj("click", "Pizza added", "8", pizzaObj);
        setEvents("amplitude", "Click", sendObj);
      }

      // this.$store.commit(
      //   keyMapsMutation.UPDATE_CART_ITEM_PRICE_ARRAY,
      //   this.totalToppingPriceWhichIsAdded || []
      // );
    },
    updateResultObj(obj) {
      this.$emit("item-customised");
      this.$store.commit(keyMapsMutation.UPDATE_CART, [
        { ...this.result, ...obj },
        this.lastEdit,
      ]); //  this is the special where we have to send lastEdit Status
      this.$store.commit(keyMapsMutation.UPDATE_UNIQUE_CART_ITEMS, {
        id: this.result.id,
      });
      if (this.result.id) {
        this.$store.commit(keyMapsMutation.UPDATE_SELECTED_TOPPINGS, [
          this.addAndReplaceToppingStore,
          this.result.id,
        ]);
      }
      // this.$store.commit(
      //   keyMapsMutation.UPDATE_CART_ITEM_PRICE_ARRAY,
      //   this.totalToppingPriceWhichIsAdded
      // );
      this.$emit("item-customised");
      // this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, true);
      this.$store.commit(
        keyMapsMutation.UPDATE_CURRENT_SEL_TOPPING_OBJ,
        this.currentSelectedTopping
      );
    },
    handleScroll() {
      let elem = document.querySelector(".name-desc");
      if (elem) {
        if (window.scrollY >= 200) {
          this.isPizzaInfoSticky = true;
        } else if (window.scrollY < 200) {
          this.isPizzaInfoSticky = false;
        }
      }
    },
    getHeight() {
      let elem = this.$refs.custDetailsElem;
      this.elemHeight = elem?.offsetHeight;
      return this.elemHeight;
    },
    addToppingToCart(e, item) {
      //  add topping to cart
      if (e.target.checked) {
        this.result.addTopngs.push(item.toppingId);
        this.totalToppingPriceWhichIsAdded.push(e.target.value);

        this.allToppingDisplay.push(item.name);
        this.currentSelectedTopping.push(item);
        this.selectedToppingCustomAttribute = item.custAttrField;

        this.allRecentToppingCustomAtrr.push(item.custAttrField);
      } else {
        this.allRecentToppingCustomAtrr.splice(
          this.allRecentToppingCustomAtrr.findIndex(
            (a) => a == item.custAttrField
          ),
          1
        );
        this.result.addTopngs.splice(
          this.result.addTopngs.findIndex((a) => a == item.toppingId),
          1
        );
        // }
        this.allToppingDisplay.splice(
          this.allToppingDisplay.findIndex((a) => a == item.name),
          1
        );

        this.currentSelectedTopping.splice(
          this.currentSelectedTopping.findIndex(
            (a) => a.toppingId == item.toppingId
          ),
          1
        );

        this.totalToppingPriceWhichIsAdded.splice(
          this.totalToppingPriceWhichIsAdded.findIndex(
            (a) => a == e.target.value
          ),
          1
        );
        this.checkToppingNotSelect();
      }
    },
    checkItemCustomizedChanges(arr){
      let newObj = arr[arr.length - 1]
      if(newObj){
        if(newObj.isUserCustomizable){
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    },
    toppingCheck(id) {

        //     if (this.result?.addTopngs?.length !== undefined) {
        // if (this.result.addTopngs.includes(id)) {
      let check = false;
      // check the topping is checked or not
      if (this.result?.addTopngs?.length !== undefined) {
        // let ary = this.allCartToppingItem.filter(i => i.id == this.result.id)
        if (this.result.addTopngs.includes(id)) {
          return (check = true);
        } else {
          return (check = false);
        }
      }
      return check;
    },
    namesOfDefaultToppings() {
      // get the toppings name through topping id
      this.replaceToppingDisplay = this.toppingFormStore.filter((i) =>
        this.result.defaultToppings?.includes(i.toppingId)
      );
    },
    replaceToppingFun(data) {
      // replace topping function
      this.replaceToppingDisableKey = data.toppingId;
      this.triggerReplaceToppingModal();
      this.selectForReplaceTopping = data.name || "";
      this.remToppings = [data.toppingId];
    },
    resetReplaceTopping() {
      //  reset the replace topping object
      this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
      this.replacedToppingName = "";
      this.remToppings = [];
      this.repToppings = [];
      this.result.remTopngs = [];
      this.result.repTopngs = [];
      this.replacedSelectedToppingCustomAttri = "";
    },
    checkToppingNotSelect() {
      if (!(this.currentSelectedTopping.length > 0) && !this.newFeature) {
        // when we have check any topping then replace topping should reset again
        this.resetReplaceTopping();
      }
    },
    triggerReplaceToppingModal() {
      document.body.classList.add("body-stop-scroll-mobile");
      this.showReplaceToppingModal = true;
    },
    closeReplaceToppingModal() {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showReplaceToppingModal = false;
      if (this.remToppings && !this.repToppings) {
        this.repToppings = [];
      }
      this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, false);
    },
    checkAllowToppingExistInDefaultToppings(data) {
      let p = this.replaceToppingDisplay.find((i) =>
        i.custAttrField.includes(data)
      );
      return (p?.length !== undefined && p.length) > 0;
    
    },
    saveReplaceToppingName(data) {
      this.checkDefaultToppingAddValidation(data);
      this.$store.commit(keyMapsMutation.UPDATE_DISABLE_REPLACE_NAME, true);
    },
    disableCheckboxWithEventFire(item) {
      if (
        this.replacedSelectedToppingCustomAttri == item.custAttrField &&
        this.checkAllowToppingExistInDefaultToppings(
          this.replacedSelectedToppingCustomAttri
        )
      ) {
        return true;
      } else {
        return false;
      }
    },
    thisCombinationNotAllow() {
      alert(
        "Sorry this combination of toppings is not allowed. Please try a different combination!"
      );
    },
    addExtraCheeseBurst(e, data) {
      if (e.target.checked) {
        this.result.addTopngs.push(Number(e.target.value));
        this.totalToppingPriceWhichIsAdded.push(data);
      } else {
        this.result.addTopngs.splice(
          this.result.addTopngs.findIndex((a) => a == Number(e.target.value)),
          1
        );
        this.totalToppingPriceWhichIsAdded.splice(
          this.totalToppingPriceWhichIsAdded.findIndex((a) => a == data),
          1
        );
      }
    },
    resetAll() {
      if(this.dataType) {
        let preEventObj = this.getEventObj();
        if(this.lastEdit) {
          preEventObj["actionType"] = "upsellCustomise"
        } else {
          preEventObj["resetAll"] = 1;
        }
        const eventObj = utility.amplitudeEventObj("click", this.dataType + " card", "0", preEventObj);
        setEvents("amplitude", "Click", eventObj);
      }
      this.allToppingDisplay = [];
      this.$store.commit(keyMapsMutation.REMOVE_CURRENT_SEL_TOPPING_OBJ, {
        id: this.result.id,
      });

      // if(){ // if there is not item in cart
      //   this.finalVegToppings = [];
      //   this.finalNonVegToppings = [];
      // } else { // if there is item in cart

      // }
      // let ary = this.allCartToppingItem.filter(i => i.id == this.result.id)
      // ary[ary.length - 1].addTopngs = []
      this.result.addTopngs = [];
      this.$store.commit(keyMapsMutation.RESET_CURRENT_SEL_TOPPING_OBJ);
      this.resetReplaceTopping();
      this.totalToppingPriceWhichIsAdded = [];
    },
    repToppingDisplay(data) {
      switch (data) {
        case true:
          return [];
          break;
        case false:
          return this.replacedToppingText[this.result.id];
        default:
          break;
      }
    },
    replaceTopDis(rep, rem) {
      let replaceTopDisplay = ""
      if (rep && rem) {
        let removeTop = this.toppingFormStore.find(
          (i) => Number(i.toppingId) === Number(rep[0])
        );
        let addTop = this.toppingFormStore.find(
          (i) => Number(i.toppingId) === Number(rem[0])
        );
        if (removeTop && addTop) {
          replaceTopDisplay = `${removeTop.name} with ${addTop.name}` || "";
        }
      }
      return replaceTopDisplay  || ""  
    },
    checkDefaultToppingAddValidation(data) {
      let element = document.getElementById("generic-modal-mask-id");
      let defaultAtrr = __.defaultToppingsCustomAtrrArry(
        this.result.defaultToppings,
        this.toppingFormStore
      );
      if (
        this.allRecentToppingCustomAtrr.includes(data.custAttrField) &&
        defaultAtrr.includes(data.custAttrField)
      ) {
        this.combinationNotAllowModal = true;
        element.classList.add("overflow-y-hiddn");
      } else {
        this.replacedToppingName = data.name || "";
        this.repToppings = [data.toppingId];
        this.closeReplaceToppingModal();
        this.replacedSelectedToppingCustomAttri = data.custAttrField; // this for reverse checking after selecting the replaced item and then click the topping
      }
    },
    combinationNotAllowcloseModalM() {
      let element = document.getElementById("generic-modal-mask-id");
      this.combinationNotAllowModal = false;
      element.classList.remove("overflow-y-hiddn");
    },
    tping(item) {
      // to get the current toppings name
      if (this.toppingFormStore?.length !== undefined) {
        let j = this.toppingFormStore.filter(i =>  item.includes(Number(i.toppingId))).map(k => k.name)
        return j && j.join() || ""
      }
    },

    getTheCurrentPriceOfItem(){
      if(this.result.addTopngs){
        let customToppingPriceary = []
        this.result.addTopngs.forEach(i => {
          customToppingPriceary.push(this.showToppingPrice(this.toppingFormStore.find(j => Number(j.toppingId) === Number(i)).price))
        })
        this.totalToppingPriceWhichIsAdded = customToppingPriceary || []
      }
    }
  },
  computed: {
    toppingFormStore() {
      return this.$store.state.Cart.toppingItems;
    },
    replaceDisable() {
      return this.$store.state.Cart.replaceDisable;
    },
    selectedToppings() {
      return (
        this.$store.state.Cart.selectedAndReplacedToppings.addedToppings || {}
      );
    },
    replacedToppingText() {
      return (
        this.$store.state.Cart.selectedAndReplacedToppings.replacedToppings ||
        {}
    )
    },
    getToppingCartItemArray() {
      return this.$store.state.Cart.itemPriceToppingArray || [];
    },
    getCurrentSelToppObj() {
      return this.$store.state.Cart.currentSelToppObj || [];
    },
    allCartToppingItem() {
      return this.$store.state.Cart.cartItems || [];
    },
    ifAddedTextShow() {
      return this.allCartToppingItem[this.allCartToppingItem.length - 1]
        ?.addTopngs !== undefined &&
        this.allCartToppingItem[this.allCartToppingItem?.length - 1]?.addTopngs
          ?.length > 0
        ? true
        : false;
    },
    ifReplaceTextShow() {
      return this.allCartToppingItem[this.allCartToppingItem?.length - 1]
        ?.repTopngs !== undefined &&
        this.allCartToppingItem[this.allCartToppingItem?.length - 1]?.repTopngs
          ?.length > 0
        ? true
        : false;
    },
  },
};
</script>

