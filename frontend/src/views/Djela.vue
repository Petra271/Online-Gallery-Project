<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <h1 class="te">Djela</h1>
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
        <v-icon>mdi-image-plus</v-icon>
        </v-btn>
    </div>

    <div class="col right">
        <h2>Dodaj novo djelo</h2>
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
          :rules="[v => !!v || 'Potrebno je unijeti naziv djela']"
          label="Naziv djela"
          required
        ></v-text-field>

        <v-text-field
          v-model="price" 
          :rules="priceRules"
          label="Cijena djela"
          required
        ></v-text-field>

        <v-file-input 
          :rules="[v => !!v || 'Potrebno je priložiti sliku djela']"
          small-chips    
          accept="application/jpg"
          label="Priložite sliku djela"
        ></v-file-input>

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
            <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      color="black" 
                      class="img_btn" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      @click="k = k - 1"
                      >
                      <v-icon>mdi-image-edit-outline</v-icon>
                    </v-btn>
                  </template>
                  <span>Izbriši</span>
                </v-tooltip>
              </v-card-title>
            </v-img>
            <p class="naziv">Djelo {{n}}</p>
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

      k: 5,
      name: '',
      price: '',
      priceRules: [
        v => !!v || 'Potrebno je unijeti cijenu djela',
        v => /.+\..+\ kn/.test(v) || 'Cijena mora biti valjana',
      ],
    }
  },

  methods: {
    validate() {
        this.$refs.form.validate()
        if (this.valid) {
          this.$store.commit('show_tool', true)
        }
        this.overlay = false;
        this.k++;
    },
  }
}
/*data: () => {
    return {
        show: false,
    }
}*/
</script>

<style>

.comp {
 margin-top: 200px;
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
.gumbi {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    width: 100%;
    float: left;
}
.col {
    width: 50%;
    float: center;
}
.block {
    background: none repeat scroll 0 0;
    display: block;
    overflow: auto;
    width: fit-content;
}
.col {
    width: fit-content;
    float: left;
}
.add {
    width: 100px;
    background: green;
    border-radius: 15px;
    margin: 0 auto;
    text-align: center;
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