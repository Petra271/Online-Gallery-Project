<template>
  <div id="app">
    <v-card class="overflow-hidden">
      <v-app-bar
        fixed
        color="#fcb69f"
        dark
        shrink-on-scroll
        src="@/assets/coland.jpg"
        
      >
        <template v-slot:img="{ props }">
          <v-img v-if="!$store.getters.mode"
            v-bind="props"
            gradient="to top right,  rgba(0, 0, 0, 0.8),  rgba(0, 0, 0, 0.6)"
          ></v-img>

          <v-img v-else
            v-bind="props"
            gradient="to top right,  rgba(255, 255, 255, 0.5),  rgba(255, 255, 255, 0.3)"
          ></v-img>
        </template>

        <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->

        <!-- <v-toolbar-title><v-btn text rounded class="home">onlinegalerija</v-btn></v-toolbar-title> -->
        <v-toolbar-title @click="home()"
          :class="{'tool_title': !$store.getters.mode, 'tool_title black--text': $store.getters.mode}"
          style="cursor: pointer;"
          >
          onlinegalerija
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <div v-if="!this.$store.getters.logged_in">
          <v-btn text rounded @click="sign_in()" :class="{'black--text': $store.getters.mode}">prijava</v-btn>
          <v-btn text rounded @click="register()" :class="{'black--text': $store.getters.mode}">registracija</v-btn>
          <v-btn icon @click="mode()">
            <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-coach-lamp</v-icon>
          </v-btn>
        </div>

        
        <div v-else>
          
          <v-btn icon @click="mode()">
            <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-coach-lamp</v-icon>
          </v-btn>
          <v-btn icon @click="home()">
            <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-home-roof</v-icon>
          </v-btn>

          <v-menu rounded="b-xl">
            <template v-slot:activator="{ on: menu, attrs }">
              <v-tooltip bottom>
                <template v-slot:activator="{ on: tooltip }">
                  <v-btn icon
                    v-bind="attrs"
                    v-on="{ ...tooltip, ...menu }"
                  >
                    <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-menu</v-icon>
                  </v-btn>
                </template>
                <span>Izbornik</span>
              </v-tooltip>
            </template>

            <v-list>
              <v-list-item to="/transakcije" link> Transakcije <v-spacer></v-spacer> <v-icon>mdi-cash-multiple</v-icon> </v-list-item>
              <v-list-item to="/moj_profil" link> Moj profil <v-spacer></v-spacer> <v-icon>mdi-account-details</v-icon> </v-list-item>
              <v-list-item to="/natjecaji" link> Natječaji <v-spacer></v-spacer> <v-icon>mdi-medal-outline</v-icon> </v-list-item>
              <v-list-item link v-on:click="sign_out()"> Odjava <v-spacer></v-spacer> <v-icon>mdi-exit-to-app</v-icon> </v-list-item>
            </v-list>
          </v-menu>
        </div>
      </v-app-bar>   
    </v-card>

    <v-snackbar
      timeout="3000"
      :value="snackOut"
      outlined
      multi-line
    >
      <p style="text-align: center; margin-bottom: -1%; font-size: 16px;">      
        Uspješno ste se odjavili.
      </p>
    </v-snackbar>

    <div id="nav">
      <!-- <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link> |
      <router-link to="/front">Enter</router-link> -->
    </div>
    <router-view/>
  </div>
  
</template>

<script>
export default {
  name: 'Header',

  data: () => {
    return {
      snackOut: false,
      sign_att: false,
      register_att: false,
      form: false,
      sign_in_form: false,
      register_form: false,
      enter_exh: false,
      artist_check: false,

      colors: ['deep-purple accent-4', 'error', 'teal darken-1'],
      valid: true,
      name: '',
      surname: '',
      payPal: '',
      password_sign: '',
      password_reg: '',
      nameRules: [
        v => !!v || 'Name is required',
        // v => (v && v.length <= 10) || 'Name must be less than 10 characters',
      ],
      email: '',
      email_sign: '',
      emailRules: [
        v => !!v || 'Potrebno je upisati E-mail',
        v => /.+@.+\..+/.test(v) || 'E-mail mora biti valjan',
      ],
      select: null,
      checkbox: false,
      show_sign: false,
      show_reg: false,
      pass_rules: {
          required: value => !!value || 'Potrebno je upisati lozinku.',
          min: v => v.length >= 8 || 'Min 8 characters',
          emailMatch: () => (`The email and password you entered don't match`),
        },
    }
  },

  computed: {

  },

  methods: {
    home() {
      this.$router.push('/')
    },

    snackbar() {
      setTimeout(() => { 
        this.snackOut = false;        
      }, 3000);
    },

    sign_in() {
      //this.sign_att = true;
      //this.sign_in_form = true;
      //this.register_form = false;
      this.$store.commit('sign_in', true)
      this.$store.commit('register', false)
    },

    scrollToClass(className) {
      // Get the first element with the given class name
      let el = this.$el.querySelector(className)
      // Get the bounding rectangle so we can get the element position position
      let rect = el.getBoundingClientRect()
      // Scroll to the element (using x and y axis)
      window.scrollTo(rect.left, rect.top)
    },

    register() {
      //this.register_att = true;
      // this.register_form = true;
      // this.sign_in_form = false;
      this.$store.commit('register', true)
      this.$store.commit('sign_in', false)
    },

    sign_out() {
      // this.sign_in_form = false;
      // this.register_form = false;
      // this.enter_exh = false;
      // this.$store.commit('show_tool', false)
      // this.$store.commit('sign_in', false)
      // this.$store.commit('register', false)
      // this.$router.push('/')
      console.log('sign out')
      this.$store.dispatch('logout')
        .then(() => this.sign_out_success())
        .catch(err => console.log(err))
      // this.$store.commit('show_tool', false)
      // this.$store.commit('sign_in', false)
      // this.$store.commit('register', false)
    },

    sign_out_success() {
      this.$store.commit('show_tool', false)
      this.$store.commit('sign_in', false)
      this.$store.commit('register', false)
      this.snackOut = true
      this.$router.push('/')
    },

    validate() {
        this.$refs.form.validate()
        if (this.valid) {
          this.form = true;
          this.$store.commit('show_tool', true)
        }
      //   this.enter_exh = true;
      //   this.sign_in_form = false;
      //   this.register_form = false;
    },

    cancel() {
      this.$refs.form.reset()
      // this.sign_in_form = false;
      // this.register_form = false;
    },

    home() {
      this.$router.push('/')
    },

    mode() {
      // if (this.$store.getters.mode) {
      //    this.$store.commit('theme', false)
      // } else {
      //    this.$store.commit('theme', true)
      // }
      this.$store.getters.mode ? this.$store.commit('theme', false) : this.$store.commit('theme', true)
    }
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Work+Sans:wght@100;400&display=swap');

#app {
  font-family: 'Work Sans', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center;
  color: #2c3e50; */
}

.tool_title {
  font-weight: 200;
  color: "{$store.getters.mode ? black : white}";
}

/* .tool_image {
  background-image: linear-gradient(top right, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.6));
} */

.prijava {
  
  font-weight: 100;
  /* margin-bottom: 10px; */
}

.btn {
    text-transform: none !important;
}

.home {
    text-transform: none !important;
    font-size: 28px !important;
    font-family: 'Work Sans', sans-serif !important;
    -webkit-font-smoothing: antialiased !important;
    -moz-osx-font-smoothing: grayscale !important;
}
/* #nav {
  

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
} */
</style>
