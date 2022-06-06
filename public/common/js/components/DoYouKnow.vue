<template>
  <div class="col-sm-12 pTB10 color-light-grey doyouknow-align">
    <div><i class="jfi-bulb" :class="bulbSizeChange"></i></div>
    <div class="fs-11 bold mt5">Do You Know ?</div>
    <div class="fs-11">
      Farmhouse is the most popular Pizza at Domino’s
    </div>
  </div>
</template>

<script>
export default {
  name: "DoYouKnow",
  props: [
    'itemSize'
  ],
  data(props){
    return {}
  },
  computed: {
    bulbSizeChange(){
      return this.itemSize > 2 ? 'fs-22': 'bulb-font'
    }
  },
};
</script>

<style scoped></style>
