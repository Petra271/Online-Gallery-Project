<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <h2 class="te"> Aktivni natječaji </h2>
  </div>
  <div class="add_coll">
    <v-btn text
    color="black" 
    text-transform: none 
    :class="{ 'show-btns': hover }"
    v-bind="attrs"
    v-on="{ ...tooltip, ...menu }"
    @click="dialog=true"
    >
    <v-icon>mdi-image-plus</v-icon>
    Dodaj novi natječaj
    </v-btn>
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
      <tr v-for="(entry, i) in allScores" :key="i" @click="openDialogInfo(entry)" style="cursor: pointer">
        <!-- <th scope="row">{{ ++i }}</th> -->
        <td>{{ entry.name }}</td>
        <td>{{ entry.date1 }}</td>
        <td>{{ entry.duration }}</td>
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
              :items="collections"
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
import Header from '@/components/Header'

export default {
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
     }
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
        duration: "P" + this.duration + "DT0H0M",
        name: this.name,
        description: this.opis,
        style: this.stil,
        privision: parseFloat(this.provizija)
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
    openDialogInfo(entry) {
      this.dialog_info = true;
      this.dialogName = entry.name;
      this.dialogDate = entry.date1;
      this.dialogDuration = entry.duration;
      this.dialogDescription = entry.opis;
      this.dialogStyle = entry.stil;
      this.dialogCommission = entry.provizija;
    },
    apply() {
      this.dialog_info = false;
      this.selectedCollections = [];
    }
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