/* not being used */
<template>
  <div class="card-item" :class="{ 'card-item-search': isSearchable }">
    <div class="card-size" :class="{ 'card-rounded': isRounded }">
      <div class="c-img-wrap">
        <img
          class="card-img"
          src="../../../dist/default/images/global/pizza-img-500.jpg"
          alt=""
        />
      </div>
      <div class="card-content">
        <div class="at-top" v-if="!isSearchable">
          <div class="tag-bg-black speciality">
            <span class="ico-trending"><i class="jfi-trending"></i></span
            >Popular in Veg Pizzas
          </div>
          <div class="selling-tag">BESTSELLER</div>
        </div>
        <div class="at-botm" :class="{ 'card-botm-gradient': !isSearchable }">
          <div class="tag-bg-yellow p-disc" v-if="!isSearchable">
            <div class="fs-12 bold lh18 disp-col-inline">
              <i class="jfi-offer-tag mr8"
                ><span class="ico-percent">%</span></i
              >
              20% OFF
            </div>
            <div class="fs-10 lh16 bold">
              Code: <span class="upercase">NONSTOP60</span>
            </div>
          </div>
          <!-- extra info -->
          <div class="extra-info">
            <div class="tag-bg-black rating-info ib" v-if="!isSearchable">
              <span class="bold">4.2</span>
              <i class="jfi-star ml5 mr5"></i>
              <span class="color-white">2k</span>
            </div>
            <div class="tag-bg-black ib ml8" v-if="!isSearchable">
              <span class="ico-red-holder"><i class="jfi-trending"></i></span>
              <span class="bold upercase fs-10">SPICY</span>
            </div>
            <div
              v-if="cdata.isCustomizable"
              class="tag-customize ib fr customized"
            >
              <span class="ico-checkmark"><i class="jfi-checker"></i></span>
              <span class="mr10 ml5 bold cust-text"></span>
              <i class="jfi-arrow-line"></i>
            </div>
          </div>
          <!-- pizza title and desc -->
          <div class="pizza-info">
            <h2>
              <i
                class="mr8"
                :class="[
                  {
                    'tag-veg': cdata.productType === 0,
                  },
                  {
                    'tag-non-veg': cdata.productType === 1,
                  },
                ]"
              ></i>
              <span class="pizza-title bold">{{ cdata.name }}</span>
            </h2>
            <h3 class="pizza-desc lh16">
              {{ cdata.description }}
            </h3>

            <!-- search-cards-only -->
            <div class="search-cards-only" v-if="isSearchable">
              <div class="tag-bg-white rating-info ib">
                <span class="bold color-base">4.2</span>
                <i class="jfi-star ml5 mr5"></i>
                <span class="color-base">2k</span>
              </div>
              <div class="tag-bg-white ib ml8">
                <span class="ico-red-holder"><i class="jfi-trending"></i></span>
                <span class="bold upercase fs-10">SPICY</span>
              </div>
              <div class="tag-bg-white ib ml8">
                <span class="bold upercase fs-10">Bestseller</span>
              </div>
            </div>
          </div>

          <!-- pizza price and Add cta -->
          <div
            class="selected-info"
            :class="{ 'bg-grad-black': !isSearchable }"
          >
            <div
              class="selections"
              v-for="(crust, i) in cdata.crust.filter(
                (c) => c.crustId == cdata.selectedCrustId
              )"
              :key="i"
            >
              <div class="pizza-price disp-col-inline">
                <span>
                  <i class="rs">Rs.</i>
                  <span
                    v-for="(size, n) in crust.sizes.filter(
                      (s) => s.sizeId == cdata.selectedSizeId
                    )"
                    :key="n"
                  >
                    {{ size.price }}
                  </span>
                </span>
                <!-- for EVD case only -->
                <div class="evd-price ml8">
                  <span class="striked-price ib mr8"
                    ><i class="rs">Rs.</i> 350</span
                  >
                  <div class="saving-tag bold ib">
                    Save <i class="rs">Rs.</i> 120
                    <span class="lft-arr">
                      <i class="jfi-arrow-line"></i>
                    </span>
                  </div>
                </div>
              </div>
              <div class="size-selected">
                <a
                  href="javascript:void(0);"
                  class="selected-p-profile disp-col-inline"
                  :class="{ 'color-white': !isSearchable }"
                >
                  <ul class="dashed-botm disp-col-inline three-dot">
                    <li>
                      <span
                        class="p-size"
                        v-for="(size, n) in crust.sizes.filter(
                          (s) => s.sizeId == cdata.selectedSizeId
                        )"
                        :key="n"
                      >
                        {{ size.name }}
                      </span>
                      <span class="p-crust">{{ crust.name }}</span>
                    </li>
                    <!-- <li>
                        <span class="p-size"> </span>
                        <span class="p-crust">{{
                          getCrust(cdata.crust, cdata.selectedCrustId)
                        }}</span>
                      </li> -->
                  </ul>

                  <i class="jfi-arrow-line"></i>
                </a>
              </div>
            </div>
            <div class="main-cta">
              <div
                v-if="counter != 0"
                class="cta-edit"
                :class="{ dark: isSearchable }"
              >
                <span
                  class="anim-ico-minus item-status-anim"
                  @click="decrement"
                ></span>

                <span>{{ counter }}</span>

                <span
                  class="anim-ico-plus tr item-status-anim"
                  @click="increment"
                ></span>
              </div>

              <button
                v-if="counter == 0"
                class="btn bold hand cta-add"
                @click.prevent="increment"
              >
                Add <span class="ico-plus">&#43;</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="customizations">
      <span class="color-base">Added:</span>
      <span class="color-grey">
        Paneer, Extra cheese, Mushroom, onion, capsicum, Grilled mushrooms.
      </span>

      <span class="color-base">Replaced:</span>
      <span class="color-grey">Mushroom with capsicum </span>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, reactive } from "vue";
