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
    artist: false,
    status: '',
    token: sessionStorage.getItem('token') || '',
    user : {},
    collectionData: {},
    artworkData: {},
    contestData: {},
    collections: [],
    currentCollection: '',
    currentStyle: '',
    removeArtwork: {},
    applyToContestData: {},
    artBuySrc: '',
    exhibitionData: {},
    artistBuyName: '',
    artDescription: []
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
      sessionStorage.setItem('logged_in', logged_in)
      state.logged_in = logged_in
    },

    theme(state, mode) {
      state.mode = mode
    },

    log_admin(state, admin) {
      state.admin = admin
    },

    log_artist(state, artist) {
      state.artist = artist
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

    set_currentCollection(state, currentCollection) {
      state.currentCollection = currentCollection
    },

    set_currentStyle(state, currentStyle) {
      state.currentStyle = currentStyle
    },

    remove_artworkData(state, removeArtwork) {
      state.removeArtwork = removeArtwork
    },

    set_applyToContest(state, applyToContestData) {
      state.applyToContestData = applyToContestData
    },

    set_exhibitionData(state, exhibitionData) {
      state.exhibitionData = exhibitionData
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
    },

    set_col(state, collections) {
      state.collections = collections
    },

    buy_art(state, artBuySrc) {
      state.artBuySrc = artBuySrc
    },

    set_art_desc(state, artDescription) {
      state.artDescription = artDescription
    },

    set_artist_buy(state, artistBuyName) {
      state.artistBuyName = artistBuyName
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
          let token = resp.data
          //const user = resp.data.user
          sessionStorage.setItem('logged_in', true)
          //console.log('login ' + sessionStorage.getItem('logged_in'))
          sessionStorage.setItem('tokenBefore', token)
          let userType = token.toString().charAt(token.length - 1)
          userType = userType.toString()
          console.log('userType ' + userType)
          if (userType === '1') {
            commit('log_admin', true)
            commit('log_artist', false)
            console.log('jedan ' + userType)
            
          } else if (userType === '2') {
            commit('log_artist', true)
            commit('log_admin', false)
            console.log('dva ' + userType)
          } else {
            commit('log_artist', false)
            commit('log_admin', false)
          }
          sessionStorage.setItem('userType', userType)
          token = token.substring(0, token.length - 2)
          sessionStorage.setItem('token', token)
          console.log('login ' + sessionStorage.getItem('token'))
          commit('auth_success', token, user)
          sessionStorage.setItem('filtered', false)
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
          sessionStorage.setItem('logged_in', false)
          sessionStorage.setItem('filtered', false)
          console.log('logout ' + sessionStorage.getItem('logged_in'))
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
    
    remove_Artwork({commit}, removeArtwork){
      return new Promise((resolve, reject) => {
        console.log('aft ' + sessionStorage.getItem('token'))
        console.log(removeArtwork)
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/removeArtwork`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
                'Content-Type': 'application/json'
              }, 
              data: removeArtwork, 
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

    create_applyToContest({commit}, applyToContestData){
      return new Promise((resolve, reject) => {
        console.log('aft ' + sessionStorage.getItem('token'))
        console.log(applyToContestData)
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/applyToContest`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
                'Content-Type': 'application/json'
              }, 
              data: applyToContestData, 
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

    create_exhibition({commit}, exhibitionData){
      return new Promise((resolve, reject) => {
        console.log('aft ' + sessionStorage.getItem('token'))
        console.log(JSON.stringify(exhibitionData))
        axios({url: `${process.env.VUE_APP_BACKEND_URI}/admin/createExhibition`, 
              headers: {
                'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
                // 'Content-Type': 'application/json'
              }, 
              data: exhibitionData, 
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
    artist: state =>  state.artist,
    isLoggedIn: state => !!state.token,
    authStatus: state => state.status,
    user: state => state.user,
    token: state => state.token,
    collections: state => state.collections,
    currentCollection: state => state.currentCollection,
    currentStyle: state => state.currentStyle,
    artBuySrc: state => state.artBuySrc,
    artDescription: state => state.artDescription,
    artistBuyName: state => state.artistBuyName
  }
})
