<template>
  <v-app>
    <div class="front_page">
      <v-card class="overflow-hidden">
         <v-app-bar
            fixed
            color="#fcb69f"
            dark
            shrink-on-scroll
            src="https://picsum.photos/1920/1080?random"
            
         >
            <template v-slot:img="{ props }">
            <v-img
               v-bind="props"
               gradient="to top right, rgba(19,84,122,.5), rgba(128,208,199,.8)"
            ></v-img>
            </template>

            <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->

            <v-toolbar-title>onlinegalerija</v-toolbar-title>

            <v-spacer></v-spacer>

            <div v-if="!form">
            <v-btn text rounded class="prijava" v-on:click="sign_in()">prijava</v-btn>
            <v-btn text rounded class="prijava" v-on:click="register()">registracija</v-btn>
            </div>

            
            <div v-else>
               <v-menu rounded="b-xl">
                  <template v-slot:activator="{ on: menu, attrs }">
                  <v-tooltip bottom>
                     <template v-slot:activator="{ on: tooltip }">
                        <v-btn icon
                        v-bind="attrs"
                        v-on="{ ...tooltip, ...menu }"
                        >
                        <v-icon>mdi-account</v-icon>
                        </v-btn>
                     </template>
                     <span>Moj Profil</span>
                  </v-tooltip>
                  </template>

                  <v-list>
                  <v-list-item link> Transakcije  <v-spacer></v-spacer> <v-icon>mdi-cash-multiple</v-icon> </v-list-item>
                  <v-list-item link> Osobni Podaci <v-spacer></v-spacer> <v-icon>mdi-account-details</v-icon> </v-list-item>
                  <v-list-item link v-on:click="sign_out()"> Odjava <v-spacer></v-spacer> <v-icon>mdi-exit-to-app</v-icon> </v-list-item>
                  </v-list>
               </v-menu>

               <v-btn icon>
                  <v-icon>mdi-dots-vertical</v-icon>
               </v-btn>
            </div>
         </v-app-bar>   
      </v-card>
    </div>
  </v-app>
</template>

<script>
export default {
   name: 'Header',

   data: () => {
    return {
      sign_att: false,
      register_att: false,
      form: false,
      sign_in_form: false,
      register_form: false,
      enter_exh: false,
      artist_check: false,

      colors: ['deep-purple accent-4', 'error', 'teal darken-1'],
      valid: true,
      name: '',
      surname: '',
      payPal: '',
      password_sign: '',
      password_reg: '',
      nameRules: [
        v => !!v || 'Name is required',
        // v => (v && v.length <= 10) || 'Name must be less than 10 characters',
      ],
      email: '',
      email_sign: '',
      emailRules: [
        v => !!v || 'Potrebno je upisati E-mail',
        v => /.+@.+\..+/.test(v) || 'E-mail mora biti valjan',
      ],
      select: null,
      checkbox: false,
      show_sign: false,
      show_reg: false,
      pass_rules: {
          required: value => !!value || 'Potrebno je upisati lozinku.',
          min: v => v.length >= 8 || 'Min 8 characters',
          emailMatch: () => (`The email and password you entered don't match`),
        },
    }
  },

  methods: {
    sign_in() {
      //this.sign_att = true;
      this.sign_in_form = true;
      this.register_form = false;
    },

    register() {
      //this.register_att = true;
      this.register_form = true;
      this.sign_in_form = false;
    },

    sign_out() {
      this.sign_in_form = false;
      this.register_form = false;
      this.enter_exh = false;
    },

    validate() {
        this.$refs.form.validate()
        if (this.valid) {
          this.form = true;
        }
        this.enter_exh = true;
        this.sign_in_form = false;
        this.register_form = false;
    },

    cancel() {
      this.$refs.form.reset()
      this.sign_in_form = false;
      this.register_form = false;
    },

    gallery() {
      return this.$router.push('/about');
    }

  }
  
};
</script>

<style>
.front_page {
  background-color: rgb(214, 136, 46);
  /* background-color:#f08989; */
}
</style>