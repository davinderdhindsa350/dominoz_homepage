<template>
  <div class="search-wrap">
    <div class="search-bar plr12 mb16">
      <i class="jfi-back-v2"></i>
      <input
        @click.prevent="triggerSearchModal($event)"
        class="search-query-input"
        id="searchQueryInput"
        type="text"
        name="searchQueryInput"
        placeholder="Search Pizzas, Sides &amp; more..."
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

    <!-- search modal -->
    <search-modal
      v-if="showSearchModal"
      :showSearchModal="showSearchModal"
      :moduleProps="moduleProps"
      @close="closeSearchModal()"
    ></search-modal>
  </div>
</template>

<script>
import { defineAsyncComponent } from "vue";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

export default {
  name: "SearchProducts",
  props: ["moduleProps"],
  components: {
    SearchModal: defineAsyncComponent(() => import("./SearchModal.vue"))
  },
  data() {
    return {
      showSearchModal: false,
    };
  },
  methods: {
    triggerSearchModal(e) {
      document.body.classList.add("body-stop-scroll-mobile");
      this.showSearchModal = true;
      //Amplitude Click event load
      const eventObj = Utility.amplitudeEventObj("click", "search", "3", {});
      setEvents("amplitude", "Click", eventObj);
    },
    closeSearchModal() {
      this.showSearchModal = false;
    }
  },
};
</script>
