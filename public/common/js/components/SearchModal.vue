<template>
	<!-- search modal -->
	<transition
		name="custom-classes-transition"
		enter-active-class="modal-show"
		leave-active-class="modal-hiding"
	>
		<generic-modal
			v-if="showSearchModal"
			@close="closeSearchModal()"
			:height="searchModalDim.height"
			:width="searchModalDim.width"
			:modalClass="searchModalDim.modalClass"
		>
			<template v-slot:header>
				<!-- <i @click="closeSearchModal()" class="jfi-cross sticky-pop-cross"></i> -->
			</template>
			<template v-slot:body>
				<div class="search-modal">
					<div class="search-bar plr12 mb16 focussed search-box-shadow">
						<i @click="closeSearchModal('cross')" class="jfi-back-v2"></i>
						<input
							class="search-query-input"
							id="searchQueryInputModal"
							type="text"
							name="searchQueryInputModal"
							placeholder="Search Pizzas, Sides &amp; more..."
							v-model="searchText"
							@input="activeDebounce($event.target.value)"
						/>
						<button
							class="search-query-submit"
							id="searchQuerySubmit"
							type="submit"
							name="searchQuerySubmit"
						>
							<i class="jfi-search"></i>
						</button>
					</div>

					<div class="result-dropdown result-dropdown-active">
						<!-- list type dropdown -->
						<div class="plr12 list-type-ddn hide">
							<div class="ddn-title disp-col-inline jcsb bold">
								<div>Recently searched</div>
								<a href="javascript:void(0);" class="">Clear</a>
							</div>
							<div class="ddn-content">
								<ul class="ddn-listing">
									<li class="disp-col-inline">
										<i class="jfi-clock"></i>
										<span class="ml10 fs-12 lh18">Pepproni</span>
									</li>
									<li class="disp-col-inline">
										<i class="jfi-clock"></i>
										<span class="ml10 fs-12 lh18">Paneer</span>
									</li>
									<li class="disp-col-inline">
										<i class="jfi-clock"></i>
										<span class="ml10 fs-12 lh18">Cheese</span>
									</li>
								</ul>
							</div>
						</div>
						<!-- result type dropdown -->
						<div class="result-type-ddn">
							<!-- no results -->
							<div
								class="no-results"
								v-if="
									searchAllProductsItem.length === 0 && !isPopularSearchToggle
								"
							>
								<div class="fs-12 lh18 color-light-grey tc">
									Oops, No result found! Try searching “Pizzas” or “Garlic
									bread”
								</div>
							</div>
							<!-- results populated -->
							<div class="results-populated">
								<div
									class="plr12 fs-11 color-slaty lh16 mb8"
									v-if="searchAllProductsItem.length > 0"
								>
									Showing {{ searchAllProductsItem.length }} results
								</div>

								<div class="cards-wrap clearfix">
									<div class="cards-holder">
										<product-card
											v-for="(result, i) in searchAllProductsItem"
											:key="result.code"
											:data-cardRounded="vpsProps.cardRounded"
											:data-cardSearchable="vpsProps.cardSearchable"
											:data-result="result"
											@add-to-cart="addToCartEvent(result, i)"
											@open-customise="openCustomise(result, 'customise', i)"
											@open-upsell="openCustomise(result, 'upsell', i)"
										></product-card>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- popular search UI -->
					<div
						class="plr16"
						v-if="searchAllProductsItem.length === 0 && isPopularSearchToggle"
					>
						<div class="fs-12 popular-search bold mb15">Popular searches</div>
						<div
							v-for="(item, ind) in (modulePropsParsed?.popularSearch?.split(',') || [])"
							:key="ind"
							:class="[
								'color-base pb12 pt10 cursor-pointer',
								ind !== (modulePropsParsed?.popularSearch?.split(',') || []).length - 1 &&
									'border-bottom-light',
							]"
							@click="popularItemFun(item, ind)"
						>
							<i class="jfi-search mr5"></i>
							{{ item }}
						</div>
					</div>
				</div>
			</template>
			<template v-slot:footer>
				<div></div>
			</template>
		</generic-modal>
	</transition>
</template>

<script>
import { mapGetters } from "vuex";
import GenericModal from "./GenericModal.vue";
import ProductCard from "./ProductCard.vue";
import _ from "../helpers/lodash";
import Utility from "../helpers/utility";
import { keyMapsGetter } from "../helpers/storeKeys";
import setEvents from "../helpers/events";

