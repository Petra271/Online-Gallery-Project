<template>
  <v-app>
    <!-- <div class="front_page"> -->
      <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
      <div>
        <Header/>
      </div>
    <!-- <v-card class="overflow-hidden">
    <v-app-bar
      fixed
      color="#fcb69f"
      dark
      shrink-on-scroll
      src="https://picsum.photos/1920/1080?random"
      
    >
      <template v-slot:img="{ props }">
        <v-img
          v-bind="props"
          gradient="to top right, rgba(19,84,122,.5), rgba(128,208,199,.8)"
        ></v-img>
      </template> -->

      <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->

      <!-- <v-toolbar-title>onlinegalerija</v-toolbar-title>

      <v-spacer></v-spacer>

      <div v-if="!form">
      <v-btn text rounded class="prijava" v-on:click="sign_in()">prijava</v-btn>
      <v-btn text rounded class="prijava" v-on:click="register()">registracija</v-btn>
      </div>

      
        <div v-else>
          <v-menu rounded="b-xl">
            <template v-slot:activator="{ on: menu, attrs }">
              <v-tooltip bottom>
                <template v-slot:activator="{ on: tooltip }">
                  <v-btn icon
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
              <v-list-item link> Transakcije  <v-spacer></v-spacer> <v-icon>mdi-cash-multiple</v-icon> </v-list-item>
              <v-list-item link> Osobni Podaci <v-spacer></v-spacer> <v-icon>mdi-account-details</v-icon> </v-list-item>
              <v-list-item link v-on:click="sign_out()"> Odjava <v-spacer></v-spacer> <v-icon>mdi-exit-to-app</v-icon> </v-list-item>
            </v-list>
          </v-menu>

          <v-btn icon>
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </div>
    </v-app-bar>   
  </v-card> -->
  
  <div class="gal_title text-center">Dobrodošli u online galeriju</div>
  
  <!-- ---------------- PRIJAVA ------------------------- -->
    <div v-if="this.$store.getters.sign_in_form" class="form" ref="enter_form">
      <v-form
        ref="form"
        v-model="valid"
        lazy-validation
      >

        <v-text-field
          v-model="email_sign"
          :rules="emailRules"
          label="E-mail"
          required
        ></v-text-field>

        <v-text-field
          v-model="password_sign"
          :append-icon="show_sign ? 'mdi-eye' : 'mdi-eye-off'"
          :rules="[pass_rules.required, pass_rules.min]"
          :type="show_sign ? 'text' : 'password'"
          name="input-10-1"
          label="Lozinka"
          counter
          @click:append="show_sign = !show_sign"
          required
        ></v-text-field>

      <div class="form_buttons">
        <v-btn rounded 
          :disabled="!valid"
          color="rgba(1, 61, 21)"
          class="mr-4 white--text"
          @click="validate()"
        >
          Prijava
        </v-btn>

        <v-btn rounded
          color="rgb(120, 1, 1)"
          class="mr-4 white--text"
          @click="cancel()"
        >
          Natrag
        </v-btn>

      </div>
      </v-form>
    </div>

    <!-- ------------------- REGISTRACIJA ------------------ -->
    <div v-if="this.$store.getters.register_form" class="form" ref="enter_form">
      <v-form
        ref="form"
        v-model="valid"
        lazy-validation
      >
        <v-text-field
          v-model="name"
          :rules="[v => !!v || 'Potrebno je upisati ime']"
          label="Ime"
          required
        ></v-text-field>

        <v-text-field
          v-model="surname" 
          :rules="[v => !!v || 'Potrebno je upisati prezime']"
          label="Prezime"
          required
        ></v-text-field>

        <v-text-field
          v-model="email"
          :rules="emailRules"
          label="E-mail"
          required
        ></v-text-field>

        <v-text-field
          v-model="payPal"
          :rules="payPalRules"
          label="PayPal račun"
          required
        ></v-text-field>

        <v-text-field
          v-model="password_reg"
          :append-icon="show_reg ? 'mdi-eye' : 'mdi-eye-off'"
          :rules="[pass_rules.required, pass_rules.min]"
          :type="show_reg ? 'text' : 'password'"
          name="input-10-1"
          label="Lozinka"
          hint="Najmanje 8 znakova"
          counter
          @click:append="show_reg = !show_reg"
        ></v-text-field>

        <v-file-input v-if="artist_check"  
          :rules="[v => !!v || 'Potrebno je priložiti datoteku']"
          small-chips    
          accept="application/pdf"
          label="Priložite PDF datoteku"
        ></v-file-input>

        <v-alert
          :value="artist_check"
          color="blue"
          dark
          border="top"
          icon="mdi-brush"
          transition="scale-transition"
        >
          Ukoliko se želite registrirati kao umjetnik trebate priložiti svoj portfolio u obliku PDF datoteke.
        </v-alert>
        
        <v-checkbox
          v-model="artist_check"
          label="Ja sam umjetnik"
        ></v-checkbox>

        <!-- <v-select
          v-model="select"
          :items="items"
          :rules="[v => !!v || 'Item is required']"
          label="Item"
          required
        ></v-select> -->

      <div class="form_buttons">
        <v-btn rounded
          :disabled="!valid"
          color="rgba(1, 61, 21)"
          class="mr-4 white--text"
          @click="validate()"
        >
          Registracija
        </v-btn>

        <v-btn rounded
          color="rgba(120, 1, 1)"
          class="mr-4 white--text"
          @click="cancel()"
        >
          Natrag
        </v-btn>

      </div>
      </v-form>
    </div>

  <!-- --------------------- SLIKE -------------------------------- -->
    <div class="exh_text"> Aktivne izložbe </div>
    <v-row>
      <v-col
        v-for="n in 8"
        :key="n"
        class="d-flex child-flex"
        cols="4"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
          >
            <v-img
              :src="`https://picsum.photos/500/300?image=${n * 5 + 10}`"
              :lazy-src="`https://picsum.photos/10/6?image=${n * 5 + 10}`"
              aspect-ratio="1"
              class="grey lighten-2 img"
            >
            <v-expand-transition>
              <div
                v-if="hover"
                class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
                style="height: 100%;"
              >
              <div class="izl">
                <p>Izložba {{n}}</p> <br>
                Traje do: 1{{n}}.11.2020
              </div>
              </div>
            </v-expand-transition>
              <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      color="black" 
                      class="img_btn" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      to="/izlozba">
                      <v-icon>mdi-door-open</v-icon>
                    </v-btn>
                  </template>
                  <span>Posjeti izložbu</span>
                </v-tooltip>
              </v-card-title>
            </v-img>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>

    <div class="exh_text"> Nadolazeće izložbe </div>
    <v-row>
      <v-col
        v-for="n in 8"
        :key="n"
        class="d-flex child-flex"
        cols="4"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
          >
            <v-img
              :src="`https://picsum.photos/500/300?image=${n * 3 + 10}`"
              :lazy-src="`https://picsum.photos/10/6?image=${n * 3 + 10}`"
              aspect-ratio="1"
              class="grey lighten-2 img"
            >
            <v-expand-transition>
              <div
                v-if="hover"
                class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
                style="height: 100%;"
              >
                <div class="izl">
                  <p>Izložba {{n}}</p> <br>
                  Traje do: 1{{n}}.11.2020
                </div>
              </div>
            </v-expand-transition>
              <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      color="black" 
                      class="img_btn" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      to="/izlozba"
                      >
                      <v-icon>mdi-door-open</v-icon>
                    </v-btn>
                  </template>
                  <span>Posjeti izložbu</span>
                </v-tooltip>
              </v-card-title>

              <template v-slot:placeholder>
                <v-row
                  class="fill-height ma-0"
                  align="center"
                  justify="center"
                >
                  <v-progress-circular
                    indeterminate
                    color="grey lighten-5"
                  ></v-progress-circular>
                </v-row>
              </template>
            </v-img>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>

  </div>

    <!-- <v-main>
      <HelloWorld/>
    </v-main> -->
  </v-app>
