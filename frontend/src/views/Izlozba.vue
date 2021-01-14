<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <div :class="$store.getters.mode ? 'white--text' : 'black--text'"
          style="cursor: pointer; width: 65%;"
          @click="showDescription()"
          @mouseover="informIn()" @mouseleave="informOut()"
        >
      <div class="exh_author"> <b><i> {{author}} </i></b></div>
      <div class="exh_name"> {{exhDescription["Name"]}} </div>
    </div>

    <!-- <div>++{{$store.getters.collections}}</div>
    <div>{{exhDescription}}</div> -->
    <!-- <div>EXHIBITION: {{exhibition}} <br><br><br><br></div> 
    <div>OPIS IZLOŽBE: {{exhDescription}} <br><br></div>
    <div>OPIS KOLEKCIJE: {{collectionDesc}} <br><br></div>
    <div>KOLEKCIJE: {{collections}} <br><br></div>
    <div>ARTDESC: {{artDescription}} <br><br></div>
    <div>ARTSRC: {{artSources}} <br><br></div> -->

    <div v-for="(colInd, i) in collections" :key="colInd" class="collection"
        :class="$store.getters.mode ? 'white--text' : 'black--text'"
    > 
      <div v-if="multipleAuthors" class="col_author"><b><i> {{collectionDesc[i]["Author"]}} </i></b></div>
      <div class="col_name">{{collectionDesc[i]["Name"]}}</div>
    
  <v-row>
      <v-col
        v-for="(art, j) in artDescription[i]"
        :key="art"
        class="d-flex child-flex"
        cols="12"
        sm="3"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
          >
          <div></div>
            <v-img
              :src="'data:image/jpg;base64,' + artSources[i][j]"
              :lazy-src="'data:image/jpg;base64,' + artSources[i][j]"
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
                <div class="art_name"><i><b>{{artDescription[i][j]["Name"]}}</b></i></div>
              </div>
            </v-expand-transition>
              <v-card-title class="align-end fill-height" primary-title>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on: tooltip }">
                    <v-btn icon
                      class="img_btn" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      @click="show_art(i, j)"
                      >
                      <v-icon :color="!$store.getters.mode ? 'white' : 'black'">mdi-palette</v-icon>
                    </v-btn>
                  </template>
                  <span>Pogledajte djelo</span>
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
            <div></div>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>

  </div>

    <!-- ----------- OPIS IZLOŽBE----------- -->
    <v-overlay
      :opacity="opacityExh"
      :z-index="zIndexExh"
      :value="overlayExh"
      :color="!$store.getters.mode ? overlayLight : overlayDark"
    >

      <v-btn icon class="desc_button"
        rounded x-large
        :color="!$store.getters.mode ? 'black' : 'white'"
        @click="overlayExh = false"
      >
        <v-icon>mdi-window-close</v-icon>
      </v-btn>
      <v-card class="desc_card"
              :class="$store.getters.mode ? 'white--text' : 'black--text'"
              style="background-color: rgba(0, 0, 0, 0);" 
              :elevation="0"
            >
        <div style="font-size: 20px;">
          {{info}}
        </div>
      </v-card>
    </v-overlay>
    <!-- ----------- OPIS IZLOŽBE----------- -->

    <v-overlay
      :opacity="opacity"
      :z-index="zIndex"
      :value="overlay"
      :color="!$store.getters.mode ? overlayLight : overlayDark"
    >
    <v-row class="art-red"
      mb6
      no-gutters
    >
    <v-col>
      <v-card class="artwork" color="rgba(0, 0, 0, 0)" :elevation="0">
        <div></div>
        <v-img rounded
          :src="'data:image/jpg;base64,' + artSources[indI][indJ]"
          :lazy-src="'data:image/jpg;base64,' + artSources[indI][indJ]"
          aspect-ratio="1"
          class="grey lighten-2 img"
        >
        </v-img>
        <v-row>
          <div class="art_desc"
               :class="$store.getters.mode ? 'white--text' : 'black--text'"
              >
            Dimenzije: 180 x 220 <br>
            Tehnika: {{techniqueComp}} <br>
            <!-- Cijena: prava sitnica -->
            Cijena: {{artDescription[indI][indJ]["Price"].substring(0, artDescription[indI][indJ]["Price"].indexOf('.'))}} kn
          </div>
          <div class="buy_btn">
            <v-row>
              <div>
                <v-btn color="rgb(33, 1, 1)" @click="overlay=false">Natrag</v-btn>  
              </div>
              <div style="padding-left: 30px;">
                <v-btn 
                  :disabled="artDescription[indI][indJ]['sold'] == 1"
                  color="rgba(1, 24, 12)" 
                  to="/kupovina">Kupnja</v-btn>
              </div>
            </v-row>
          </div>
        </v-row>
      </v-card>
    </v-col>
    <v-col>
      <!-- --------------KOMENTARI--------------- -->
      <v-card class="com_marg_card" color="rgba(0, 0, 0, 0)"  :elevation="0">
      <v-card class="overflow-hidden comments" color="rgba(0, 0, 0, 0)" :elevation="0">
        <div class="no_of_coms" 
           :class="$store.getters.mode ? 'white--text' : 'black--text'"
        >
        {{ comments.length == 0 ? 'Trenutačno nema komentara' : 
            comments.length % 10 == 1 && comments.length % 100 != 11 ? `${comments.length} komentar` : `${comments.length} komentara`}}
        </div>
        <!-- <v-app-bar
          :collapse="!collapseOnScroll"
          :collapse-on-scroll="collapseOnScroll"
          absolute
          color="black"
          dark
          dense
          scroll-target="#scrolling-techniques-6"
        > -->
          <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
          <!-- <v-icon>mdi-forum</v-icon> -->
          <!-- <v-btn icon
            @click="change_admin()"
          ><v-icon>mdi-forum</v-icon></v-btn>

          <v-toolbar-title class="com_title">{{ comments.length == 0 ? 'Trenutačno nema komentara' : 
            comments.length % 10 == 1 && comments.length % 100 != 11 ? `${comments.length} komentar` : `${comments.length} komentara`}}</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn icon
            v-model="collapseOnScroll"
            @click="overlay = false"
          ><v-icon>mdi-exit-to-app</v-icon></v-btn>
        </v-app-bar> -->
        <v-sheet
          id="scrolling-techniques-6"
          class="overflow-y-auto"
          max-height="588"
          color="rgba(0, 0, 0, 0)"
        >
        <v-dialog
          v-model="dialog"
          :retain-focus="false"
          persistent
          max-width="311"
        >
          <template v-slot:activator="{}">
           
          </template>
          <v-card>
            <v-card-title class="headline">
              Jeste li sigurni da želite izbrisati komentar?
            </v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="black" text @click="dialog = false">
                Ne
              </v-btn>
              <v-btn color="black" text @click="delete_comment(comments[comIndex]['id'], comIndex)">
                Da
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      <v-container>
        <!-- <div class="com_edges"></div>     -->
        <div v-for="(com, index) in comments" :key="com">
          <!-- <div :class="{ 'one_com_light': com[0] != '+' && !$store.getters.mode, 
          'one_com_dark': com[0] != '+' && $store.getters.mode, 'one_com_admin': com[0] == '+'}"> -->
          <div v-if="renderComponent" :class="{ 'one_com_light': !$store.getters.mode, 
          'one_com_dark': $store.getters.mode, 'one_com_artist': com['isByArtist'] == 1}">
            <v-row class="username_and_avatar">
              <!-- <v-avatar size="24px">
                <img
                  src="https://cdn.vuetifyjs.com/images/john.jpg"
                  alt="John"
                >
              </v-avatar> -->
              <p :class="{ 'username_l': !$store.getters.mode, 'username_d': $store.getters.mode}"><b>{{com["name"]}} {{com["surname"]}}</b></p>
                <v-btn v-if="$store.getters.admin" icon small 
                  @click="dialog = true, comIndex = index"
                  style="margin-top: 2%;"
                  v-bind:style= "com['isByArtist'] == 1 ? 'margin-left: 64%;' :'margin-left: 67%;'"  
                  :color="$store.getters.mode ? 'black' : 'white'"
                >
                  <v-icon>mdi-trash-can-outline</v-icon>
                </v-btn>
              </v-row>
            <p class="com_content"
              :class="{ 'con_l': !$store.getters.mode, 'con_d': $store.getters.mode}"
            >
            {{com["content"]}}</p>
          </div>
        </div>
        <div class="com_edges"></div>
      </v-container>

    </v-sheet>
        
      </v-card>
        <v-card :class="{ 'enter_com_light': $store.getters.mode, 'enter_com_dark': !$store.getters.mode }"
         
         v-bind:style= "$store.getters.mode ? 'background-color: black; border: 1px solid white; border-radius: 0px;' :
            'background-color : white; border: 1px solid black; border-radius: 0px;'">
        <v-text-field 
            v-model="comment"
            :light="!$store.getters.mode"
            class="mx-2 com_write"
            :class="{ 'com_write_l': !$store.getters.mode, 'com_write_d': $store.getters.mode }"
            label="Dodajte komentar"
            rows="1" 
            clearable
            :color="!$store.getters.mode ? inputLight : inputDark"
            append-outer-icon="mdi-triangle"
            @keyup.13="sendComment(indI, indJ)"
            @click:append-outer="sendComment(indI, indJ)"
          ></v-text-field>
        </v-card>
      </v-card>
    </v-col>
    </v-row>
    </v-overlay>
  </div>
  </v-app>
