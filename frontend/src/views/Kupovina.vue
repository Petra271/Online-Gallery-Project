<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <div :class="$store.getters.mode ? 'white--text' : 'black--text'"
          style="width: 65%;"
        >
      <div class="exh_author"> <b><i> {{author}} </i></b></div>
      <div class="exh_name"> Dalmatinski pejzaž </div>
    </div>
    <div class="art_info"
        :class="$store.getters.mode ? 'white--text' : 'black--text'"
        style="width: 65%;"
    > 
      Slika je nastala u ranoj fazi umjetnikova stvaralaštva. 
      Ističu se brojne silnice – povijesni, politički i društveni kontekst, intelektualni i duhovni horizont, 
      ideologemi i ukus vremena.
    </div>
    <div>
      <v-img class="art_buy"
        :src="artwork"
        :lazy-src="artwork"
        max-height="50%"
        max-width="50%"
        contain
      ></v-img>
    </div>
    
    <div v-if="!delivery" class="buy_info"> <b> Molimo upišite podatke o dostavi: </b> </div>
    <div v-if="!delivery" class="buy_form">
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

  <v-row v-else>
    <div class="art_data"
                :class="$store.getters.mode ? 'art_data_bl' : 'art_data_bd'">
      <v-card class="art_data_card" 
              :class="$store.getters.mode ? 'white--text' : 'black--text'"
              v-bind:style= "[$store.getters.mode ? {'background-color': 'black'} : {'background-color': 'white'}]"
              :elevation="0">
        <div style="padding: 6px; font-size: 18px;">
          Autor: Jerolim Miše <br><br>
          Djelo: Dalmatinski pejzaž <br><br>
          Tehnika: ulje na platnu <br><br>
          Dimenzije: 180 x 220 <br><br>
          Iznos: 56 552 HRK <br><br>
          PDV: 14 138 HRK <br><br>
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
          Ime: Mato {{$store.getters.user.name}}<br><br>
          Prezime: Lovrak {{$store.getters.user.surname}}<br><br>
          Adresa: {{street}}, {{postcode}}, {{town}} <br><br>
          Broj mobitela: {{phone}} <br><br>
          Dostava se očekuje unutar 3 do 5 radnih dana <br><br>
        </div>
      </v-card>
    </div>
  </v-row>
  <div  v-if="delivery" class="buy_btns" style="padding: 5px;">
      <v-btn rounded
      color="rgb(33, 1, 1)"
      class="mr-4 white--text"
      @click="delivery = false"
    >
      Natrag
    </v-btn>

    <v-btn rounded 
      :disabled="!buy_valid"
      color="rgba(1, 24, 12)"
      class="mr-4 white--text"
    >
      Potvrdi
    </v-btn>
    </div>
  </div>
</v-app>
</template>

<script>
import Header from '@/components/Header'

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
     }
  },

  mounted() {
    var logged = (localStorage.getItem('logged_in') === 'true');
    this.$store.commit('show_tool', logged ? true : false)
  },

  methods: {
    validate() {
      this.$refs.form.validate()
      this.delivery = true;
    },

    reset () {
        this.$refs.form.reset()
    },
  }
}
</script>

<style>

.comp {
 margin-top: 200px;
}

.exh_author {
  margin-top: 2.5%;
  margin-bottom: -3%;
  margin-left: 2%;
  font-size: 80px;
}

.exh_name {
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
  margin-left: 25%;
  margin-bottom: 1%;
  font-size: 26px;
}

.buy_form {
  align-content: center;
  margin: auto;
  margin-bottom: 10%;
  max-width: 50%;
  padding: 2%;
  border: 1px solid black;
  border-radius: 50px;
  background-color: white;
}

.delivery_btns {
  margin: auto;
  margin-top: 1%;
  margin-left: 69%;
}

.art_data {
  margin: auto;
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
  height: 320px;
}
</style>