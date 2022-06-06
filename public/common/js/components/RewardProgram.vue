<template>
  <div
    :class="{ hide: !isOnStorePage, 'reward-program pd5 flex-row': true }"
    @click="handleClick($event)"
    v-if="
      isLoggedIn &&
      getUserData?.rewardType &&
      getUserData?.rewardType != 'UNKNOWN' &&
      getUserData?.rewardType != 'CHEESY'
    "
  >
    <img class="reward-icon" :src="getRewardIconPath" />
    <span class="reward-name bold fs-10 lh11" v-html="getRewardIconText"></span>
  </div>
</template>

<script>
import { keyMapsGetter } from "../helpers/storeKeys";
import { v2PwaBasePath } from "../config/apiEndpoints";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

export default {
  name: "RewardProgram",

  components: {},

  data() {
    return {
      isOnStorePage: window.location.href.includes(
        Utility.getCurrentStoreFolder()
      ),
    };
  },

  methods: {
    handleClick(event) {
      if (this.getUserData?.rewardType == "CHEESY") {
        this.goto();
      } else {
        window.location.href = v2PwaBasePath + "/pizza-pal";
      }
    },
    goto() {
      Utility.scrollUptoTo("cheesy_rewards_widget");
      //Amplitude click event load
      const preEventObj = { category: "cheesyRewards" };
      const eventObj = Utility.amplitudeEventObj(
        "click",
        "topbar",
        "1",
        preEventObj
      );
      setEvents("amplitude", "Click", eventObj);
    },
  },

  computed: {
    isLoggedIn() {
      return this.$store.state.Auth.isLoggedIn;
    },
    getRewardIconText() {
      return this.$store.getters[keyMapsGetter.GET_REWARD_ICON_TEXT];
    },
    getRewardIconPath() {
      return this.$store.getters[keyMapsGetter.GET_REWARD_ICON_PATH];
    },
    getUserData() {
      return this.$store.getters[keyMapsGetter.GET_USER_DATA];
    },
  },
};
</script>

<style scoped></style>
