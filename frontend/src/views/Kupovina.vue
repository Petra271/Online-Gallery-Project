<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <div :class="$store.getters.mode ? 'white--text' : 'black--text'"
          style="width: 65%;"
        >
      <div class="exh_author_b"> <b><i> {{artistName}} </i></b></div>
      <div class="exh_name_b"> {{artDescription["Name"]}} </div>
    </div>
    <div class="art_info"
        :class="$store.getters.mode ? 'white--text' : 'black--text'"
        style="width: 65%;"
    > 
      <!-- Slika je nastala u ranoj fazi umjetnikova stvaralaštva. 
      Ističu se brojne silnice – povijesni, politički i društveni kontekst, intelektualni i duhovni horizont, 
      ideologemi i ukus vremena. -->
      {{artDescription["Description"]}}
    </div>
    <div>
      <v-img class="art_buy"
        :src="'data:image/jpg;base64,' + artBuySrc"
        :lazy-src="'data:image/jpg;base64,' + artBuySrc"
        max-height="40%"
        max-width="40%"
        contain
      ></v-img>
    </div>
    
    <div v-if="!delivery && !bought" class="buy_info"> <b> Molimo upišite podatke o dostavi: </b> </div>
    <div v-if="!delivery && !bought" class="buy_form">
      <v-form
        ref="form"
        class="form_write"
        v-model="buy_valid"
        lazy-validation
      >
        <v-text-field
          v-model="street"
          label="Adresa"
          required
        ></v-text-field>

        <v-text-field
          v-model="town"
          label="Grad"
          required
        ></v-text-field>

        <v-text-field
          v-model="postcode"
          :rules="postRules"
          label="Poštanski broj"
          required
        ></v-text-field>

        <v-text-field
          v-model="phone"
          :rules="phoneRules"
          label="Broj mobitela"
          required
        ></v-text-field>

      <div class="delivery_btns">
        <v-btn rounded
          color="rgb(33, 1, 1)"
          class="mr-4 white--text"
          @click="reset()"
        >
          Poništi
        </v-btn>

        <v-btn rounded 
          :disabled="!buy_valid"
          color="rgba(1, 24, 12)"
          class="mr-4 white--text"
          @click="validate()"
        >
          Dalje
        </v-btn>
      </div>
      </v-form>
    </div>

  <v-row v-if="delivery && !bought">
    <div class="art_data"
                :class="$store.getters.mode ? 'art_data_bl' : 'art_data_bd'">
      <v-card class="art_data_card" 
              :class="$store.getters.mode ? 'white--text' : 'black--text'"
              v-bind:style= "[$store.getters.mode ? {'background-color': 'black'} : {'background-color': 'white'}]"
              :elevation="0">
        <div style="padding: 6px; font-size: 18px;">
          Autor: {{artistName}} <br><br>
          Djelo: {{artDescription["Name"]}} <br><br>
          Tehnika: {{artTechnique}} <br><br>
          Dimenzije: 180 x 220 <br><br>
          <!-- Iznos: {{artDescription["Price"]}} <br><br>
          PDV: 14 138 HRK <br><br> -->
        </div>
      </v-card>
    </div>
    <div class="art_data"
                :class="$store.getters.mode ? 'art_data_bl' : 'art_data_bd'"
                style="margin-left: -22%;">
      <v-card class="art_data_card" 
              :class="$store.getters.mode ? 'white--text' : 'black--text'"
              v-bind:style= "[$store.getters.mode ? {'background-color': 'black'} : {'background-color': 'white'}]"
              :elevation="0">
        <div style="padding: 6px; font-size: 18px;">
          Iznos: {{artDescription["Price"].substring(0, artDescription["Price"].indexOf('.'))}} kn<br><br>
          PDV: 14 138 HRK <br><br>
          <!-- Ime: Mato {{$store.getters.user.name}}<br><br>
          Prezime: Lovrak {{$store.getters.user.surname}}<br><br> -->
          Adresa: {{street}}, {{postcode}}, {{town}} <br><br>
          Broj mobitela: {{phone}} <br><br>
          <!-- Dostava se očekuje unutar 3 do 5 radnih dana <br><br> -->
        </div>
      </v-card>
    </div>
  </v-row>
  <div v-if="delivery" class="buy_btns" style="padding: 5px;">
      <v-btn
      color="rgb(33, 1, 1)"
      class="mr-4 white--text"
      @click="delivery = false"
    >
      Natrag
    </v-btn>

    <v-btn 
      :disabled="!buy_valid"
      color="rgba(1, 24, 12)"
      class="mr-4 white--text"
      @click="buyArt()"
    >
      Potvrdi
    </v-btn>
    </div>
    <div v-if="!delivery && bought" class="thanks">
     <b> Hvala Vam na kupnji </b>
    </div>

  </div>
