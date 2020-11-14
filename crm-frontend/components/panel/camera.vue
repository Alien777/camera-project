<template>
  <div>
    <br>
    <br>
    <b-row>
      <b-button @click="refreshAvailableClient" variant="outline-success">
        <b-icon icon="  arrow-repeat"></b-icon>
      </b-button>
      <b-col cols="5">
        <DataList v-model="selectedClient" tooltip="Wyszukaj urządzenie z ktorego chcesz zgrać z kamerki" title='app'
                  :options="clients"/>
      </b-col>
      <b-button @click="refreshAvailableDevices" variant="outline-success">
        <b-icon icon="  arrow-repeat"></b-icon>
      </b-button>
      <b-col cols="4">
        <DataList v-model="selectedDevice" tooltip="Wyszukaj urządzenie z ktorego chcesz zgrać z kamerki" title='laptop'
                  :options="devices"/>
      </b-col>


      <b-button @click="add">Dodaj</b-button>


      <div v-for="item of this.recordingCameras">
        <div><p>{{ item.client }} {{ item.device }}</p></div>
        <Watcher/>
      </div>

    </b-row>

  </div>
</template>

<script>

import DataList from '/home/adam/Documents/CRM-PROJECT/crm-frontend/components/field/datalist.vue'
import Watcher from '/home/adam/Documents/CRM-PROJECT/crm-frontend/components/field/watcher.vue'
import websocket, {EchoResponder} from "@/plugins/websocket";
import requestResponse from "@/plugins/requestResponse";

const Cookie = process.client ? require('js-cookie') : undefined
let sockets;
export default {
  components: {
    DataList,
    Watcher
  },
  mounted: function () {
    websocket("ws://127.0.0.1:7000", {
      onComplete: function onComplete(socket) {
        sockets = socket;
        this.refreshAvailableClient();
      }
    }, new EchoResponder(this.messageReceiver))
  },

  data() {
    return {
      clients: [],
      devices: [],
      recordingCameras: [],
      selectedClient: "",
      selectedDevice: "",
      taskId: "",
      isStart: false
    }
  },
  methods: {
    messageReceiver(payload) {
      console.log(payload.metadata+"--->"+payload.data)
      if (String(payload.metadata).includes("available-client")) {
        console.log("KURWA")
        this.clients=payload.data
      }else
      if (String(payload.metadata).includes("available-devices")) {
        this.devices=payload.data
      }
    },
    add() {
      if (this.selectedClient.blink() || !this.selectedDevice.blink()) {
        return;
      }
      this.recordingCameras.push({client: this.selectedClient, device: this.selectedDevice, isActive: false});
    },
    showBoxStop() {
      this.$bvModal.msgBoxConfirm('Spowoduje to, że żadne dane  z kamerki nie będą zapisywanie na seerwrze.', {
        title: 'Czy przerwać nagrywanie?',
        okVariant: 'danger',
        okTitle: 'Tak',
        cancelVariant: 'success',
        cancelTitle: 'Nie',
        hideHeaderClose: false,

      }).then(value => {

        if (value) {
          this.stop();
        }
      }).catch(err => {

      })
    },



    refreshAvailableClient() {
      this.clients = [];
      requestResponse(sockets, "api-plugin.available.clients", "", {
        onComplete: c=>{}
      })
    },

    refreshAvailableDevices() {
      requestResponse(sockets, "api-plugin.available.devices", this.selectedClient, {
        onComplete: c=>{}
      })
    },

    start() {
      this.isStart = !this.isStart;
      this.$axios.$post('http://127.0.0.1:8081/api-plugin/start/recording', {
        "clientId": this.selectedClient,
        "device": this.selectedDevice
      }).then(value => {
        this.taskId = value;
        console.log("Start " + value)
      }).catch(reason => {
        console.log("Error")
      });

    },

    stop() {
      this.isStart = !this.isStart;

      this.$axios.$post('http://127.0.0.1:8081/api-plugin/stop/recording/' + this.selectedClient + "/" + this.taskId).then(value => {
        console.log("Stop")
      }).catch(reason => {
        console.log("Error")
      });

    },

  },
  watch: {
    'selectedClient': function (client) {
      this.refreshAvailableDevices(client);
    }
  }

}
</script>