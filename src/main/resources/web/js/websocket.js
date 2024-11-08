export default function testWebSocket(name) {
    const ws = new WebSocket(`ws://localhost:9000/ws/${name}`)
    ws.onmessage = function (e) {
        console.log(`ws message: ${e.data}`)
    }
    return ws
}