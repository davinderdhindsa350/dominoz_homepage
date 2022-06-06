<template>
  <div
    class="cheesy-reward col-sm-6 mb32"
    v-if="false && getUserData?.rewardType == 'CHEESY'"
    id="cheesy_rewards_widget"
  >
    <h3 class="fs-18 lh21 bold mb12">Cheesy Rewards</h3>
    <div class="un-enrolled flex-column" v-if="!getUserData.isEnrolled">
      <div class="flex-row">
        <img :src="getCheesyRewardIconPath" class="pd10" />
        <div class="ml10 pd12">
          <h3 class="bold fs-14 lh20 mb6">Earn points towards FREE PIZZAS!</h3>
          <p class="fs-12 lh18 mr30">
            1 Order = 100 Points<br />
            600 Points = 1 Free Pizza
          </p>
        </div>
      </div>
      <div class="rwd-bottom flex-row pd10">
        <h3 class="fs-11 ml10">Enroll now & start collecting points</h3>
        <button
          class="btn medium pt6 pb6 pr12 pl12"
          @click="gotoEnroll($event)"
        >
          Enroll now
        </button>
      </div>
    </div>
    <div class="enrolled flex-row" v-if="getUserData.isEnrolled">
      <div class="flex-column left pd12">
        <div
          class="cheesy-reward-widget-image ml5 mb4"
          v-bind:style="{
            backgroundImage: `url('` + getCheesyRewardIconPath + `')`,
          }"
        >
          <p class="bold">
            {{
              Math.floor(
                getUserData.rewardData.points / getUserData.rewardData.maxPoints
              )
            }}
          </p>
        </div>

        <h3
          class="fs-12 lh13 bold"
          v-if="!getUserData.rewardData.elizibleForPizza"
        >
          {{ getUserData.rewardData.points }}/{{
            getUserData.rewardData.maxPoints
          }}
        </h3>
        <h3 class="fs-12 lh13" v-if="!getUserData.rewardData.elizibleForPizza">
          Points
        </h3>
        <h3 class="fs-12 lh13" v-if="getUserData.rewardData.elizibleForPizza">
          Redeem
        </h3>
        <h3 class="fs-12 lh13" v-if="getUserData.rewardData.elizibleForPizza">
          Free Pizza
        </h3>
      </div>
      <div class="flex-column middle pd12">
        <h3
          class="bold fs-14 lh20 mt5"
          v-if="!getUserData.rewardData.elizibleForPizza"
        >
          You are
          {{ getUserData.rewardData.maxPoints - getUserData.rewardData.points }}
          points away from a free pizza.
        </h3>
        <h3
          class="bold fs-14 lh20 mt5"
          v-if="getUserData.rewardData.elizibleForPizza"
        >
          Time to Redeem your FREE Pizzas!
        </h3>
        <p
          class="fs-12 lh17 mb5 bold enrolled-points-cta"
          v-if="!getUserData.rewardData.elizibleForPizza"
          @click="goto($event)"
        >
          Place order to earn points
        </p>
        <button
          class="btn medium pt6 pb6 pr12 pl12"
          v-if="getUserData.rewardData.elizibleForPizza"
          @click="goto($event)"
        >
          Select Free Pizza
        </button>
      </div>
      <i class="jfi-arrow-line right pd12 color-secondary"></i>
    </div>
  </div>
</template>

<script>
import { keyMapsGetter } from "../helpers/storeKeys";
import { staticImgBaseUrl, v2PwaBasePath } from "../config/apiEndpoints";
import Utility from "../helpers/utility";
import setEvents from "../helpers/events";

export default {
  name: "CheesyRewards",

  components: {},

  data() {
    return { staticImgBaseUrl: staticImgBaseUrl };
  },
  mounted() {
    this.eventFunction(false);
  },
  methods: {
    eventFunction(isClicked = false, eObj = {}) {
      if (this.getUserData?.rewardType == "CHEESY") {
        let preEventObj = {};
        if (!this.getUserData?.isEnrolled) {
          preEventObj = {
            category: "cheesyRewards",
            enrollmentStatus: "no",
            ...eObj,
          };
        } else if (this.getUserData?.isEnrolled) {
          if (!getUserData.rewardData.elizibleForPizza) {
            preEventObj = {
              subCategory: "place order to earn points",
              category: "cheesyRewards",
              enrollmentStatus: "yes",
              points: getUserData.rewardData.points,
              ...eObj,
            };
          } else {
            preEventObj = {
              subCategory: "selectfreepizza",
              category: "cheesyRewards",
              enrollmentStatus: "yes",
              points: getUserData.rewardData.maxPoints,
              ...eObj,
            };
          }
        }
        let eventType = isClicked ? "Click" : "Impression";
        let userAction = isClicked ? "click" : "impression";
        const eventObj = Utility.amplitudeEventObj(
          userAction,
          "Cheesy Rewards",
          "12",
          preEventObj
        );
        setEvents("amplitude", eventType, eventObj);
      }
    },
    gotoEnroll(event, item) {
      this.eventFunction(true, { subCategory: "enrollNow", points: "0" });
      window.location.href = `${v2PwaBasePath}/info`;
    },
    goto() {
      this.eventFunction(true);
      Utility.scrollUptoTo("element_catg_1");
    },
  },

  computed: {
    getUserData() {
      return this.$store.getters[keyMapsGetter.GET_USER_DATA];
    },
    getCheesyRewardIconPath() {
      return this.$store.getters[keyMapsGetter.GET_CHEESY_REWARD_ICON_PATH];
    },
  },
};
</script>

<style scoped></style>
