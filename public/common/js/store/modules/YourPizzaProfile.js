export default {
    namespaced: true,
    state: {
      crustQuestion: [
        {
          question: "What's your favourite Crust?",
          crust: [
            {name: "Classic hand tossed", selected: false},
            {name: "New hand tossed", selected: true},
            {name: "Wheat thin crust", selected: false},
            {name: "Cheese burst", selected: false},
            {name: "Fresh Pan Pizza", selected: true}
          ],
        },
        {
          question: "Add your special days",
          crust: [
            {name: "Add birthday", selected: false},
            {name: "Add anniversery", selected: true}
          ],
        },
        {
          question: "Your favourite toppings?",
          crust: [
            {name: "Grilled mushroom", selected: false},
            {name: "Onion", selected: false},
            {name: "Olive", selected: true},
            {name: "Jalapeno", selected: false},
            {name: "Capsicum", selected: true},
            {name: "Tomato", selected: false},
            {name: "Paneer", selected: false}
          ],
        },
        {
          question: "Any toppings that you dislike?",
          crust: [
            {name: "Grilled mushroom", selected: true},
            {name: "Onion", selected: true},
            {name: "Olive", selected: false},
            {name: "Jalapeno", selected: false},
            {name: "Capsicum", selected: false},
            {name: "Tomato", selected: false},
            {name: "Paneer", selected: false}
          ],
        },
      ],
    },
    mutations: {
      updateSelectedCrust(state, payload){
        if(payload){
          let {mainQuestionInd, selectedCrustInd, choosed} = payload;
          let newState = [...state.crustQuestion]
          newState[mainQuestionInd].crust[selectedCrustInd].selected = !choosed
          // let update = select ? (select = false): (select = true)
          state.crustQuestion = newState;
        }
      }
    },
    actions: {
      // getCrustData({commit}, payload){
      //   commit('updateSelectedCrust', payload)
      //   console.log("data displaying")
      // }
    },
  };
  