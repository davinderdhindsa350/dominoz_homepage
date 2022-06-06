<template>
  <div>
    <div class="filters-wrap">
      <div id="order-filters-id">
        <div class="slide-panel">
          <div class="order-filters col-sm-12" :dummy="clearAllFilters">
            <Carousel
              :pauseAutoplayOnHover="true"
              :transition="500"
              :itemsToShow="itemsToShow"
            >
              <Slide v-for="(item, index) in filters" :key="index">
                <div
                  :class="filterClasses[item.title]"
                  @click="handleFiltersClick(item, $event, index)"
                >
                  <i :class="filterIconPre[item.type]"></i>
                  {{
                    item.group == "SORT"
                      ? appliedSortFilters.length > 0
                        ? appliedSortFilters[0].title
                        : item.title
                      : item.title
                  }}
                  <i :class="filterIconPost[item.viewType]"></i>
                </div>
              </Slide>
              <template #addons> </template>
            </Carousel>
          </div>

          <div
            class="pizza-counter fs-11 lh16 pt3"
            v-if="
              productCounterString &&
              !productCounterString.startsWith('undefined')
            "
          >
            {{ productCounterString }}
          </div>
        </div>
      </div>
    </div>
    <!-- filter select popup -->
    <generic-modal
      v-if="showMilestoneModal"
      @close="closeModal()"
      :height="msModalDim.height"
      :width="msModalDim.width"
      :animationStyle="msModalDim.animationStyle"
    >
      <template v-slot:header>
        <p class="modal-title fs-18 lh25 bold mb20 ml10">Sort by</p>
        <i @click="closeModal(true)" class="jfi-cross sticky-pop-cross"></i>
      </template>
      <template v-slot:body>
        <ul class="list-wrap">
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
        </ul>
        <p
          class="clear-sort-cta fs-12 lh18 bold flex-row hand color-secondary"
          @click="clearSortFilter($event)"
          v-if="appliedSortFilters[0]"
        >
          Reset
        </p>
      </template>
      <template v-slot:footer>
        <div></div>
      </template>
    </generic-modal>
  </div>
</template>

<script>
import { Carousel, Slide } from "vue3-carousel";
import GenericModal from "./GenericModal.vue";
import "vue3-carousel/dist/carousel.css";
import { keyMapsMutation } from "../helpers/storeKeys";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

