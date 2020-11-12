<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <h1 class="te">Moj profil</h1>
    <div class="mk">Moje kolekcije</div>
  </div>
  <div class="block">
    <div class="col left">
        <v-btn icon
        color="black" 
        class="img_btn" 
        :class="{ 'show-btns': hover }"
        v-bind="attrs"
        v-on="{ ...tooltip, ...menu }"
        @click="overlay = !overlay"
        >
        <v-icon>mdi-folder-edit-outline</v-icon>
        </v-btn>
    </div>

    <div class="col right">
        <h2>Dodaj novu kolekciju</h2>
    </div>
  </div>

  <div  v-if="overlay">
    <div class="overlay"></div>
    <div class="modal">
      <v-card-actions>
        <v-spacer class="d-flex justify-space-between align-end" />
        <v-btn outlined
          elevation="7"
          @click="overlay = !overlay"
          >
          <v-icon>mdi-window-close</v-icon>
        </v-btn>
      </v-card-actions>
      <div class="form">
      <v-form
        ref="form"
        v-model="valid"
        lazy-validation
      >
        <v-text-field
          v-model="name"
          :rules="[v => !!v || 'Potrebno je unijeti naziv kolekcije']"
          label="Naziv kolekcije"
          required
        ></v-text-field>
        
        <v-combobox
          v-model="stil"
          :items="items"
          :rules="[v => !!v || 'Potrebno je odabrati stil kolekcije']"
          label="Stil kolekcije"
          required
        ></v-combobox>
        
        <v-text-field
          v-model="opis" 
          :rules="[v => !!v || 'Potrebno je unijeti opis kolekcije']"
          label="Opis kolekcije"
          required 
        ></v-text-field>

      <div class="form_buttons">
        <v-btn
          :disabled="!valid"
          color="success"
          class="mr-4"
          @click="validate()"
        >
          Dodaj
        </v-btn>

      </div>
      </v-form>
    </div>
    </div>
  </div>

  <v-row>
      <v-col
        v-for="n in k"
        :key="n"
        class="d-flex child-flex text"
        cols="3"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
          >
            <v-img
              :src="`https://thumbs.worthpoint.com/zoom/images1/1/0717/08/signed-pablo-picasso-art-drawing_1_b334bdf84d37091e6c77f53ec272f23d.jpg`"
              :lazy-src="`https://picsum.photos/10/6?image=${n * 4}`"
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
                <p>Stil {{n}}</p> <br>
              </div>
              </div>
            </v-expand-transition>
            <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      color="black" 
                      class="btn_img" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      @click="k = k - 1"
                      >
                      <v-icon>mdi-folder-remove-outline</v-icon>
                    </v-btn>
                  </template>
                  <span>Izbriši</span>
                </v-tooltip>
              </v-card-title>
            </v-img>
            <v-btn text to="/djela">
                <p class="naziv">Kolekcija {{n}}</p>
            </v-btn>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>
  </v-app>
</template>

<script>
import Header from '@/components/Header'

export default {
  name: 'App',
  
  components: {
    Header
  },

  data: () => {
    return {
      overlay: false,
      valid: true,
      form: false,
      add_form: false,

      k: 3,
      name: '',
      stil: '',
      opis: '',
      odabrani_stil: 'stil',

      items: [
        'fotografije',
        'ulje na platnu',
        'mješoviti',
      ],
    }
  },
  
  methods: {
    validate() {
        this.$refs.form.validate()
        if (this.valid) {
          this.$store.commit('show_tool', false)
        }
        this.overlay = false;
        this.k++;
    },
  }
}

</script>

<style>

.comp {
 margin-top: 200px;
}
.mk {
  font-size: 40px;
  margin-left: 2.5%;
  margin-top: 5%;
  background-color: white;
  width: fit-content;
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
  text-align: center;
}
.naziv{
    font-size: 20px;
    font-style: oblique;
}
.te {
  font-size: 80px;
}
.block {
    background: none repeat scroll 0 0;
    display: block;
    overflow: auto;
    width: fit-content;
}
.overlay {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 10;
}
.modal {
    width: 60%;
    height: 60%;
    position: fixed;
    top: 15%; 
    left: 15%;
    background-color: white;
    border-radius: 5px;
    /*text-align: center;*/
    z-index: 11; /* 1px higher than the overlay layer */
}
</style>