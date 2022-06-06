<template>
  <div v-html="data"></div>
</template>

<script>

export default {
  name: "HtmlComponent",
  components: {
    // 
  },
  data() {
    return {
      data: '<h1>html rendered '+
                           '<span style="color:blue;">'+
                              'on this component</span>'+
                         '</h1>'
    };
  },
  mounted() {
    // 
  },
  methods: {
    // 
  }
};
</script>


