<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
    <div>
      <Header/>
    </div>
    <h1>This is an about page</h1>
    
    <h2 class="te"> Prikaz kolekcija raznih umjetnika </h2>
  </div>
  <v-row>
      <v-col
        v-for="n in 8"
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
              :src="`https://picsum.photos/500/300?image=${n * 4}`"
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
                      class="img_btn" 
                      :class="{ 'show-btns': hover }"
                      v-bind="attrs"
                      v-on="{ ...tooltip, ...menu }"
                      :disabled="!$store.getters.logged_in"
                      >
                      <v-icon>mdi-information-variant</v-icon>
                    </v-btn>
                  </template>
                  <span>Opis kolekcije</span>
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
  font-size: 80px;
  text-align: center;
}
</style>