</template>

<script>
import HelloWorld from '../components/HelloWorld';
import Header from '@/components/Header'

export default {
  name: 'App',

  components: {
    HelloWorld,
    Header
  },

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
      payPalRules: [
        v => !!v || 'Potrebno je upisati PayPal račun',
        v => /.+@.+\..+/.test(v) || 'Paypal račun mora biti valjan',
      ],
      select: null,
      checkbox: false,
      show_sign: false,
      show_reg: false,
      pass_rules: {
          required: value => !!value || 'Potrebno je upisati lozinku.',
          min: v => v.length >= 8 || 'Minimalno 8 znakova',
          emailMatch: () => (`The email and password you entered don't match`),
        },
    }
  },

  mounted() {
        // Set the default value of this.item based on what's in the store
        this.sign_in_form = this.$store.getters.sign_in_form
        //this.enter_exh = this.$store.getters.logged_in
        if (this.$store.getters.logged_in) {
          this.enter_exh = true;
        } else {
          this.enter_exh = false;
        }
    },

  methods: {
    sign_in() {
      //this.sign_att = true;
      // this.sign_in_form = true;
      // this.register_form = false;
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
          // this.form = true;
          this.$store.commit('show_tool', true)
          this.$store.commit('sign_in', false)
          this.$store.commit('register', false)
        }
        if (this.$store.getters.logged_in) {
          this.enter_exh = true;
        }
        this.sign_in_form = false;
        this.register_form = false;
    },

    cancel() {
      this.$refs.form.reset()
      // this.sign_in_form = false;
      // this.register_form = false;
      this.$store.commit('sign_in', false)
      this.$store.commit('register', false)
    },

  }
  
};
</script>

<style>
.front_page {
  background-color: rgb(214, 136, 46);
  /* background-color:#f08989; */
}

.light {
  /* background-color: rgb(214, 136, 46); */
  background-color: rgb(93, 202, 166);
  color: #2c3e50;
}

.dark {
  background-color: rgb(22, 5, 12);
  color: #016b4b;
}

.prijava {
  margin-top: 7px;
}

.form {
  align-content: center;
  margin: auto;
  margin-top: 2%;
  max-width: 50%;
  padding: 2%;
  border-radius: 50px;
  background-color: white;
}

.form_buttons {
  align-content: center;
  margin-right: auto;
  margin-left: auto;
}

.gal_title {
  align-content: center;
  justify-content: center;
  font-size: 100px;
  /* font-family:  Georgia; */
  font-family: Georgia;
  margin-left: 2.5%;
  margin-top: 12%;
}

.exh_text {
  font-size: 50px;
  font-family:  Georgia;
  margin-left: 2.5%;
  margin-top: 5%;
}

.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.5;
  position: absolute;
  width: 100%;
}

.images {
  align-content: right;
  justify-content: center;
  margin: auto;
  margin-top: 5%;
  margin-bottom: 5%;
  height: 0%;
  width: 95%;
  /* background-color: rgb(214, 136, 46); */
  border-radius: 50px;
}

.img_hov {
  border-radius: 50px;
}

.img {
  margin: 7%;
  border-radius: 50px;
}

.izl {
  align-content: center;
  /* margin-left: 15%; */
}

.img_btn {
  background-color:rgb(209, 74, 74, 0.3);  
  /* background-color: rgb(22, 5, 12, 0.3); */
  /* opacity: 0.8; */
}
</style>
