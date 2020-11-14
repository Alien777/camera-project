const Cookie = process.client ? require('js-cookie') : undefined

export default function (socket, endpoint, message, received) {
    socket.requestResponse({
        data: {
            data: message,
            auth: JSON.parse(Cookie.get('auth'))
        },
        metadata: String.fromCharCode(endpoint.length) + endpoint,
    }).subscribe({
        onComplete: data => {
            received.onComplete(data.data);
        },
        onError: error => {
            console.error(error);
        },
        onSubscribe: cancel => {
        }
    });

}