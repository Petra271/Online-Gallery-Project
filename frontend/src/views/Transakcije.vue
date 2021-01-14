<template>
<v-app>
  <div :class="{ 'light': !$store.getters.mode, 'dark': $store.getters.mode }">
     <div>
      <Header/>
    </div>
    <h2 class="te"> Moje transakcije </h2>
  </div>

  <table class="table mt-5 col">
    <thead>
      <tr>
        <th scope="col">Kupljeno djelo</th>
        <th scope="col">Primatelj</th>
        <th scope="col">Platitelj</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(entry, i) in pictures" :key="i">
        <!-- <th scope="row">{{ ++i }}</th> -->
        <td>
            <v-row align="center" style="padding-left: 20px">
            <v-img
              :src="pictures[i]"
              :lazy-src="pictures[i]"
              aspect-ratio="1"
              class="grey lighten-2 image"
            ></v-img>
            <div class="infos">
                <b>Umjetnik:</b> {{names[i]}}<br/>
                <b>Naziv:</b> {{pictures[i]}}<br/>
                <b>Cijena:</b> {{prices[i]}} kn</div>
            </v-row>
        </td>
        <td style="font-size:23px">{{ names[i] }}</td>
        <td style="font-size:23px">{{ names[i] }}</td>
      </tr>
    </tbody>
  </table>

  <table class="table mt-5 col">
    <thead>
      <tr>
        <th scope="col">Kupljeno djelo</th>
        <th scope="col">Kupac</th>
        <th scope="col">Umjetnik</th>
        <th scope="col">Provizija</th>
        <th scope="col">Cijena</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(status, i) in allArts" :key="i">
        <!-- <th scope="row">{{ ++i }}</th> -->
        <td>{{ allArts[i] }}</td>
        <td>{{ allPayers[i] }}</td>
        <td>{{ allReceivers[i] }}</td>
        <td>{{ allProvisions[i] }}</td>
        <td>{{ allPrices[i] }}</td>
      </tr>
    </tbody>
  </table>
  
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
        transactions: [],
        transactionsRecipient: "",
        transactionsPayer: "",
        pictures: [require('@/assets/pictures/picture1.jpg'),
        require('@/assets/pictures/picture2.jpg'),
        require('@/assets/pictures/picture3.jpg'),
        require('@/assets/pictures/picture4.jpg'),
        require('@/assets/pictures/picture5.jpg'),
        require('@/assets/pictures/picture6.jpg'),
        ],
        names: [
            'ivo', 'lovro', 'ivan', 'maja', 'ivona', 'ja'
        ],
        prices: [
            '200', '500', '675', '860', '150', '1000'
        ],
        allTransactions: null,
        userTransactions: null,
        allArts: [],
        allPrices: [],
        allPayers: [],
        allReceivers: [],
        allProvisions: [],
        userArts: [],
        userPrices: [],
        userPayers: [],
        userReceivers: [],
        userProvisions: [],
     }
  },

  mounted() {
    var logged = (sessionStorage.getItem('logged_in') === 'true');
    this.$store.commit('show_tool', logged ? true : false)
  },

  methods: {
    getAllTransactions() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/transaction/getAllTransactions`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            method: 'GET'
      })
      .then((response) => {
        this.allTransactions = response.data;
        for (let [user, transactions] of Object.entries(this.allTransactions)) {
          for (let [key, value] of Object.entries(JSON.parse(transactions))) {
            if (key == 'Name') {
            this.allArts.push(value)
            }
            if (key == 'TotalAmount') {
              this.allPrices.push(value)
            }
            if (key == 'PayerId') {
              this.allPayers.push(value)
            }
            if (key == 'ReceiverId') {
              this.allReceivers.push(value)
            }
            if (key == 'Provision') {
              this.allProvisions.push(value)
            }
          }
        }
      })
      .catch(err => {
          console.log(err)
      });
    },

    getAllTransactionsByUser() {
      console.log('exhibit ' + sessionStorage.getItem('token'))
      axios({url: `${process.env.VUE_APP_BACKEND_URI}/transaction/getAllTransactionsByUser`, 
            headers: {
              'Authorization':  `Bearer ${sessionStorage.getItem('token')}`
            },
            method: 'GET'
      })
      .then((response) => {
        this.userTransactions = response.data;
        for (let [key, value] of Object.entries(this.userTransactions)) {
          if (key == 'Name') {
            this.userArts.push(value)
          }
          if (key == 'TotalAmount') {
            this.userPrices.push(value)
          }
          if (key == 'PayerId') {
            this.userPayers.push(value)
          }
          if (key == 'ReceiverId') {
            this.userReceivers.push(value)
          }
          if (key == 'Provision') {
            this.userProvisions.push(value)
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
.col {
  width: 100%;
  table-layout: fixed;
  color: black;
  padding-left: 10px;
  padding-right: 10px;
}
.col td{
  padding: 10px;
  padding-top: 15px;
  padding-bottom: 15px;
  font-size: 15px;
}
.col tr:nth-child(even){
  background-color: #f2f2f2;
}
/* .col tr:hover {
  background-color: #ddd;
} */
.col th {
  padding: 10px;
  padding-top: 20px;
  padding-bottom: 20px;
  text-align: left;
  background-color: rgba(49, 54, 54, 0.842);
  color: white;
  font-size: 20px; 
}
.image {
  max-width: 100px;
}
.infos {
  padding-left: 20px;
}
</style>