export default {
  name: "OrderFilters",
  props: ["dataResults", "moduleProps"],

  components: {
    Carousel,
    Slide,
    GenericModal,
  },

  data() {
    return {
      msModalDim: {
        width: "100vw",
        height: "calc(91vh - 56px)",
        animationStyle: "slide-up-stick-bottom",
        modalClass: "modal-bg-blackout",
      },
      showMilestoneModal: false,
      filterIconPre: {
        VEG: "tag-veg mr5",
        NONVEG: "tag-non-veg mr5",
      },
      filterIconPost: {
        select: "jfi-arrow-line ml7",
        sort: "jfi-arrow-switch ml7",
      },
      filterClasses: {},
      filters: [],
      sortOptions: [],
      appliedFilters: [],
      appliedSortFilters: [],
      cachedComponentOffsetTop: 0,
      itemsToShow: 3,
    };
  },

  mounted() {
    let foundSort = false;

    let flt = (this.dataResults && JSON.parse(this.dataResults)) || [];

    this.sortOptions = flt.filter((_) => {
      return _.group == "SORT";
    });

    this.filters = flt
      .filter((_) => {
        if (_.group != "SORT") {
          return true;
        } else if (!foundSort) {
          foundSort = true;
          return true;
        } else {
          return false;
        }
      })
      .map((_) => {
        let __ = Object.assign({}, _);
        if (__.group == "SORT") {
          __.title = "Sort by";
        }
        return __;
      });

    this.resetFilterRelatedUI();

    window.addEventListener("scroll", () => {
      this.watchSticky();
    });
    //Amplitude impression event load
    // const eventObj = Utility.amplitudeEventObj("impression", "filters", "6", {});
    // setEvents("amplitude", "Impression", eventObj);
  },

  destroyed() {
    window.removeEventListener("scroll", this.watchSticky);
  },

  methods: {
    watchSticky() {
      let componentRef = document.getElementById("order-filters-id");
      if (!componentRef) return;
      if (!this.cachedComponentOffsetTop && componentRef.offsetTop > 0) {
        this.cachedComponentOffsetTop = componentRef.offsetTop;
      }

      if (
        this.isTrueLocationDetected ||
        this.appliedFilters.length > 0 ||
        this.appliedSortFilters.length > 0
      ) {
        if (window.pageYOffset >= this.cachedComponentOffsetTop) {
          componentRef.classList.add("watch-sticky");
        } else {
          componentRef.classList.remove("watch-sticky");
        }
      } else if (
        (!this.isTrueLocationDetected && this.appliedFilters.length == 0) ||
        this.appliedSortFilters.length == 0
      ) {
        if (window.pageYOffset >= this.cachedComponentOffsetTop) {
          componentRef.classList.remove("watch-sticky");
        }
      }
    },
    handleFiltersClick(item, e, index) {
      //Amplitude Click event load
      const preEventObj = {
        positionWithinSection: index + 1,
        category: item.title,
      };
      const eventObj = Utility.amplitudeEventObj(
        "click",
        "filters",
        "6",
        preEventObj
      );
      setEvents("amplitude", "Click", eventObj);

      if (item.group == "SORT" || item.options?.length > 0) {
        this.triggerModal(e);
      } else {
        this.applyFilter(e, item);
      }
    },
    clearSortFilter(e) {
      this.appliedSortFilters = [];
      this.filterIconPost.sort = "jfi-arrow-switch ml7";
      this.$store.commit(keyMapsMutation.FILTER_PRODUCT_ITEMS, {
        filterProductItem: [...this.appliedFilters, ...this.appliedSortFilters],
        cartData: this.$store.state.Cart.cartItems,
        cartQuantitiy: this.$store.getters["Cart/cartQuantity"],
      });
      this.filterClasses["Sort by"] =
        "order-filter-item flex-row fs-12 lh-18 pd8 ml3 mr3";
      this.closeModal();
      //Amplitude Click event load
      const preEventObj = {
        positionWithinSection: "3",
        category: "sort_by",
        subCategory: "reset",
      };
      const eventObj = Utility.amplitudeEventObj(
        "click",
        "sort by clicks",
        "popup",
        preEventObj
      );
      setEvents("amplitude", "Click", eventObj);
    },
    applyFilter(e, item) {
      if (item.group == "SORT") {
        this.appliedSortFilters = [item];
        if (item.type == "HIGH_TO_LOW") {
          this.filterIconPost.sort = "jfi-long-arrow arrow-down ml7";
        } else if (item.type == "LOW_TO_HIGH") {
          this.filterIconPost.sort = "jfi-long-arrow arrow-up ml7";
        }
        this.filterClasses["Sort by"] =
          "order-filter-item flex-row fs-12 lh-18 pd8 ml3 mr3 selected-filter-item";
        //Amplitude Click event load
        const preEventObj = {
          positionWithinSection: item.position,
          category: "sort_by",
          subCategory: item.title,
        };
        const eventObj = Utility.amplitudeEventObj(
          "click",
          "sort by clicks",
          "popup",
          preEventObj
        );
        setEvents("amplitude", "Click", eventObj);
      } else {
        let addToAppliedFilter = true;
        for (var i = 0; i < this.appliedFilters.length; i++) {
          if (
            this.appliedFilters[i].group == item.group &&
            this.appliedFilters[i].type == item.type
          ) {
            addToAppliedFilter = false;
            this.filterClasses[item.title] =
              "order-filter-item flex-row fs-12 lh-18 pd8 ml3 mr3";
            this.appliedFilters.splice(i, 1);
            break;
          }
        }
        if (addToAppliedFilter) {
          if (this.appliedFilters.length > 0) {
            this.filterClasses[this.appliedFilters[0].title] =
              "order-filter-item flex-row fs-12 lh-18 pd8 ml3 mr3";
          }
          this.appliedFilters = [item];
          this.filterClasses[item.title] =
            "order-filter-item flex-row fs-12 lh-18 pd8 ml3 mr3 selected-filter-item";
        }
      }

      this.$store.commit(keyMapsMutation.FILTER_PRODUCT_ITEMS, {
        filterProductItem: [...this.appliedFilters, ...this.appliedSortFilters],
        cartData: this.$store.state.Cart.cartItems,
        cartQuantitiy: this.$store.getters["Cart/cartQuantity"],
      });
      this.closeModal();
    },
    triggerModal(e) {
      document.body.classList.add("body-stop-scroll-mobile");
      this.showMilestoneModal = true;
      //Amplitude Impression event load
      const eventObj = Utility.amplitudeEventObj("impression", "filters", "6", {
        category: "sort_by",
      });
      setEvents("amplitude", "Impression", eventObj);
    },
    closeModal(isCrossClicked = false) {
      document.body.classList.remove("body-stop-scroll-mobile");
      this.showMilestoneModal = false;
      if (isCrossClicked) {
        //Amplitude Click event load
        const preEventObj = {
          positionWithinSection: "top right",
          category: "sort_by",
          subCategory: "cross tap",
        };
        const eventObj = Utility.amplitudeEventObj(
          "click",
          "sort by clicks",
          "popup",
          preEventObj
        );
        setEvents("amplitude", "Click", eventObj);
      }
    },
    resetFilterRelatedUI() {
      this.appliedFilters = [];
      this.appliedSortFilters = [];
      this.filters.map((_) => {
        this.filterClasses[_.title] =
          "order-filter-item flex-row fs-12 lh-18 pd8 ml3 mr3";
      });
    },
  },
  computed: {
    clearAllFilters() {
      let _ = this.$store.state.ProductItems.clearFilterRef;
      if (_) {
        this.resetFilterRelatedUI();
        this.$store.commit(keyMapsMutation.FILTER_PRODUCT_ITEMS, []);
      }
    },
    isTrueLocationDetected() {
      return this.$store.state.Location.isTrueLocationDetected;
    },
    productCounterString() {
      return this.$store.state.ProductItems.productCounterString;
    },
  },
};
</script>

<style scoped></style>
