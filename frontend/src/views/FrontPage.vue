<template>
  <v-app>
    <!-- <div class="front_page"> -->
    <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
      <div>
        <Header/>
      </div>


  <div class="gal_title text-center"
      style="cursor: pointer"
      @mouseover="descIn()" @mouseleave="descOut()"
      >
      Dobrodošli u online galeriju
  </div>
  <!-- <div><v-btn @click="getCollections()">izložbe</v-btn></div> -->
  <!-- <v-btn @click="snackReg = !snackReg">snack</v-btn> -->
  <div class="gal_desc">{{description}}
  </div>

        <!-- <div v-for="(artworks, colDesc) in collections" :key="colDesc">
          key - {{colDesc}}
          <div v-for="(artSrc, artInfo) in artworks" :key="artInfo">
            {{artInfo}}
            <div v-for="(info, key) in JSON.parse(artInfo)" :key="key">
            {{key}} = {{info}}
            </div>
            <v-img style="height: 200px; width: 200px;"
              :src="'data:image/jpg;base64,' + artSrc"
              :lazy-src="'data:image/jpg;base64,' + artSrc"
              aspect-ratio="1"
              class="grey lighten-2 img"
            ></v-img>
          </div>
        </div> -->
        <!-- <div v-for="(artwork, exhDesc) in exhibits" :key="exhDesc">
          key - {{exhDesc}} 
          
            <div v-for="(info, key) in JSON.parse(exhDesc)" :key="key">
            {{key}} = {{info}}
            </div>
            <v-img style="height: 200px; width: 200px;"
              :src="'data:image/jpg;base64,' + artwork"
              :lazy-src="'data:image/jpg;base64,' + artwork"
              aspect-ratio="1"
              class="grey lighten-2 img"
            ></v-img>
            
          <br>
            -----------------------
        </div> -->
        <!-- <div>{{exhibits}}</div> -->
        <!-- <div>{{artists}}</div>
        <div>{{selectedArtists}}</div>
        <div>{{techniques}}</div>
        <div>{{techniques[selectedTech - 1]}}</div>
        <div>{{exhibitsFiltered}}</div> 
        <div>{{collections}}</div>
        <div>{{exhDescriptions}}</div>
        <div>{{artSources}}</div> -->
  <!-- <div>
    {{exhibits}}
  </div> -->
  <!-- ---------------- PRIJAVA ------------------------- -->
    <div v-if="this.$store.getters.sign_in_form" class="form" ref="enter_form">
      <v-form
        ref="form"
        class="form_write"
        v-model="valid"
        lazy-validation
      >

        <v-text-field class="form_write"
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
          color="rgba(1, 24, 12)"
          class="mr-4 white--text"
          @keyup.13="validate_s()"
          @click="validate_s()"
        >
          Prijava
        </v-btn>

        <v-btn rounded
          color="rgb(33, 1, 1)"
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
          type="file"
          v-model="pdf"
          :rules="[v => !!v || 'Potrebno je priložiti datoteku']"
          small-chips    
          accept="application/pdf"
          label="Priložite PDF datoteku"
        ></v-file-input>

        <v-alert
          :value="artist_check"
          color="black"
          dark
          icon="mdi-brush"
          transition="scale-transition"
        >
          Ukoliko se želite registrirati kao umjetnik trebate priložiti svoj portfolio u obliku PDF datoteke.
        </v-alert>
        
        <v-checkbox
          color="black"
          v-model="artist_check"
          label="Ja sam umjetnik"
        ></v-checkbox>

      <div class="form_buttons">
        <v-btn rounded
          :disabled="!valid"
          color="rgba(1, 24, 12)"
          class="mr-4 white--text"
          @click="validate_r()"
        >
          Registracija
        </v-btn>

        <v-btn rounded
          color="rgba(33, 1, 1)"
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
        cols="12"
        sm="4"
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
                class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal display-3"
                :class="!$store.getters.mode ? 'hover_light white--text' : 'hover_dark'"
                style="height: 50%;"
              >
                <div class="izl">
                  <!-- <p>Izložba {{n}}</p> <br>
                  Traje do: 1{{n}}.11.2020 -->
                  <div class="izl_author"><i><b> Jerolim Miše </b></i></div>
                  <div class="izl_name"><i> -Od buntovnika do barda </i></div>
                </div>
              </div>
            </v-expand-transition>
              <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      to="/izlozba"
                      >
                      <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-door-open</v-icon>
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
                class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal display-3"
                :class="{'hover_light white--text': !$store.getters.mode, 'hover_dark':$store.getters.mode}"
                style="height: 50%;"
              >
                <div class="izl">
                  <div class="izl_author"><i><b>Jerolim Miše</b></i></div>
                  <div class="izl_name"><i>-Od buntovnika do barda</i></div>
                </div>
              </div>
            </v-expand-transition>
              <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      to="/izlozba">
                      <v-icon v-if="$store.getters.mode" color="black">mdi-door-open</v-icon>
                      <v-icon v-else color="white">mdi-door-open</v-icon>
                    </v-btn>
                  </template>
                  <span>Posjeti izložbu</span>
                </v-tooltip>
              </v-card-title>

              <!-- <template v-slot:placeholder>
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
              </template> -->
            </v-img>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>


    <!-- ---------------- IZLOŽBE UŽIVO ---------------- -->
    <div class="exh_text"> Izložbe uživo </div>
    <v-row>
      <v-col
        v-for="n in noOfExhs" :key="n"
        class="d-flex child-flex"
        cols="12"
        sm="4"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
          >
            <v-img
              :src="'data:image/jpg;base64,' + artSources[n - 1]"
              :lazy-src="'data:image/jpg;base64,' + artSources[n - 1]"
              aspect-ratio="1"
              class="grey lighten-2 img"
            >
            <v-expand-transition>
              <div
                v-if="hover"
                class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal display-3"
                :class="!$store.getters.mode ? 'hover_light white--text' : 'hover_dark'"
                style="height: 50%;"
              >
                <div class="izl">
                  <!-- <div class="izl_author"><i><b> {{exhDescriptions[n - 1]["Artists"]}} </b></i></div>
                  <div class="izl_name"><i> -{{exhDescriptions[n - 1]["Name"]}} </i></div> -->
                  <div ><i><b> {{exhDescriptions[n - 1]["Artists"]}} <br> </b></i>
                  <i> -{{exhDescriptions[n - 1]["Name"]}} </i></div>
                </div>
              </div>
            </v-expand-transition>
              <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      @click="getExhibition(exhDescriptions[n - 1]['Name'])"
                     
                      >
                      <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-door-open</v-icon>
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

    <!-- ---------- FILTERI ---------- -->
    <v-row justify="center">
      <v-dialog
        transition="dialog-bottom-transition"
        v-model="filters"
        persistent
        max-width="630"
      >
        <v-card>
          <v-card-title class="text-left justify-center py-6"
                        style="font-size: 100%;">
                        <!-- border-bottom: 1px solid black"> -->
            <div class="display-3"
            style="margin-right: auto; margin-bottom: 2%;">
              Prikaz
            </div>
            <div>
            Sortirajte izložbe prema odgovarajućem stilu, 
            pripadajućim umjetnicima i razdoblju trajanja
            <!-- {{tab}} {{tehnike[radioGroup - 1]}} {{selectedArtists}} -->
            </div>
          </v-card-title>

          <v-tabs
            v-model="tab"
            background-color="transparent"
            color="black"
            grow
          >
            <v-tab
              v-for="filter in filter_types"
              :key="filter"
            >
              {{ filter }}
            </v-tab>
          </v-tabs>

          <v-tabs-items v-model="tab">
            <v-tab-item>
              <v-card flat>
                <v-card-text class="black--text">Odaberite tehniku izložbi koje želite prikazati</v-card-text>
                <v-radio-group class="technique" v-model="selectedTech">
                  <v-radio
                    v-for="(teh, i) in techniques.length"
                    :key="teh"
                    :label="techniques[i]"
                    :value="teh"
                    color="black"
                  ></v-radio>
                  <v-radio
                    label="Poništite odabir"
                    @click="selectedTech = null"
                    :value="4"
                    color="black"
                  ></v-radio>
                </v-radio-group>
              </v-card>
            </v-tab-item>
            <v-tab-item>
              <v-card flat>
                <v-card-text class="black--text">Odaberite jednog ili više umjetnika čije izložbe želite prikazati</v-card-text>
                <v-autocomplete
                  v-model="selectedArtists"
                  :items="artists"
                  class="mx-4"
                  multiple
                  small-chips
                  deletable-chips
                  clearable
                  label="Umjetnici"
                  color="black"
                ></v-autocomplete>
              </v-card>
            </v-tab-item>
            <v-tab-item>
              <v-card flat>
                <div class="calendar">
                  <v-date-picker
                    v-model="exh_dates"
                    scrollable
                    range
                    locale="hr"
                    :elevation="1"
                    :first-day-of-week="1"
                    selected-items-text="Željeni raspon"
                    color="black"
                  ></v-date-picker>
                </div>
              </v-card>
            </v-tab-item>
          </v-tabs-items>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="black"
              text
              @click="filters = false"
            >
              Natrag
            </v-btn>
            <v-btn
              color="black"
              text
              @click="applyFilters()"
            >
              Primijeni
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
    <v-btn fab 
          :dark="!$store.getters.mode" 
          :color="$store.getters.mode ? 'white' : 'rgb(0, 0, 0, 0.9)'"
          fixed right bottom
          @click="filters = true"
          >
      <v-icon>mdi-sort</v-icon>
    </v-btn>

    <v-snackbar
      timeout="3000"
      :value="snackReg"
      outlined
      multi-line
    >
      <p style="text-align: center; margin-bottom: -1%; font-size: 16px;">      
      Registracija uspješna. Molimo <br>
      prijavite se ukoliko želite nastaviti.</p>
    </v-snackbar>    

    <v-row>
      <v-col
        cols="12"
        sm="6"
      >
        <!-- <v-date-picker
          v-model="exh_dates"
          scrollable
          range
          locale="hr"
          :first-day-of-week="1"
          color="black"
        ></v-date-picker>
      </v-col>
      <v-col
        cols="12"
        sm="6"
      >
        <v-text-field
          v-model="dateRangeText"
          label="Date range"
          prepend-icon="mdi-calendar"
          readonly
        ></v-text-field>  -->
        <!-- model: {{ exh_dates }} -->
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
import axios from 'axios';

