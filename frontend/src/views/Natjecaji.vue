<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <h2 class="te"> Aktivni natječaji </h2>
  </div>
  <div class="add_coll">
    <div style="cursor: pointer;" @click="dialog=true">
    <v-icon color="black">mdi-image-plus</v-icon>
    Dodaj novi natječaj
    </div>
  </div>

  <div justify="center" v-if="dialog">
    <v-dialog
      v-model="dialog"
      persistent
      max-width="500"
    >
      <v-card>
        <v-card-title class="headline">
          Dodajte novi natječaj
        </v-card-title>
        <v-card-text>
          <v-form
        ref="form"
        v-model="valid"
        lazy-validation
      >
        <v-text-field
          v-model="name"
          :rules="[v => !!v || 'Potrebno je unijeti naziv natječaja']"
          label="Naziv izložbe"
          required
        ></v-text-field>
        
        <v-combobox
          v-model="stil"
          :items="items"
          :rules="[v => !!v || 'Potrebno je odabrati stil natječaja']"
          label="Stil izložbe"
          required
        ></v-combobox>
        
        <v-text-field
          v-model="opis" 
          :rules="[v => !!v || 'Potrebno je unijeti opis kolekcije']"
          label="Opis izložbe"
          required 
        ></v-text-field>
        
        <v-row>
        <v-col
          cols="12"
          sm="6"
        >
          <v-dialog
            ref="dialog1"
            v-model="modal1"
            :return-value.sync="date1"
            persistent
            width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date1"
                label="Datum početka"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="date1"
              scrollable
              locale="hr"
              color="black"
            >
              <v-spacer></v-spacer>
              <v-btn
                text
                color="primary"
                @click="modal1 = false"
              >
                Cancel
              </v-btn>
              <v-btn
                text
                color="primary"
                @click="$refs.dialog1.save(date1)"
              >
                OK
              </v-btn>
            </v-date-picker>
          </v-dialog>
        </v-col>
        <v-col
          cols="12"
          sm="6"
        >
          <v-dialog
            ref="dialog2"
            v-model="modal2"
            :return-value.sync="date2"
            persistent
            width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date2"
                label="Datum završetka"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="date2"
              scrollable
              locale="hr"
              color="black"
            >
              <v-spacer></v-spacer>
              <v-btn
                text
                color="primary"
                @click="modal2 = false"
              >
                Cancel
              </v-btn>
              <v-btn
                text
                color="primary"
                @click="$refs.dialog2.save(date2)"
              >
                OK
              </v-btn>
            </v-date-picker>
          </v-dialog>
        </v-col>
        <v-spacer></v-spacer>
      </v-row>  

      <v-text-field
        v-model="provizija"
        :rules="[v => !!v || 'Potrebno je unijeti vrijednost provizije']"
        label="Vrijednost provizije"
        required
      ></v-text-field>

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
            @click="onSubmit"
          >
            DODAJ
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>

  <table class="table mt-5 col">
    <thead>
      <tr>
        <th scope="col">Naziv natječaja</th>
        <th scope="col">Vrijeme početka</th>
        <th scope="col">Trajanje natječaja (u danima)</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(status, i) in names" :key="i" @click="openDialogInfo(i)" style="cursor: pointer">
        <!-- <th scope="row">{{ ++i }}</th> -->
        <td>{{ names[i] }}</td>
        <td>{{ beginDates[i] }}</td>
        <td>{{ durations[i] }}</td>
      </tr>
    </tbody>
  </table>

  <div justify="center" v-if="dialog_info">
    <v-dialog
      v-model="dialog_info"
      persistent
      max-width="500"
    >
      <v-card>
        <v-card-title class="headline">
          {{ this.dialogName }}
        </v-card-title>
        <v-card-text style="color:black">
          Natječaj je otvoren od {{ this.dialogDate }}, a bit će otvoren {{ this.duration }} dana.<br/>
          Stil ove izložbe jest {{ this.dialogStyle }}.<br/>
          {{ this.dialogDescription }}<br/>
          Vrijednost provizije u postocima koja se plaća galeriji za svaku prodanu sliku iznosi {{ this.dialogCommission }}.<br/>
          <br/>
          <br/>
          <div class="headline"> PRIJAVA NA NATJEČAJ </div>
          <v-form
            ref="form"
            v-model="valid"
            lazy-validation
          >
            <v-autocomplete
              v-model="selectedCollections"
              :items="collectionNames"
              :rules="[v => !!v || 'Potrebno je odabrati kolekcije za natječaj']"
              label="Kolekcije"
              multiple
              small-chips
              deletable-chips
              clearable
            ></v-autocomplete>
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            @click="dialog_info = false"
          >
            PREKID
          </v-btn>
          <v-btn
            color="green darken-1"
            text
            @click="apply"
          >
            PRIJAVA
          </v-btn>
        </v-card-actions>
    </v-card>
    </v-dialog>
  </div>
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
      dialog: false,
      dialog1: false,
      dialog2: false,
      dialog_info: false,
      name: "", 
      score: "", 
      allScores: [],
      opis: "",
      stil: "",
      provizija: "",
    
      items: [
        'FOTOGRAFIJE',
        'ULJE_NA_PLATNU',
        'MJESOVITI',
        'AKVAREL',
        'KOLAZ',
        'OLOVKA',
        'PASTELE'
      ],
      collections: [
        'Kolekcija 1',
        'Kolekcija 2',
        'Kolekcija 3',
      ],
      selectedCollections: [],
      date1: new Date().toISOString().substr(0, 10),
      date2: new Date().toISOString().substr(0, 10),
      
      duration: 0,
      modal: false,
      modal1: false,
      modal2: false,

      dialogName: "",
      dialogStyle: "",
      dialogDescription: "",
      dialogDuration: "",
      dialogCommission: "",
      dialogDate: "",
      contests: null,
      names: [],
      beginDates: [],
      durations: [],
      descriptions: [],
      styles: [],
      comissions: [],
      collectionNames: [],
      collections: null,
      selectedContest: '',
    }
  },

  mounted() {
    this.getContests();
    this.getCollections();
    var logged = (sessionStorage.getItem('logged_in') === 'true');
    this.$store.commit('show_tool', logged ? true : false)
  },
  // computed: {
  //   sortedList: function() {
  //     return this.allScores.slice().sort(function(a, b) {
  //       return b.score - a.score;
  //     });
  //   },
  // },
  methods: {
    onSubmit() {
      var parts1 = this.date1.split('-');
      var parts2 = this.date2.split('-');
      this.duration = new Date(parts2[0], parts2[1] - 1, parts2[2]) - new Date(parts1[0], parts1[1] - 1, parts1[2]);
      this.duration = this.duration / 86400000;
      this.allScores.push({ name: this.name, date1: this.date1, duration: this.duration, opis:this.opis, stil: this.stil, provizija: this.provizija});
      
      let data = {
        beginDateTime: this.date1 + " 00:00",
        duration: "PT" + this.duration + "H0M",
        name: this.name,
        description: this.opis,
        style: this.stil,
        provision: this.provizija
      }
      this.$store.commit('set_contestData', data)
      data = JSON.stringify(data)
      this.$store.dispatch('create_contest', data)
       .catch(err => console.log(err))

      this.clearForm(); 
      this.dialog = false;
    },

    clearForm() {
      this.name = "";
      this.stil = "";
      this.opis = "";
      this.provizija = "";
    },

    openDialogInfo(i) {
      this.dialog_info = true;
      this.dialogName = this.names[i];
      this.dialogDate = this.beginDates[i];
      this.dialogDuration = this.duration[i];
      this.dialogDescription = this.descriptions[i];
      this.dialogStyle = this.styles[i];
      this.dialogCommission = this.comissions[i];
      this.selectedContest = this.names[i]
    },

    apply() {
      let data = {
        contestName: this.selectedContest,
        collections: this.selectedCollections,
      }
      this.$store.commit('set_applyToContest', data)
      data = JSON.stringify(data)
      this.$store.dispatch('create_applyToContest', data)
       .catch(err => console.log(err))


      this.dialog_info = false;
      this.selectedCollections = [];
    },

    getContests() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/visitor/getContests`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            method: 'GET'
      })
      .then((response) => {
        this.contests = response.data
        // console.log(JSON.stringify(this.contests))
        for (let [name, value] of Object.entries(this.contests)) {
          this.names.push(name)
          // console.log(name)
          for (let [key, info] of Object.entries(JSON.parse(value))) {
            // console.log(info)
            if (key == 'BeginDate') {
              this.beginDates.push(info.substr(0, 10))
            }
            if (key == 'Duration') {
              var d = info.toString()
              this.durations.push(d.substr(d.indexOf('T') + 1, d.indexOf('H') - 2))
              // console.log(d.substr(d.indexOf('T') + 1, d.indexOf('H') - 2))
            }
            if (key == 'Provision') {
              this.comissions.push(info)
            }
            if (key == 'Description') {
              this.descriptions.push(info)
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

    getCollections() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/artist/getCollections`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            params: {
              'type' : 'all'
            },
            method: 'GET'
      })
      .then((response) => {
        this.collections = response.data;
        // console.log(this.collections)
        for (let [description, value] of Object.entries(this.collections)) {
          for (let [key, info] of Object.entries(JSON.parse(description))) {
            if (key == 'Name') {
              this.collectionNames.push(info)
            }
          }
        }
      })
      .catch(err => {
          console.log(err)
      });
    },
  },
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
.add_coll {
  margin-left: 1%;
  margin-top: 2%;
}
.col {
    table-layout: fixed;
}
.col td{
  padding: 10px;
  padding-top: 15px;
  padding-bottom: 15px;
  font-size: 25px;
}
.col tr:nth-child(even){
  background-color: #f2f2f2;
}
.col tr:hover {
  background-color: #ddd;
}
.col th {
  padding: 10px;
  padding-top: 20px;
  padding-bottom: 20px;
  text-align: left;
  background-color: rgba(49, 54, 54, 0.842);
  color: white;
  font-size: 30px;
  
}
</style>