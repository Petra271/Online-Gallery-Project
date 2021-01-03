<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <h2 class="te"> Aktivni natječaji </h2>
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
  font-size: 80px;
  font-family:  'Work Sans', sans-serif;
  margin-left: 2%;
  margin-top: 2%;
}
</style>