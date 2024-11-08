export default function testSse(name) {
    const eventSource = new EventSource(`//localhost:9000/sse/${name}`)

    eventSource.onmessage = function (e) {
        console.log(`sse message: ${e.data}`)
    }

    eventSource.addEventListener("event", function (e) {
        console.log(`sse event: ${e.data}`)
    })

    return eventSource
}