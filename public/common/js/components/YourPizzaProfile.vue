<template>
  <div class="col-sm-12 pizza-profile-main">
    <div class="pizza-profile-title d-flex justify-content-between pt12">
      <span class="fs-18 bold"> Your Pizza Profile </span>
      <span class="fs-14 color-secondary bold" @click="triggerModal()">
        View All
      </span>
    </div>
    <div class="pizza-body flex-column mt11">
      <div class="pizza-ask pTB16 plr12">
        <div class="flex-row fs-14 color-base">
          What’s your favourite Crust?
        </div>
        <div class="flex-row flex-wrap pizza-crust mt8">
          <div
            v-for="(crust, index) in pizzaQuestion[0]?.crust"
            :key="index"
            :class="[
              'pizza-btn cursor-pointer',
              {
                'selected-pizza-crust': crust.selected,
              },
            ]"
            @click="setCrustDataFun(crust.selected, 0, index)"
          >
            {{ crust?.name }}
          </div>
        </div>
      </div>
      <div
        class="flex-row pizza-profile-footer justify-content-center align-items-center fs-11"
      >
        <i class="tag-veg mr5 pr16"></i>
        <span>
          Your choices will help us to make your experience awesome! you can
          always change them later
        </span>
      </div>
    </div>

    <!-- filter select popup -->
    <generic-modal
      v-if="showYourDominoProfileModal"
      @close="closeModal()"
      :height="yourDominoModalStyle.height"
      :width="yourDominoModalStyle.width"
      :animationStyle="yourDominoModalStyle.animationStyle"
      :contentScroll="yourDominoModalStyle.contentScroll"
      :modalClass="yourDominoModalStyle.modalClass"
    >
      <template v-slot:header>
        <span>
          <i
            @click="closeModal()"
            class="jfi-arrow-double fs-18 cursor-pointer"
          ></i>
        </span>
        <span class="fs-18 bold ml10">Your Domino's Profile</span>
      </template>
      <template v-slot:body>
        <div class="pt18 mb8">
          <div class="fs-18">Hi Darpan,</div>
          <div class="fs-12 color-slaty mt8">
            Your choices will help us to make your experience awesome! it will
            help us provide you better suggestion & Offers
          </div>
        </div>
        <!-- Pizza Crust Info -->
        <div
          class="pizza-ask pt24"
          v-for="(item, ind) in pizzaQuestion"
          :key="ind"
        >
          <div class="flex-row fs-14 color-base bold">
            {{ item?.question }}
          </div>
          <div class="flex-row flex-wrap pizza-crust mt8">
            <div
              v-for="(crust, index) in item.crust || []"
              :key="index"
              :class="[
                'pizza-btn',
                {
                  'selected-pizza-crust': crust.selected,
                },
              ]"
              @click="setCrustDataFun(crust.selected, ind, index)"
            >
              {{ crust.name }}
            </div>
          </div>
          <hr class="mt24 mb0 mt0" />
        </div>
      </template>
    </generic-modal>
  </div>
</template>

<script>
import { computed, ref } from "vue";
import { useStore } from "vuex";
import GenericModal from "./GenericModal.vue";
import { keyMapsMutation } from '../helpers/storeKeys';
export default {
  name: "YourPizzaProfile",
  components: {
    GenericModal,
  },
  setup() {
    const store = useStore();
    const pizzaQuestion = computed(
      () => store.state.YourPizzaProfile.crustQuestion
    );
    const showYourDominoProfileModal = ref(false);

    const yourDominoModalStyle = ref({
      width: "100vw",
      height: "100vh",
      modalClass: "modal-bg-blackout",
      contentScroll: true,
      modalClass: "full-screen-popup"
    });

    const triggerModal = (e) => {
      document.body.classList.add("body-stop-scroll-mobile");
      showYourDominoProfileModal.value = true;
    };

    const closeModal = () => {
      document.body.classList.remove("body-stop-scroll-mobile");
      showYourDominoProfileModal.value = false;
    };

    const setCrustDataFun = (data, mainQuestionInd, selectedCrustInd) => {
      let newData = {};
      newData["mainQuestionInd"] = mainQuestionInd;
      newData["selectedCrustInd"] = selectedCrustInd;
      newData["choosed"] = data;
      updateTheState(newData);
    };

    const updateTheState = (payload) =>
      store.commit(keyMapsMutation.UPDATE_SELECTED_CRUST, payload);

    return {
      showYourDominoProfileModal,
      yourDominoModalStyle,
      triggerModal,
      closeModal,
      pizzaQuestion,
      updateTheState,
      setCrustDataFun,
    };
  },
};
</script>

<style lang="scss" scoped></style>
