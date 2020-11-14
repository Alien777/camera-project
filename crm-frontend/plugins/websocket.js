import RSocketWebSocketClient from "rsocket-websocket-client";
import {IdentitySerializer, JsonSerializer, RSocketClient} from "rsocket-core";


const Cookie = process.client ? require('js-cookie') : undefined

export class EchoResponder {
    constructor(callback) {
        this.callback = callback;
    }

    fireAndForget(payload) {
        console.log(payload.data)
        if (String(payload.metadata).includes("login-status-endpoint")) {
            let auth = JSON.parse(Cookie.get(`auth`));
            auth.clientId = payload.data;
            Cookie.set('auth', auth)
        } else {
            this.callback(payload);
        }
    }

}

export default function (url, status, responder) {

    const transport = new RSocketWebSocketClient({
        url: url
    });
    const client = new RSocketClient({
        // send/receive JSON objects instead of strings/buffers
        serializers: {
            data: JsonSerializer,
            metadata: IdentitySerializer
        },
        responder: responder,
        setup: {
            // // ms btw sending keepalive to server
             keepAlive: 60000,
            // // ms timeout if no keepalive response
            lifetime: 180000,
            // format of `data`
            dataMimeType: 'application/json',

            metadataMimeType: 'message/x.rsocket.routing.v0',
        },
        transport
    });

    client.connect().subscribe({
        onComplete: socket => {
            socket.requestResponse({
                data: {
                    data: "WEB",
                    auth: JSON.parse(Cookie.get('auth'))
                },
                metadata: String.fromCharCode('login-client-endpoint'.length) + 'login-client-endpoint',
            }).subscribe({
                onComplete: () => {

                },
                onError: error => {
                },
                onSubscribe: subscription => {

                },
            });
            status.onComplete(socket);
        }
    });
}
