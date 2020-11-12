<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <!-- <h1>This is an about page</h1> -->
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
        <v-img rounded
          :src="pictures[index]"
          :lazy-src="pictures[index]"
          aspect-ratio="1"
          class="grey lighten-2 img"
        >
        </v-img>
      </v-card>
    </v-col>
    <v-col>
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
          <v-icon>mdi-forum</v-icon>

          <v-toolbar-title class="com_title">{{ comments.length == 0 ? 'Trenutačno nema komentara' : 
            comments.length == 1 ? `${comments.length} komentar` : `${comments.length} komentara`}}</v-toolbar-title>

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
      <v-container>
        <div v-for="com in comments" :key="com" class="te">{{com}}</div>
        <div class="te">tekst</div>
        <div class="te">tekst</div>
        <div class="te">tekst</div>

        <div class="te">tekst</div>
        <div class="te">tekst</div>
        <!-- <div v-if="commentSent">koment</div> -->
      </v-container>

    </v-sheet>
        
      </v-card>
      <v-text-field 
          v-model="comment"
          class="mx-2 com_enter"
          label="Dodajte komentar"
          rows="1"
          prepend-icon="mdi-comment"
          clearable
          append-outer-icon="mdi-send"
          @click:append-outer="sendComment()"
        ></v-text-field>
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
      commentSent: false
    }
  },

  methods: {
    show_art(n) {
      this.overlay = !this.overlay;
      this.index = n;

    },

    sendComment() {
      this.commentSent = !this.commentSent
      this.comments.push(this.comment)
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
  margin-top: 9%;
  height: 400px;
  width: 600px;
  border-radius: 50px;
}

.com_title {
  margin-left: 3%;
}

.comments {
  overflow-y: scroll;
  margin-top: 9%;
  height: 550px;
  width: 600px;
}

.com_enter {
  width: 590px;
}

.scrollbar-hidden::-webkit-scrollbar {
  display: none;
}
</style>