export default {
  name: "SearchModal",
  props: ["moduleProps", "showSearchModal"],
  components: {
    GenericModal,
    ProductCard,
  },
  data() {
    return {
      searchModalDim: {
        width: "100vw",
        height: "100vh",
        modalClass: "full-screen-popup search-modal-wrap",
      },
      vpsProps: {
        cardRounded: false,
        cardSearchable: true,
      },
      searchAllProductsItem: [],
      searchText: "",
      isPopularSearchToggle: true,
      modulePropsParsed: {}
    };
  },
  computed: {
    ...mapGetters({
      product: keyMapsGetter.GET_PRODUCT_ITEMS,
    }),
  },
  mounted() {
    this.modulePropsParsed = JSON.parse(this.moduleProps);
    this.isPopularSearchToggle = true;
  },
  methods: {
    openCustomise(product, subCategory, index) {
      //Amplitude Click event load
      const preEventObj = { 
        "searchSection": "search bar", "searchQuery": this.searchText, 
        "category": product.name, "categoryPosition": index+1, "subCategory": subCategory,
        "screenName": "searchFeed", "previousScreenname": "homePage"
      };
      if(this.searchAllProductsItem?.length > 0) {
        preEventObj["resultCount"] = this.searchAllProductsItem.length;
        preEventObj["topResult"] = this.searchAllProductsItem[0].name;
      }
      const eventObj = Utility.amplitudeEventObj("click", "search", "popup", preEventObj);
      setEvents("amplitude", "Click", eventObj);
    },
    addToCartEvent(product, index) {
      //Amplitude Click event load
      const preEventObj = { 
        "searchSection": "search bar", "searchQuery": this.searchText, 
        "category": product.name, "categoryPosition": index+1, "subCategory": "Add",
        "screenName": "searchFeed", "previousScreenname": "homePage"
      };
      if(this.searchAllProductsItem?.length > 0) {
        preEventObj["resultCount"] = this.searchAllProductsItem.length;
        preEventObj["topResult"] = this.searchAllProductsItem[0].name;
      }
      const eventObj = Utility.amplitudeEventObj("click", "search", "popup", preEventObj);
      setEvents("amplitude", "Click", eventObj);
    },
    closeSearchModal(type) {
      //Amplitude Click event load
      const preEventObj = { 
        "searchSection": "search bar", "searchQuery": this.searchText, 
        "subCategory": "back", "screenName": "searchFeed", "previousScreenname": "homePage"
      };
      if(this.searchAllProductsItem?.length > 0) {
        preEventObj["resultCount"] = this.searchAllProductsItem.length;
        preEventObj["topResult"] = this.searchAllProductsItem[0].name;
      }
      const eventObj = Utility.amplitudeEventObj("click", "search", "3", preEventObj);
      setEvents("amplitude", "Click", eventObj);
      document.body.classList.remove("body-stop-scroll-mobile");
      this.$emit("close");
      this.clearSearchText();
    },
    filterDataByTextSearch(searchArry, searchText) {
      return Utility.textLowerAndTrimSpace(searchArry).includes(searchText);
    },
    filteredSearchItem() {
      let search = Utility.textLowerAndTrimSpace(this.searchText); //
      let productJsonData = [].concat.apply([], this.product).map((i) => {
        if (i["sortOrder"]) {
          delete i["sortOrder"];
        }
        if (i["customCategoryId"]) {
          delete i["customCategoryId"]; //  for remove duplicay while edit the previous order data
        }
        if (i["customCategoryName"]) {
          delete i["customCategoryName"]; //  for remove duplicay while edit the previous order data
        }
        return i;
      });
      let searchedItems = productJsonData.filter((i) => {
        if (i.name && i.description) {
          return (
            this.filterDataByTextSearch(i.name, search) ||
            this.filterDataByTextSearch(i.description, search)
          );
        }
      });
      if(searchedItems.length > 0){
        this.$nextTick(() => {
          this.searchAllProductsItem = [];
          this.searchAllProductsItem = Utility.sortQniqueArry(searchedItems);
        });
      } else {
        this.isPopularSearchToggle = false;
        this.searchAllProductsItem = [];
      }
    },
    activeDebounce: _.debounce(function (value) {
      if(value == ""){
        this.isPopularSearchToggle = true;
        this.searchAllProductsItem = [];
      } else {
        this.filteredSearchItem(value);
      }
    }, 500),
    popularItemFun(data, index) {
      this.isPopularSearchToggle = false;
      this.searchText = data || "";
      this.filteredSearchItem();
      //Amplitude Click event load
      const preEventObj = { 
        "searchSection": "popularSearch", "searchQuery": "default_popular", 
        "category": data, "categoryPosition": index+1,
        "screenName": "searchFeed", "previousScreenname": "homePage"
      };
      const eventObj = Utility.amplitudeEventObj("click", "search", "popup", preEventObj);
      setEvents("amplitude", "Click", eventObj);
    },
    clearSearchText(){
      this.searchText = ""
      // this.activeDebounce(this.searchText)
      this.isPopularSearchToggle = true;
      console.log("search text", this.searchText);
    }
  },
};
</script>
