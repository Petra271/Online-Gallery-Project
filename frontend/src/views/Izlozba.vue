<template>
<v-app>
  <div class="front_page">
    <div>
      <Header2/>
    </div>
    <h1>This is an about page</h1>
    <v-container class="cont">
    <h2 class="te"> Prikaz Djela Iz Izložbe </h2>
  </v-container>
  
  </div>
  </v-app>
</template>

<script>
import Header2 from '@/components/Header2'

export default {
  name: 'App',
  
  components: {
    Header2
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