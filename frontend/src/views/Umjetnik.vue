<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <h1 class="te">Moj profil</h1>
    <div class="mk">Moje kolekcije</div>
  </div>
  <div class="add_col">
    <div style="cursor: pointer;" @click="dialog=true">
    <v-icon color="black">mdi-folder-edit-outline</v-icon>
    Dodaj novu kolekciju
    </div>
  </div>

  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      persistent
      max-width="500"
    >
      <v-card>
        <v-card-title class="headline">
          Dodajte novu kolekciju
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
            color="black"
            text
            @click="dialog = false"
          >
            Poništi
          </v-btn>
          <v-btn
            color="black"
            text
            @click="add_coll"
          >
            Dodaj
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>

  <v-row>
      <v-col
        v-for="(status, n) in names.length" 
        :key="n"
        class="d-flex child-flex"
        cols="12"
        sm="3"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images_u"
          >
            <v-img
              :src="'data:image/jpg;base64,' + pictures[n]"
              :lazy-src="'data:image/jpg;base64,' + pictures[n]"
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
                <p>{{ styles[n] }}</p> <br>
              </div>
              </div>
            </v-expand-transition>
            <!-- <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      color="black" 
                      class="btn_img" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      @click="delete_coll(n - 1)"
                      >
                      <v-icon>mdi-folder-remove-outline</v-icon>
                    </v-btn>
                  </template>
                  <span>Izbriši</span>
                </v-tooltip>
              </v-card-title> -->
            </v-img>
            <div @click="djela(names[n], styles[n])" style="cursor: pointer;">
                <p class="naziv">{{names[n]}}</p>
            </div>
          </v-card>
        </v-hover>
      </v-col> 
    </v-row>
  </v-app>
</template>

<script>
import Header from '@/components/Header';
import axios from 'axios';

export default {
  name: 'App',
  
  components: {
    Header
  },

  data: () => {
    return {
      valid: true,
      form: false,
      add_form: false,

      k: 3,
      name: '',
      stil: '',
      opis: '',
      odabrani_stil: 'stil',

      items: [
        'FOTOGRAFIJE',
        'ULJE_NA_PLATNU',
        'MJESOVITI',
        'AKVAREL',
        'KOLAZ',
        'OLOVKA',
        'PASTELE'
      ],
      colls: [
        'Kolekcija 1', 'Kolekcija 2', 'Kolekcija 3', 'Kolekcija 4'
      ],
      dialog: false,
      collections: null,
      pictures: [],
      names: [],
      styles: []
    }
  },

  mounted() {
    this.setUser()
    this.getCollectionSingles();
    var logged = (sessionStorage.getItem('logged_in') === 'true');
    this.$store.commit('show_tool', logged ? true : false);
  },
  
  methods: {
    setUser() {
      if (sessionStorage.getItem('userType') === '1') {
      this.$store.commit('log_admin', true)
      this.$store.commit('log_artist', false)
      } else if (sessionStorage.getItem('userType') === '2') {
        this.$store.commit('log_admin', false)
        this.$store.commit('log_artist', true)
      } else {
        this.$store.commit('log_admin', false)
        this.$store.commit('log_artist', false)
      }
    },
    
    validate() {
      this.$refs.form.validate()
      if (this.valid) {
        this.$store.commit('show_tool', false)
      }
      this.overlay = false;
      this.k++;
    },

    djela(currentCollection, currentStyle) {
      this.$store.commit('set_currentCollection', currentCollection)
      sessionStorage.setItem('currentCollection', currentCollection)
      sessionStorage.setItem('currentStyle', currentStyle)
      this.$store.commit('set_currentStyle', currentStyle)
      this.$router.push('/moj_profil/djela')
    },

    add_coll() {
      let data = {
        name: this.name,
        description: this.opis,
        style: this.stil
      }
      this.$store.commit('set_collectionData', data)
      data = JSON.stringify(data)
      this.$store.dispatch('create_collection', data)
       .catch(err => console.log(err))

      this.colls.push(this.name)
      // this.$store.commit('name', this.name)
      this.dialog = false
      window.location.reload(); 
    },

    delete_coll(n) {
      this.colls.splice(n, 1)
    },

    getCollectionSingles() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/getCollections`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            params: {
              'type' : 'singles'
            },
            method: 'GET'
      })
      .then((response) => {
        this.collections = response.data;
        // console.log(this.collections)
        for (let [description, value] of Object.entries(this.collections)) {
          this.pictures.push(value)
          for (let [key, info] of Object.entries(JSON.parse(description))) {
            if (key == 'Name') {
              this.names.push(info)
            }
            if (key == 'Style') {
              this.styles.push(info)
            }
          }
        }
      })
      .catch(err => {
          console.log(err)
      });
    },
  }

}

</script>

<style>

/* .comp {
 margin-top: 200px;
} */
.mk {
  font-size: 50px;
  font-family:  'Work Sans', sans-serif;
  margin-left: 1%;
  margin-top: 3%;
}
.images_u {
  align-content: right;
  justify-content: center;
  margin: auto;
  margin-top: 0%;
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
  margin-left: 1%;
  margin-top: 1%;
}
.add_col {
  margin-left: 1.5%;
  margin-top: 1%;
  font-size: 22px;
  color: black;
}
/* .overlay {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 10;
} */
/* .modal {
    width: 60%;
    height: 60%;
    position: fixed;
    top: 15%; 
    left: 15%;
    background-color: white;
    border-radius: 5px;
    text-align: center;
    z-index: 11; 1px higher than the overlay layer
} */

</style>