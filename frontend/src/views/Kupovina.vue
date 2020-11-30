<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <v-container class="cont">
    <h2 class="te"> Kupovina Djela </h2>
  </v-container>
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

     }
  }
}
</script>

<style>

.comp {
 margin-top: 200px;
}
.te {
  font-size: 100px;
}
</style>