</v-app>
</template>

<script>
import Header from '@/components/Header'
import axios from 'axios'

export default {
   components: {
    Header
  },

  data: () => {
     return {
        authorOrigi: 'Jerolim Miše',
        author: 'Jerolim Miše',
        artwork: require('@/assets/mise_untitled.jpg'),
        postRules: [
        v => !!v || 'Potrebno je upisati poštanski broj',
        v => /.*[0-9]/.test(v) || 'Poštanski broj mora biti valjan',
        ],
        phoneRules: [
        v => !!v || 'Potrebno je upisati broj mobitela',
        v => /.*[0-9]/.test(v) || 'Broj mora biti valjan',
        ],
        buy_valid: false,
        street: '',
        town: '',
        postcode: '',
        phone: '',
        delivery: false,
        artBuySrc: '',
        artistName: '',
        artTechnique: '',
        artPrice: '',
        artDescription: [],
        bought: false
     }
  },

  mounted() {
    var logged = (sessionStorage.getItem('logged_in') === 'true');
    this.$store.commit('show_tool', logged ? true : false)
    this.getArtInfo()
    this.$vuetify.goTo(0)
  },

  methods: {
    validate() {
      this.$refs.form.validate()
      this.delivery = true;
    },

    reset () {
        this.$refs.form.reset()
    },

    getArtInfo() {
      this.artBuySrc = localStorage.getItem('artBuySrc')
      this.artistName = localStorage.getItem('artistBuyName')
      this.artDescription = JSON.parse(localStorage.getItem('artDesc'))
      let tmp = this.artDescription["Style"]
       //this.artTechnique = String(this.artTechnique) + 'a'
      //this
      this.artPrice = this.artPrice.substring(0, this.artPrice.indexOf('.'))
      tmp = tmp.toLowerCase()
      tmp = tmp.charAt(0).toUpperCase() + tmp.slice(1)
      tmp = tmp.replace(/_/g, ' ')
      this.artTechnique = tmp
    },

    buyArt() {
      this.bought = true 
      this.delivery = false
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/transaction/addTransaction`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
              'Content-Type': 'application/json'
            },
            params: {
              'artworkId' : this.artDescription["id"],
              'provision' : localStorage.getItem('provision')
            },
            //data: commentData, 
            method: 'POST'
      })
      .then((response) => {
        setTimeout(() => { 
          
        }, 200);
        //this.forceRerender()
        
        
      })
      .catch(err => {
          console.log(err)
      });
    }
  }
}
</script>

<style>

.comp {
 margin-top: 200px;
}

.exh_author_b {
  margin-top: 2.5%;
  margin-bottom: -3%;
  margin-left: 2%;
  font-size: 80px;
}

.exh_name_b {
  /* margin-top: 4%; */
  margin-left: 2%;
  font-size: 80px;
  font-weight: 100;
}

.art_info {
  margin-top: 2%;
  margin-left: 1.5%;
  font-size: 24px;
}

.art_buy {
  margin: auto;
  margin-top: 3%;
  margin-bottom: 3%;
  /* margin-left: 24%; */
}

.buy_info {
  margin-left: 30%;
  margin-bottom: 1%;
  font-size: 26px;
}

.buy_form {
  align-content: center;
  margin: auto;
  margin-bottom: 10%;
  max-width: 40%;
  padding: 2%;
  border: 1px solid black;
  border-radius: 50px;
  background-color: white;
}

.delivery_btns {
  margin: auto;
  margin-top: 1%;
  margin-left: 59%;
}

.art_data {
  margin: auto;
  margin-left: 30.5%;
  /* border-left: 2px solid white; */
  margin-bottom: 2%;
  max-width: 20%;
}

.art_data_bl {
  border-left: 2px solid white;
}

.art_data_bd {
  border-left: 2px solid black;
}

.buy_btns {
  text-align: center;
  margin-bottom: 5%;
}

.art_data_card {
  /* border: 1px solid black;
  border-radius: 50px; */
  height: 230px;
}

.thanks {
  margin: auto;
  text-align: center;
  font-size: 60px;
  margin-bottom: 5%;
}
</style>