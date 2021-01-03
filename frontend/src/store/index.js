import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";
import auth from './modules/auth';
import axios from 'axios';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    sign_in_form: false,
    register_form: false,
    logged_in: false,
    mode: false,
    admin: false,
    status: '',
    token: localStorage.getItem('token') || '',
    user : {}
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

    //AUTHENTICATION
    auth_request(state){
      state.status = 'loading'
    },

    auth_success(state, token, user){
      state.status = 'success'
      state.token = token
      state.user = user
    },

    auth_error(state){
      state.status = 'error'
    },

    logout(state){
      state.status = ''
      state.token = ''
    },
    // change(state, register_in_form) {
    //   state.register_in_form = register_in_form
    // }
  },
  actions: {
    login({commit}, user){
      return new Promise((resolve, reject) => {
        commit('auth_request')
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/login`, data: user, method: 'POST' })
        .then(resp => {
          const token = resp.data.token
          const user = resp.data.user
          localStorage.setItem('token', token)
          axios.defaults.headers.common['Authorization'] = token
          commit('auth_success', token, user)
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error')
          localStorage.removeItem('token')
          reject(err)
        })
      })
    },

    register({commit}, user){
      return new Promise((resolve, reject) => {
        commit('auth_request')
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/visitor/registration`, data: user, method: 'POST' })
        .then(resp => {
          const token = resp.data.token
          const user = resp.data.user
          localStorage.setItem('token', token)
          axios.defaults.headers.common['Bearer '] = token
          commit('auth_success', token, user)
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error', err)
          localStorage.removeItem('token')
          reject(err)
        })
      })
    },
  },

  logout({commit}){
    return new Promise((resolve, reject) => {
      commit('logout')
      localStorage.removeItem('token')
      delete axios.defaults.headers.common['Authorization']
      resolve()
    })
  },
  modules: {
  },
  getters: {
    sign_in_form: state =>  state.sign_in_form, 
    register_form: state =>  state.register_form, 
    logged_in: state =>  state.logged_in,
    mode: state =>  state.mode,
    admin: state =>  state.admin,
    isLoggedIn: state => !!state.token,
    authStatus: state => state.status,
    user: state => state.user
  }
})