import { useStore } from "vuex";
import { keyMapsGetter } from '../helpers/storeKeys';

export default {
  name: "ProductCardOld",
  props: ["cardRounded", "cardSearchable", "cdata", "moduleProps"],
  emits: ["getItem"],
  components: {},

  setup(props, { emit }) {
    const store = useStore();

    const isRounded = props.cardRounded || false;
    const isSearchable = props.cardSearchable || false;

    let cdata = props.cdata || [];
    let moduleProps = props.moduleProps || {
      title: "",
      type: "",
      cardType: "",
      aspectRatio: "",
      itemsInScreen: "",
    };

    // const getCrust = (crustData, selectedCrustId) => {
    //   let cNameArr = crustData.filter(function (c) {
    //     return c.crustId == selectedCrustId ? c.name : "";
    //   });

    //   for (let i = 0; i < cNameArr.length; i++) {
    //     return cNameArr[i].name;
    //   }
    // };

    let counter = ref(0);

    const emitItemData = () => {
      // if (counter != 0)

      const obj = reactive({ ...cdata });
      obj.quantity = counter.value;
      obj.price = 24;

      emit("getItem", obj);
      // emit("getItem", {
      //   id: cdata.id,
      //   quantity: counter.value,
      //   price: 24,
      // });
    };

    const setCart = () => {
      if (moduleProps.title == store.getters["Cart/getModulePropTitle"]) {
        let allCartItems = store.getters[keyMapsGetter.GET_CART_ITEMS];
        // console.log("typeof(allCartItems)===", typeof allCartItems);
        allCartItems.forEach((cartItem) => {
          console.log("cartItem.id", cartItem.id);
          if (cdata.id == cartItem.id) {
            counter.value = cartItem.quantity;
          }
        });
        if (counter.value != 0) emitItemData();
      }
    };

    const increment = () => {
      counter.value++;

      emitItemData();
      // store.commit("Cart/upTodateLocalStorage", cdata);
    };
    const decrement = () => {
      if (counter.value > 0) counter.value--;

      emitItemData();
      // store.commit("Cart/upTodateLocalStorage", cdata);
    };

    onMounted(() => {
      setCart();
    });

    return {
      isRounded,
      isSearchable,
      cdata,
      moduleProps,
      // getCrust,
      counter,
      emitItemData,
      setCart,
      increment,
      decrement,
    };
  },
};
</script>
