import Vue from 'vue'
import Vuex, { Store } from 'vuex'
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
    token: sessionStorage.getItem('token') || '',
    user : {},
    collectionData: {},
    artworkData: {},
    contestData: {}
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
      console.log('showtool ' + logged_in)
      localStorage.setItem('logged_in', logged_in)
      state.logged_in = logged_in
    },

    theme(state, mode) {
      state.mode = mode
    },

    log_admin(state, admin) {
      state.admin = admin
    },

    set_collectionData(state, collectionData) {
      state.collectionData = collectionData
    },

    set_artworkData(state, artworkData) {
      state.artworkData = artworkData
    },

    set_contestData(state, contestData) {
      state.contestData = contestData
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

    set_user(state, user) {
      state.user = user
    }
    // change(state, register_in_form) {
    //   state.register_in_form = register_in_form
    // }
  },
  actions: {
    login({commit}, user){
      return new Promise((resolve, reject) => {
        commit('auth_request')
        console.log(user)
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/login`, data: user, method: 'POST' })
        .then(function(resp) {
          // console.log('data ' + resp.data)
          const token = resp.data
          //const user = resp.data.user
          localStorage.setItem('logged_in', true)
          console.log('login ' + localStorage.getItem('logged_in'))
          sessionStorage.setItem('token', token)
          console.log('login ' + sessionStorage.getItem('token'))
          commit('auth_success', token, user)
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error')
          sessionStorage.removeItem('token')
          reject(err)
        })
      })
    },

    register({commit}, user){
      return new Promise((resolve, reject) => {
        commit('auth_request')
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/visitor/registration`, data: user, method: 'POST' })
        .then(resp => {
          // const token = resp.data.token
          const user = resp.data.user
          // localStorage.setItem('token', token)
          // axios.defaults.headers.common['Authorization'] = token
          // commit('auth_success', token, user)
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error', err)
          sessionStorage.removeItem('token')
          reject(err)
        })
      })
    },

    logout({commit}){
      return new Promise((resolve, reject) => {
        // commit('logout')
        // console.log('bef ' + localStorage.getItem('token') + ' ' + Store.state.token)
        console.log('aft ' + sessionStorage.getItem('token'))
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/visitor/logout`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
              }, 
              data: {}, 
              method: 'POST' 
            })
        .then(resp => {
          console.log('aft1 ' + sessionStorage.getItem('token'))
          sessionStorage.removeItem('token')
          localStorage.setItem('logged_in', false)
          console.log('logout ' + localStorage.getItem('logged_in'))
          delete axios.defaults.headers.common['Authorization']
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error', err)
          sessionStorage.removeItem('token')
          reject(err)
        })
      })
    },
    create_collection({commit}, collectionData){
      return new Promise((resolve, reject) => {
        console.log('aft ' + sessionStorage.getItem('token'))
        console.log(collectionData)
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/createCollection`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
                'Content-Type': 'application/json'
              }, 
              data: collectionData, 
              method: 'POST' 
            })
        .then(resp => {
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error', err)
          // sessionStorage.removeItem('token')
          reject(err)
        })
      })
    },
    add_artwork({commit}, artworkData){
      return new Promise((resolve, reject) => {
        console.log('aft ' + sessionStorage.getItem('token'))
        console.log(artworkData)
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/addArtwork`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
                // 'Content-Type': 'application/json'
              }, 
              data: artworkData, 
              method: 'POST' 
            })
        .then(resp => {
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error', err)
          // sessionStorage.removeItem('token')
          reject(err)
        })
      })
    },
    create_contest({commit}, contestData){
      return new Promise((resolve, reject) => {
        console.log('aft ' + sessionStorage.getItem('token'))
        console.log(contestData)
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/admin/createContest`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
                'Content-Type': 'application/json'
              }, 
              data: contestData, 
              method: 'POST' 
            })
        .then(resp => {
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error', err)
          // sessionStorage.removeItem('token')
          reject(err)
        })
      })
    },
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
    user: state => state.user,
    token: state => state.token
  }
})