export default {
  name: 'App',

  components: {
    HelloWorld,
    Header
  },

  data: () => {
    return {
      description: '',
      snackReg: false,
      sign_att: false,
      register_att: false,
      timer: null,
      form: false,
      sign_in_form: false,
      register_form: false,
      enter_exh: false,
      artist_check: false,

      pdf: 'empty_file',
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
      is_admin: false,
      exh_dates: ['2020-12-10', '2020-12-20'],
      filters: false,
      tab: null,
      filter_types: ['Stil', 'Umjetnici', 'Datum'],
      radioGroup: 0,
      tehnike: ['Akvarel', 'Mozaik', 'Tempera', 'Ulje na platnu'],
      artistsTmp: [
        'Mersad Berber',
        'Edo Murtić',
        'Jerolim Miše',
        'Vlaho Bukovac',
        'Vasko Lipovac',
        'Ivan Rabuzin',
        'Oton Iveković'
      ],
      artists: [],
      techniques: [],    
      selectedArtists: [],
      selectedTech: null,
      hasSaved: false,
      model: null,

      collections: null,
      exhibits: null,
      exhibitsFiltered: [],
      exhDescriptions: [],
      artSources: [],
      noOfExhs: 0,
      imgMapVals: null,
      imagesExh: [],
      imagesInfo: [],

    }
  },

  mounted() {
        // Set the default value of this.item based on what's in the store
        this.sign_in_form = this.$store.getters.sign_in_form
        //this.enter_exh = this.$store.getters.logged_in
        //sessionStorage.setItem('logged_in', false)
        console.log('mounted ' + sessionStorage.getItem('logged_in'))
        var logged = (sessionStorage.getItem('logged_in') === 'true');
        this.$store.commit('show_tool', logged ? true : false)

        console.log('mounted')
        this.getExhibitionSingles()
        //this.getCollections()
    },

  methods: {
    descIn() {
      this.timer = setTimeout(() => { 
        this.description = `Online galerija osnovana je 2020. godine s ciljem promicanja kulture i umjetnosti. 
      Plod mladih i britkih umova online galerija stremi podizanju svijesti i javnog mnijenja o umjetnosti te širenju iste među mladima, 
      pogotovo ferovcima`;
      }, 450);
    },

    descOut() {
      setTimeout(() => { 
        this.description = '';
        clearTimeout(this.timer);
      }, 300);
    },

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
      this.$store.dispatch('logout')
        .then(() => {
          this.sign_out_success()
        })
      // this.$store.commit('show_tool', false)
      // this.$store.commit('sign_in', false)
      // this.$store.commit('register', false)
    },

    sign_out_success() {
      this.$store.commit('show_tool', false)
      this.$store.commit('sign_in', false)
      this.$store.commit('register', false)
      this.$router.push('/')
    },

    validate_s() {
        this.$refs.form.validate()
        let email = this.email_sign
        let password = this.password_sign
        console.log('login' + email + ' ' + password)
        this.$store.dispatch('login', { email, password })
       .then(() => this.sign_success())
       .catch(err => console.log(err))
        //  this.sign_success()

    },

    sign_success() {
        if (this.valid) {
          // this.form = true;
          this.$store.commit('show_tool', true)
          this.$store.commit('sign_in', false)
          this.$store.commit('register', false)
        }
        if (this.$store.getters.logged_in) {
          this.enter_exh = true;
        }
        // this.sign_in_form = false;
        // this.register_form = false;
        this.$store.commit('sign_in', false)
        this.$store.commit('register', false)
        this.$router.push('/')
    },

    validate_r() {
        let data = {
          name: this.name,
          surname: this.surname,
          email: this.email,
          password: this.password_reg,
          paypalMail: this.payPal,
          flag: this.pdf == 'empty_file' ? false : true
          // is_admin: this.is_admin
        }
        this.$store.commit('set_user', data)
        data = JSON.stringify(data)
        let formData = new FormData()
        formData.append('file', this.pdf)
        formData.append('json', new Blob([
            data
        ], {
            type: "application/json"
        }))
        console.log(data)
        console.log(formData)
        // for (var pair of formData.entries()) {
        //     console.log(pair[0]+ ', ' + pair[1]); 
        // }
        // for (var pair of formData.values()) {
        //     console.log(pair[0]+ ', ' + pair[1]); 
        // }
        // for (let [key, value] of formData) {
        //   console.log(`${key}: ${value}`)
        // }

        this.$store.dispatch('register', formData)
       .then(() => this.validate_success())
       .catch(err => console.log(err))
    },

    validate_success() {
      this.$refs.form.validate()
      // if (this.valid) {
      //     // this.form = true;
      //     this.$store.commit('show_tool', true)
      //     this.$store.commit('sign_in', false)
      //     this.$store.commit('register', false)
      //   }
      //   if (this.$store.getters.logged_in) {
      //     this.enter_exh = true;
      //   }
        // this.sign_in_form = false;
        // this.register_form = false;
        this.snackReg = true
        this.$store.commit('sign_in', false)
        this.$store.commit('register', false)
        this.$router.push('/')
        
    },

    cancel() {
      this.$refs.form.reset()
      // this.sign_in_form = false;
      // this.register_form = false;
      this.$store.commit('sign_in', false)
      this.$store.commit('register', false)
    },

    // customFilter (item, queryText, itemText) {
    //   const textOne = item.toLowerCase()
    //   const textTwo = item.toLowerCase()
    //   const searchText = queryText.toLowerCase()
    //   return textOne.indexOf(searchText) > -1 ||
    //     textTwo.indexOf(searchText) > -1
    // },

    // save () {
    //   this.hasSaved = true
    // }

    getCollections() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/getCollections`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            params: {
              'type' : 'all'
            },
            method: 'GET'
      })
      .then((response) => {
        console.log('data ' + response.data);
        this.collections = response.data;
        console.log('dataexh ' + this.collections)
        // let i = 0
        // for (const val of this.collections.values()) {
        //     var colMap = new Map(Object.entries(val));
        //     for (const [key, value] of colMap.entries()) {
        //         console.log('valf ' + value)
        //         //this.bt = btoa(String.fromCharCode.apply(null, new Uint8Array(value)))
        //     }
        //     this.$store.commit('img_info', colMap.keys())
        //     this.imagesExh = colMap.values()
        //     this.imagesInfo = colMap.keys()
        //     i++
        // }
      })
      .catch(err => {
          console.log(err)
      });
    },

    getExhibition(exhName) {
      console.log('collection ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/visitor/getExhibition`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            params: {
              'exName' : exhName
            },
            method: 'GET'
      })
      .then((response) => {
        
        //this.collections = response.data;
        this.$store.commit('set_col', response.data)
        console.log('colls ' + this.collections);
        this.$router.push('/izlozba')
        
      })
      .catch(err => {
          console.log(err)
      });
    },
    
    getExhibitionSingles() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/visitor/getExhibitionSingles`, method: 'GET'})
      .then((response) => {
        this.exhibits = response.data;
        console.log('getExh ' + this.exhibits)
        let i = 0
        let tech = 0
        
        for (let [description, value] of Object.entries(this.exhibits)) {
            this.exhDescriptions[this.noOfExhs] = JSON.parse(description)
            this.artSources[this.noOfExhs] = value
            this.noOfExhs++
            for (let [key, info] of Object.entries(JSON.parse(description))) {
                if (key == 'Artists') {
                  if (info.includes(",")) {
                      var tmp = info.split(",")
                      for (let j = 0; j < tmp.length; j++) {
                          this.artists[i] = tmp[j]
                          i++
                      }
                  } else {
                    this.artists[i] = info
                    i++
                  }
                } else if (key == 'Style') {
                  this.techniques[tech] = info
                  tech++
                }
            }
        }

        this.artists = [...new Set(this.artists)];
        this.techniques = [...new Set(this.techniques)];
        
      })
      .catch(err => {
          console.log(err)
      });
    },

    applyFilters() {
      let i = 0
      for (let [description, value] of Object.entries(this.exhibits)) {
          let include = false
          var descJSON = JSON.parse(description)
          for (let [key, info] of Object.entries(JSON.parse(description))) {
              
              // if (key == 'Style') {
              //     if (this.selectedTech == info) {
              //       include = true
              //     } else {
              //       include = false
              //     }
              // } else if (key == 'Artists') {
              //     if (this.selectedArtists != null) {
              //       for (let i = 0; i < this.selectedArtists; i++) {
              //           if (this.selectedArtists[i] == info) {
              //             include = true
              //           } else {
              //             include = false
              //           }
              //       }
              //     } 
              // }
          }
          this.exhibitsFiltered[i] = '-----------------'
          i++
      }
      this.filters = false
    }
    
  },

  computed: {
      dateRangeText () {
        return this.exh_dates.join(' ~ ')
      }
  },

  watch: {
      search (val) {
        val && val !== this.select && this.querySelections(val)
      }
  }
  
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Work+Sans:wght@100;400&display=swap');

.front_page {
  background-color: rgb(214, 136, 46);
  /* background-color:#f08989; */
}

.light {
  /* background-color: rgb(214, 136, 46); */
  /* background-color: rgb(93, 202, 166); */
  background-color: white;
  /* color: #2c3e50; */
  color: black;
}

.dark {
  /* background-color: rgb(22, 5, 12);
  color: #016b4b; */
  /* background-color:rgb(22, 20, 20); */
  background-color: black;
  color: white;
}

.prijava {
  margin-top: 7px;
}

.form {
  align-content: center;
  margin: auto;
  margin-top: -3%;
  max-width: 50%;
  padding: 2%;
  /* border-style: solid;
  border-width: 1px;
  border-color: black; */
  border: 1px solid black;
  border-radius: 50px;
  background-color: white;
}

.form::placeholder {
  color: black;
}

.form_write::placeholder {
  color: black;
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
  margin-top: 10%;
  font-family: 'Work Sans', sans-serif;
}

.exh_text {
  font-size: 50px;
  font-family:  'Work Sans', sans-serif;
  margin-left: 2%;
  margin-top: 5%;
}

.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.8;
  position: absolute;
  width: 100%;
}

.hover_light {
  background-color: black;
}

.hover_dark {
  background-color: rgba(255, 255, 255, 1);
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
  margin: 5%;
  /* border-radius: 50px; */
}

.izl {
  /* align-content: left;
  margin-top: -50px; */
  font-size: 60%;
  font-family: 'Work Sans', sans-serif;
  /* margin-left: 15%; */
}

.izl_author {
  /* margin-top: -28%;
  margin-left: -4%; */
}

.izl_name {
  /* margin-top: -5%;
  margin-left: -4%; */
  font-weight: 100;
}

.technique {
  margin-left: 2%;
}

.calendar {
  text-align: center;
  margin-top: 4%;
  margin-bottom: 1%;
}

.gal_desc {
  margin: auto;
  text-align: center;
  margin-top: 2%;
  margin-bottom: 8%;
  /* margin-left: 2.5%; */
  max-width: 60%;
  font-size: 26px;
}

.gal_title {
  align-content: center;
  justify-content: center;
}

/*.img_btn {
  /* background-color:rgb(209, 74, 74, 0.3);   */
  /* color: blue; */
  /* background-color: rgb(22, 5, 12, 0.3); */
  /* opacity: 0.8; }*/
</style>