</template>

<script>
import Header from '@/components/Header'
import pic1 from '@/assets/colstreet.jpg'
import axios from 'axios'
//import text1 from '@/assets/jerolim'

export default {
  name: 'App',
  
  components: {
    Header
  },

  data: () => {
    return {
      overlay: false,
      overlayExh: false,
      overlayLight: 'white',
      overlayDark: 'black',
      inputLight: 'black',
      inputDark: 'white',
      zIndex: 0,
      zIndexExh: 0,
      opacity: 0.9,
      opacityExh: 0.9,
      index: 0,
      authorOrigi: '',
      author: 'Saznajte više o izložbi',
      info: `Moderna galerija – nacionalni muzej moderne umjetnosti ovogodišnji izložbeni program zaključuje kritičkom retrospektivom
velikana hrvatskog slikarstva Jerolima Miše, koju priređuje u suradnji s Galerijom umjetnina iz Splita, 
a u povodu 130 obljetnice rođenja i 50 godina smrti istaknutog hrvatskoga slikara.

Izložba pod nazivom Jerolim Miše: od buntovnika do barda u autorskom konceptu dr. sc. Ane Šeparović donosi fenomenološki i problemski prikaz 
opusa kojeg predstavlja kroz karakteristične tematske odnosno idejne cjeline. Kako se Miše iskušao u raznovrsnim medijima i žanrovima: slikarstvu,
 grafičkom dizajnu, likovnoj kritici i književnosti, slike su na izložbi praćene njegovim tekstovima, kao i tekstovima njegovih suvremenika. 
 Na taj način istaknute su brojne silnice – povijesni, politički i društveni kontekst, intelektualni i duhovni horizont, 
 ideologemi i ukus vremena – koje su se prelamale u njegovim djelima i koje nam otkrivaju iznimnu slojevitost ovog vrlo kompleksnog opusa. 
 Izložba pokazuje djela iz svih razdoblja: od 1910-ih kada se kao mladi slikar predstavlja kao buntovnik koji ruši stare naslijeđene vrijednosti,
  sve do poslijeratnog razdoblja kada postaje dio likovnog establišmenta – bard, koji još za života stječe kanonsko mjesto 
  unutar hrvatske likovne umjetnosti.

Postav izložbe, u koordinaciji Lade Bošnjak Velagić, više kustosice Moderne galerije, objedinjuje 130 umjetnikovih slika i crteža, 
najvećim dijelom iz zbirki Moderne galerije i Galerije umjetnina iz Splita, ali i iz brojnih hrvatskih muzejskih institucija te
 javnih i privatnih kolekcija.

Uz izložbu je predstavljena i knjiga Jerolim Miše, Dokumenti, vrijeme, kritike iz Biblioteke Povijest Moderne galerije. 
Riječ je o petom u nizu izdanja kojima se ova značajna institucija na svojevrstan način odužuje istaknutim osobnostima koje su sudjelovale u 
njezinom profiliranju i ostvarivanju statusa koji joj danas s pravom pripada. Uvažena povjesničarka umjetnosti i 
leksikografkinja dr. sc. Ana Šeparović – čiji su znanstveni radovi počevši od disertacije iz 2014., 
sustavno rasvjetljavali Mišino dugo djelatno razdoblje – priredila je ovu knjigu u kojoj se donosi izbor njegovih likovnih kritika i 
presjek javnog djelovanja umjetnika, koji je unatoč kratkom formalnom ravnateljstvu u Modernoj galeriji (od 1. travnja do 1. srpnja 1955.), 
neposredno utjecao na njezin ustroj i izložbene djelatnosti tijekom dužega razdoblja. Urednica knjige je dr. sc. Libuše Jirsak, 
donedavna viša kustosica Moderne galerije, koja je priredila i uredila prethodna tri izdanja iz iste biblioteke.

Nakon premijernog predstavljanja u Zagrebu, izložba Jerolim Miše: od buntovnika do barda , 
bit će pokazana u Galeriji umjetnina u Splitu, od 11. veljače do 28. ožujka 2021.`,
      pictures : [require('@/assets/pictures/picture1.jpg'),
      require('@/assets/pictures/picture1.jpg'),
      require('@/assets/pictures/picture2.jpg'),
      require('@/assets/pictures/picture3.jpg'),
      require('@/assets/pictures/picture4.jpg'),
      require('@/assets/pictures/picture5.jpg'),
      require('@/assets/pictures/picture6.jpg'),
      ],
      selection: [],
      comments: [],
      collapseOnScroll: true,
      renderComponent: true,
      comment: '',
      comIndex: -1,
      dialog: false,
      exhDescription: '',
      exhibition: [],
      exhibitionRes: [],
      collectionDesc: [],
      collections: [],
      artDescription: [],
      artSources: [],
      multipleAuthors: false,
      indI: 0,
      indJ: 0

    }
  },

  mounted() {
    var logged = (sessionStorage.getItem('logged_in') === 'true');
    this.$store.commit('show_tool', logged ? true : false)
    this.getColItems()
  },

  methods: {

    informIn() {
      setTimeout(() => { 
        this.author = 'Saznajte više o izložbi';
      }, 250);
    },

    informOut() {
      setTimeout(() => { 
        if (this.multipleAuthors) {
          this.author = ''
        } else {
          this.author = this.authorOrigi;
        }
      }, 250);
    },

    showDescription() {
      this.overlayExh = true;
    },

    show_art(i, j) {
      
      //this.overlay = !this.overlay;
      this.indI = i;
      this.indJ = j;
      //console.log('artsource ' + this.artSources[i][j])
      this.$store.commit('buy_art', this.artSources[i][j])
      console.log('artdesc1')
      localStorage.setItem('artBuySrc', this.artSources[i][j])
      localStorage.setItem('artDesc', JSON.stringify(this.artDescription[i][j]))
      localStorage.setItem('artistBuyName', this.collectionDesc[i]["Author"])
      localStorage.setItem('provision', this.exhDescription["Provision"])
      // this.$store.commit('set_art_desc', this.artDescription[i][j])
      // this.$store.commit('set_artist_buy', this.collectionDesc[i]["Author"])
      this.getComments(this.artDescription[i][j]['id'])
      setTimeout(() => { 
        this.overlay = !this.overlay;
      }, 500);
    },

    sendComment(i, j) {
      // if(this.$store.getters.admin) {
      //   this.comments.push('+' + this.comment);
      //   this.comment = '';
      // } else {
      //   this.comments.push(this.comment);
      //   this.comment = '';
      // }
      this.comments[this.comments.length] = this.comment
      let id = this.artDescription[i][j]['id']
      console.log('idsend ' + id)
      //console.log('index ' + this.indI + ' ' + this.indJ)
      let commentData = {
        content: this.comment,
        artworkId: id,
        commentId: 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee'
      }
      //commentData = JSON.stringify(commentData)

      axios({url: `${process.env.VUE_APP_BACKEND_URI}/comment/add`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
              'Content-Type': 'application/json'
            },
            data: commentData, 
            method: 'POST'
      })
      .then((response) => {
        setTimeout(() => { 
          this.overlay = false
        }, 200);
        //this.forceRerender()
        
        this.comment = ''
      })
      .catch(err => {
          console.log(err)
      });
    },

    getComments(id) {
      console.log('getcom ' + id)
      // let commentData = {
      //   artworkId: id
      // }
      // commentData = JSON.stringify(commentData)

      axios({url: `${process.env.VUE_APP_BACKEND_URI}/comment/get`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
              'Content-Type': 'application/json'
            },
            params: {
              'id' : id
            }, 
            method: 'GET'
      })
      .then((response) => {
        let i = 0
        for (let [id, comValue] of Object.entries(response.data)) {
          //console.log('commentjson ' + comValue)
          this.comments[i] = comValue
          i++
        }
        this.renderComponent = false;

        this.$nextTick(() => {
          // Add the component back in
          this.renderComponent = true;
        });
        //console.log('comlength ' + this.comments.length)
      })
      .catch(err => {
          console.log(err)
      });
    },

    delete_comment(id, index) {
      let commentData = {
        content: 'abcd',
        commentId: id,
        artworkId: 'abcd'
      }
      commentData = JSON.stringify(commentData)
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/comment/remove`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`,
              'Access-Control-Allow-Origin': '*'
            },
            params: {
            'id' : id
            }, 
            method: 'POST'
      })
      .then((response) => {
        this.dialog = false;
        this.comments.splice(index, 1)
      })
      .catch(err => {
          console.log(err)
      });
    },

    // forceRerender() {
    //   // Remove my-component from the DOM
    //   this.renderComponent = false;

    //   this.$nextTick(() => {
    //     // Add the component back in
    //     this.renderComponent = true;
    //   });
    // },

    // change_admin() {
    //   if(this.$store.getters.admin) {
    //     this.$store.commit('log_admin', false);
    //   } else {
    //     this.$store.commit('log_admin', true);
    //   }
    // },

    transformTech(i, j) {
      let tmp = this.artDescription[i][j]["Style"]
      tmp = tmp.toLowerCase()
      tmp = tmp.charAt(0).toUpperCase() + tmp.slice(1)
      tmp = tmp.replace(/_/g, ' ')
      return tmp
    },

    getColItems() {
      console.log('mountedizl ' + localStorage.getItem('exhibition'))
      this.exhibitionRes = JSON.parse(localStorage.getItem('exhibition'))
      this.multipleAuthors = false
      let i = 0
      //JSON - opis izložbe: sve kolekcije
      for (let [description, value] of Object.entries(this.exhibitionRes)) {
          this.exhDescription = JSON.parse(description)
          this.exhibition[0] = value
          console.log('velicina kolekcija ' + Object.entries(value).length)
          //KOLEKCIJE S OPISOM I JSON - opis kolekcije: JSON(opis djela: source djela)
          for (let [colDescription, artwork] of Object.entries(value)) {
              this.collectionDesc[i] = JSON.parse(colDescription)
              this.collections[i] = artwork
              console.log('drugi for ' + i)
              let j = 0
              let artDescTmp = []
              let artSrcTmp = []
              //JSON - opis djela: source djela
              for (let [artDesc, artSrc] of Object.entries(artwork)) {
                  artDescTmp[j] = JSON.parse(artDesc)
                  artSrcTmp[j] = artSrc
                  j++
              }

              this.artDescription[i] = artDescTmp
              this.artSources[i] = artSrcTmp
              i++
          }
      }
      this.authorOrigi = this.exhDescription["Artists"]
      //this.authorOrigi = 'Mersad Berber,Jerolim Miše'
      if (this.authorOrigi.indexOf(',') != -1) {
        this.multipleAuthors = true
        //this.authorOrigi = this.authorOrigi.substring(0, this.authorOrigi.indexOf(','))
      }
    }
  },

  computed: {
      techniqueComp () {
        let tmp = this.artDescription[this.indI][this.indJ]["Style"]
        tmp = tmp.toLowerCase()
        tmp = tmp.charAt(0).toUpperCase() + tmp.slice(1)
        tmp = tmp.replace(/_/g, ' ')
        return tmp
      }
  },
}
</script>

