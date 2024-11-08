import testSse  from './sse.js'
import testWebSocket from './websocket.js'

import requestPermissionForNotificationsApi from './notificationPermission.js'

testSse('bob')

document.getElementById("notifications-permission")
    .addEventListener("click", requestPermissionForNotificationsApi)

new SharedWorker("/js/listenForEventAndNotifyWorker.js", {
    name: 'listenForEventAndNotifyWorker'
})
