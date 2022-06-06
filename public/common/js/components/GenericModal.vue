<template>
  <div
    :class="[
      'modal-mask',
      { 'sticky-bottom show': animationStyle == 'slide-up-stick-bottom' },
      modalClass,
    ]"
    @click.self="closeModal()"
    id="generic-modal-mask-id"
  >
    <div
      class="modal-container animated"
      :style="{
        width: width,
        height: height,
        maxWidth: width,
        padding: padding,
      }"
    >
      <div class="overflow-hide">
        <div class="modal-header">
          <slot name="header">
            <!-- <h2 class="modal-default-button" @click="closeModal()">XX</h2> -->
          </slot>
        </div>

        <div
          class="modal-body"
          :class="{ 'modal-content-scroll': contentScroll }"
          :style="[contentScroll && { 'max-height': height }]"
        >
          <slot name="body"> Loading... </slot>
        </div>

        <div class="modal-footer">
          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted } from "@vue/runtime-core";
export default {
  name: "GenericModal",
  props: [
    "width",
    "height",
    "animationStyle",
    "modalClass",
    "contentScroll",
    "padding",
  ],
  emits: ["close"],
  setup(props, { emit }) {
    const width = props.width || "",
      height = props.height || "",
      animationStyle = props.animationStyle || "",
      modalClass = props.modalClass || "";

    const closeModal = () => {
      document.body.classList.remove("body-stop-scroll-mobile");
      emit("close");
    };

    onMounted(() => {
      if (document.getElementById("searchQueryInputModal")?.value !== undefined) {
        document.getElementById("searchQueryInputModal").focus();
      }
    });

    return {
      width,
      height,
      animationStyle,
      modalClass,
      closeModal,
    };
  },
};
</script>

<style scoped></style>