<style>

.comp {
 margin-top: 200px;
}

.exh_author {
  margin-top: 2.5%;
  margin-bottom: -3%;
  margin-left: 3.5%;
  font-size: 80px;
}

.exh_name {
  /* margin-top: 4%; */
  margin-left: 3.5%;
  font-size: 80px;
  font-weight: 100;
}

.collection {
  margin-top: 4%;
  margin-bottom: -2%;
  margin-left: 1.5%;
  font-size: 80px;
}

.col_author {
  margin-top: 2.5%;
  margin-bottom: -1.5%;
  margin-left: 1%;
  font-size: 60px;
}

.col_name {
  margin-left: 1%;
  margin-bottom: -2%;
  font-size: 60px;
  font-weight: 100;
}

.desc_card {
  height: 600px;
  width: 600px;
  margin-top: 18%;
  /* margin-bottom: 2%; */
  overflow-y: scroll;
}

.desc_text {
  font-size: 20px;
}

.desc_button {
  /* margin-bottom: -15%; */
   margin-bottom: -42%;
  margin-left: 167%;
}

.art_name {
  margin-top: -2%;
  margin-bottom: auto;
  margin-right: auto;
  font-size: 70%;
  padding: 2%;
}

.art-red {
  margin-top: 6%;
  width: 1400px;
}

