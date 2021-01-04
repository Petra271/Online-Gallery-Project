<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <h1 class="te">Moj profil</h1>
    <div class="mk">Kolekcija X</div>
  </div>
  <div class="add_coll">
    <v-btn text
    color="black" 
    class="img_btn"
    text-transform: none 
    :class="{ 'show-btns': hover }"
    v-bind="attrs"
    v-on="{ ...tooltip, ...menu }"
    @click="dialog=true"
    >
    <v-icon>mdi-image-plus</v-icon>
    Dodaj novo djelo
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
          :src="name"
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

        <v-flex xs12 class="text-xs-center text-sm-center text-md-center text-lg-center">
          <img :src="imageUrl" height="150" v-if="imageUrl"/>
          <v-text-field label="Select Image" @click='pickFile' v-model='imageName'></v-text-field>
          <input
            type="file"
            style="display: none"
            ref="image"
            accept="image/*"
            @change="onFilePicked"
          >
        </v-flex>      
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
            @click="add_art"
          >
            DODAJ
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>

  <v-row>
      <v-col
        v-for="(status, n) in pictures" 
        :key="n"
        class="d-flex child-flex text"
        cols="12"
        sm="3"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
            v-model="image"
          >
            <v-img
              :src="pictures[n]"
              :lazy-src="pictures[n]"
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
                      @click="delete_art(n)"
                      >
                      <v-icon>mdi-image-edit-outline</v-icon>
                    </v-btn>
                  </template>
                  <span>Izbriši</span>
                </v-tooltip>
              </v-card-title>
            </v-img>
            <p class="naziv">{{names[n]}}</p>
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
      pictures : [require('@/assets/pictures/picture1.jpg'),
      require('@/assets/pictures/picture2.jpg'),
      require('@/assets/pictures/picture3.jpg'),
      require('@/assets/pictures/picture4.jpg'),
      require('@/assets/pictures/picture5.jpg'),
      require('@/assets/pictures/picture6.jpg'),
      ],
      name: '',
      names: ['Djelo 1', 'Djelo 2', 'Djelo 3', 'Djelo 4', 'Djelo 5', 'Djelo 6'],
      images: [],
      name: '',
      price: '',
      priceRules: [
        v => !!v || 'Potrebno je unijeti cijenu djela',
        //v => /.+\..+\ kn/.test(v) || 'Cijena mora biti valjana',
      ],
      title: "Image Upload",
      dialog: false,
      imageName: '',
      imageUrl: '',
      imageFile: '',
      picked: false
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
    delete_art(n) {
      this.pictures.splice(n, 1);
      this.names.splice(n, 1)
    },
    /*sendComment(file) {
      this.pictures.push(this.file);
      var reader = new FileReader();
      reader.readAsDataURL(file);
      
    },
    onFileChange(item, e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;
      this.createImage(item, files[0]);
    },
    createImage(item, file) {
      var image = new Image();
      var reader = new FileReader();

      reader.onload = (e) => {
        item.image = e.target.result;
      };
      reader.readAsDataURL(file);
    },*/
    pickFile() {
      this.$refs.image.click()
    },
    onFilePicked(e) {
      const files = e.target.files
      if (files[0] !== undefined) {
        this.imageName = files[0].name
        this.imageFile = files[0]
        if (this.imageName.lastIndexOf('.') <= 0) {
          return
        }
        const fr = new FileReader()
        fr.readAsDataURL(files[0])
        fr.addEventListener('load', () => {
          this.imageUrl = fr.result
          this.imageFile = files[0] // this is an image file that can be sent to server...
        })
        
      } else {
        this.imageName = ''
        this.imageFile = ''
        this.imageUrl = ''
      }
    },
    add_art() {
      const fr = new FileReader()
      fr.readAsDataURL(this.imageFile)
      fr.addEventListener('load', () => {
        this.imageUrl = fr.result
        this.pictures.push(this.imageUrl);
        this.names.push(this.name)
      })
      this.dialog = false
    }
  }
}
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
.gumbi {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%;
  float: left;
}
/*.col {
    width: 50%;
    float: center;
}*/
.block {
  background: none repeat scroll 0 0;
  display: block;
  overflow: auto;
  width: fit-content;
}
.col {
  width: fit-content;
  float: left;
  margin-left: 2%;
  margin-top: 3%;
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
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 10;
}
.modal {
  position: fixed;
  top: 20%; 
  left: 28%;
  align-content: center;
  background-color: white;
  border-radius: 5px;
  /* margin: auto; */
  /* margin-top: 2%; */
  width: 40%;
  padding: 2%;
  text-align: center;
  z-index: 11; /* 1px higher than the overlay layer */
}
.v-btn {
  text-transform:none !important;
}
.mk {
  font-size: 50px;
  font-family:  'Work Sans', sans-serif;
  margin-left: 2%;
  margin-top: 3%;
}
.add_coll {
  margin-left: 1%;
  margin-top: 2%;
}
</style>