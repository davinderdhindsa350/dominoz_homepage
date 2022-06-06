<template>
  <div @click="goto('pageTop')" :class="['scroll-to-top', {'mb44': coupleApplied}]" v-if="currentStatus">
      <i class="jfi-arrow-double"></i>
  </div>
</template>

<script>
import Utility from '../helpers/utility';
import setEvents from "../helpers/events";

export default {
  name: "ScrollToTop",
  data() {
    return {
      currentStatus: 0 ,
      coupleApplied: 0
    };
  },
  mounted() {
    window.addEventListener("scroll", () => {
      let value = window.pageYOffset
      let obj = document.getElementsByClassName("offer-applied-message")
      if(value > 200){
        this.currentStatus = 1
      } else {
        this.currentStatus = 0
      }
      if(obj.length > 0){
        this.coupleApplied = obj.length
      } else {
        this.coupleApplied = obj.length
      } 
    });
  },
  methods: {
    goto(itmId) {
      if(itmId) {
        Utility.scrollUptoTo(itmId);
      }
      //Amplitude click event load
      const preEventObj = { "scrollType": "up" };
      const eventObj = Utility.amplitudeEventObj("click", "bottomMenu", "-1", preEventObj);
      setEvents("amplitude", "Click", eventObj);
    }
  }
};
</script>
