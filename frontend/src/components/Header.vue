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
          <v-img
            v-bind="props"
            gradient="to top right, rgba(19,84,122,.5), rgba(128,208,199,.8)"
          ></v-img>
        </template>

        <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->

        <!-- <v-toolbar-title><v-btn text rounded class="home">onlinegalerija</v-btn></v-toolbar-title> -->
        <v-toolbar-title>onlinegalerija</v-toolbar-title>

        <v-spacer></v-spacer>

        <div v-if="!this.$store.getters.logged_in" class="prijava">
        <v-btn text rounded v-on:click="sign_in()">prijava</v-btn>
        <v-btn text rounded v-on:click="register()">registracija</v-btn>
        <v-btn icon v-on:click="mode()"><v-icon>mdi-coach-lamp</v-icon></v-btn>
        </div>

        
        <div v-else>
          <v-menu rounded="b-xl">
            <template v-slot:activator="{ on: menu, attrs }">
              <v-tooltip bottom>
                <template v-slot:activator="{ on: tooltip }">
                  <v-btn icon class="prijava"
                    v-bind="attrs"
                    v-on="{ ...tooltip, ...menu }"
                  >
                    <v-icon>mdi-account</v-icon>
                  </v-btn>
                </template>
                <span>Moj Profil</span>
              </v-tooltip>
            </template>

            <v-list>
              <v-list-item link> Transakcije <v-spacer></v-spacer> <v-icon>mdi-cash-multiple</v-icon> </v-list-item>
              <v-list-item link> Osobni Podaci <v-spacer></v-spacer> <v-icon>mdi-account-details</v-icon> </v-list-item>
              <v-list-item link v-on:click="sign_out()" to="/"> Odjava <v-spacer></v-spacer> <v-icon>mdi-exit-to-app</v-icon> </v-list-item>
            </v-list>
          </v-menu>
          <v-btn icon class="prijava" v-on:click="mode()"><v-icon>mdi-coach-lamp</v-icon></v-btn>
          <v-btn icon class="prijava" to="/"><v-icon>mdi-home-roof</v-icon></v-btn>
        </div>
      </v-app-bar>   
    </v-card>
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

  methods: {
    sign_in() {
      //this.sign_att = true;
      //this.sign_in_form = true;
      //this.register_form = false;
      this.$store.commit('sign_in', true)
      this.$store.commit('register', false)
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
      this.$store.commit('show_tool', false)
      this.$store.commit('sign_in', false)
      this.$store.commit('register', false)
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
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center;
  color: #2c3e50; */
}

.prijava {
  margin-top: 0px;
  /* margin-bottom: 10px; */
}

.btn {
    text-transform: none !important;
}

.home {
    text-transform: none !important;
    font-size: 28px !important;
    font-family: Avenir, sans-serif !important;
    -webkit-font-smoothing: antialiased !important;
    -moz-osx-font-smoothing: grayscale !important;
}
#nav {
  

  /* a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  } */
}
</style>
