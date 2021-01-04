<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <h1 class="te">Moj profil</h1>
    <div class="mk">Moje kolekcije</div>
  </div>
  <div class="add_coll">
    <v-btn text
    color="black" 
    class="img_btn" 
    :class="{ 'show-btns': hover }"
    v-bind="attrs"
    v-on="{ ...tooltip, ...menu }"
    @click="dialog=true"
    >
    <v-icon>mdi-folder-edit-outline</v-icon>
    Dodaj novu kolekciju
    </v-btn>
  </div>

  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      persistent
      max-width="500"
    >
      <v-card>
        <v-card-title class="headline">
          Dodajte novo djelo
        </v-card-title>
        <v-card-text>
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
      </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            @click="dialog = false"
          >
            PREKID
          </v-btn>
          <v-btn
            color="green darken-1"
            text
            @click="add_coll"
          >
            DODAJ
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>

  <v-row>
      <v-col
        v-for="(status, n) in colls" 
        :key="n"
        class="d-flex child-flex text"
        cols="12"
        sm="3"
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
            <!-- <v-expand-transition>
              <div
                v-if="hover"
                class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
                style="height: 100%;"
              >
              <div class="izl">
                <p>Stil {{n}}</p> <br>
              </div>
              </div>
            </v-expand-transition> -->
            <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      color="black" 
                      class="btn_img" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      @click="delete_coll(n)"
                      >
                      <v-icon>mdi-folder-remove-outline</v-icon>
                    </v-btn>
                  </template>
                  <span>Izbriši</span>
                </v-tooltip>
              </v-card-title>
            </v-img>
            <v-btn text to="/moj_profil/djela">
                <p class="naziv">{{colls[n]}}</p>
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
      colls: [
        'Kolekcija 1', 'Kolekcija 2', 'Kolekcija 3'
      ],
      dialog: false,
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
    add_coll() {
      this.colls.push(this.name)
      this.$store.commit('name', this.name)
      this.dialog = false
    },
    delete_coll(n) {
      this.colls.splice(n, 1)
    }
  }
}

</script>

<style>

.comp {
 margin-top: 200px;
}
.mk {
  font-size: 50px;
  font-family:  'Work Sans', sans-serif;
  margin-left: 2%;
  margin-top: 3%;
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
  /* border-radius: 50px; */
  text-align: center;
}
.naziv{
    font-size: 20px;
    font-style: oblique;
}
.te {
  font-size: 80px;
  font-family:  'Work Sans', sans-serif;
  margin-left: 2%;
  margin-top: 2%;
}
.add_coll {
  margin-left: 1%;
  margin-top: 2%;
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
.v-btn {
  text-transform:none !important;
}
</style>