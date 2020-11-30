import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    sign_in_form: false,
    register_form: false,
    logged_in: false,
    mode: false,
    admin: false
    //register_in_form: false
  },
  mutations: {
    sign_in(state, sign_in_form) {
      state.sign_in_form = sign_in_form
    },
    
    register(state, register_form) {
      state.register_form = register_form
    },

    show_tool(state, logged_in) {
      state.logged_in = logged_in
    },

    theme(state, mode) {
      state.mode = mode
    },

    log_admin(state, admin) {
      state.admin = admin
    },
    // change(state, register_in_form) {
    //   state.register_in_form = register_in_form
    // }
  },
  actions: {
  },
  modules: {
  },
  getters: {
    sign_in_form: state =>  state.sign_in_form, 
    register_form: state =>  state.register_form, 
    logged_in: state =>  state.logged_in,
    mode: state =>  state.mode,
    admin: state =>  state.admin
  }
})
