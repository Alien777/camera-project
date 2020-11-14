<template>
  <div>
    <b-button id="watch-current-movie" variant="secondary-outline">
      <b-icon variant="secondary" v-b-modal.my-modal @click="watch" icon="tv"></b-icon>
    </b-button>
    <b-tooltip triggers="hover" target="watch-current-movie" variant="secondary">Oglądaj aktualny obraz
    </b-tooltip>

    <b-modal id="my-modal">
      <b-img v-bind:src="'data:image/jpeg;base64,'+this.currentImg" alt="Red dot"></b-img>
    </b-modal>


  </div>
</template>
<script>


import websocket from "@/plugins/websocket";

export default {
  name: 'App',
  data: function () {
    return {
      socket: null,
      currentImg: [],
    }
  },
  mounted: function () {
    websocket("ws://127.0.0.1:6010", function () {
      function connected(socket) {
        this.socket = socket;
      }

      function error(error) {
      }

      function cancel(cancel) {
      }
    })

  },

  methods: {
    watch() {
      if (this.socket) {
        this.socket
            .requestStream({
              data: {message: "request - stream from javascript!"},
              metadata: String.fromCharCode('watch'.length) + 'watch',
            }).subscribe({
          onComplete: () => console.log('complete'),
          onError: error => {
            console.log(error);
          },
          onNext: payload => {
            console.log(payload.data);
            this.currentImg = payload.data.data;
          },
          onSubscribe: subscription => {
            // console.log(subscription);
            subscription.request(2147483647);
          },
        });
      } else {
        console.log("not connected...");
      }
    },


  }
}
</script>