.artwork {
  margin-left: 7%;
  margin-top: 3%;
  height: 300px;
  width: 570px;
  border-radius: 30px;
}

.art_desc {
  margin-left: 7%;
  margin-top: -3%;
}

.buy_btn {
  margin-top: -9%;
  margin-left: 50%;
}

.com_title {
  margin-left: 3%;
}

.no_of_coms {
  font-size: 20px;
} 

.comments {
  overflow-y: scroll;
  margin-top: 5%;
  height: 500px;
  width: 499px;
}

.com_edges {
  margin-bottom: 26.5%;  
}

.one_com_light {
  margin-left: -3%;
  margin-bottom: -2%;
  background-color: black;
}

.one_com_dark {
  margin-left: -3%;
  margin-bottom: -2%;
  background-color: white;
}

.one_com_artist {
  margin-left: -3%;
  /* background-color: rgb(51, 168, 90); */
  margin-bottom: -2%;
  /* background-color: rgba(187, 202, 46, 0.3);
  border: 1px solid #B388EB;
  border-radius: 50px; */
  background-color: rgb(87, 0, 0);
}

.username_and_avatar {
  margin-top: 1%;
  margin-left: 5%;
  font-size: 16px;
}

.username_l {
  margin-top: 2%;
  color: white;
  /* margin-left: -2%; */
}

.username_d {
  margin-top: 2%;
  color: black;
  /* margin-left: -2%; */
}

.enter_com_light {
  border: 10px, solid, rgb(0, 0, 0);
  width: 470px;
  /* color: green; */
  
}

.enter_com_light .v-icon { 
  transform: rotate(90deg);
}

.enter_com_dark {
  width: 470px;
}

.enter_com_dark .v-icon { 
  transform: rotate(90deg);
  color: black;
}

.com_write_l {
  margin-top: 0%;
  padding-top: 4%;
  padding-left: 2%;
  width: 450px;
}

.com_write_d {
  margin-top: 0%;
  padding-top: 4%;
  padding-left: 2%;
  width: 450px;
}

/* .com_write::placeholder {
  color:green;
} */

.com_write_l .v-label {
  color: black;
  opacity: 1;
}

.com_content {
  margin-top: -6.5%;
  /* margin-left: 5%;
  margin-right: 5%; */
  margin-left: 1.8%;
  padding: 3%;
  font-size: 17px;
}

.con_l {
  color: white;
}

.con_d {
  color: black;
}

.com_marg_card {
  margin-left: 10%;
  width: 500px;
}
.scrollbar-hidden::-webkit-scrollbar {
  display: none;
}
</style>
