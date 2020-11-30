<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <p class="te"> Kolekcija x </p>
  <v-row>
      <v-col
        v-for="n in 6"
        :key="n"
        class="d-flex child-flex"
        cols="4"
      >
        <v-hover v-slot="{ hover }" open-delay="200">
          <v-card class="images"
            :elevation="hover ? 12 : 2"
            :class="{ 'on-hover': hover }"
          >
          <div></div>
            <v-img
              :src="pictures[n]"
              :lazy-src="pictures[n]"
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
                  <p>Poker pasi {{n}}</p> <br>
                  Umjetnik: Jale
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
                      @click="show_art(n)"
                      >
                      <v-icon>mdi-palette</v-icon>
                    </v-btn>
                  </template>
                  <span>Pogledaj djelo</span>
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
    <v-overlay
      :opacity="opacity"
      :z-index="zIndex"
      :value="overlay"
    >
    <v-row class="art-red"
      mb6
      no-gutters
    >
    <v-col>
      <v-card class="artwork" color="rgba(0, 0, 0, 0)" :elevation="0">
        <div></div>
        <v-img rounded
          :src="pictures[index]"
          :lazy-src="pictures[index]"
          aspect-ratio="1"
          class="grey lighten-2 img"
        >
        </v-img>
        <v-row class="art_desc">
          <div>
            Autor: Reshka<br>
            Dimenzije: 180 x 220 <br>
            Stil: ulje na platnu <br>
            Cijena: prava sitnica
          </div>
          <div class="buy_btn">
            <!-- DISABLED AKO JE VEĆ KUPLJENO -->
            <v-btn rounded color="rgb(131, 4, 4)" to="/kupovina">Kupnja</v-btn>
          </div>
        </v-row>
      </v-card>
    </v-col>
    <v-col>
      <!-- --------------KOMENTARI--------------- -->
      <v-card class="com_marg_card" color="rgba(0, 0, 0, 0)"  :elevation="0">
      <v-card class="overflow-hidden comments" color="rgba(0, 0, 0, 0)" :elevation="0">
        <v-app-bar
          :collapse="!collapseOnScroll"
          :collapse-on-scroll="collapseOnScroll"
          absolute
          color="rgb(4, 196, 132)"
          dark
          dense
          scroll-target="#scrolling-techniques-6"
        >
          <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
          <!-- <v-icon>mdi-forum</v-icon> -->
          <v-btn icon
            @click="change_admin()"
          ><v-icon>mdi-forum</v-icon></v-btn>

          <v-toolbar-title class="com_title">{{ comments.length == 0 ? 'Trenutačno nema komentara' : 
            comments.length % 10 == 1 && comments.length % 100 != 11 ? `${comments.length} komentar` : `${comments.length} komentara`}}</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn icon
            v-model="collapseOnScroll"
            @click="overlay = false"
          ><v-icon>mdi-exit-to-app</v-icon></v-btn>
        </v-app-bar>
        <v-sheet
          id="scrolling-techniques-6"
          class="overflow-y-auto"
          max-height="600"
          color="rgba(0, 0, 0, 0)"
        >
        <v-dialog
          v-model="dialog"
          :retain-focus="false"
          persistent
          max-width="311"
        >
          <template v-slot:activator="{ on, attrs }">
           
          </template>
          <v-card>
            <v-card-title class="headline">
              Jeste li sigurni da želite izbrisati komentar?
            </v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="green darken-1" text @click="delete_comment(comIndex)">
                Da
              </v-btn>
              <v-btn color="green darken-1" text @click="dialog = false">
                Ne
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      <v-container>
        <div class="com_edges"></div>    
        <div v-for="(com, index) in comments" :key="index">
          <div :class="{ 'one_com': com[0] != '+', 'one_com_admin': com[0] == '+' }">
            <v-row class="username_and_avatar">
              <v-avatar size="24px">
                <img
                  src="https://cdn.vuetifyjs.com/images/john.jpg"
                  alt="John"
                >
              </v-avatar>
              <p class="username">Vincent V.</p>
                <v-btn v-if="$store.getters.admin" icon small 
                  @click="dialog = true, comIndex = index"
                  style="margin-left: 65%;"
                ><v-icon>mdi-trash-can-outline</v-icon></v-btn>
              </v-row>
            <p class="com_content">{{com[0] == '+' ? com.substring(1, com.length) : com}}</p>
          </div>
        </div>
        <div class="com_edges"></div>
      </v-container>

    </v-sheet>
        
      </v-card>
      <v-text-field 
          v-model="comment"
          class="mx-2 com_write"
          label="Dodajte komentar"
          rows="1"
          prepend-icon="mdi-comment"
          clearable
          append-outer-icon="mdi-send"
          @keyup.13="sendComment()"
          @click:append-outer="sendComment()"
        ></v-text-field>
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

export default {
  name: 'App',
  
  components: {
    Header
  },

  data: () => {
    return {
      overlay: false,
      zIndex: 0,
      opacity: 0.9,
      index: 0,
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
      comment: '',
      comIndex: -1,
      dialog: false
    }
  },

  methods: {
    show_art(n) {
      this.overlay = !this.overlay;
      this.index = n;

    },

    sendComment() {
      if(this.$store.getters.admin) {
        this.comments.push('+' + this.comment);
        this.comment = '';
      } else {
        this.comments.push(this.comment);
        this.comment = '';
      }
    },

    change_admin() {
      if(this.$store.getters.admin) {
        this.$store.commit('log_admin', false);
      } else {
        this.$store.commit('log_admin', true);
      }
    },

    delete_comment(index) {
      // const index = this.comments.indexOf(com);
      // if (index > -1) {
      //   this.comments.splice(index, 1);
      // }
      // this.dialog = false;
      // if(this.comments[index] === com) { 
      this.comments.splice(index, 1)
      this.dialog = false;
    }
  }
}
</script>

<style>

.comp {
 margin-top: 200px;
}

.te {
  margin-top: 4%;
  margin-left: 2.5%;
  font-size: 80px;
}

.art-red {
  margin-top: 6%;
  width: 1400px;
}

.artwork {
  margin-left: 7%;
  margin-top: 7%;
  height: 400px;
  width: 570px;
  border-radius: 50px;
}

.art_desc {
  margin-left: 7%;
  margin-top: -4%;
}

.buy_btn {
  margin-left: 45%;
}

.com_title {
  margin-left: 3%;
}

.comments {
  overflow-y: scroll;
  margin-top: 9%;
  height: 550px;
  width: 500px;
}

.com_edges {
  margin-bottom: 8.5%;  
}

.one_com {
  /* background-color: rgb(51, 168, 90); */
  margin-bottom: 1%;
  background-color: rgb(51, 168, 90, 0.2);
  /* comments toolbar "rgb(4, 196, 132)" */
  border: 1px solid #B388EB;
  border-radius: 50px;
}

.one_com_admin {
  /* background-color: rgb(51, 168, 90); */
  margin-bottom: 1%;
  background-color: rgba(187, 202, 46, 0.3);
  border: 1px solid #B388EB;
  border-radius: 50px;
}

.username_and_avatar {
  margin-top: 1%;
  margin-left: 5%;
  font-size: 16px;
}

.username {
  margin-left: 1%;
}

.delete_com {
  
}

.com_content {
  margin-left: 5%;
  font-size: 22px;
}

.com_write {
  width: 490px;
}

.com_marg_card {
  margin-left: 10%;
  width: 500px;
}
.scrollbar-hidden::-webkit-scrollbar {
  display: none;
}
</style>
