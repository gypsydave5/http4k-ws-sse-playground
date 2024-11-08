const myUserId = crypto.randomUUID()

eventSource = new EventSource(`//localhost:9000/sse/${myUserId}`)

eventSource.onmessage = function (e) {
    console.log(`Received ${JSON.stringify(e.data)}`)
    new Notification(`sse message: ${e.data}`)
}

eventSource.addEventListener("event", function (e) {
    console.log(`sse event: ${e.data}`)
    new Notification(`sse event: ${e.data}`)
})

eventSource.onerror = function (e) {
    console.error(`sse error: ${e.data}`)
}
