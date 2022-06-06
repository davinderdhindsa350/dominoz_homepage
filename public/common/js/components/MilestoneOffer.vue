/* not being used */
<template>
  <div class="milestone-offer flex-row">
    <div>
      <ul class="fs-14 bold disp-col-inline ms-timeline">
        <li class="">
          <div class="bold order-no">1st Order</div>
          <div class="fs-10 bold">10% Off</div>
          <div class="border-arrow-img"></div>
        </li>
        <li>
          <div class="bold order-no">2nd Order</div>
          <div class="fs-10 bold">20% Off</div>
          <div class="border-arrow-img"></div>
        </li>
        <li>
          <div class="bold order-no">3rd Order</div>
          <div class="fs-10 bold">30% Off</div>
          <div class="border-arrow-img"></div>
        </li>
        <li>
          <div class="bold order-no">4th Order</div>
          <div class="fs-10 bold">40% Off</div>
          <div class="border-arrow-img"></div>
        </li>
      </ul>
    </div>
    <slot name="footer"></slot>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  name: "MilestoneOffer",

  components: {
    
  },

  props: {
  },

  setup(props) {
    
    return{

    }
  }
};
</script>
